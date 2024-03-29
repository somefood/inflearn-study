## 레이스 컨디션(Race Condition) 이란 ?

- 둘 이상의 스레드가 공유 데이터에 액세스할 수 있고 동시에 변경하려고 할 때 발생하는 문제
  - 둘 이상의 스레드 : 요청
  - 공유 데이터 : 재고 데이터
  - 동시에 변경하려고 할 때 : 업데이트 할때
  - 발생하는 문제 : 값이 정상적으로 바뀌지 않는 문제

- 해결방법
  - 하나의 스레드만 데이터에 액세스 할 수 있도록 한다.

# 섹션 2 Synchronized 이용해보기

## Synchronized 이용해보기

- decrease 메서드에 synchronized 메서드를 붙여보자

```java
@Transactional
public synchronized void decrease(Long id, Long quantity) {
    // Stock 조회
    // 재고를 감소시킨 뒤
    // 갱신된 값을 저장

    final Stock stock = stockRepository.findById(id).orElseThrow();
    stock.decrease(quantity);

    stockRepository.saveAndFlush(stock);
}
```

- 돌리면 실패하게 되는데, 이유는 @Transactional 어노테이션이 붙어 있으면 래핑 클래스로 감싸지게 되는데,
  - 아래는 간단한 예시처럼 트랜잭션 종료 시점에 데이터베이스에 업데이트 하는데,
  - decrease 메서드가 완료 되었고, 실제 데이터베이스가 업데이트 되기 전에 다른 Thread가 decrease 메서드를 호출 할 수 있음
  - 그렇게 되면 다른 Thread는 갱신되기 전 값을 가져와 이전하고 동일한 문제가 발생됨
- @Transactional 어노테이션을 제거하면 원하는데로 테스트가 가능함

```java
package com.example.stock.transactioln;

import com.example.stock.service.StockService;

public class TransactionStockService {

    private final StockService stockService;

    public TransactionStockService(StockService stockService) {
        this.stockService = stockService;
    }

    public void decrease(Long id, Long quantity) {
        startTransaction();

        stockService.decrease(id, quantity);

        endTransaction();
    }

    private void startTransaction() {
        System.out.println("Transaction Start");
    }

    private void endTransaction() {
        System.out.println("Commit");
    }
}
```

## 문제점

- java synchronized 문제점
  - 서버가 1대일때는 되는듯싶으나 여러대의 서버를 사용하게되면 사용하지 않았을때와 동일한 문제가 발생된다.
  - 인스턴스단위로 thread-safe 이 보장이 되고, 여러서버가 된다면 여러개의 인스턴스가 있는것과 동일하기 때문

# 섹션 3 Database 이용해보기

## Mysql 을 활용한 다양한 방법

- Pessimistic(비관적) Lock
  - 실제로 데이터에 Lock 을 걸어서 정합성을 맞추는 방법. 
  - exclusive lock 을 걸게되며 다른 트랜잭션에서는 lock 이 해제되기전에 데이터를 가져갈 수 없게됨.
  - 데드락이 걸릴 수 있기때문에 주의하여 사용하여야 함.

- Optimistic(낙관적) Lock
  - 실제로 Lock 을 이용하지 않고 버전을 이용함으로써 정합성을 맞추는 방법. 
  - 먼저 데이터를 읽은 후에 update 를 수행할 때 현재 내가 읽은 버전이 맞는지 확인하며 업데이트 함. 
  - 내가 읽은 버전에서 수정사항이 생겼을 경우에는 application에서 다시 읽은후에 작업을 수행해야 함.

- Named Lock
  - 이름을 가진 metadata locking. 
  - 이름을 가진 lock 을 획득한 후 해제할때까지 다른 세션은 이 lock 을 획득할 수 없도록 함. 
  - 주의할점으로는 transaction 이 종료될 때 lock 이 자동으로 해제되지 않음. 
  - 별도의 명령어로 해제를 수행해주거나 선점시간이 끝나야 해제됩니다.
  - Pessimistic Lock은 로우나 테이블 단위로 락을 걸지만, Named Lock은 메타데이터에 락을 걺

## Pessimistic Lock 활용해보기

- @Lock 어노테이션을 활용해서 Pessimistic 락을 쉽게 걸 수 있다.

```java
public interface StockRepository extends JpaRepository<Stock, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Stock s where s.id = :id")
    Stock findByIdWithPessimisticLock(Long id);
}
```

