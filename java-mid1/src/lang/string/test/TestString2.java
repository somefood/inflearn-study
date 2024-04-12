package lang.string.test;

public class TestString2 {

    public static void main(String[] args) {
        String[] arr = {"hello", "java", "jvm", "spring", "jpa"};
        int total = 0;

        for (String s : arr) {
            final int length = s.length();
            System.out.println(s + ":" + length);
            total += length;
        }
        System.out.println("sum = " + total);
    }
}
