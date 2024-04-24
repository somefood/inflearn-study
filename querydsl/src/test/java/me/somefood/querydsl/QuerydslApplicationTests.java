package me.somefood.querydsl;

import static me.somefood.querydsl.entity.QHello.hello;
import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import me.somefood.querydsl.entity.Hello;
import me.somefood.querydsl.entity.QHello;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Commit
class QuerydslApplicationTests {

    @Autowired
    EntityManager em;

    @Test
    void contextLoads() {
        Hello newHello = new Hello();
        em.persist(newHello);

        JPAQueryFactory query = new JPAQueryFactory(em);;

        final Hello result = query
            .selectFrom(hello)
            .fetchOne();

        assertThat(result).isEqualTo(newHello);
        assertThat(result.getId()).isEqualTo(newHello.getId());
    }

}
