package iams.cardgame.tute.movement;

import iams.cardgame.tute.Card;
import iams.cardgame.tute.tr.Translator;

public record ThrowResponseMovement(Card currentCard) implements Movement {

    @Override
    public String toString(Translator tr) {
        return tr.getCardNameString(this.currentCard);
    }

}
