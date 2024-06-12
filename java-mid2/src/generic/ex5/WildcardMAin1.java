package generic.ex5;

import generic.animal.Animal;
import generic.animal.Cat;
import generic.animal.Dog;

public class WildcardMAin1 {

    public static void main(String[] args) {
        Box<Object> objBox = new Box<>();
        Box<Dog> dogBox = new Box<>();
        Box<Cat> catBox = new Box<>();

        dogBox.set(new Dog("멍멍이", 100));
        WildcardEx.printGenericV1(dogBox);
        WildcardEx.printWildcardV1(dogBox);

        WildcardEx.printGenericV2(dogBox);
        WildcardEx.printWildcardV2(dogBox);
        final Dog dog = WildcardEx.printAndReturnGeneric(dogBox);
        System.out.println("dog = " + dog);

        final Animal animal = WildcardEx.printAndReturnWildcard(dogBox);
        System.out.println("animal = " + animal);
    }
}
