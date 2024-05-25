package iams.cardgame.tute.movement;

import iams.cardgame.tute.CardModel.Rank;
import iams.cardgame.tute.tr.Translator;

public record TuteMovement(Rank rank) implements Movement {

    @Override
    public String toString(Translator tr) {
        return tr.getTuteDeclarationString(this.rank);
    }

}
