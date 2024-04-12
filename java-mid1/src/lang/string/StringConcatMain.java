package lang.string;

public class StringConcatMain {

    public static void main(String[] args) {
        String a = "hello";
        String b = " java";

        String result1 = a.concat(b);
        String result2 = a + b; // String만 자주 쓰이기 때문에 + 연산 허용해준것

        System.out.println("result1 = " + result1);
        System.out.println("result2 = " + result2);
    }
}
