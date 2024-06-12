package generic.ex4;

public class MethodMain1 {

    public static void main(String[] args) {
        Integer i = 10;
        final Object object = GenericMethod.objMethod(i);
//        final Integer result = (Integer) GenericMethod.objMethod(i);

        // 타입 인자(Type Argument) 명시적 전달
        System.out.println("명시적 타입 인자 전달");
        final Integer result = GenericMethod.<Integer>genericMethod(i);
        final Integer integerValue = GenericMethod.<Integer>numberMethod(10);
        final Double doubleValue = GenericMethod.<Double>numberMethod(20.0);

        System.out.println("타입 추론");
        final Integer result1 = GenericMethod.genericMethod(i);
        final Integer integerValue1 = GenericMethod.numberMethod(10);
        final Double doubleValue1 = GenericMethod.numberMethod(20.0);
    }
}
