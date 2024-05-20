package lang.wrapper;

public class WrapperUtilsMain {

    public static void main(String[] args) {
        final Integer i1 = Integer.valueOf(10); // 숫자, 래퍼 객체 변환
        final Integer i2 = Integer.valueOf("10");// 문자열, 래퍼 객체 변환
        final int intValue = Integer.parseInt("10");// 문자열 전용, 기본형 변환

        // 비교
        final int compareResult = i1.compareTo(20); // 내 값이 크면 1, 같으면 0, 내 값이 작으면 -1 반환
        System.out.println("compareResult = " + compareResult);

        // 산술 연산
        System.out.println("sum: " + Integer.sum(10, 20));
        System.out.println("min: " + Integer.min(10, 20));
        System.out.println("max: " + Integer.max(10, 20));
    }

}
