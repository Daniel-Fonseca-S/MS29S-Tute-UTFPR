package iams.cardgame.animators;

import iams.cardgame.tute.Card;

public record MoveCardAnimator(Card card, double x, double y, double rotation) implements AnimationController.Animator {

    @Override
    public boolean tick() {
        return this.card.stepTo(this.x, this.y, this.rotation, 30, 0);
    }
}
