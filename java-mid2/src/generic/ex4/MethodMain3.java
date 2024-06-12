package generic.ex4;

import generic.animal.Cat;
import generic.animal.Dog;

public class MethodMain3 {

    public static void main(String[] args) {
        final Dog dog = new Dog("멍멍이", 100);
        final Cat cat = new Cat("냐옹이", 50);

        final ComplexBox<Dog> hospital = new ComplexBox<>();
        hospital.set(dog);

        final Cat returnCat = hospital.printAndReturn(cat);
        System.out.println("returnCat = " + returnCat);
    }
}
