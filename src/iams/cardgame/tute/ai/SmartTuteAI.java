package iams.cardgame.tute.ai;

import iams.cardgame.tute.Card;
import iams.cardgame.tute.CardModel.Rank;
import iams.cardgame.tute.TuteGamePlayerContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

public class SmartTuteAI implements TuteAI {
    private final TuteGamePlayerContext context;

    public SmartTuteAI(TuteGamePlayerContext context) {
        this.context = context;
    }

    static boolean addHighRankSameSuitCardsIfNotPinta(Card thrownCard, Collection<Card> myCards, ArrayList<Card> candidateCardsToThrow, TuteGamePlayerContext context) {
        for (Card c : myCards) {
            if (c.suit != context.getPintaSuit() &&
                    c.suit == thrownCard.suit &&
                    (c.rank == Rank.Ace || c.rank == Rank.V3)) {
                candidateCardsToThrow.add(c);
            }
        }

        return candidateCardsToThrow.isEmpty();
    }

    static void addSuitableCardsToCandidates(Card thrownCard, Collection<Card> myCards, ArrayList<Card> candidateCardsToThrow, TuteGamePlayerContext context) {
        if (candidateCardsToThrow.isEmpty()) {
            for (Card c : myCards) {
                if (c.suit == thrownCard.suit &&
                        c.rank.countValue > thrownCard.rank.countValue) {
                    candidateCardsToThrow.add(c);
                }
            }
        }

        if (candidateCardsToThrow.isEmpty() && thrownCard.suit != context.getPintaSuit()) {
            for (Card c : myCards) {
                if (c.suit == context.getPintaSuit() &&
                        c.rank.countValue == 0) {
                    candidateCardsToThrow.add(c);
                }
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
            BasicTuteAI.getMaximumPintValue(myCards, candidateCardsToThrow, this.context);
        } else {
            BasicTuteAI.getMinimumPintValue(myCards, candidateCardsToThrow, this.context);
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
            BasicTuteAI.addHigherValueSuitCards(thrownCard, myCards, candidateCardsToThrow, this.context);

            addSuitableCardsToCandidates(thrownCard, myCards, candidateCardsToThrow, this.context);

        }

        if (candidateCardsToThrow.isEmpty())
            candidateCardsToThrow.add(myCards.iterator().next());

        return candidateCardsToThrow.get(ThreadLocalRandom.current().nextInt(candidateCardsToThrow.size()));
    }

    private boolean addHighRankSuitCards(Card thrownCard, Collection<Card> myCards, ArrayList<Card> candidateCardsToThrow) {
        return addHighRankSameSuitCardsIfNotPinta(thrownCard, myCards, candidateCardsToThrow, this.context);
    }
}
