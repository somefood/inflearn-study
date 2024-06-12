package generic.ex3;

import generic.animal.Animal;
import generic.animal.Cat;
import generic.animal.Dog;

public class AnimalHospitalMainV1 {

    public static void main(String[] args) {
        final AnimalHospitalV1 dogHospital = new AnimalHospitalV1();
        final AnimalHospitalV1 catHospital = new AnimalHospitalV1();

        final Dog dog = new Dog("멍멍이1", 100);
        final Cat cat = new Cat("냐옹이1", 300);

        dogHospital.set(dog);
        dogHospital.checkup();

        catHospital.set(cat);
        catHospital.checkup();

        dogHospital.set(cat); // 다른 타입 입력: 컴파일 오류가 발생하지 않음. 둘 다 Animal 자식이니

        // 문제2: 개 타입 반환
        dogHospital.set(dog);
        final Dog biggerDog = (Dog) dogHospital.bigger(new Dog("멍멍이2", 200));
//        final Dog biggerDog = (Dog) dogHospital.bigger(new Cat("냥냥이2", 200)); // 캐스팅 에러 발생함
        System.out.println("biggerDog = " + biggerDog);
    }
}
