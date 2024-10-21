package sample.cafekiosk.spring.domain.order;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sample.cafekiosk.spring.IntegrationTestSupport;
import sample.cafekiosk.spring.domain.orderproduct.OrderProduct;
import sample.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
    }

    @DisplayName("지정한 날짜에 지정한 상태의 오더 정보들을 갖고온다.")
    @Test
    void findOrdersBy() {
        // given
        final Product product = createProduct("001", ProductType.HANDMADE, ProductSellingStatus.SELLING, "아메리카노", 4000);
        productRepository.save(product);

        final LocalDateTime registeredDateTime = LocalDateTime.now();
        final Order order = Order.create(List.of(product), registeredDateTime);
        orderRepository.save(order);

        final OrderProduct orderProduct = new OrderProduct(order, product);
        orderProductRepository.save(orderProduct);

        // when
        final List<Order> orders = orderRepository.findOrdersBy(registeredDateTime.minusDays(1L),
                                                                  registeredDateTime.plusDays(1L),
                                                                  OrderStatus.INIT);

//        final List<Order> orders = orderRepository.findAll();

        // then
        assertThat(orders).hasSize(1)
            .extracting("orderStatus")
            .contains(OrderStatus.INIT);
    }

    private Product createProduct(
        String productNumber,
        ProductType type,
        ProductSellingStatus sellingStatus,
        String name,
        int price
    ) {
        return Product.builder()
            .productNumber(productNumber)
            .type(type)
            .sellingStatus(sellingStatus)
            .name(name)
            .price(price)
            .build();
    }
}
