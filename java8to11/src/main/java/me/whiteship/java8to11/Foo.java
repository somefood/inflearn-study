package me.whiteship.java8to11;

public class Foo {

    public static void main(String[] args) {

        // 익명 내부 클래스 anonymous inner class
        RunSomething runSomething = (number) -> number + 10;

        System.out.println(runSomething.doIt(1));
        System.out.println(runSomething.doIt(1));
        System.out.println(runSomething.doIt(1));
    }
}
