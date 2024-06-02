package nested.test;

import nested.test.OuterClass1.NestedClass;

public class OuterClass1Main {

    public static void main(String[] args) {
        final NestedClass nestedClass = new NestedClass();

        nestedClass.hello();
    }
}
