package collection.array;

public class MyArrayListV3Main {

    public static void main(String[] args) {
        final MyArrayListV3 list = new MyArrayListV3();
        list.add("a");
        list.add("b");
        list.add("c");
        System.out.println(list);

        // 원하는 위치에 추가
        System.out.println("addList");
        list.add(3, "addList"); // O(1)
        System.out.println(list);

        list.add(0, "addFirst"); // O(n)
        System.out.println(list);

        // 삭제
        final Object removed1 = list.remove(4);// remove last O(1)
        System.out.println("removed(4) = " + removed1);
        System.out.println(list);

        final Object removed2 = list.remove(0);// remove First O(n)
        System.out.println("remove(0) = " + removed2);
        System.out.println(list);
    }
}
