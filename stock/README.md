## 레이스 컨디션(Race Condition) 이란 ?

- 둘 이상의 스레드가 공유 데이터에 액세스할 수 있고 동시에 변경하려고 할 때 발생하는 문제
  - 둘 이상의 스레드 : 요청
  - 공유 데이터 : 재고 데이터
  - 동시에 변경하려고 할 때 : 업데이트 할때
  - 발생하는 문제 : 값이 정상적으로 바뀌지 않는 문제

- 해결방법
  - 하나의 스레드만 데이터에 액세스 할 수 있도록 한다.

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
