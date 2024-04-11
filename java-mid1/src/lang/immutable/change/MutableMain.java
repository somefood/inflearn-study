package lang.immutable.change;

public class MutableMain {

    public static void main(String[] args) {
        final MutableObj mutableObj = new MutableObj(10);
        mutableObj.add(20);

        System.out.println("mutableObj = " + mutableObj.getValue());
    }

}
