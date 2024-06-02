package nested.test;

import nested.test.OuterClass2.InnerClass;

public class OuterClass2Main {

    public static void main(String[] args) {
        OuterClass2 o = new OuterClass2();
        final InnerClass innerClass = o.new InnerClass();
        innerClass.hello();
    }
}
