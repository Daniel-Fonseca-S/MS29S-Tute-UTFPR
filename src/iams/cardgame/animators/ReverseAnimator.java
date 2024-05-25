package iams.cardgame.animators;

import iams.cardgame.tute.Card;

public class ReverseAnimator implements AnimationController.Animator {
    private final boolean faceUp;
    private final Card card;

    public ReverseAnimator(Card card, boolean faceUp) {
        this.card = card;
        this.faceUp = faceUp;
    }

    @Override
    public boolean tick() {
        this.card.faceUp = faceUp;

        return true;
    }

}
