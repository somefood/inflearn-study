package thread.sync.test;

public class Immutable {

    private final int value; // final 키워드로 멀티 쓰레드에서도 괜찮

    public Immutable(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
