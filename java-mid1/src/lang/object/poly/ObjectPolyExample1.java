package lang.object.poly;

public class ObjectPolyExample1 {
    public static void main(String[] args) {
        Dog dog = new Dog();
        Car car = new Car();

        action(dog);
        action(car);
    }

    private static void action(Object obj) {
//        obj.sound(); // 컴파일 오류, Object는 해당 메서드가 없음!
//        obj.move(); // 컴파일 오류, Object는 해당 메서드가 없음!

        // 객체에 맞는 다운캐스팅 필요
        if (obj instanceof Dog dog) { // 이렇게 다운 캐스팅 가능 ((Dog) obj).sound()
            dog.sound();
        } else if (obj instanceof Car car) {
            car.move();
        }
    }
}
