package lang.immutable.change;

public class ImmutableMain2 {

    public static void main(String[] args) {
        final ImmutableObj obj1 = new ImmutableObj(10);
        obj1.add(20); // 불변인데 반환 안 받으면 아무짝에 쓸모없음

        System.out.println("obj1 = " + obj1.getValue());
//        System.out.println("obj2 = " + obj2.getValue());
    }

}
