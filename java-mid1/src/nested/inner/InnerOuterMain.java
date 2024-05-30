package nested.inner;

public class InnerOuterMain {

    public static void main(String[] args) {
        InnerOuter outer = new InnerOuter();

        InnerOuter.Inner inner = outer.new Inner(); // 인스턴스 클래스라서 인스턴스를 참조해서 new를 해주어야 함
        inner.print();

        System.out.println("innerClass = " + inner.getClass());
    }
}
