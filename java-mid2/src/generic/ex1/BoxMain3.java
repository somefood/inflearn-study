package generic.ex1;

public class BoxMain3 {

    public static void main(String[] args) {
        GenericBox<Integer> integerBox = new GenericBox<Integer>(); // 생성 시점에 T의 타입 결정
        integerBox.set(10);
        // integerBox.set("문자열100"); // Integer 타입만 허용, 컴파일 오류
        final Integer integer = integerBox.get(); // Integer 타입 변환(캐스팅 X)
        System.out.println("integer = " + integer);

        final GenericBox<String> stringBox = new GenericBox<String>();
        stringBox.set("hello");
        final String str = stringBox.get();
        System.out.println("str = " + str);

        // 원하는 모든 타입 사용 가능
        final GenericBox<Double> doubleBox = new GenericBox<>();
        doubleBox.set(10.5);
        final Double doubleValue = doubleBox.get();
        System.out.println("doubleValue = " + doubleValue);

        // 타입 추론: 생성하는 제네릭 타입 생략 가능
        GenericBox<Integer> integerBox2 = new GenericBox<>();
    }
}
