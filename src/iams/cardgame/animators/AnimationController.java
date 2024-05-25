package iams.cardgame.animators;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AnimationController implements ActionListener {
    public interface Animator {
        boolean tick();
    }

    private final JPanel panel;

    private final ArrayList<Animator> animators = new ArrayList<>();

    public AnimationController(JPanel panel) {
        this.panel = panel;

        Timer t = new Timer(20, this);
        t.setRepeats(true);
        t.start();
    }

    public void add(Animator animator) {
        this.animators.add(animator);
    }

    public void repaint() {
        this.panel.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!this.animators.isEmpty()) {
            Animator animator = this.animators.get(0);

            if (animator.tick()) {
                this.animators.remove(0);
            }

            this.panel.repaint();
        }
    }
}
