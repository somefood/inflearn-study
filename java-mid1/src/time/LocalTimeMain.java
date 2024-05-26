package time;

import java.time.LocalTime;

public class LocalTimeMain {

    public static void main(String[] args) {
        final LocalTime nowTime = LocalTime.now();
        final LocalTime ofTime = LocalTime.of(9, 10, 30);

        System.out.println("현재 시간 = " + nowTime);
        System.out.println("지정 시간 = " + ofTime);

        final LocalTime ofTimePlus = ofTime.plusSeconds(30);
        System.out.println("지정 시간+30s = " + ofTimePlus);
    }
}