- 해당 메서드를 활용하면 for update 문구가 붙는데, 이게 락을 거는 것임
- 해당 락의 장점은 충돌이 빈번하게 일어난다면 Optimistic 락보다 성능이 좋을 수 있음
- 또한, 락을 통해 업데이트를 제어하기 때문에 데이터 정합성이 보장됨
- 하지만 별도의 락을 잡는 것이기에 성능 감소가 일어날 수 있음

```sql
select s1_0.id,s1_0.product_id,s1_0.quantity from stock s1_0 where s1_0.id=? for update
```

## Optimistic Lock

- 실제로 락을 이용하지 않고 버전을 이용하여 버전을 맞추는 방법
- 내가 읽은 버전에서 수정사항이 생겼을 경우 애플리케이션에서 다시 읽은 후에 작업을 수행해야 함

```java
@Service
public class OptimisticLockStockService {

    private final StockRepository stockRepository;

    public OptimisticLockStockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional
    public void decrease(Long id, Long quantity) {
        final Stock stock = stockRepository.findByIdWithOptimisticLock(id);

        stock.decrease(quantity);
        stockRepository.save(stock);
    }
}
```

- Optimistic Lock 지정

```java
public interface StockRepository extends JpaRepository<Stock, Long> {
    
    @Lock(LockModeType.OPTIMISTIC)
    @Query("select s from Stock s where s.id = :id")
    Stock findByIdWithOptimisticLock(Long id);
}
```

