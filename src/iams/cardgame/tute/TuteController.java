package iams.cardgame.tute;

import iams.cardgame.animators.*;

import javax.swing.*;
import java.util.Collection;

public class TuteController extends AnimationController {
    private static final int SEPARATION = 40;

    public TuteController(JPanel panel) {
        super(panel);
    }

    Animator getDeckCardMove(Collection<Card> cards) {
        MultiAnimator m1 = new MultiAnimator();

        for (Card card : cards) {
            m1.add(new MoveCardAnimator(card, 20 + (double) CardModel.WIDTH / 2, (double) Main.BOARD_HEIGHT / 2, 0));
        }

        return m1;
    }

    Animator getPintaMovement(Card card) {
        return new MoveCardAnimator(card, 40 + (double) CardModel.HEIGHT / 2, (double) Main.BOARD_HEIGHT / 2, 90);
    }

    Animator getPlayer1PintaCardThrow(Card card) {
        return new ThrowCardAnimator(card, 40 + (double) (3 * CardModel.HEIGHT) / 2, (double) Main.BOARD_HEIGHT / 2 + CardModel.HEIGHT, 0);
    }

    Animator getPlayer2PintaCardThrow(Card card) {
        return new ThrowCardAnimator(card, 40 + (double) (3 * CardModel.HEIGHT) / 2, (double) Main.BOARD_HEIGHT / 2 - CardModel.HEIGHT, 0);
    }

    Animator getPlayer1CardThrow(Card card) {
        return new ThrowCardAnimator(card, (double) Main.BOARD_WIDTH / 2, (double) Main.BOARD_HEIGHT - CardModel.HEIGHT, 0);
    }

    Animator getPlayer2PlayerCardThrow(Card card) {
        return new ThrowCardAnimator(card, (double) Main.BOARD_WIDTH / 2, CardModel.HEIGHT, 0);
    }

    Animator getCenterCardThrow(Card card) {
        return new ThrowCardAnimator(card, (double) Main.BOARD_WIDTH / 2, (double) Main.BOARD_HEIGHT / 2, 0);
    }

    Animator getPlayer1CardMovement(Card card, int i, int numCards) {
        double centering = -(numCards - 1.) / 2 + i;

        return new MoveCardAnimator(card,
                (double) Main.BOARD_WIDTH / 2 + SEPARATION * centering,
                Main.BOARD_HEIGHT - ((double) CardModel.HEIGHT / 2 + 30 + (30 * Math.cos(Math.PI / 8 * centering))),
                5 * centering);
    }

    Animator getPlayer2CardMovement(Card card, int i, int numCards) {
        double centering = -(numCards - 1.) / 2 + i;

        return new MoveCardAnimator(card,
                (double) Main.BOARD_WIDTH / 2 - SEPARATION * centering,
                (double) CardModel.HEIGHT / 2 + 30 + (30 * Math.cos(Math.PI / 8 * centering)),
                180 + 5 * centering);
    }

    Animator relocatePlayer1Cards(Collection<Card> player1) {
        MultiAnimator m1 = new MultiAnimator();

        int i = 0;

        for (Card card : player1) {
            m1.add(new MoveToFrontAnimator(card));

            m1.add(this.getPlayer1CardMovement(card, i++, player1.size()));
        }

        return m1;
    }

    Animator relocatePlayer2Cards(Collection<Card> player2) {
        MultiAnimator m1 = new MultiAnimator();

        int i = 0;

        for (Card card : player2) {
            m1.add(new MoveToFrontAnimator(card));

            m1.add(this.getPlayer2CardMovement(card, i++, player2.size()));

        }

        return m1;
    }

    public Animator getPlayer1WinDeckMovement(Card card1, Card card2) {
        MultiAnimator m1 = new MultiAnimator();

        m1.add(new ThrowCardAnimator(card1, Main.BOARD_WIDTH, Main.BOARD_HEIGHT - (double)CardModel.HEIGHT, 0));
        m1.add(new ThrowCardAnimator(card2, Main.BOARD_WIDTH, Main.BOARD_HEIGHT - (double)CardModel.HEIGHT, 0));

        m1.add(new ReverseAnimator(card1, false));
        m1.add(new ReverseAnimator(card2, false));

        return m1;
    }

    public Animator getPlayer2WinDeckMovement(Card card1, Card card2) {
        MultiAnimator m1 = new MultiAnimator();

        m1.add(new ThrowCardAnimator(card1, Main.BOARD_WIDTH, CardModel.HEIGHT, 0));
        m1.add(new ThrowCardAnimator(card2, Main.BOARD_WIDTH, CardModel.HEIGHT, 0));

        m1.add(new ReverseAnimator(card1, false));
        m1.add(new ReverseAnimator(card2, false));

        return m1;
    }

    public Animator getCenterWinCardThrow(Collection<Card> cards) {
        MultiAnimator m1 = new MultiAnimator();

        for (Card card : cards) {
            m1.add(new MoveCardAnimator(card,
                    card.getX() - (double) Main.BOARD_WIDTH / 2,
                    card.getY(), card.getRotation()));
        }

        return m1;
    }

    Animator getCenterCardMove(Collection<Card> cards) {
        MultiAnimator m1 = new MultiAnimator();

        for (Card card : cards) {
            m1.add(new MoveCardAnimator(card, (double) Main.BOARD_WIDTH / 2, (double) Main.BOARD_HEIGHT / 2, 0));
        }

        return m1;
    }
}
