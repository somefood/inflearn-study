package blackjack;

import java.util.Scanner;

public class Game {

    private static final int INIT_RECEIVE_CARD_COUNT = 2;

    private void initPhase(CardDeck cardDeck, Gamer gamer, Dealer dealer) {
        System.out.println("처음 2장의 카드를 각자 뽑겠습니다.");
        for (int i = 0; i < INIT_RECEIVE_CARD_COUNT; i++) {
            Card card = cardDeck.draw();
            gamer.receiveCard(card);

            Card card2 = cardDeck.draw();
            dealer.receiveCard(card2);
        }
    }


    public void play() {
        System.out.println("======== Blackjack Game =======");
        Scanner sc = new Scanner(System.in);

        Dealer dealer = new Dealer();
        Gamer gamer = new Gamer();
        Rule rule = new Rule();
        CardDeck cardDeck = new CardDeck();

        initPhase(cardDeck, gamer, dealer);
        playingPhase(sc, cardDeck, gamer, dealer);
    }

    private void playingPhase(Scanner sc, CardDeck cardDeck, Gamer gamer, Dealer dealer) {
        String gamerInput, dealerInput;
        boolean isGamerTurn = false,
                isDealerTurn = false;

        while (true) {
            System.out.println("카드를 뽑겠습니까? 종료를 원하시면 0을 입력하세요.");
            gamerInput = sc.nextLine();

            if ("0".equals(gamerInput)) {
                isGamerTurn = true;
            } else {
                Card card = cardDeck.draw();
                gamer.receiveCard(card);
            }

            System.out.println("카드를 뽑겠습니까? 종료를 원하시면 0을 입력하세요.");
            dealerInput = sc.nextLine();

            if ("0".equals(dealerInput)) {
                isDealerTurn = true;
            } else {
                Card card = cardDeck.draw();
                dealer.receiveCard(card);
            }

            if (isGamerTurn && isDealerTurn) {
                break;
            }
        }
    }
}
