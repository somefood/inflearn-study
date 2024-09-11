package thread.control;

import static util.MyLogger.log;

import thread.start.HelloRunnable;

public class ThreadInfoMain {

    public static void main(String[] args) {
        // main 스레드
        final Thread mainThread = Thread.currentThread();
        log("mainThread = " + mainThread);
        log("mainThread.threadId()=" + mainThread.threadId()); // 자바가 자동으로 만들어주고 중복되지 않음
        log("mainThread.getName()=" + mainThread.getName()); // 중복 될 수 있음
        log("mainThread.getPriority()=" + mainThread.getPriority()); // 기본값 5, 높다고 운영체제 스케줄러 꼭 더 많이 실행시키진 않음
        log("mainThread.getThreadGroup()=" + mainThread.getThreadGroup());
        log("mainThread.getState()=" + mainThread.getState());

        // myThread 스레드
        final Thread myThread = new Thread(new HelloRunnable(), "myThread");
        log("myThread = " + myThread);
        log("myThread.threadId()=" + myThread.threadId()); // 자바가 자동으로 만들어주고 중복되지 않음
        log("myThread.getName()=" + myThread.getName()); // 중복 될 수 있음
        log("myThread.getPriority()=" + myThread.getPriority()); // 기본값 5, 높다고 운영체제 스케줄러 꼭 더 많이 실행시키진 않음
        log("myThread.getThreadGroup()=" + myThread.getThreadGroup());
        log("myThread.getState()=" + myThread.getState());
    }
}
