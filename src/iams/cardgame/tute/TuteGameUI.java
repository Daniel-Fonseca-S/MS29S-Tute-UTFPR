package iams.cardgame.tute;

import iams.cardgame.animators.*;
import iams.cardgame.tute.CardModel.Rank;
import iams.cardgame.tute.CardModel.Suit;
import iams.cardgame.tute.TuteGame.Declaration;
import iams.cardgame.tute.ai.BasicTuteAI;
import iams.cardgame.tute.ai.TuteAI;
import iams.cardgame.tute.movement.*;
import iams.cardgame.tute.tr.Translator;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;

public class TuteGameUI {
    private final Translator tr;

    public final TuteGame game;
    public final TuteController controller;

    public HumanPlayer human;
    public TuteAI ai;

    public String notification = null;

    public Card player2playedCard = null;

    public int player1Points = -1;
    public int player2Points = -1;

    public JFrame Frame = null;

    public Boolean RefreshCardsRemain = false;

    private PlayTurn playTurn;

    public class MessageAnimator implements AnimationController.Animator {
        private final String message;

        private int delay;

        public MessageAnimator(String message, int delay) {
            this.message = message;

            this.delay = delay;
        }

        @Override
        public boolean tick() {
            if (this.delay > 0) {
                TuteGameUI.this.notification = message;

                this.delay--;

                return false;
            } else {
                TuteGameUI.this.notification = null;

                return true;
            }
        }
    }

    public int getPlayer1Points() {
        return this.player1Points;
    }

    public int getPlayer2Points() {
        return this.player2Points;
    }

    public String getNotification() {
        return this.notification;
    }

    public TuteGameUI(Translator tr, TuteGame game, TuteController controller, HumanPlayer humanPlayer) {
        this.tr = tr;
        this.game = game;

        this.human = humanPlayer;

        this.controller = controller;

        this.playTurn = new PlayTurn(this, game, controller, tr);

        this.initialize();
    }

    void initialize() {
        for (Card card : this.game.getDeck())
            this.controller.add(new ReverseAnimator(card, false));

        this.controller.add(this.controller.getDeckCardMove(this.game.getDeck()));

        this.ai = new BasicTuteAI(new TuteGamePlayerContext(this.game, false));

        for (int i = 0; i < TuteGame.NUM_CARDS_PER_PLAYER; i++)
            this.dealNewCards();

        refreshCardsRemain(this.game.getDeck().size() + "");

        this.controller.add(new ReverseAnimator(this.game.getPinta(), true));

        this.controller.add(this.controller.relocatePlayer1Cards(this.game.getPlayer1Cards()));

        this.controller.add(this.controller.relocatePlayer2Cards(this.game.getPlayer2Cards()));

        this.controller.add(this.controller.getPintaMovement(this.game.getPinta()));

        this.controller.add(new WaitAnimator(50));

        this.controller.add(new AnimationController.Animator() {
            @Override
            public boolean tick() {
                playTurn.startTurn();

                return true;
            }
        });
    }

    public void dealNewCards() {
        if (this.game.isDeckEmpty()) {
            return;
        }

        if (this.game.isPlayer1Turn()) {
            Card c1 = this.game.dealCardToPlayer1();

            this.controller.add(new MoveToFrontAnimator(c1));

            this.controller.add(this.controller.getPlayer1CardThrow(c1));

            this.controller.add(new ReverseAnimator(c1, true));
        }

        Card c2 = this.game.dealCardToPlayer2();

        this.controller.add(new MoveToFrontAnimator(c2));

        this.controller.add(this.controller.getPlayer2PlayerCardThrow(c2));

        this.controller.add(new ReverseAnimator(c2, false));

        if (!this.game.isPlayer1Turn()) {
            Card c1 = this.game.dealCardToPlayer1();

            this.controller.add(new MoveToFrontAnimator(c1));

            this.controller.add(this.controller.getPlayer1CardThrow(c1));

            this.controller.add(new ReverseAnimator(c1, true));
        }
    }

    private void renuncio(Card currentCard) {
        this.game.declareRenuncio(this.player2playedCard, currentCard);

        this.abortGame(this.tr.getDeclareRenuncioString());
    }

    public void tute(Rank rank) {
        this.game.declareTute();

        this.abortGame(this.tr.getTuteDeclarationString(rank));
    }

