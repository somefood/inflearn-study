package collection.array;

public class MyArrayListV3BadMain {

    public static void main(String[] args) {
        final MyArrayListV3 numberList = new MyArrayListV3();

        // 숫자만 입력하기를 기대
        numberList.add(1);
        numberList.add(2);
        numberList.add("문자3"); // 문자를 입력
        System.out.println(numberList);

        // Object를 반환하므로 다운 캐스팅 필요
        final Integer num1 = (Integer) numberList.get(0);
        final Integer num2 = (Integer) numberList.get(1);

        // ClassCastException 발생
        final Integer num3 = (Integer) numberList.get(2);
    }
}