package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // 컴포넌트 스캔 대상
@Transactional(readOnly = true) // 트랙잭션 안에서 일어나기
@RequiredArgsConstructor // final 가진 필드만 생성자로 만들어줌
public class MemberService {

    // final 로 지정하는거 추천
    private final MemberRepository memberRepository;

    /**
     * 회원가입
     */
    @Transactional // 클래스 단에 설정한것보다 우선권을 가짐
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // 동시성 문제가 일어날 수 있음
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
