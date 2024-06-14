package blackjack;

import java.util.ArrayList;
import java.util.List;

public class Gamer {

    private List<Card> cards;

    public Gamer() {
        cards = new ArrayList<>();
    }

    public void receiveCard(Card card) {
        this.cards.add(card);
        this.showCards();
    }
    
    public List<Card> openCards() {
        return this.cards;
    }
    
    public void showCards() {
        StringBuilder sb = new StringBuilder();
        sb.append("현재 보유 카드 목록 \n");

        for (Card card : cards) {
            sb.append(card.toString());
            sb.append("\n");
        }

        System.out.println(sb);
    }
}
