package generic.ex3;

import generic.animal.Cat;
import generic.animal.Dog;

public class AnimalHospitalMainV2 {

    public static void main(String[] args) {
        AnimalHospitalV2<Dog> dogHospital = new AnimalHospitalV2<>();
        AnimalHospitalV2<Cat> catHospital = new AnimalHospitalV2<>();
        AnimalHospitalV2<Integer> integerHospital = new AnimalHospitalV2<>(); // 다른 것도 다 들어올 수 있음
        AnimalHospitalV2<Object> objectHospital = new AnimalHospitalV2<>(); // 다른 것도 다 들어올 수 있음
    }
}