    private void abortGame(String message) {
        this.controller.add(new MessageAnimator(message, 100));

        this.controller.add(this.controller.getCenterCardMove(this.game.getCardRasters()));

        this.human.clearSelection();

        this.controller.add(new AnimationController.Animator() {
            @Override
            public boolean tick() {
                TuteGameUI.this.game.countPointsAndRestart(TuteGameUI.this.game.isPlayer1Turn());
                TuteGameUI.this.initialize();
                return true;
            }
        });
    }

    public void completeGame(boolean player1WonLastTrick) {
        this.human.clearSelection();

        ArrayList<Card> player1Wins = new ArrayList<Card>(this.game.getPlayer1Baza());
        ArrayList<Card> player2Wins = new ArrayList<Card>(this.game.getPlayer2Baza());

        Collections.reverse(player1Wins);
        Collections.reverse(player2Wins);

        this.controller.add(this.controller.getCenterWinCardThrow(player1Wins));

        this.player1Points = 0;
        this.player2Points = 0;

        for (Card card : player1Wins) {
            this.controller.add(new MoveToFrontAnimator(card));
            this.controller.add(this.controller.getCenterCardThrow(card));
            this.controller.add(new ReverseAnimator(card, true));

            if (card.rank.countValue > 0) {
                this.controller.add(new AnimationController.Animator() {
                    @Override
                    public boolean tick() {
                        TuteGameUI.this.player1Points += card.rank.countValue;

                        return true;
                    }
                });

                this.controller.add(new MessageAnimator(this.tr.getPlusPointsString(card.rank.countValue), 30));
            }
        }

        if (player1WonLastTrick) {
            this.controller.add(new WaitAnimator(15));

            this.controller.add(new AnimationController.Animator() {
                @Override
                public boolean tick() {
                    TuteGameUI.this.player1Points += 10;
                    return true;
                }
            });

            this.controller.add(new MessageAnimator(this.tr.getPlus10DeMonteString(), 30));
        }

        for (Suit suit : Suit.values()) {
            Declaration declaration = this.game.getDeclaration(suit);

            if (declaration == Declaration.Player1) {
                this.controller.add(new WaitAnimator(15));

                int points = suit == TuteGameUI.this.game.getPinta().suit ? 40 : 20;

                this.controller.add(new AnimationController.Animator() {
                    @Override
                    public boolean tick() {
                        TuteGameUI.this.player1Points += points;
                        return true;
                    }
                });

                this.controller.add(new MessageAnimator(this.tr.getPlusTwentyFortyPointsString(points, suit), 30));
            }
        }

        this.controller.add(new WaitAnimator(50));

        this.controller.add(this.controller.getCenterWinCardThrow(player2Wins));

        for (Card card : player2Wins) {
            this.controller.add(new MoveToFrontAnimator(card));
            this.controller.add(this.controller.getCenterCardThrow(card));
            this.controller.add(new ReverseAnimator(card, true));

            if (card.rank.countValue > 0) {
                this.controller.add(new AnimationController.Animator() {
                    @Override
                    public boolean tick() {
                        TuteGameUI.this.player2Points += card.rank.countValue;

                        return true;
                    }
                });

                this.controller.add(new MessageAnimator(this.tr.getPlusPointsString(card.rank.countValue), 30));
            }
        }

        if (!player1WonLastTrick) {
            this.controller.add(new WaitAnimator(15));

            this.controller.add(new AnimationController.Animator() {
                @Override
                public boolean tick() {
                    TuteGameUI.this.player2Points += 10;
                    return true;
                }
            });

            this.controller.add(new MessageAnimator(this.tr.getPlus10DeMonteString(), 30));
        }

        for (Suit suit : Suit.values()) {
            Declaration declaration = this.game.getDeclaration(suit);

            if (declaration == Declaration.Player2) {
                this.controller.add(new WaitAnimator(15));

                int points = suit == TuteGameUI.this.game.getPinta().suit ? 40 : 20;

                this.controller.add(new AnimationController.Animator() {
                    @Override
                    public boolean tick() {
                        TuteGameUI.this.player2Points += points;
                        return true;
                    }
                });

                this.controller.add(new MessageAnimator(this.tr.getPlusTwentyFortyPointsString(points, suit), 30));
            }
        }

        this.controller.add(new AnimationController.Animator() {
            @Override
            public boolean tick() {
                TuteGameUI.this.game.countPointsAndRestart(player1WonLastTrick);

                return true;
            }
        });

        this.controller.add(new WaitAnimator(100));

        this.controller.add(this.controller.getCenterCardMove(this.game.getCardRasters()));

        this.controller.add(new AnimationController.Animator() {
            @Override
            public boolean tick() {
                TuteGameUI.this.player1Points = TuteGameUI.this.player2Points = -1;

                TuteGameUI.this.initialize();

                return true;
            }
        });
    }

