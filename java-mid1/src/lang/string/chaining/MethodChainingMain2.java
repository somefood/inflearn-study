package lang.string.chaining;

public class MethodChainingMain2 {

    public static void main(String[] args) {
        final ValueAdder adder = new ValueAdder();
        final ValueAdder adder1 = adder.add(1);
        final ValueAdder adder2 = adder1.add(2);
        final ValueAdder adder3 = adder2.add(3);

        final int result = adder3.getValue();
        System.out.println("result = " + result);

        System.out.println("result = " + adder);
    }
}
