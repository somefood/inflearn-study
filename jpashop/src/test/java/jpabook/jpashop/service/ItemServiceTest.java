package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Album;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.item.Movie;
import jpabook.jpashop.repository.ItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemServiceTest {

    @Autowired ItemService itemService;
    @Autowired ItemRepository itemRepository;

    @Test
    @DisplayName("상품 추가")
    void addItem() throws Exception {
        // given
        Item book = new Book();
        book.setName("노인과 바다");

        // when
        itemService.saveItem(book);
        List<Item> items = itemService.findItems();

        // then
        Assertions.assertThat(book.getName()).isEqualTo(items.stream().findFirst().get().getName());
    }
}