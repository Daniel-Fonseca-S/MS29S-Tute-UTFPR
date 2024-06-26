package iams.cardgame.tute;

import iams.cardgame.tute.tr.Translator;
import iams.ui.GraphicsPanel;

import javax.accessibility.Accessible;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.jar.JarFile;

public class Main extends GraphicsPanel {
    Languages lg  = new Languages();

    public final Translator tr = Translator.get(lg.getDefaultLanguage());

    public static final String TIMES_NEW_ROMAN = "Times New Roman";
    private static final Font FONT = new Font(TIMES_NEW_ROMAN, Font.BOLD, 28); //$NON-NLS-1$

    private static final Font SMALL_FONT = new Font(TIMES_NEW_ROMAN, Font.BOLD, 22); //$NON-NLS-1$

    private static Color color = new Color(13, 98, 69);

    public static final int BOARD_WIDTH = 700;
    public static final int BOARD_HEIGHT = 700;

    private final TuteController controller = new TuteController(this);

    public final TuteGame game = new TuteGame();

    private final HumanPlayer humanPlayer = new HumanPlayer(this.game, this.tr);

    private final TuteGameUI gameUI = new TuteGameUI(this.tr, this.game, this.controller, this.humanPlayer);

    private final GameMouseListener gameMouseListener = new GameMouseListener(this, this.game, this.gameUI, this.humanPlayer);

    public Main() {
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                this.componentResized(e);
            }

