package lang.string.chaining;

public class MethodChainingMain1 {

    public static void main(String[] args) {
        final ValueAdder adder = new ValueAdder();
        adder.add(1);
        adder.add(2);
        adder.add(3);

        final int result = adder.getValue();
        System.out.println("result = " + result);
    }

}
