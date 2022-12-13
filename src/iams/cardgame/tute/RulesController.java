package iams.cardgame.tute;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import iams.cardgame.tute.tr.Translator;

public class RulesController {
	
	public static void ShowRules(Translator tr, ImageIcon appIcon) throws IOException
	{
		JFrame rulesFrame = new JFrame(tr.getWindowTitle());
        rulesFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        rulesFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JTextArea textArea = new JTextArea(40, 100);
        textArea.setText(tr.getRulesText());
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        
        JOptionPane.showMessageDialog(rulesFrame, scrollPane, tr.getMenuItemNames("RULES"), JOptionPane.INFORMATION_MESSAGE, appIcon);
	}
}
