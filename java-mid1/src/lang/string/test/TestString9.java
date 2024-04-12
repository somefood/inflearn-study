package lang.string.test;

public class TestString9 {

    public static void main(String[] args) {
        String email = "hello@example.com";

        final String[] strings = email.split("@");

        System.out.println("ID: " + strings[0]);
        System.out.println("Domain: " + strings[1]);
    }
}
