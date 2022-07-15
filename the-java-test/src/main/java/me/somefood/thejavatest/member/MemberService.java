package me.somefood.thejavatest.member;

import me.somefood.thejavatest.domain.Member;
import me.somefood.thejavatest.domain.Study;

import java.util.Optional;

public interface MemberService {

    Optional<Member> findById(Long memberId);

    void validate(Long memberId);

    void notify(Study newStudy);

    void notify(Member member);
}