- Facade 패턴을 활용해서 내 버전이 맞지 않으면 무한 반복문을 통해 버전을 맞출 수 있도록 해줌
- 에러가 발생하면 아래와 같이 나옴
  > org.springframework.orm.ObjectOptimisticLockingFailureException: Row was updated or deleted by another transaction (or unsaved-value mapping was incorrect) : [com.example.stock.domain.Stock#1]

```java
@Component
public class OptimisticLockStockFacade {

    private final OptimisticLockStockService optimisticLockStockService;

    public OptimisticLockStockFacade(OptimisticLockStockService optimisticLockStockService) {
        this.optimisticLockStockService = optimisticLockStockService;
    }

    public void decrease(Long id, Long quantity) throws InterruptedException {
        while (true) {
            try {
                optimisticLockStockService.decrease(id, quantity);
                break;
            } catch (Exception e) {
                Thread.sleep(50);
            }
        }
    }
}
```

```sql
insert into stock (product_id,quantity,version) values (?,?,?); -- 버전도 insert 함

update stock set product_id=?,quantity=?,version=? where id=? and version=? -- update 시 버전도 같이 넣어줌. 해당 버전이 없으면 에러가 날테니 while 반복문을 통해 맞춰줌
```

- Optimistic 락은 별도의 락을 잡지 않으므로 Pessimistic 락보다 성능상 이점이 있음
- 단점으론 업데이트가 실패했을 때 재시도 로직을 개발자가 직접 작성해 주어야 함
- 충돌이 빈번하게 일어날거 같으면 Pessimistic 락을 추천하며, 그렇지 않을 때 Optimistic 락을 사용해보도록 해보자

## Named Lock

- 이름을 가진 메타데이터 락
- 이름을 가진 락을 획득한 후 해제할 때까지 다른 세션은 이 락을 획들할 수 없게 됨
- 트랜잭션이 종료될 때 락이 자동으로 해제되지 않기 때문에 별도의 명령어로 해제를 수행해주거나 선점 시간이 끝나야 락이 해제되니 주의해야 함
  - MySQL: get-lock 명령어로 락 획득 가능/release-lock으로 락 해제 가능 
- 같은 데이터 소스를 사용하면 커넥션 풀이 부족해져 다른 서비스에도 영향을 줄 수 있기에 데이터 소스를 분리해서 사용해주는 것을 추천

```java
// 예제라 그렇지만, 별도의 JDBC를 사용해서 만들어주길 바람
public interface LockRepository extends JpaRepository<Stock, Long> {

    @Query(value = "select get_lock(:key, 3000)", nativeQuery = true)
    void getLock(String key);

    @Query(value = "select release_lock(:key)", nativeQuery = true)
    void releaseLock(String key);
}
```

- 락을 잡고 해제해줘야해서 Facade 디자인패턴 사용

```java
@Component
public class NamedLockStockFacade {

    private final LockRepository lockRepository;

    private final StockService stockService;

    public NamedLockStockFacade(LockRepository lockRepository, StockService stockService) {
        this.lockRepository = lockRepository;
        this.stockService = stockService;
    }

    public void decrease(Long id, Long quantity) {
        try {
            lockRepository.getLock(id.toString());
            stockService.decrease(id, quantity);
        } finally {
            lockRepository.releaseLock(id.toString());
        }
    }
}
```

```sql
select get_lock(?, 3000)
```

- 네임드락은 분산락을 구현할 때 사용
- Pessimistic Lock 은 타임아웃을 구현하기 어렵지만 네임드락은 구현하기 쉬움 
- 데이터 삽입 시에 정합성을 맞춰야 하는 경우에도 네임드락을 사용하면 좋음
- 하지만, 트랜잭션 종료 시에 락 해제, 세션 관리를 잘 해줘야 하기 때문에 구현 방법이 복잡해짐

# 섹션 4 Redis 이용해보기

## Lettuce를 작성하여 재고감소 로직 작성하기

- 레디스에서 간단하게 락을 갖고오는 방법 (setnx)
  - `setnx key value`: intger 1 반환. 다른데서 같은 키를 얻고자 하면 0을 반환한다.
  - `del key`: 락을 다시 반환한다.

```java
@Component
public class LettuceLockStockFacade {

    private final RedisLockRepository redisLockRepository;
    private final StockService stockService;

    public LettuceLockStockFacade(RedisLockRepository redisLockRepository, StockService stockService) {
        this.redisLockRepository = redisLockRepository;
        this.stockService = stockService;
    }

    public void decrease(Long id, Long quantity) throws InterruptedException {
        while (!redisLockRepository.lock(id)) {
            Thread.sleep(100);
        }

        try {
            stockService.decrease(id, quantity);
        } finally {
            redisLockRepository.unlock(id);
        }
    }
}
```

- Thread.sleep(100): 시간을 설정하여어 레디스에 부하되지 않게 조정해주는게 좋다. 

## Redisson을 활용하여 재고로직 작성하기

- subscribe ch1: ch1을 구독하기
- publish ch1 hello: ch1에 메시지 보내기
- 레디스는 자신이 점유하고 있는 락을 해제할 때 채널에 메시지를 보내줌으로써 락을 획득해야 하는 쓰레드에게 알려줄 수 있다
- Lettuce는 계속 락 획득을 시도하는 반면, 레디스는 락 해제가 되었을 때 한 번 혹은 몇 번만 시도하기 때문에 레디스의 부하를 줄여줄 수 있다.
- 레디슨 라이브러리를 사용하면 Pub/Sub 기반이라 레디스의 부하를 줄여줄 수 있₩

- 의존성 추가하기
> implementation 'org.redisson:redisson-spring-boot-starter:3.27.2'

```java
@Component
public class RedissonLockStockFacade {

  private final RedissonClient redissonClient;

  private final StockService stockService;

  public RedissonLockStockFacade(RedissonClient redissonClient, StockService stockService) {
    this.redissonClient = redissonClient;
    this.stockService = stockService;
  }

  public void decrease(Long id, Long quantity) {
    final RLock lock = redissonClient.getLock(id.toString());

    try {
      final boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);

      if (!available) {
        System.out.println("lock 획득 실패");
        return;
      }

      stockService.decrease(id, quantity);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } finally {
      lock.unlock();
    }
  }
}
```

## 라이브러리 장단점

- Lettuce
  - 구현이 간단하다
  - spring data redis 를 이용하면 lettuce 가 기본이기때문에 별도의 라이브러리를 사용하지 않아도 된다.
  - spin lock 방식이기때문에 동시에 많은 스레드가 lock 획득 대기 상태라면 redis 에 부하가 갈 수 있다.

- Redisson
  - 락 획득 재시도를 기본으로 제공한다.
  - pub-sub 방식으로 구현이 되어있기 때문에 lettuce 와 비교했을 때 redis 에 부하가 덜 간다.
  - 별도의 라이브러리를 사용해야한다.
  - lock 을 라이브러리 차원에서 제공해주기 떄문에 사용법을 공부해야 한다.

- 실무에서는
  - 재시도가 필요하지 않은 lock 은 lettuce 활용
  - 재시도가 필요한 경우에는 redisson 를 활용

# 섹션 5 마무리

## Mysql과 Redis 비교하기

- Mysql
  - 이미 Mysql 을 사용하고 있다면 별도의 비용없이 사용가능하다.
  - 어느정도의 트래픽까지는 문제없이 활용이 가능하다.
  - Redis 보다는 성능이 좋지않다.

- Redis
  - 활용중인 Redis 가 없다면 별도의 구축비용과 인프라 관리비용이 발생한다.
  - Mysql 보다 성능이 좋다.
