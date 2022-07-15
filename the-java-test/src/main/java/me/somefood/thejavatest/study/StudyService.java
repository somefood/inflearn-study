package me.somefood.thejavatest.study;

import me.somefood.thejavatest.domain.Member;
import me.somefood.thejavatest.domain.Study;
import me.somefood.thejavatest.member.MemberNotFoundException;
import me.somefood.thejavatest.member.MemberService;

import java.util.Optional;

public class StudyService {

    private final MemberService memberService;

    private final StudyRepository studyRepository;

    public StudyService(MemberService memberService, StudyRepository studyRepository) {
        assert memberService != null;
        assert studyRepository != null;
        this.memberService = memberService;
        this.studyRepository = studyRepository;
    }

    public Study createNewStudy(Long memberId, Study study) {
        Optional<Member> member = memberService.findById(memberId);
        study.setOwner(member.orElseThrow(() -> new IllegalArgumentException("Member doesn't exist for id: '" + memberId + "'")));
        return studyRepository.save(study);
    }
}
