package time.test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TestZone {

    public static void main(String[] args) {
        LocalDateTime meetingDateTime = LocalDateTime.of(2024, 1, 1, 9, 0, 0);
        ZonedDateTime seoulDateTime = ZonedDateTime.of(meetingDateTime, ZoneId.of("Asia/Seoul"));
        ZonedDateTime londonDateTime = seoulDateTime.withZoneSameInstant(ZoneId.of("Europe/London"));
        ZonedDateTime newYorkDateTime = seoulDateTime.withZoneSameInstant(ZoneId.of("America/New_York"));
        
        
        System.out.println("서울의 회의 시간: " + seoulDateTime);
        System.out.println("런던의 회의 시간: " + londonDateTime);
        System.out.println("뉴욕의 회의 시간: " + newYorkDateTime);
    }
}
