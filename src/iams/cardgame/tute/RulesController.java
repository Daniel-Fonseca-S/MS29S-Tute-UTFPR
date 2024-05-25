package iams.cardgame.tute;

import iams.cardgame.tute.tr.Translator;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

public class RulesController {

    public static void showRules(Translator tr, ImageIcon appIcon) throws IOException {
        JFrame rulesFrame = new JFrame(tr.getWindowTitle());
        rulesFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        rulesFrame.setExtendedState(Frame.MAXIMIZED_BOTH);

        JTextArea textArea = new JTextArea(40, 100);
        textArea.setText(tr.getRulesText());
        textArea.setEditable(false);
        textArea.setLineWrap(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);

        JOptionPane.showMessageDialog(rulesFrame, scrollPane, tr.getMenuItemNames("RULES"), JOptionPane.INFORMATION_MESSAGE, appIcon);
    }
}
