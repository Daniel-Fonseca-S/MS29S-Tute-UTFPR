package iams.cardgame.tute;

import iams.cardgame.tute.CardModel.Suit;

import java.util.Collection;
import java.util.SortedSet;

public class TuteGamePlayerContext {
    private final TuteGame game;
    private final boolean player1;

    public TuteGamePlayerContext(TuteGame game, boolean player1) {
        this.game = game;
        this.player1 = player1;
    }

    public Collection<Card> getMyCards() {
        return this.player1 ? this.game.getPlayer1Cards() : this.game.getPlayer2Cards();
    }

    public SortedSet<Card> calculateAllowedCardsToAvoidRenuncio(Card firstCard) {
        return this.game.calculateAllowedCardsToAvoidRenuncio(firstCard, this.getMyCards());
    }

    public Suit getPintaSuit() {
        return this.game.getPinta().suit;
    }

    public boolean isDeckEmpty() {
        return this.game.isDeckEmpty();
    }

}
