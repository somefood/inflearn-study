package lang.math.test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class LottoGenerator {
    private final Random random = new Random();
    private final Set<Integer> lottoNumbers = new HashSet<>();
    
    void generate() {
        
        while (lottoNumbers.size() <= 6) {
            lottoNumbers.add(random.nextInt(45) + 1);
        }
    }
    
    void showResult() {
        System.out.println(lottoNumbers);
    }
}
