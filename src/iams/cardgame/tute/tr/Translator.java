package iams.cardgame.tute.tr;

import iams.cardgame.tute.CardModel;
import iams.cardgame.tute.CardModel.Rank;
import iams.cardgame.tute.CardModel.Suit;

import java.io.IOException;

public abstract class Translator {
    public abstract String getWindowTitle();

    public abstract String getPlayerGamesString(int player1Games, int player2Games);

    public abstract String getPlayerPointsString(int playerPoints);

    public abstract String getRankName(Rank rank);

    public abstract String getSuitName(Suit suit);

    public abstract String getCardNameString(CardModel currentCard);

    public abstract String getPlus10DeMonteString();

    public abstract String getChangePintaString();

    public abstract String getTuteDeclarationString(Rank rank);

    public abstract String getPlusPointsString(int countValue);

    public abstract String getPlusTwentyFortyPointsString(int countValue, Suit suit);

    public abstract String getTwentyFortyDeclarationString(Suit pintaSuit, Suit declarationSuit);

    public abstract String getDeclareRenuncioString();

    public abstract String getMenuItemNames(String key);

    public abstract String getCardsRemainText(String key);

    public abstract String getCardsOverdueText(String key);

    public abstract String getRulesText() throws IOException;

    public static Translator get(String defaultLanguage) {
        return switch (defaultLanguage) {
            case "PT" -> new TranslatorPtBr();
            case "EN" -> new TranslatorEn();
            case "SP" -> new TranslatorEs();
            default -> throw new AssertionError();
        };
    }


}