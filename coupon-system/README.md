# 섹션 1 프로젝트 세팅

## 요구사항정의

```text
선찬숙 100명에게 할인쿠폰을 제공하는 이벤트를 진행

이 이벤트는 아래와 같은 조건을 만족하여야 함
- 선착순 100명에게만 지급되어야 한다.
- 101개 이상이 지급되면 안된다.
- 순간적으로 몰리는 트래픽을 버틸 수 있어야 한다.
```

# 섹션 2 Redis 활용하여 문제 해결하기

```shell
docker pull redis
docker run --name myredis -d -p 6739:6739 redis

# build.gradle 의존성 추가
implementation 'org.springframework.boot:spring-boot-starter-data-redis'
```

## 문제점 해결하기

- 서버 자체를 싱글 쓰레드로 돌리면 성능 이슈가 생길 것이고
- Java의 synchronized를 활용할 수 있지만, 서버가 여러 대가 되면 또 문제가 발생됨
- DB의 락을 걸면 발급된 쿠폰 개수를 갖고오는 것부터 쿠폰을 생성할 때까지 락을 걸어야 하기에 성능에 이슈가 생길 수 있음
 
- 쿠폰 개수의 정합성을 관리하기에 Redis의 incr을 활용해서 key에 대한 value를 1씩 올릴 수 있음
- Redis를 사용하면 싱글쓰레드 형태로 갖고 오기에 정합성이 보장되고, incr의 성능도 빠름

- 아래와 같이 incr "key" 형태로 입력하면 1씩 증가하는 것을 볼 수 있음

```shell
127.0.0.1:6379> incr coupon_count
(integer) 13
127.0.0.1:6379> incr coupon_count
(integer) 14
127.0.0.1:6379> incr coupon_count
(integer) 15
127.0.0.1:6379> incr coupon_count
(integer) 16
127.0.0.1:6379> incr coupon_count
(integer) 17
127.0.0.1:6379> incr coupon_count
(integer) 18
127.0.0.1:6379> incr coupon_count
(integer) 19
127.0.0.1:6379> incr coupon_count
(integer) 20
127.0.0.1:6379> incr coupon_count
(integer) 21
```

## 문제점

- mysql에서 1분에 100개의 insert가 가능하다고 가정하고 아래 표를 보자

|Time| Request         |
|----|-----------------|
|10:00| 쿠폰생성 10,000개 요청 |
|10:01| 주문생성 요청         |
|10:02| 회원가입 요청         |

- 10:00에 쿠폰을 생성하려면 100분의 시간이 걸림
- 그러면 이후 주문생성, 회원가입은 100분 뒤에 요청이 완료됨
- 타임아웃이 없으면 느리게라도 되지만 대부분의 애플리케이션은 타임아웃이 있기에 요청들이 실패하게 됨
- 또한, 짧은 시간 내에 많은 요청이 들어오게 되면, DB 서버의 리소스를 많이 사용하게 되어 부하가 발생하고 서비스 지연 및 오류로 이어지게 됨

> Kafka를 이용해서 문제를 해결해보자

# 섹션 3 Kafka를 활용하여 문제 해결하기

```yaml
version: '2'
services:
  zookeeper:
    image: wurstmeister/zookeeper
    container_name: zookeeper
    ports:
      - "2181:2181"
  kafka:
    image: wurstmeister/kafka:2.12-2.5.0
    container_name: kafka
    ports:
      - "9092:9092"
    environment:
      KAFKA_ADVERTISED_HOST_NAME: 127.0.0.1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock
```

> docker-compose up -d 실행

## kafka 알아보기

- 분산 이벤트 스트리밍 플랫폼
- 이벤트 스트리밍이란 소스에서 목적지까지 이벤트를 실시간으로 스트리밍 하는 것
- 간단한 구조
  - producer -> Topic <- consumer
  - Topic: Queue 같은 역할
  - producer: 토픽에 데이터를 삽입할 수 있는 기능을 가짐
  - consumer: 토픽에 삽입된 데이터를 가져올 수 있음

