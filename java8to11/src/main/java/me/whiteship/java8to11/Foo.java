package me.whiteship.java8to11;

import java.util.function.*;

public class Foo {

    public static void main(String[] args) {

        // 익명 내부 클래스 anonymous inner class
        RunSomething runSomething = (number) -> number + 10;

        System.out.println(runSomething.doIt(1));

        Plus10 plus10 = new Plus10();
        System.out.println(plus10.apply(1));

        Function<Integer, Integer> p10 = (i) -> i + 10;
        UnaryOperator<Integer> p102 = (i) -> i + 10;
        Function<Integer, Integer> multiply2 = (i) -> i * 2;

        Function<Integer, Integer> multiply2AndPlus10 = p10.compose(multiply2);// 매개변수꺼 적용하고 그 값을 입력값으로
        System.out.println(multiply2AndPlus10.apply(2));

        Function<Integer, Integer> plus10AndMultiply2 = p10.andThen(multiply2); // 먼저 하고 이후꺼 연산
        System.out.println(plus10AndMultiply2.apply(2));

        Consumer<Integer> printT = (i) -> System.out.println(i);
        printT.accept(10);

        Supplier<Integer> get10 = () -> 10; // return 10
        System.out.println(get10.get());

        Predicate<String> startsWithSeokju = (s) -> s.startsWith("Seokju");
        Predicate<Integer> isEven = (i) -> i%2 == 0;
    }
}
