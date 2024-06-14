package blackjack;

import java.util.Collections;
import java.util.Stack;

public class CardDeck {
    
    private Stack<Card> cards;
    
    public CardDeck() {
        cards = this.generateCards();
        Collections.shuffle(this.cards);
    }

    private Stack<Card> generateCards() {
        Stack<Card> cards = new Stack<>();

        for (Card.Pattern pattern : Card.Pattern.values()) {
            for (Card.Denomination denomination : Card.Denomination.values()) {
                Card card = new Card(pattern, denomination);
                cards.add(card);
            }
        }
        return cards;
    }

    public Stack<Card> getCards() {
        return cards;
    }

    public Card draw() {
        return this.cards.pop();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Card card : cards) {
            sb.append(card.toString());
            sb.append("\n");
        }
        
        return sb.toString();
    }
}
