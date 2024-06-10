package generic.ex1;

public class BoxMain1 {

    public static void main(String[] args) {
        final IntegerBox integerBox = new IntegerBox();
        integerBox.set(10); // 오토 박싱
        final Integer integer = integerBox.get();
        System.out.println("integer = " + integer);

        final StringBox stringBox = new StringBox();
        stringBox.set("hello");
        final String str = stringBox.get();
        System.out.println("str = " + str);
    }
}
