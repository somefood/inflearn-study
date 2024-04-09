package lang.object.tostring;

public class ToStringMain1 {
    public static void main(String[] args) {
        Object object = new Object();
        String string = object.toString();

        // println은 내부적으로 toString을 호출한다.
        System.out.println(string);

        System.out.println(object);
    }
}
