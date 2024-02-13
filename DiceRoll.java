import java.awt.Graphics;
    import java.awt.image.BufferedImage;
    import javax.imageio.*;
    import java.io.File;
    import java.io.IOException;
    import javax.swing.JComponent;

public class DiceRoll extends JComponent {

private BufferedImage die1;
private BufferedImage die2;
private BufferedImage die3;
private BufferedImage die4;
private BufferedImage die5;
private BufferedImage die6;


public DiceRoll()
{
    try {
        die1 = (ImageIO.read(new File("die1.png")));
        die2 = ImageIO.read(new File("die2.png"));
        die3 = ImageIO.read(new File("die3.png"));
        die4 = ImageIO.read(new File("die4.png"));
        die5 = ImageIO.read(new File("die5.png"));
        die6 = ImageIO.read(new File("die6.png"));
    } catch (IOException ex){
        System.err.println("That is invalid");
    }
}


public Graphics roll(Graphics g)
{
    int dieResult = (int)(6 * Math.random());

    switch(dieResult){
    case 1: 
        g.drawImage(die1, 0, 0, null);
        break;
    case 2:
        g.drawImage(die2, 0, 0, null);
        break;
    case 3:
        g.drawImage(die3, 0, 0, null);
        break;
    case 4:
        g.drawImage(die4, 0, 0, null);
        break;
    case 5:
        g.drawImage(die5, 0, 0, null);
        break;
    case 6:
        g.drawImage(die6, 0, 0, null);
        break;
    }
    return g;
}

public void roll(JLabel dieLabel) {
    int dieResult = (int)(6 * Math.random());
    dieLabel.setIcon(new ImageIcon("die" + dieResult + ".png"));
}
}