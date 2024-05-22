package lang.system;

import java.util.Arrays;

public class SystemMain {

    public static void main(String[] args) {
        // 현재 시간(밀리초)를 가져온다.
        long currentTimeMillis = System.currentTimeMillis();
        System.out.println("currentTimeMillis = " + currentTimeMillis);
        
        // 현재 시간(나노초)를 가져온다.)
        long currentTimeNano = System.nanoTime();
        System.out.println("currentTimeNano = " + currentTimeNano);
        
        // 환경 변수를 읽는다.
        System.out.println("getEnv = " + System.getenv());
        
        // 시스템 속성을 읽는다.
        System.out.println("properties = " + System.getProperties());
        System.out.println("Java version: " + System.getProperty("java.version"));
        
        // 배열을 고속으로 복사한다.
        char[] originalArray = {'h', 'e', 'l', 'l', 'o'};
        char[] copiedArray = new char[5];
        
        System.arraycopy(originalArray, 0, copiedArray, 0, originalArray.length); // 시스템, 운영체제 레벨에서 통으로 복사해줌. 짱 빠름 (2~5배 정도)
        
        // 배열 출력
        System.out.println("copiedArray = " + copiedArray);
        System.out.println("copiedArray = " + Arrays.toString(copiedArray));
        
        // 프로그램 종료
        System.exit(0); // 0 정상 종료, 0이 아님: 오류나 예외적인 종료. 가급적 사용하지 말자. 프로그램 의도하지 않고 종료하면 뒷정리가 제대로 안될 수도 있음
        System.out.println("hello"); // 얘 실행 안됨
    }
}
