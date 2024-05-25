package iams.cardgame.animators;

import iams.cardgame.tute.Card;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public record ThrowCardAnimator(Card card, double x, double y,
                                double rotation) implements AnimationController.Animator {

    public ThrowCardAnimator(Card card, double x, double y, double rotation) {
        this.card = card;
        Random r = ThreadLocalRandom.current();
        this.x = x - 30 + 60 * r.nextDouble();
        this.y = y - 30 + 60 * r.nextDouble();
        this.rotation = rotation - 0.5 + r.nextDouble();
    }

    @Override
    public boolean tick() {
        return this.card.stepTo(this.x, this.y, this.rotation, 30, 10);
    }
}