### 토픽 생성

```shell
docker exec -it kafka kafka-topics.sh --bootstrap-server localhost:9092 --create --topic testTopic
```

### 프로듀스 실행

```shell
docker exec -it kafka kafka-console-producer.sh --topic testTopic --broker-list 0.0.0.0:9092
```

### 컨슈머 실행

```shell
docker exec -it kafka kafka-console-consumer.sh --topic testTopic --bootstrap-server localhost:9092
```

- 이후 프로듀서에서 메시지를 입력하면 컨슈머가 내용을 받을 수 있음

## Producer 사용하기

- Kafka Config 만들어주기

```java
@Configuration
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<String, Long> producerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, LongSerializer.class);

        return new DefaultKafkaProducerFactory<>(config);
    }

    /**
     * 카프카 토픽에 데이터를 전송하기 위해 사용할 템플릿
     */
    @Bean
    public KafkaTemplate<String, Long> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
```

- Producer 클래스 만들어주기

```java
@Component
public class CouponCreateProducer {

    private final KafkaTemplate<String, Long> kafkaTemplate;

    public CouponCreateProducer(KafkaTemplate<String, Long> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void create(Long userId) {
        kafkaTemplate.send("coupon_create", userId);
    }
}
```

- ApplyService 수정

```java
@Service
public class ApplyService {

    public final CouponRepository couponRepository;

    private final CouponCountRepository couponCountRepository;

    private final CouponCreateProducer couponCreateProducer;

    public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository,
                        CouponCreateProducer couponCreateProducer) {
        this.couponRepository = couponRepository;
        this.couponCountRepository = couponCountRepository;
        this.couponCreateProducer = couponCreateProducer;
    }

    public void apply(Long userId) {
        final long count = couponCountRepository.increment(); // redis flushall 한 번 해주자. incr 올라가있어서 작동 안되고 있었음 ㅎㅎ..

        if (count > 100) {
            return;
        }
        couponCreateProducer.create(userId);
    }
}
```

### Topic 생성

```shell
docker exec -it kafka kafka-topics.sh --bootstrap-server localhost:9092 --create --topic coupon_create
```

### Consumer 실행

```shell
docker exec -it kafka kafka-console-consumer.sh --topic coupon_create --bootstrap-server localhost:9092 --key-deserializer "org.apache.kafka.common.serialization.StringDeserializer" --value-deserializer "org.apache.kafka.common.serialization.LongDeserializer"
```

> redis flushall 한 번 해주자. incr 올라가있어서 작동 안되고 있었음 ㅎㅎ..

## Consumer 사용하기

- 컨슈머 설정 팩토리와 리스너를 지정한다.

```java
@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, Long> consumerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "group_1");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Long> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Long> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }
}
```

- @KafkaListner를 활용해서 토픽과 그룹을 지정해준다.
- 지정한 토픽에서 메시지가 오면 그때 쿠폰을 발행하게 된다.

```java
@Component
public class CouponCreatedConsumer {

    private final CouponRepository couponRepository;

    public CouponCreatedConsumer(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @KafkaListener(topics = "coupon_create", groupId = "group_1")
    public void listener(Long userId) {
        couponRepository.save(new Coupon(userId));
    }
}
```

- 카프카로 메시징을 하면 count 하는 시점엔 저장이 완료되지 않을 수 있기에 충분한 텀을 두고 테스트를 확인해야한다.

```java
@Test
void 여러명응모() throws InterruptedException {
    int threadCount = 1000;
    final ExecutorService executorService = Executors.newFixedThreadPool(32);
    final CountDownLatch latch = new CountDownLatch(threadCount);

    for (int i = 0; i < threadCount; i++) {
        long userId = i;
        executorService.submit(() -> {
            try {
                applyService.apply(userId);
            } finally {
                latch.countDown();
            }
        });
    }

    latch.await();

    Thread.sleep(10000);

    final long count = couponRepository.count();

    assertThat(count).isEqualTo(100);

}
```

