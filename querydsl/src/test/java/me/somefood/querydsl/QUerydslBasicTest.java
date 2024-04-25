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

    /**
     * 회원 정렬 순서
     * 1. 회원 나이 내림차순(desc)
     * 2. 회원 이름 올림차순(asc)
     * 단, 2에서 회원 이름이 없으면 마지막에 출력(nulls last)
     */
    @Test
    void sort() {
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        final List<Member> result = queryFactory
            .selectFrom(member)
            .where(member.age.eq(100))
            .orderBy(member.age.desc(), member.username.asc().nullsLast())
            .fetch();

        final Member member5 = result.get(0);
        final Member member6 = result.get(1);
        final Member memberNull = result.get(2);

        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();
    }

    @Test
    void paging1() {
        final List<Member> result = queryFactory
            .selectFrom(member)
            .orderBy(member.username.desc())
            .offset(1)
            .limit(2)
            .fetch();

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void paging2() {
        final QueryResults<Member> queryResults = queryFactory
            .selectFrom(member)
            .orderBy(member.username.desc())
            .offset(1)
            .limit(2)
            .fetchResults();

        assertThat(queryResults.getTotal()).isEqualTo(4);
        assertThat(queryResults.getLimit()).isEqualTo(2);
        assertThat(queryResults.getOffset()).isEqualTo(1);
        assertThat(queryResults.getResults().size()).isEqualTo(2);
    }
}
