package iams.cardgame.tute.tr;

import iams.cardgame.tute.CardModel;
import iams.cardgame.tute.CardModel.Rank;
import iams.cardgame.tute.CardModel.Suit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TranslatorEs extends Translator {
    @Override
    public String getWindowTitle() {
        return "Tute para dos jugadores";
    }

    @Override
    public String getPlayerPointsString(int playerPoints) {
        return "Puntos: " + playerPoints;
    }

    @Override
    public String getPlayerGamesString(int player1Games, int player2Games) {
        return "Juegos: " + player1Games + " - " + player2Games;
    }

    @Override
    public String getSuitName(Suit suit) {
        return switch (suit) {
            case BATONS -> "Bastos";
            case CUPS -> "Copas";
            case SWORDS -> "Espadas";
            case COINS -> "Oros";
            default -> throw new AssertionError();
        };
    }

    @Override
    public String getRankName(Rank rank) {
        return switch (rank) {
            case ACE -> "As";
            case V2 -> "Dos";
            case V3 -> "Tres";
            case V4 -> "Cuatro";
            case V5 -> "Cinco";
            case V6 -> "Seis";
            case V7 -> "Siete";
            case V8 -> "Ocho";
            case V9 -> "Nueve";
            case KNAVE -> "Sota";
            case KNIGHT -> "Caballo";
            case KING -> "Rey";
            default -> rank.name();
        };
    }

    @Override
    public String getCardNameString(CardModel currentCard) {
        return this.getRankName(currentCard.rank) + " de " + this.getSuitName(currentCard.suit);
    }

    @Override
    public String getPlus10DeMonteString() {
        return "+10 de monte";
    }

    @Override
    public String getChangePintaString() {
        return "Cambio de pinta";
    }

    @Override
    public String getTuteDeclarationString(Rank rank) {
        return "Tute de " + (rank == Rank.KING ? "Reyes" : "Caballos");
    }

    @Override
    public String getPlusPointsString(int countValue) {
        return "+" + countValue;
    }

    @Override
    public String getPlusTwentyFortyPointsString(int countValue, Suit suit) {
        return "+" + countValue + " de " + this.getSuitName(suit);
    }

    @Override
    public String getTwentyFortyDeclarationString(Suit pintaSuit, Suit declarationSuit) {
        if (pintaSuit == declarationSuit)
            return "Cuarenta de " + this.getSuitName(declarationSuit);
        else
            return "Veinte de " + this.getSuitName(declarationSuit);
    }

    @Override
    public String getDeclareRenuncioString() {
        return "Renuncio";
    }

    @Override
    public String getMenuItemNames(String key) {
        return switch (key) {
            case "OPTIONS" -> "Opciones";
            case "RESTART" -> "Reiniciar";
            case "QUIT" -> "Abandonar el juego";
            case "GAME" -> "Juego";
            case "RULES" -> "Normas";
            case "LANGUAGES" -> "Idiomas";
            case "PORTUGUESE" -> "Portugués (BR)";
            case "ENGLISH" -> "Inglés";
            case "SPANISH" -> "Español";
            case "COLORBACKGROUND" -> "Color de fondo";
            case "GREEN" -> "Verde";
            case "RED" -> "Rojo";
            case "BLUE" -> "Azul";
            default -> throw new AssertionError();
        };
    }

    @Override
    public String getRulesText() throws IOException {
        try {
            return new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "\\src\\iams\\cardgame\\tute\\tr\\Normas.txt")));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return "";
    }

    @Override
    public String getCardsRemainText(String key) {
        return "Cartas restantes: " + key;
    }

    @Override
    public String getCardsOverdueText(String key) {
        return "Cartas vencidas: " + key;
    }
}
