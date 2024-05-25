package iams.cardgame.tute.ai;

import iams.cardgame.tute.Card;

public interface TuteAI {
    Card calculatePlayerCardBegin();

    Card calculatePlayerCardResponse(Card thrownCard);
}
