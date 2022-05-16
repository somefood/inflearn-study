package hello.proxy.jdkdynamic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class ReflectionTest {

    @Test
    void reflection0() {
        Hello target = new Hello();

        // 공통 로직1 시작
        log.info("start");
        String result1 = target.callA();
        log.info("result={}", result1);

        // 공통 로직2 시작
        log.info("start");
        String result2 = target.callA();
        log.info("resul={}", result2);
    }

    @Test
    void reflection1() throws Exception {
        // 클래스 정보 획득
        Class<?> classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");// 내부 클래스는 앞에 $

        Hello target = new Hello();
        // callA

        Method methodCallA = classHello.getMethod("callA");
        Object result1 = methodCallA.invoke(target);

        log.info("result={}", result1);

        Method methodCallB = classHello.getMethod("callB");
        Object result2 = methodCallA.invoke(target);

        log.info("result={}", result2);

    }

    @Test
    void reflection2() throws Exception {
        // 클래스 정보 획득
        Class<?> classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");// 내부 클래스는 앞에 $

        Hello target = new Hello();
        // callA

        Method methodCallA = classHello.getMethod("callA");
        dynamicCall(methodCallA, target);


        Method methodCallB = classHello.getMethod("callB");
        dynamicCall(methodCallB, target);
    }

    private void dynamicCall(Method method, Object target) throws Exception {
        log.info("start");
        Object result = method.invoke(target);
        log.info("result={}", result);
    }

    @Slf4j
    static class Hello {
        public String callA() {
            log.info("call A");
            return "A";
        }

        public String callB() {
            log.info("call B");
            return "B";
        }
    }
}
