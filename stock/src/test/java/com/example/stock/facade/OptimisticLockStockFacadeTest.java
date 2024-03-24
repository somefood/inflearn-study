package com.example.stock.facade;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OptimisticLockStockFacadeTest {
    @Autowired
    private OptimisticLockStockFacade optimisticLockStockFacade;

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    public void before() {
        stockRepository.saveAndFlush(new Stock(1L, 100L));
    }

    @AfterEach
    public void after() {
        stockRepository.deleteAll();
    }

    @Test
    void 동시에_100개의_요청() throws InterruptedException {
        int threadCount = 100;
        final ExecutorService executorService = Executors.newFixedThreadPool(32); // 비동기로 실행하는 작업을 단순화하여 사용할 수 있게 해줌. 쓰레드풀 만들어줌
        final CountDownLatch latch = new CountDownLatch(threadCount); // 다른 쓰레드에서 수행 중인 작업이 완료될 때까지 대기할 수 있도록 해줌

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    optimisticLockStockFacade.decrease(1L, 1L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        final Stock stock = stockRepository.findById(1L).orElseThrow();
        // 100 - (1 * 100) = 0
        assertThat(stock.getQuantity()).isEqualTo(0L);
    }
}
