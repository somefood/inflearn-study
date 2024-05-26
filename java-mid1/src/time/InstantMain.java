package time;

import java.time.Instant;
import java.time.ZonedDateTime;

public class InstantMain {

    public static void main(String[] args) {
        
        // 생성
        final Instant now = Instant.now(); // UTC 기준
        System.out.println("now = " + now);

        final ZonedDateTime zdt = ZonedDateTime.now();
        final Instant from = Instant.from(zdt);
        System.out.println("from = " + from);

        final Instant epochStart = Instant.ofEpochSecond(0);
        System.out.println("epochStart = " + epochStart);

        // 계산
        final Instant later = epochStart.plusSeconds(3600);
        System.out.println("later = " + later);

        // 조회
        final long laterEpochSecond = later.getEpochSecond();
        System.out.println("laterEpochSecond = " + laterEpochSecond);
    }
}
