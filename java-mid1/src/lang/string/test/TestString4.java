package lang.string.test;

public class TestString4 {

    public static void main(String[] args) {
        String str = "hello.txt";
        int extensionIndex = str.indexOf(".txt");
        final String fileName = str.substring(0, extensionIndex);
        final String extName = str.substring(extensionIndex);

        System.out.println("fileName = " + fileName);
        System.out.println("extName = " + extName);
    }

}
