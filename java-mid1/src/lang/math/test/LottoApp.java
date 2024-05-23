package lang.math.test;

public class LottoApp {

    public static void main(String[] args) {
        LottoGenerator lottoGenerator = new LottoGenerator();
        lottoGenerator.generate();
        
        lottoGenerator.showResult();
    }
}
