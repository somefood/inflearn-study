package generic.test.ex2;

public class Pair<T1, T2> {

    private T1 firstData;
    private T2 secondData;

    public void setFirst(T1 firstData) {
        this.firstData = firstData;
    }

    public void setSecond(T2 secondData) {
        this.secondData = secondData;
    }

    public T1 getFirst() {
        return firstData;
    }

    public T2 getSecond() {
        return secondData;
    }

    @Override
    public String toString() {
        return "Pair{" +
            "firstData=" + firstData +
            ", secondData=" + secondData +
            '}';
    }
}
