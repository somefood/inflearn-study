package generic.ex3;

import generic.animal.Cat;
import generic.animal.Dog;

public class AnimalHospitalMainV0 {

    public static void main(String[] args) {
        final DogHospital dogHospital = new DogHospital();
        final CatHospital catHospital = new CatHospital();

        final Dog dog = new Dog("멍멍이1", 100);
        final Cat cat = new Cat("냐옹이1", 300);

        dogHospital.set(dog);
        dogHospital.checkup();

        catHospital.set(cat);
        catHospital.checkup();

//        dogHospital.set(cat); // 다른 타입 입력: 컴파일 오류

        // 문제2: 개 타입 반환
        dogHospital.set(dog);
        final Dog biggerDog = dogHospital.bigger(new Dog("멍멍이2", 200));
        System.out.println("biggerDog = " + biggerDog);
    }
}
