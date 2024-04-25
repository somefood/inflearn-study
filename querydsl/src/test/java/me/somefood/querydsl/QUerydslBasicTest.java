package me.somefood.querydsl;

import static me.somefood.querydsl.entity.QMember.member;
import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import me.somefood.querydsl.entity.Member;
import me.somefood.querydsl.entity.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class QUerydslBasicTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;


    @BeforeEach
    void before() {
        queryFactory = new JPAQueryFactory(em);
        final Team teamA = new Team("teamA");
        final Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        final Member member1 = new Member("member1", 10, teamA);
        final Member member2 = new Member("member2", 20, teamA);

        final Member member3 = new Member("member3", 30, teamB);
        final Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    void startJPQL() {
        // member1 find
        final String qlString =
            "select m from Member m " +
            "where m.username = :username";
        final Member findMember = em.createQuery(qlString, Member.class)
            .setParameter("username", "member1")
            .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    void startQuerydsl() {
//        final QMember m = new QMember("m");
        final Member findMember = queryFactory
            .select(member)
            .from(member)
            .where(member.username.eq("member1")) // 파라미터 바인딩 처리
            .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    void search() {
        final Member findMember = queryFactory
            .selectFrom(member)
            .where(member.username.eq("member1")
                       .and(member.age.eq(10)))
            .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    // where 메서드 안에 연속해서 넣으면 and로 인식됨
    @Test
    void searchAndParam() {
        final Member findMember = queryFactory
            .selectFrom(member)
            .where(
                member.username.eq("member1"),
                member.age.between(10, 30)
            )
            .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    void resultFetch() {
//        final List<Member> fetch = queryFactory
//            .selectFrom(member)
//            .fetch();
//
//        final Member fetchOne = queryFactory
//            .selectFrom(member)
//            .fetchOne();
//
//        final Member fetchFirst = queryFactory
//            .selectFrom(member)
//            .fetchFirst();

//        final QueryResults<Member> results = queryFactory
//            .selectFrom(member)
//            .fetchResults();
//
//        results.getTotal();
//        final List<Member> content = results.getResults();

        final long total = queryFactory
            .selectFrom(member)
            .fetchCount();
    }
}
