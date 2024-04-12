package lang.string.test;

public class TestString8 {

    public static void main(String[] args) {
        String original = "Hello Java";

        final String replace = original.replace("Java", "Jvm");
        System.out.println("replace = " + replace);
    }
}
