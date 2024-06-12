package generic.ex3;

import generic.animal.Cat;
import generic.animal.Dog;

public class AnimalHospitalMainV3 {

    public static void main(String[] args) {


        // Animal 자식들만 수용 가능
        // AnimalHospitalV3<Integer> integerHospital = new AnimalHospitalV3<>();
        // AnimalHospitalV3<Object> objectHospital = new AnimalHospitalV3<>();

        AnimalHospitalV3<Dog> dogHospital = new AnimalHospitalV3<>();
        AnimalHospitalV3<Cat> catHospital = new AnimalHospitalV3<>();

        final Dog dog = new Dog("멍멍이1", 100);
        final Cat cat = new Cat("냐옹이1", 300);

        dogHospital.set(dog);
        dogHospital.checkup();

        catHospital.set(cat);
        catHospital.checkup();

        // dogHospital.set(cat); // 다른 다입 입력: 컴파일 오류 발생

        // 문제2: 개 타입 반환
        dogHospital.set(dog);
        final Dog biggerDog = dogHospital.bigger(new Dog("멍멍이2", 200));
//        final Dog biggerDog = (Dog) dogHospital.bigger(new Cat("냥냥이2", 200)); // 캐스팅 에러 발생함
        System.out.println("biggerDog = " + biggerDog);
    }
}
