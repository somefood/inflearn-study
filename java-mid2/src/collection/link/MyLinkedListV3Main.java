package collection.link;

public class MyLinkedListV3Main {

    public static void main(String[] args) {
        MyLinkedListV3<String> list = new MyLinkedListV3<>();
        
        // 마지막에 추가 O(n)
        list.add("a");
        list.add("b");
        list.add("c");
//        list.add(1);

        System.out.println(list);
        
        // 첫 번째 항목에 추가, 삭제
        System.out.println("첫 번째 항목에 추가");
        list.add(0, "d"); // O(1)
        System.out.println(list);

        System.out.println("첫 번째 항목에 삭제");
        list.remove(0); // remove First O(1)
        System.out.println(list);
        
        // 중간 항목에 추가 삭제
        System.out.println("중간 항목에 추가 삭제");
        list.add(1, "e"); // O(n)
        System.out.println(list);

        System.out.println("중간 항목에 삭제");
        list.remove(1); // remove O(n)
        System.out.println(list);
    }
}
