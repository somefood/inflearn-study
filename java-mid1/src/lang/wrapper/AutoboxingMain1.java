package lang.wrapper;

public class AutoboxingMain1 {

    public static void main(String[] args) {
        // Primitive -> Wrapper
        int value = 7;
        final Integer boxedValue = Integer.valueOf(value);

        // Wrapper -> Primitive
        final int unboxedValue = boxedValue.intValue();

        System.out.println("boxedValue = " + boxedValue);
        System.out.println("unboxedValue = " + unboxedValue);
    }
}
