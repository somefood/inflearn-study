package generic.test.ex3;

import generic.test.ex3.unit.Marine;
import generic.test.ex3.unit.Zealot;

public class UnitUtilTest {

    public static void main(String[] args) {
        final Marine m1 = new Marine("마린1", 40);
        final Marine m2 = new Marine("마린2", 50);
        Marine resultMarine = UnitUtil.maxHp(m1, m2);
        System.out.println("resultMarine = " + resultMarine);

        final Zealot z1 = new Zealot("질럿1", 100);
        final Zealot z2 = new Zealot("질럿2", 150);
        Zealot resultZealot = UnitUtil.maxHp(z1, z2);
        System.out.println("resultZealot = " + resultZealot);
    }
}
