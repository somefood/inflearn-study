package lang.string.test;

public class TestString10 {

    public static void main(String[] args) {
        String fruits = "apple,banana,mango";

        final String[] strings = fruits.split(",");
        for (String fruit : strings) {
            System.out.println(fruit);
        }

        final String joinedString = String.join("->", strings);
        System.out.println("joinedString = " + joinedString);
    }
}
