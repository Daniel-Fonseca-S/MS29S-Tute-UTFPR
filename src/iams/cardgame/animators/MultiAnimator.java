package iams.cardgame.animators;

import iams.cardgame.animators.AnimationController.Animator;

import java.util.ArrayList;

public class MultiAnimator implements AnimationController.Animator {
    private final ArrayList<Animator> animators = new ArrayList<>();

    public void add(Animator animator) {
        this.animators.add(animator);
    }

    @Override
    public boolean tick() {
        if (!this.animators.isEmpty()) {
            this.animators.removeIf(Animator::tick);
        }

        return this.animators.isEmpty();
    }
}
