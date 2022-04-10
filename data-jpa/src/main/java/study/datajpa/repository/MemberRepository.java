package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 간단한 쿼리 조회할 때 쓰기 추천
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    // By뒤에 없으면 전체에서 TOP3개
    List<Member> findTop3HelloBy();

//    List<Member> findByUsername(@Param("username") String username);

    // 애플리케이션 로딩 시점에 파싱해서 문법 체크
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

}
