package lang.immutable.change;

public class ImmutableMain1 {

    public static void main(String[] args) {
        final ImmutableObj obj1 = new ImmutableObj(10);
        final ImmutableObj obj2 = obj1.add(20);

        System.out.println("obj1 = " + obj1.getValue());
        System.out.println("obj2 = " + obj2.getValue());
    }

}