    public void onHumanMovement(Movement movement) {
        if (movement != null) {
            this.fireWaitForUserClick(false);

            if (movement instanceof PintaMovement) {
                Card cardChangeableByPinta = this.game.getPlayer1CardChangeableByPinta();

                if (cardChangeableByPinta == null)
                    throw new AssertionError();

                this.controller.add(new MoveToFrontAnimator(cardChangeableByPinta));

                this.controller.add(this.controller.getPlayer1PintaCardThrow(cardChangeableByPinta));

                this.controller.add(new MoveToBackAnimator(cardChangeableByPinta));

                this.controller.add(new MessageAnimator(this.tr.getChangePintaString(), 100));

                this.game.changePintaPlayer1();

                this.controller.add(this.controller.relocatePlayer1Cards(this.game.getPlayer1Cards()));

                this.controller.add(this.controller.getPintaMovement(this.game.getPinta()));

                this.controller.add(new AnimationController.Animator() {
                    @Override
                    public boolean tick() {
                        TuteGameUI.this.fireWaitForUserClick(true);

                        return true;
                    }
                });
            } else if (movement instanceof TuteMovement) {
                this.tute(((TuteMovement) movement).getRank());

                this.controller.add(new AnimationController.Animator() {
                    @Override
                    public boolean tick() {
                        TuteGameUI.this.fireWaitForUserClick(true);

                        return true;
                    }
                });
            } else if (movement instanceof TwentyFortyMovement) {
                Suit declarationSuit = ((TwentyFortyMovement) movement).getSuit();

                this.game.declare(declarationSuit);

                this.controller.add(new MessageAnimator(
                        this.tr.getTwentyFortyDeclarationString(this.game.getPinta().suit, declarationSuit), 100));

                this.controller.add(new AnimationController.Animator() {
                    @Override
                    public boolean tick() {
                        TuteGameUI.this.fireWaitForUserClick(true);

                        return true;
                    }
                });
            } else if (movement instanceof ThrowMovement) {
                Card currentCard = ((ThrowMovement) movement).getCurrentCard();

                this.controller.add(new MoveToFrontAnimator(currentCard));

                this.controller.add(this.controller.getCenterCardThrow(currentCard));

                this.controller.add(new WaitAnimator(20));

                Card player2Card = TuteGameUI.this.ai.calculatePlayerCardResponse(currentCard);

                this.controller.add(new MoveToFrontAnimator(player2Card));

                this.controller.add(this.controller.getCenterCardThrow(player2Card));

                this.controller.add(new ReverseAnimator(player2Card, true));

                boolean player1Wins = this.game.playCards(currentCard, player2Card);

                playTurn.playTurn(currentCard, player2Card, player1Wins);
            } else if (movement instanceof ThrowResponseMovement) {
                Card currentCard = ((ThrowResponseMovement) movement).getCurrentCard();

                this.controller.add(new MoveToFrontAnimator(currentCard));

                this.controller.add(this.controller.getCenterCardThrow(currentCard));

                if (this.game.calculateIfRenuncio(this.player2playedCard, currentCard, this.game.getPlayer1Cards())) {
                    this.renuncio(currentCard);
                } else {
                    boolean player1Wins = this.game.playCards(this.player2playedCard, currentCard);

                    playTurn.playTurn(currentCard, this.player2playedCard, player1Wins);

                    this.player2playedCard = null;
                }
            }
        }

        this.human.clearSelection();
    }

    protected void fireWaitForUserClick(boolean b) {
        for (Card card : this.game.getCardRasters())
            card.enableHighlight(b);

        this.human.fireWaitForUserClick(b);

        this.controller.repaint();
    }

    public void setFrame(JFrame frame) {
        this.Frame = frame;
    }

    public String refreshCardsRemain(String remaningCards) {
        final Languages lg = new Languages();
        final Translator tr = Translator.get(lg.getDefaultLanguage());
        RefreshCardsRemain = true;

        return tr.getCardsRemainText(remaningCards);
    }
}
