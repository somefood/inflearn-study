package exception.basic.checked;

public class Client {
    public void call() throws MyCheckedException { // 난 해결 못해서 호출된곳에 던진다는 의미
        throw new MyCheckedException("ex");
    }
}
