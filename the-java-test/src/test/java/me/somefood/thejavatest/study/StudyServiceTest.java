package me.somefood.thejavatest.study;

import me.somefood.thejavatest.domain.Member;
import me.somefood.thejavatest.domain.Study;
import me.somefood.thejavatest.member.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

    @Mock
    MemberService memberService;

    @Mock
    StudyRepository studyRepository;

    @Test
    void createNewStudy(@Mock MemberService memberService, @Mock StudyRepository studyRepository) {
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);

//        Optional<Member> optional = memberService.findById(1L);
//        assertEquals(Optional.empty(), optional);
//
//        memberService.validate(2L);
        Member member = new Member();
        member.setId(1L);
        member.setEmail("hsj4665@gmail.com");

        when(memberService.findById(any())).thenReturn(Optional.of(member)); // any(아무거나) 받아도 리턴

        Optional<Member> findById = memberService.findById(1L);
        assertEquals("hsj4665@gmail.com", findById.get().getEmail());

        Study study = new Study(10, "java");
        studyService.createNewStudy(1L, study);

        doThrow(new IllegalArgumentException()).when(memberService).validate(1L);
        assertThrows(IllegalArgumentException.class, () -> memberService.validate(1L));

        when(memberService.findById(any()))
                .thenReturn(Optional.of(member))
                .thenThrow(new RuntimeException())
                .thenReturn(Optional.empty());

        Optional<Member> byId = memberService.findById(1L);
        assertEquals("hsj4665@gmail.com", byId.get().getEmail());

        assertThrows(RuntimeException.class, () -> memberService.findById(2L));

        assertEquals(Optional.empty(), memberService.findById(3L));
    }

}