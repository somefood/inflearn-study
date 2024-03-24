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
