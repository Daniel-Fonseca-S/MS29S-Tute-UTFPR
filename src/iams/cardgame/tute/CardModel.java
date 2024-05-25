package iams.cardgame.tute;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class CardModel implements Comparable<CardModel> {
    public static final int HEIGHT = 123;
    public static final int WIDTH = 80;

    public enum Suit {
        COINS,
        CUPS,
        SWORDS,
        BATONS;
    }

    public enum Rank {
        ACE(12, 11),
        V2(1, 0),
        V3(11, 10),
        V4(2, 0),
        V5(3, 0),
        V6(4, 0),
        V7(5, 0),
        V8(6, 0),
        V9(7, 0),
        KNAVE(8, 2),
        KNIGHT(9, 3),
        KING(10, 4),
        ;

        public final int relativeValue;
        public final int countValue;

        Rank(int relativeValue, int countValue) {
            this.relativeValue = relativeValue;
            this.countValue = countValue;
        }
    }

    public final Suit suit;

    public final Rank rank;
    public final BufferedImage image;

    public CardModel(Suit suit, Rank rank, BufferedImage image) {
        this.suit = suit;
        this.rank = rank;
        this.image = image;
    }

    public String toString() {
        return this.rank.name().replaceAll("^V", "") + "-" + this.suit.name();
    }

    private static final BufferedImage SOURCE_IMAGE;

    public static final BufferedImage BACK;

    static {
        try {
            SOURCE_IMAGE = ImageIO.read(Objects.requireNonNull(CardModel.class.getResourceAsStream("spanish-deck.png")));

            BACK = SOURCE_IMAGE.getSubimage(WIDTH * (Rank.values().length), 0, WIDTH, HEIGHT);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    static ArrayList<CardModel> createDeck() {
        ArrayList<CardModel> deck = new ArrayList<>();

        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                if ((rank == Rank.V8 || rank == Rank.V9))
                    continue;

                deck.add(new CardModel(suit, rank,
                        SOURCE_IMAGE.getSubimage(WIDTH * rank.ordinal(),
                                HEIGHT * suit.ordinal(), WIDTH, HEIGHT)));
            }
        }

        return deck;
    }

    @Override
    public int compareTo(CardModel o) {
        if (this.suit != o.suit)
            return this.suit.compareTo(o.suit);

        return this.rank.compareTo(o.rank);
    }
}
