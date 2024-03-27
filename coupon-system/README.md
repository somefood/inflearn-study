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
