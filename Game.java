
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Game extends JFrame implements ActionListener 
{
JPanel mainPanel;
JPanel optionPanel;
JPanel dicePanel;
JButton rollDice;
JMenu options;
JMenuItem quit;
JMenuItem explanation;
JMenuBar menuBar;
JLabel diceLabel;
JLabel diceLabel2;
DiceRoll dr;
Graphics die1;
Graphics die2;

public Game()
{
    setTitle("Rollin' Dice");
    this.setPreferredSize(new Dimension(600,600));

    mainPanel = new JPanel();
    optionPanel = new JPanel();
    dicePanel = new JPanel();
    rollDice = new JButton("Roll Dice");
    options = new JMenu("Options");
    quit = new JMenuItem("Quit");
    explanation = new JMenuItem("Explanation");
    menuBar = new JMenuBar();
    dr = new DiceRoll();
    diceLabel = new JLabel();
    diceLabel2 = new JLabel();

    options.add(quit);
    options.add(explanation);

    menuBar.add(options);

    optionPanel.add(menuBar);
    optionPanel.setPreferredSize(new Dimension(600,100));

    dicePanel.add(rollDice);

    dicePanel.add(diceLabel);
    dicePanel.add(diceLabel2);

    mainPanel.setPreferredSize(new Dimension(600,600));
    mainPanel.add(optionPanel);
    mainPanel.add(dicePanel);


    quit.addActionListener(this);
    explanation.addActionListener(this);
    rollDice.addActionListener(this);

    this.getContentPane().add(mainPanel);

    this.pack();
    this.setVisible(true);
}


public void actionPerformed(ActionEvent e) {
    if (e.getSource()== quit)
        System.exit(0);

    if (e.getSource() == explanation)
    {
        JOptionPane.showMessageDialog(mainPanel,
                "Win: Roll a sum that is an even number \nLose: Roll a sum that is an odd number" + dicePanel, "Rules", JOptionPane.INFORMATION_MESSAGE); 
    }

    if (e.getSource() == rollDice)
    {

        dr.roll(die1);
        dr.roll(die2);

        diceLabel.updateUI();

        dicePanel.updateUI();
    }

}

public static void main (String []args)
{
    Game dg = new Game();
}
}