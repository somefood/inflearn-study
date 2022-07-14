package me.somefood.thejavatest;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StudyTest {


    /**
     * 테스트케이스 매서드명은 snake_case 사용해서 좀 읽기 쉽게 함
     */

    @Test
    @DisplayName("스터디 만들기 😄")
    void create_new_study() {
        Study study = new Study();
        assertNotNull(study);
        System.out.println("hello");
    }

    @Test
    @DisplayName("스터디 만들기231231")
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