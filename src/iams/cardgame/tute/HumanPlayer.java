package iams.cardgame.tute;

import iams.cardgame.tute.CardModel.Rank;
import iams.cardgame.tute.movement.*;
import iams.cardgame.tute.tr.Translator;

import java.awt.*;
import java.util.ArrayList;

public class HumanPlayer {
    private final Translator tr;
    private final TuteGame game;

    private boolean waitForUserClick = false;

    private String player1Message = null;

    private Card hoverCard = null;
    private final ArrayList<Card> otherCurrentCards = new ArrayList<>();

    public HumanPlayer(TuteGame game, Translator tr) {
        this.tr = tr;
        this.game = game;
    }

    public void onMouseOver(Card card, boolean over) {
        if (over) {
            if (this.hoverCard == card)
                return;

            this.clearSelection();

            this.hoverCard = card;
        } else if (this.hoverCard != null) {
            this.clearSelection();

            this.hoverCard = null;
        }

        this.onMouseOver();
    }

    private void onMouseOver() {
        Movement movement = this.getPlayerMovement();

        if (movement != null)
            setPlayer1Message(movement.toString(this.tr));
        else
            setPlayer1Message(null);
    }

    public Movement getPlayerMovement() {
        setPlayer1Message(null);

        if (!this.waitForUserClick || this.hoverCard == null)
            return null;

        if (this.game.isPlayer1Turn()) {
            return handlePlayer1Turn();
        } else if (this.game.getPlayer1Cards().contains(this.hoverCard)) {
            this.hoverCard.setHighlightColor(Color.yellow);
            return new ThrowResponseMovement(this.hoverCard);
        }

        return null;
    }

    private Movement handlePlayer1Turn() {
        if (this.game.canMakeDeclarations() && this.hoverCard == this.game.getPinta() && this.game.getPlayer1CardChangeableByPinta() != null) {
            this.hoverCard.setHighlightColor(Color.green);
            return new PintaMovement();
        }

        if (this.game.getPlayer1Cards().contains(this.hoverCard)) {
            return handlePlayer1Card();
        }

        return null;
    }

    private Movement handlePlayer1Card() {
        if (this.game.canMakeDeclarations()) {
            Movement declarationMovement = handleDeclaration();
            if (declarationMovement != null) {
                return declarationMovement;
            }
        }

        this.hoverCard.setHighlightColor(Color.yellow);
        return new ThrowMovement(this.hoverCard);
    }

    private Movement handleDeclaration() {
        if ((this.hoverCard.rank == Rank.King || this.hoverCard.rank == Rank.Knight) && this.game.canDeclareTute(this.hoverCard.rank, this.game.getPlayer1Cards())) {
            highlightCardsOfSameRank();
            return new TuteMovement(this.hoverCard.rank);
        }

        if (this.hoverCard.rank == Rank.King && this.game.getDeclaration(this.game.getPinta().suit) == null && this.game.getDeclaration(this.hoverCard.suit) == null) {
            Card knight = this.game.hasCard(Rank.Knight, this.hoverCard.suit, this.game.getPlayer1Cards());
            if (knight != null) {
                highlightCardsForTwentyForty(knight);
                return new TwentyFortyMovement(this.game.getPinta().suit, this.hoverCard.suit);
            }
        }

        return null;
    }

    private void highlightCardsOfSameRank() {
        for (Card c : this.game.getPlayer1Cards()) {
            if (c.rank == this.hoverCard.rank) {
                this.otherCurrentCards.add(c);
                c.setHighlightColor(Color.cyan);
            }
        }
    }

    private void highlightCardsForTwentyForty(Card knight) {
        this.otherCurrentCards.add(knight);
        this.hoverCard.setHighlightColor(Color.cyan);
        knight.setHighlightColor(Color.cyan);
    }

    public void clearSelection() {
        if (this.hoverCard != null)
            this.hoverCard.setHighlightColor(null);

        this.hoverCard = null;

        for (Card c : this.otherCurrentCards)
            c.setHighlightColor(null);

        this.otherCurrentCards.clear();
    }

    public void fireWaitForUserClick(boolean b) {
        this.waitForUserClick = b;

        if (this.waitForUserClick)
            this.onMouseOver();
    }

    public String getPlayer1Message() {
        return player1Message;
    }

    public void setPlayer1Message(String player1Message) {
        this.player1Message = player1Message;
    }
}
