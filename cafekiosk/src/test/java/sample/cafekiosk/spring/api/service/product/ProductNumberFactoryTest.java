package sample.cafekiosk.spring.api.service.product;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sample.cafekiosk.spring.IntegrationTestSupport;

class ProductNumberFactoryTest extends IntegrationTestSupport {

    @Autowired
    private ProductNumberFactory productNumberFactory;

    @DisplayName("다음 상품 번호를 생성한다.")
    @Test
    void createNextProductNumber() {
        // given
        // when
        final String nextProductNumber = productNumberFactory.createNextProductNumber();
        // then
        assertEquals("001", nextProductNumber);
    }
}
