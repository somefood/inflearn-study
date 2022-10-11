package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class CheckedTest {

    @Test
    void checkedCatch() {
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void checkedThrow() {
        Service service = new Service();
        assertThatThrownBy(() -> service.callThrow())
                .isInstanceOf(MyCheckedException.class);
    }

    /**
     * Exception을 상속받은 예외는 체크가 됨
     */
    static class MyCheckedException extends Exception {
        public MyCheckedException(String message) {
            super(message);
        }
    }

    /**
     * Checked 예외는
     * 예외를 잡아서 처리하거나, 던지거나 둘 중 하나를 필수로 선택해야 한다.
     */
    static class Service {
        Repository repository = new Repository();

        /**
         * 예외를 잡아서 처리하는 코드
         */
        public void callCatch() {
            try {
                repository.call(); // 안 잡으면 컴파일 오류
            } catch (MyCheckedException e) {
                // 예외 처리
                log.info("예외 처리 message={}", e.getMessage(), e); // 마지막 파라미터에 예외 넣으면 출력
            }
        }

        /**
         * 체크 예외를 밖으로 던지는 코드
         * 체크 예외는 잡지 않고 던지려면 throws 키워드를 메소드에 필수로 선언해야 함
         * @throws MyCheckedException
         */
        public void callThrow() throws MyCheckedException {
            repository.call();
        }
    }

    static class Repository {
        public void call() throws MyCheckedException {
            throw new MyCheckedException("ex"); // 잡거나 던지지 않으면 컴파일 에러 뜸
        }
    }
}