            @Override
            public void componentResized(ComponentEvent e) {
                Main.this.initializeBoundingBox(new Rectangle2D.Double(0, 0, BOARD_WIDTH, BOARD_HEIGHT));
            }
        });

        this.addMouseListener(this.gameMouseListener);
        this.addMouseMotionListener(this.gameMouseListener);
    }

    @Override
    protected void paint(Graphics2D g2, AffineTransform tx2) {
        this.gameMouseListener.mouseMoved();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setColor(color);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());

        g2.setFont(FONT);
        FontMetrics fm = g2.getFontMetrics();

        g2.transform(tx2);

        g2.setColor(Color.yellow);

        if (this.gameUI.getPlayer1Points() >= 0) {
            String pointsString = this.tr.getPlayerPointsString(this.gameUI.getPlayer1Points());
            g2.drawString(pointsString, 150 - fm.stringWidth(pointsString), BOARD_HEIGHT - 100);
        }

        if (this.gameUI.getPlayer2Points() >= 0) {
            String pointsString = this.tr.getPlayerPointsString(this.gameUI.getPlayer2Points());
            g2.drawString(pointsString, 150 - fm.stringWidth(pointsString), 100 + fm.getMaxAscent());
        }

        g2.setColor(Color.white);

        String cardsRemain = this.gameUI.refreshRemainingCards(String.valueOf(this.game.getDeck().size()));
        g2.drawString(cardsRemain, -fm.stringWidth(cardsRemain), 330 + fm.getMaxAscent());

        String notification = this.gameUI.getNotification();

        if (notification != null && !notification.isEmpty()) {
            g2.drawString(notification, BOARD_WIDTH / 2 + CardModel.HEIGHT, BOARD_HEIGHT / 2 + fm.getMaxAscent() / 2);
        }

        FontMetrics fmPointsTurn = g2.getFontMetrics();

        g2.setFont(new Font(TIMES_NEW_ROMAN, Font.BOLD, 14));
        g2.setColor(Color.white);

        String pointsTurnPlayer1 = this.tr.getCardsOverdueText(String.valueOf(this.game.getPlayer1Baza().size() / 2));
        g2.drawString(pointsTurnPlayer1, 875 - fmPointsTurn.stringWidth(pointsTurnPlayer1), 645 + fmPointsTurn.getMaxAscent());

        String pointsTurnPlayer2 = this.tr.getCardsOverdueText(String.valueOf(this.game.getPlayer2Baza().size() / 2));
        g2.drawString(pointsTurnPlayer2, 875 - fmPointsTurn.stringWidth(pointsTurnPlayer2), 10 + fmPointsTurn.getMaxAscent());

        g2.setFont(SMALL_FONT);
        FontMetrics fm2 = g2.getFontMetrics();

        g2.setColor(new Color(152, 251, 152));

        String gameMessage = this.tr.getPlayerGamesString(
                this.game.getPlayer1Games(),
                this.game.getPlayer2Games());

        g2.drawString(gameMessage,
                BOARD_WIDTH / 2 - fm.stringWidth(gameMessage) / 2,
                5 + fm.getMaxAscent());

        if (this.humanPlayer.getPlayer1Message() != null && !this.humanPlayer.getPlayer1Message().isEmpty()) {
            g2.drawString(this.humanPlayer.getPlayer1Message(),
                    BOARD_WIDTH / 2 - fm2.stringWidth(this.humanPlayer.getPlayer1Message()) / 2,
                    BOARD_HEIGHT - fm2.getMaxAscent());
        }

        Card[] cardRasters = this.game.getCardRasters().toArray(new Card[0]);

        Arrays.sort(cardRasters, Card.PAINT_COMPARATOR);

        for (Card card : cardRasters)
            card.draw(g2, this);
    }

    public static void restartGame(JFrame frame) {
        frame.dispose();
        try {
            main(null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        final Languages lg = new Languages();
        final Translator tr = Translator.get(lg.getDefaultLanguage());

        Path path = Paths.get("iconhq.png");

        Path pathIcon = path.toRealPath();

        ImageIcon appIcon = new ImageIcon(pathIcon.toString());

        JFrame frame = new JFrame(tr.getWindowTitle());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.add(new Main());
        frame.setLocationByPlatform(true);
        frame.setMinimumSize(new Dimension(800, 500));

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (frame.getExtendedState() != Frame.MAXIMIZED_BOTH) {
                    Rectangle b = frame.getBounds();
                    frame.setBounds(b.x, b.y, b.width, b.width * 9 / 16);
                }
            }
        });

        JMenuBar menuBar = new JMenuBar();

        JMenu options = new JMenu(tr.getMenuItemNames("OPTIONS"));
        JMenuItem restart = new JMenuItem(tr.getMenuItemNames("RESTART"));
        JMenuItem exit = new JMenuItem(tr.getMenuItemNames("QUIT"));

        JMenu game = new JMenu(tr.getMenuItemNames("GAME"));
        JMenuItem rules = new JMenuItem(tr.getMenuItemNames("RULES"));
        JMenu languages = new JMenu(tr.getMenuItemNames("LANGUAGES"));
        JMenuItem english = new JMenuItem(tr.getMenuItemNames("ENGLISH"));
        JMenuItem portuguese = new JMenuItem(tr.getMenuItemNames("PORTUGUESE"));
        JMenuItem spanish = new JMenuItem(tr.getMenuItemNames("SPANISH"));

        JMenu colorBackground = new JMenu(tr.getMenuItemNames("COLORBACKGROUND"));
        JMenuItem backgroundGreen = new JMenuItem(tr.getMenuItemNames("GREEN"));
        JMenuItem backgroundRed = new JMenuItem(tr.getMenuItemNames("RED"));
        JMenuItem backgroundBlue = new JMenuItem(tr.getMenuItemNames("BLUE"));

        JPanel painel = new JPanel();
        JPanel containerPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        menuBar.add(game);
        menuBar.add(options);

        options.add(restart);
        options.add(exit);
        options.add(colorBackground);

        game.add(rules);
        game.add(languages);
        languages.add(english);
        languages.add(portuguese);
        languages.add(spanish);
        colorBackground.add(backgroundGreen);
        colorBackground.add(backgroundRed);
        colorBackground.add(backgroundBlue);
        painel.setBackground(color);
        containerPanel.setBackground(color);
        buttonPanel.setBackground(color);

        backgroundGreen.addActionListener(e -> {
            color = new Color(13, 98, 69);
            frame.paint(frame.getGraphics());
            painel.setBackground(color);
            containerPanel.setBackground(color);
            buttonPanel.setBackground(color);
        });

        backgroundRed.addActionListener(e -> {
            color = new Color(92, 0, 27);
            frame.paint(frame.getGraphics());
            painel.setBackground(color);
            containerPanel.setBackground(color);
            buttonPanel.setBackground(color);
        });

        backgroundBlue.addActionListener(e -> {
            color = new Color(0, 53, 95);
            frame.paint(frame.getGraphics());
            painel.setBackground(color);
            containerPanel.setBackground(color);
            buttonPanel.setBackground(color);
        });

        String[] hierarquia_cartas = {
                "Ás : Vale 11 pontos",
                "3 : Vale 10 pontos",
                "Rei : Vale 4 pontos",
                "Cavalo : Vale 3 pontos",
                "Valete : Vale 2 pontos",
                "   7, 6 , 5, 4, 2 : Vale 0 pontos   "};

        painel.setLayout(new GridLayout(hierarquia_cartas.length + 1, 1));
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Hierarquia das Cartas");
        titledBorder.setTitleColor(Color.WHITE);
        containerPanel.setBorder(new EmptyBorder(0, 5, 0, 0));
        painel.setBorder(titledBorder);

        // Adiciona os componentes ao painel
        for (int i = 0; i < hierarquia_cartas.length; i++) {
            JLabel label = new JLabel(hierarquia_cartas[i]);
            label.setFont(new Font("Arial", Font.BOLD, 18));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setForeground(Color.WHITE);
            painel.add(label);
        }

        rules.addActionListener(e -> {
            try {
                RulesController.showRules(tr, appIcon);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        restart.addActionListener(e -> restartGame(frame));

        exit.addActionListener(e -> frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)));

        english.addActionListener(e -> {
            lg.setDefaultLanguage("EN");
            restartGame(frame);
        });

        portuguese.addActionListener(e -> {
            lg.setDefaultLanguage("PT");
            restartGame(frame);
        });

        spanish.addActionListener(e -> {
            lg.setDefaultLanguage("SP");
            restartGame(frame);
        });

        try {
            RulesController.showRules(tr, appIcon);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        JToggleButton toggleButton = new JToggleButton("Mostrar/Esconder Hierarquia");
        toggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (toggleButton.isSelected()) {
                    containerPanel.add(painel, BorderLayout.CENTER);
                } else {
                    containerPanel.remove(painel);
                }
                containerPanel.revalidate();
                containerPanel.repaint();
            }
        });
        toggleButton.setPreferredSize(new Dimension(200, 30));
        toggleButton.setMaximumSize(new Dimension(200, 30));
        toggleButton.setMinimumSize(new Dimension(200, 30));

        buttonPanel.add(toggleButton, BorderLayout.WEST);
        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.add(containerPanel, BorderLayout.WEST);

        frame.setJMenuBar(menuBar);
        frame.setVisible(true);

    }
}
