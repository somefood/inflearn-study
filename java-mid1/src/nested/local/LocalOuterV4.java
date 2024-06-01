package nested.local;

import java.lang.reflect.Field;

public class LocalOuterV4 {
    
    private int outInstanceVar = 3;

    public Printer process(int paramVar) {
        int localVar = 1; // 지역 변수는 스택 프레임이 종료되는 순간 함께 제거된다.

        class LocalPrinter implements Printer {
            int value = 0;

            public void print() {
                System.out.println("value=" + value);

                value = 30;
                // 인스턴스는 지역변수보다 더 오래 살아남는다.
                System.out.println("localVar=" + localVar);
                System.out.println("paramVar=" + paramVar);
                System.out.println("outInstanceVar=" + outInstanceVar);
            }
        }

        LocalPrinter printer = new LocalPrinter();
        // 만약 localVar의 값을 변경한다면? 다시 캡쳐해야 하나?
        // localVar = 10;
        // paramVar = 20;
        return printer;
    }

    public static void main(String[] args) {
        LocalOuterV4 localOuter = new LocalOuterV4();
        Printer printer = localOuter.process(2);
        // printer.print()를 나중에 시행한다. process()의 스택 프레임이 사라진 이후에 실행
        printer.print();

        // 지역 클래스에 값들을 캡쳐해서 갖고 있게 됨으로써, 메서드가 종료되어도 값을 볼 수 있게 되는 것이다.
        System.out.println("필드 확인");
        final Field[] fields = printer.getClass().getDeclaredFields();
        for (Field field : fields) {
            System.out.println("field = " + field);
        }
    }
}
