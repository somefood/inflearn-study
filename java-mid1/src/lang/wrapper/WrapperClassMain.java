package lang.wrapper;

public class WrapperClassMain {

    public static void main(String[] args) {
        final Integer newInteger = new Integer(10); // 미래 삭제 예정, 대신에 valueOf() 사용. 호환성 중시해서 언제 없어질진 모름 ㅎ
//        final Integer newInteger = Integer.valueOf(10); // 이렇게 하면 == 도 true 나옴
        final Integer integerObj = Integer.valueOf(10); // -128~127 자주 사용하는 숫자 값 재사용, 불변 (성능 최적화)
        final Long longObj = Long.valueOf(100);
        final Double doubleObj = Double.valueOf(10.5);

        System.out.println("newInteger = " + newInteger);
        System.out.println("integerObj = " + integerObj);
        System.out.println("longObj = " + longObj);
        System.out.println("doubleObj = " + doubleObj);

        System.out.println("내부 값 읽기");
        final int intValue = integerObj.intValue();
        System.out.println("intValue = " + intValue);

        final long longValue = longObj.longValue();
        System.out.println("longValue = " + longValue);

        System.out.println("비교");
        System.out.println("==: " + (newInteger == integerObj));
        System.out.println("equals " + (newInteger.equals(integerObj)));
    }
}
