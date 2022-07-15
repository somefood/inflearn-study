package me.somefood.thejavatest;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StudyTest {


    /**
     * 테스트케이스 매서드명은 snake_case 사용해서 좀 읽기 쉽게 함
     */

    @Test
    @DisplayName("스터디 만들기 fast")
    @FastTest
    void create_new_study() {

//        assumeTrue("LOCAL".equalsIgnoreCase(System.getenv("TEST_ENV")));
        Study study = new Study(20);
        assertAll(
                () -> assertNotNull(study),
                () -> assertEquals(StudyStatus.DRAFT, study.getStatus(), "스터디를 처음 만들면 상태값은 " + StudyStatus.DRAFT + " 여야 한다."),
                () -> assertEquals(StudyStatus.DRAFT, study.getStatus(), () -> "스터디를 처음 만들면 상태값은" + StudyStatus.DRAFT + "한다."),
                () -> assertTrue(study.getLimit() > 0, "스터디 최대 참석 가능 인원은 0보다 커야 한다.")
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Study(-10));
        String message = exception.getMessage();
        assertEquals("limit은 0보다 커야 한다", message);

        assertTimeout(Duration.ofMillis(400), () -> {
            new Study(100);
            Thread.sleep(300);
        });
    }

    @Test
    @DisplayName("스터디 만들기 slow")
    @SlowTest
    void create_new_study1() {
        Study study = new Study();
        assertNotNull(study);
        System.out.println("hello1");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("before all");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("after all");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("before each");
    }

    @AfterEach
    void afterEach() {
        System.out.println("after each");
    }
}