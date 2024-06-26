package iams.cardgame.animators;

import iams.cardgame.tute.Card;

public class MoveToFrontAnimator implements AnimationController.Animator {
    private final Card card;

    public MoveToFrontAnimator(Card card) {
        this.card = card;
    }

    @Override
    public boolean tick() {
        this.card.moveToFront();

        return true;
    }

}
