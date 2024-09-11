package thread.start;

import static util.MyLogger.log;

public class InnerRunnableMainV1 {

    public static void main(String[] args) {
        log("main() start");

        final MyRunnable runnable = new MyRunnable();
        final Thread thread = new Thread(runnable);
        thread.start();

        log("main() end");
    }

    static class MyRunnable implements Runnable {
        @Override
        public void run() {
            log("MyRunnable.run() start");
            log("MyRunnable.run() end");
        }
    }
}
