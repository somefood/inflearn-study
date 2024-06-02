package nested.test;

public class AnonymousMain {

    public static void main(String[] args) {
        final Hello hello = new Hello() {
            @Override
            public void hello() {
                System.out.println("AnonymousMain.hello");
            }
        };

        hello.hello();
    }
}
