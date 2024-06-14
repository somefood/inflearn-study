package blackjack;

import java.util.ArrayList;
import java.util.List;

public class Dealer implements Player {
    
    private List<Card> cards;
    private boolean turn;
    
    private static final int CAN_RECEIVE_POINT = 16;
    private static final String NAME = "딜러";

    public Dealer() {
        cards = new ArrayList<>();
    }
    
    @Override
    public void receiveCard(Card card) {
        if (this.isReceivedCard()) {
            this.cards.add(card);
            this.showCards();
        } else {
            System.out.println("카드의 총 합이 17이상입니다. 더이상 카드를 받을 수 없습니다.");
        }
    }

    @Override
    public void showCards(){
        StringBuilder sb = new StringBuilder();
        sb.append("현재 보유 카드 목록 \n");

        for(Card card : cards){
            sb.append(card.toString());
            sb.append("\n");
        }

        System.out.println(sb.toString());
    }

    @Override
    public List<Card> openCards() {
        return this.cards;
    }

    @Override
    public void turnOff() {
        this.setTurn(false);
    }
    
    @Override
    public void turnOn() {
        this.setTurn(true);
    }

    @Override
    public boolean isTurn() {
        return this.turn;
    }

    @Override
    public String getName() {
        return NAME;
    }

    private void setTurn(boolean turn) {
        this.turn = turn;
    }

    private boolean isReceivedCard() {
        return getPointSum() <= CAN_RECEIVE_POINT;
    }
    
    private int getPointSum() {
        int sum = 0;
        for (Card card : cards) {
            sum += card.getPoint();
        }
        return sum;
    }
}
