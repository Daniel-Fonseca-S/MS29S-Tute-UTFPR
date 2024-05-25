package iams.cardgame.tute.tr;

import iams.cardgame.tute.CardModel;
import iams.cardgame.tute.CardModel.Rank;
import iams.cardgame.tute.CardModel.Suit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TranslatorPtBr extends Translator {
    @Override
    public String getWindowTitle() {
        return "Tute para dois jogadores";
    }

    @Override
    public String getPlayerPointsString(int playerPoints) {
        return "Pontos: " + playerPoints;
    }

    @Override
    public String getPlayerGamesString(int player1Games, int player2Games) {
        return "Jogos: " + player1Games + " - " + player2Games;
    }

    @Override
    public String getSuitName(Suit suit) {
        return switch (suit) {
            case BATONS -> "Paus";
            case CUPS -> "Copas";
            case SWORDS -> "Espadas";
            case COINS -> "Ouros";
            default -> throw new AssertionError();
        };
    }

    @Override
    public String getRankName(Rank rank) {
        return switch (rank) {
            case ACE -> "Ás";
            case KING -> "Rei";
            case KNIGHT -> "Cavalo";
            case KNAVE -> "Valete";
            default -> rank.name().replaceAll("^V", "");
        };
    }

    @Override
    public String getCardNameString(CardModel currentCard) {
        return this.getRankName(currentCard.rank) + " de " + this.getSuitName(currentCard.suit);
    }

    @Override
    public String getPlus10DeMonteString() {
        return "+10 bônus para o vencedor da última rodada!";
    }

    @Override
    public String getChangePintaString() {
        return "Troca de carta pelo coringa!";
    }

    @Override
    public String getTuteDeclarationString(Rank rank) {
        return "Tute (" + (rank == Rank.KING ? "Reis" : "Cavalos") + ")";
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
            return "Quarenta!";
        else
            return "Vinte (" + this.getSuitName(declarationSuit) + ")";
    }

    @Override
    public String getDeclareRenuncioString() {
        return "Renúncio";
    }

    @Override
    public String getMenuItemNames(String key) {
        return switch (key) {
            case "OPTIONS" -> "Opções";
            case "RESTART" -> "Reiniciar";
            case "QUIT" -> "Sair do jogo";
            case "GAME" -> "Jogo";
            case "RULES" -> "Regras";
            case "LANGUAGES" -> "Idioma";
            case "PORTUGUESE" -> "Português (PT-BR)";
            case "ENGLISH" -> "Inglês";
            case "SPANISH" -> "Espanhol";
            case "COLORBACKGROUND" -> "Cor de fundo";
            case "GREEN" -> "Verde";
            case "RED" -> "Vermelho";
            case "BLUE" -> "Azul";
            default -> throw new AssertionError();
        };
    }

    @Override
    public String getRulesText() throws IOException {
        try {
            return new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "\\src\\iams\\cardgame\\tute\\tr\\Regras.txt")));
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
