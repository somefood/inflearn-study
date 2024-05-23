
package enumeration.ex2;

public class DiscountService {
    
    public int discount(ClassGrade grade, int price) {
        int discountPercent = 0;

        if (grade == ClassGrade.BASIC) {
            discountPercent = 10;
        } else if (grade == ClassGrade.GOLD) {
            discountPercent = 20;
        } else if (grade == ClassGrade.DIAMOND) {
            discountPercent = 30;
        } else {
            System.out.println("할인X");
        }
        
        // 10000 * (20 / 100) -> 2000
        return price * discountPercent / 100;
    }
}
