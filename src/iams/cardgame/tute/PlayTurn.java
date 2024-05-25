package iams.cardgame.tute;

import iams.cardgame.animators.*;
import iams.cardgame.tute.CardModel.Rank;
import iams.cardgame.tute.CardModel.Suit;
import iams.cardgame.tute.tr.Translator;

public class PlayTurn {
    public TuteGameUI gameUI;
    private final TuteGame game;
    private final TuteController controller;
    private final Translator tr;

    public PlayTurn(TuteGameUI gameUI, TuteGame game, TuteController controller, Translator tr) {
        this.gameUI = gameUI;
        this.game = game;
        this.controller = controller;
        this.tr = tr;
    }

    void startTurn() {

        if (this.game.canMakeDeclarations()) {
            Card cardChangeableByPinta = this.game.getPlayer2CardChangeableByPinta();

            if (cardChangeableByPinta != null) {
                this.controller.add(new ReverseAnimator(cardChangeableByPinta, true));

                this.controller.add(new MoveToFrontAnimator(cardChangeableByPinta));

                this.controller.add(this.controller.getPlayer2PintaCardThrow(cardChangeableByPinta));

                this.controller.add(new MoveToBackAnimator(cardChangeableByPinta));

                this.controller.add(gameUI.new MessageAnimator(this.tr.getChangePintaString(), 100));

                Card previousPinta = this.game.changePintaPlayer2();

                this.controller.add(this.controller.relocatePlayer2Cards(this.game.getPlayer2Cards()));

                this.controller.add(this.controller.getPintaMovement(this.game.getPinta()));

                this.controller.add(new ReverseAnimator(previousPinta, false));

                this.controller.add(() -> {

                    PlayTurn.this.startTurn();

                    return true;
                });

                return;
            }
        }

        if (!this.game.isPlayer1Turn() && !this.game.getPlayer1Cards().isEmpty()) {
            if (this.game.canMakeDeclarations()) {
                for (Rank rank : new Rank[]{Rank.KING, Rank.KNIGHT}) {
                    if (this.game.canDeclareTute(rank, this.game.getPlayer2Cards())) {
                        for (Card c : this.game.getPlayer2Cards()) {
                            if (c.rank == rank)
                                this.controller.add(new ReverseAnimator(c, true));
                        }

                        this.controller.add(() -> {
                            gameUI.tute(rank);
                            return true;
                        });

                        return;
                    }
                }

                for (Suit declarationSuit : Suit.values()) {
                    if (this.game.getDeclaration(declarationSuit) != null
                            || this.game.getDeclaration(this.game.getPinta().suit) != null)
                        continue;

                    Card knight = this.game.hasCard(Rank.KNIGHT, declarationSuit, this.game.getPlayer2Cards());
                    Card king = this.game.hasCard(Rank.KING, declarationSuit, this.game.getPlayer2Cards());

                    if (knight != null && king != null) {
                        this.game.declare(declarationSuit);

                        this.controller.add(new WaitAnimator(20));

                        this.controller.add(new ReverseAnimator(knight, true));
                        this.controller.add(new ReverseAnimator(king, true));

                        this.controller.add(gameUI.new MessageAnimator(
                                this.tr.getTwentyFortyDeclarationString(this.game.getPinta().suit, declarationSuit),
                                100));

                        this.controller.add(() -> {
                            gameUI.fireWaitForUserClick(true);

                            return true;
                        });

                        this.controller.add(new ReverseAnimator(knight, false));
                        this.controller.add(new ReverseAnimator(king, false));

                        this.controller.add(() -> {
                            PlayTurn.this.startTurn();

                            return true;
                        });

                        return;
                    }
                }
            }

            this.controller.add(new WaitAnimator(20));

            gameUI.player2playedCard = gameUI.ai.calculatePlayerCardBegin();

            this.controller.add(new ReverseAnimator(gameUI.player2playedCard, true));

            this.controller.add(new MoveToFrontAnimator(gameUI.player2playedCard));

            this.controller.add(this.controller.getCenterCardThrow(gameUI.player2playedCard));
        }

        this.controller.add(() -> {
            gameUI.fireWaitForUserClick(true);

            return true;
        });
    }

    public void playTurn(Card card1, Card card2, boolean player1Wins) {
        this.controller.add(new WaitAnimator(20));

        if (player1Wins)
            this.controller.add(this.controller.getPlayer1WinDeckMovement(card1, card2));
        else
            this.controller.add(this.controller.getPlayer2WinDeckMovement(card1, card2));

        this.controller.add(() -> {
            newTurn(player1Wins);

            return true;
        });
    }

    private void newTurn(boolean player1WonLastTrick) {
        if (this.game.arePlayerCardsEmpty()) {
            gameUI.completeGame(player1WonLastTrick);
        } else {
            gameUI.dealNewCards();

            this.controller.add(this.controller.relocatePlayer1Cards(this.game.getPlayer1Cards()));

            this.controller.add(this.controller.relocatePlayer2Cards(this.game.getPlayer2Cards()));

            this.controller.add(() -> {
                PlayTurn.this.startTurn();

                return true;
            });

            gameUI.refreshRemainingCards(this.game.getDeck().size() + "");
        }
    }
}