# 섹셔 4 요구사항 변경

## 발급가능한 쿠폰개수를 1인당 1개로 제한하기

- 인당 1개만 받게 하려면 어떻게 해야할까
- set 자료구조를 활용해서 중복을 제거하면 가능하다!
- redis `sadd key value`를 사용하면 된다.
  - sadd applied 1: 1번 유저에 대해 set에 추가
  - 아래와 같이 추가되면 1 중복이면 0을 출력한다.

```shell
127.0.0.1:6379> sadd applied 1
(integer) 1
127.0.0.1:6379> sadd applied 1
(integer) 0
127.0.0.1:6379>

```

- 자바 코드로 추가

```java
@Repository
public class AppliedUserRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public AppliedUserRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Long add(Long userId) {
        return redisTemplate
                .opsForSet()
                .add("applied_user", userId.toString());
    }
}
```

- if (apply != 1): 위에서 언급했듯이 1이 아니면 이미 추가 된 경우니 쿠폰 발급을 하지 않으면 된다.

```java
@Service
public class ApplyService {

    public final CouponRepository couponRepository;

    private final CouponCountRepository couponCountRepository;

    private final CouponCreateProducer couponCreateProducer;

    private final AppliedUserRepository appliedUserRepository;

    public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository, CouponCreateProducer couponCreateProducer, AppliedUserRepository appliedUserRepository) {
        this.couponRepository = couponRepository;
        this.couponCountRepository = couponCountRepository;
        this.couponCreateProducer = couponCreateProducer;
        this.appliedUserRepository = appliedUserRepository;
    }

    public void apply(Long userId) {
        Long apply = appliedUserRepository.add(userId);

        if (apply != 1) {
            return; // 이미 발급 받음
        }

        final long count = couponCountRepository.increment();

        if (count > 100) {
            return;
        }

        couponCreateProducer.create(userId);
    }
}
```

- 테스트 코드 작성

```java
@Test
void 한명당_한개의쿠폰만_발급() throws InterruptedException {
    int threadCount = 1000;
    final ExecutorService executorService = Executors.newFixedThreadPool(32);
    final CountDownLatch latch = new CountDownLatch(threadCount);

    for (int i = 0; i < threadCount; i++) {
        long userId = i;
        executorService.submit(() -> {
            try {
                applyService.apply(1L);
            } finally {
                latch.countDown();
            }
        });
    }

    latch.await();

    Thread.sleep(10000);

    final long count = couponRepository.count();

    assertThat(count).isEqualTo(1);

}
```

# 섹션 5 시스템 개선하기

- 카프카를 통해 이벤트 방식으로 바꾸어 봤다.
- 하지만 저장에는 실패했는데, 레디스에서는 이미 값이 증가하여 못 받는 경우가 생길 수 있다.
- 이런 경우 실패 이력을 쌓아둬서 배치를 통해 쿠폰을 지급할 수 있다.

```java
@Entity
public class FailedEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    public FailedEvent() {
    }

    public FailedEvent(Long userId) {
        this.userId = userId;
    }
}
```

```java
@Component
public class CouponCreatedConsumer {

    private final CouponRepository couponRepository;

    private final FailedEventRepository failedEventRepository;

    private final Logger logger = LoggerFactory.getLogger(CouponCreatedConsumer.class);

    public CouponCreatedConsumer(CouponRepository couponRepository, FailedEventRepository failedEventRepository) {
        this.couponRepository = couponRepository;
        this.failedEventRepository = failedEventRepository;
    }

    @KafkaListener(topics = "coupon_create", groupId = "group_1")
    public void listener(Long userId) {
        try {
            couponRepository.save(new Coupon(userId));
        } catch (Exception e) {
            logger.error("failed to create coupon::" + userId);
            failedEventRepository.save(new FailedEvent(userId));
        }
    }
}
```
