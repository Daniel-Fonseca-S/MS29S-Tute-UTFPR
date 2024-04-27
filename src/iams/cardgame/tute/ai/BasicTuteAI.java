package iams.cardgame.tute.ai;

import iams.cardgame.tute.Card;
import iams.cardgame.tute.CardModel.Rank;
import iams.cardgame.tute.TuteGamePlayerContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

import static iams.cardgame.tute.ai.SmartTuteAI.addHighRankSameSuitCardsIfNotPinta;

public class BasicTuteAI implements TuteAI {
    private final TuteGamePlayerContext context;

    public BasicTuteAI(TuteGamePlayerContext context) {
        this.context = context;
    }

    static void getMinimumPintValue(Collection<Card> myCards, ArrayList<Card> candidateCardsToThrow, TuteGamePlayerContext context) {
        int minimumValue = Rank.Ace.countValue;

        for (Card c : myCards) {
            if (c.suit != context.getPintaSuit()) {
                if (c.rank.countValue == minimumValue) {
                    candidateCardsToThrow.add(c);
                } else if (c.rank.countValue < minimumValue) {
                    minimumValue = c.rank.countValue;

                    candidateCardsToThrow.clear();

                    candidateCardsToThrow.add(c);
                }
            }
        }
    }

    static void getMaximumPintValue(Collection<Card> myCards, ArrayList<Card> candidateCardsToThrow, TuteGamePlayerContext context) {
        int maximumValue = 0;

        for (Card c : myCards) {
            if (c.suit != context.getPintaSuit()) {
                if (c.rank.countValue == maximumValue) {
                    candidateCardsToThrow.add(c);
                } else if (c.rank.countValue > maximumValue) {
                    maximumValue = c.rank.countValue;

                    candidateCardsToThrow.clear();

                    candidateCardsToThrow.add(c);
                }
            }
        }
    }

    static void addHigherValueSuitCards(Card thrownCard, Collection<Card> myCards, ArrayList<Card> candidateCardsToThrow, TuteGamePlayerContext context) {
        for (Card c : myCards) {
            if (c.suit != context.getPintaSuit() &&
                    c.suit == thrownCard.suit &&
                    c.rank.countValue > thrownCard.rank.countValue &&
                    (c.rank == Rank.Ace || c.rank == Rank.V3)) {
                candidateCardsToThrow.add(c);
            }
        }
    }

    @Override
    public Card calculatePlayerCardBegin() {
        return this.calculatePlayerCardBegin(this.context.getMyCards());
    }

    private Card calculatePlayerCardBegin(Collection<Card> myCards) {
        ArrayList<Card> candidateCardsToThrow = new ArrayList<>();

        if (this.context.isDeckEmpty()) {
            getMaximumPintValue(myCards, candidateCardsToThrow, this.context);
        } else {
            getMinimumPintValue(myCards, candidateCardsToThrow, this.context);
        }

        if (candidateCardsToThrow.isEmpty())
            candidateCardsToThrow.add(myCards.iterator().next());

        return candidateCardsToThrow.get(ThreadLocalRandom.current().nextInt(candidateCardsToThrow.size()));
    }

    @Override
    public Card calculatePlayerCardResponse(Card thrownCard) {
        Collection<Card> myCards = this.context.calculateAllowedCardsToAvoidRenuncio(thrownCard);

        ArrayList<Card> candidateCardsToThrow = new ArrayList<>();

        if (thrownCard.rank.countValue == 0) {
            if (addHighRankSuitCards(thrownCard, myCards, candidateCardsToThrow))
                return this.calculatePlayerCardBegin(myCards);
        } else {
            addHigherValueSuitCards(thrownCard, myCards, candidateCardsToThrow, this.context);

            SmartTuteAI.addSuitableCardsToCandidates(thrownCard, myCards, candidateCardsToThrow, this.context);

        }

        if (candidateCardsToThrow.isEmpty())
            candidateCardsToThrow.add(myCards.iterator().next());

        return candidateCardsToThrow.get(ThreadLocalRandom.current().nextInt(candidateCardsToThrow.size()));
    }

    private boolean addHighRankSuitCards(Card thrownCard, Collection<Card> myCards, ArrayList<Card> candidateCardsToThrow) {
        return addHighRankSameSuitCardsIfNotPinta(thrownCard, myCards, candidateCardsToThrow, this.context);
    }
}
