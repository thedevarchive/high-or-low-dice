import java.awt.*; 
import java.awt.Font.*; 
import javax.swing.plaf.FontUIResource; 
import java.awt.event.*; 
import javax.swing.*; 
import javax.swing.JSpinner.*; 
import javax.swing.JComponent; 
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.util.Random; 
import java.util.concurrent.*;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.plaf.metal.*; 

public class Dice extends JFrame
{
    private final JPanel panel1;
    private final JPanel diceFrame;  
    private JLabel d1; 
    private JLabel d2; 
    private JTextArea l1; 

    private final JPanel panel2; 
    private final JPanel inner; 
    private final JPanel wagePan; 
    private final JPanel radioFrame; 
    private final JPanel buttonFrame; 
    //private final JPanel txt; 
    private JRadioButton r1; 
    private JRadioButton r2; 
    private JRadioButton r3; 
    private JLabel l2; 
    private JLabel l3; 
    private JLabel l4; 
    private JSpinner t;
    private JButton b;  
    private JButton exit; 
    private ButtonGroup bg; 

    private int amt; 

    private static final String WIN = "cash_register_x.wav";
    private static final String LOSE = "BUZZER.wav";
    private static final String NO_MONEY = "Wa-wa-wa-sound.wav";
    private static final String GAME_WIN = "applause10.wav"; 
    private static final String ROLL = "dice.wav"; 
    private Clip buzz = null; 

    SpinnerModel value =  
                new SpinnerNumberModel(100, //initial value  
                100, //minimum value  
                10000, //maximum value  
                100); //step  

    public Dice()
    {
        UIManager.put("Label.font", new FontUIResource(new Font("Dialog", Font.PLAIN, 16)));
        UIManager.put("Button.font", new FontUIResource(new Font("Dialog", Font.PLAIN, 16)));
        //UIManager.put("Spinner.font", new FontUIResource(new Font("Dialog", Font.PLAIN, 30)));
        UIManager.put("RadioButton.font", new FontUIResource(new Font("Dialog", Font.PLAIN, 16))); 
        //UIManager.put("Spinner.arrowButtonSize", new Dimension(1000, 1000)); 

        try {
            // Set cross-platform Java L&F (also called "Metal")
        UIManager.setLookAndFeel(
            UIManager.getCrossPlatformLookAndFeelClassName());
        } catch(Exception e)
        {
            System.exit(1);
        }

        setLayout(new GridLayout(1, 2)); 
        panel1 = new JPanel(); 
        panel1.setLayout(new GridLayout(2, 1));
        diceFrame = new JPanel(); 
        diceFrame.setLayout(new GridLayout(1, 2));
        diceFrame.setBackground(new Color(0, 153, 0)); 
        d1 = new JLabel(); 
        d2 = new JLabel(); 
        l1 = new JTextArea();
        l1.setEditable(false);
        diceFrame.add(d1); 
        diceFrame.add(d2); 
        panel1.add(diceFrame); 
        panel1.add(l1); 
        add(panel1); 

        panel2 = new JPanel(); 
        panel2.setLayout(new GridLayout(2, 1)); 
        inner = new JPanel(); 
        inner.setLayout(new GridLayout(1, 2));
        wagePan = new JPanel(); 
        wagePan.setLayout(new GridLayout(4, 1));
        radioFrame = new JPanel(); 
        radioFrame.setLayout(new GridLayout(3, 1));
        r1 = new JRadioButton("Low (2-6)", false);
        r2 = new JRadioButton("Mid (7)", true); 
        r3 = new JRadioButton("High (8-12)", false); 
        radioFrame.add(r1); 
        radioFrame.add(r2); 
        radioFrame.add(r3); 
        l2 = new JLabel(); 
        //txt = new JPanel(); 
        //txt.setLayout(new GridLayout()); 
        amt = 1000; 
        String s = "Amount on hand"; 
        l2.setText(s);
        l4 = new JLabel(); 
        l4.setFont(new Font("Dialog", Font.PLAIN, 25));
        l4.setText("$" + amt); 
        l3 = new JLabel(); 
        l3.setText("Wager");
        t = new JSpinner(value); 
        JFormattedTextField tspin =((JSpinner.DefaultEditor)t.getEditor()).getTextField(); 
        tspin.setEditable(false); 
        tspin.setColumns(5); 
        ((JSpinner.NumberEditor) t.getEditor()).getTextField().setFont(new Font("Dialog", Font.PLAIN, 25));
        
        buttonFrame = new JPanel(); 
        buttonFrame.setLayout(new GridLayout(1, 2)); 
        b = new JButton(); 
        b.setText("Roll");
        b.setFont(new Font("Dialog", Font.PLAIN, 30));
        exit = new JButton(); 
        exit.setText("Quit"); 
        exit.setFont(new Font("Dialog", Font.PLAIN, 30)); 
        buttonFrame.add(b); 
        buttonFrame.add(exit); 
        wagePan.add(l2); 
        wagePan.add(l4); 
        wagePan.add(l3); 
        wagePan.add(t);

        inner.add(radioFrame); 
        inner.add(wagePan);
        panel2.add(inner); 
        panel2.add(buttonFrame); 
        add(panel2); 

        bg = new ButtonGroup(); 
        bg.add(r1); 
        bg.add(r2); 
        bg.add(r3); 

        try {
            buzz = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "FATAL ERROR", JOptionPane.ERROR_MESSAGE);
        }


        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int check = (Integer) t.getValue(); 

                if(check > amt) 
                {
                    JOptionPane.showMessageDialog(null, "Wager should not be greater than the amount you currently have.");
                }
                else 
                {
                    int delay = 400; 
                    int period = 400; 
                    Timer timer = new Timer(); 

                    timer.scheduleAtFixedRate(new TimerTask() 
                    {
                        int count = 0;

                        public void run() 
                        {
                            if(count == 0)
                            {
                                try {
                                    buzz.close(); 
                                    buzz.open(AudioSystem.getAudioInputStream(getClass().getResource(ROLL)));
                                    buzz.start(); 
                                }
                                catch (Exception ex) {
                                    System.exit(1);
                                }
                            }
                            roll(d1); 
                            roll(d2); 
                            count++;       
                                    
                            if(count == 5) {
                                timer.cancel();
                                int sum = roll(d1) + roll(d2); 
                                Timer timer2 = new Timer(); 
                                timer2.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        int w = (Integer) t.getValue(); 
                                        String dis = ""; 
                                        String bet = ""; 
                                        if(r1.isSelected())
                                        {
                                            bet = "low"; 
                                            System.out.println(bet); 
                                        }
                                        else if (r2.isSelected())
                                        {
                                            bet = "mid"; 
                                            System.out.println(bet); 
                                        }
                                        else if (r3.isSelected())
                                        {
                                            bet = "high"; 
                                            System.out.println(bet); 
                                        }
                                        dis = "You chose "; 
                                        dis += bet; 
                                        dis += "\nYou rolled ";
                                        dis += sum;  
                                        if((sum <= 6 && sum >= 2 && bet.equals("low")) || (sum == 7 && bet.equals("mid")) || (sum <= 12 && sum >= 8 && bet.equals("high"))) 
                                        {
                                            buzz.close(); 
                                            try{
                                                buzz.open(AudioSystem.getAudioInputStream(getClass().getResource(WIN)));
                                                buzz.start();
                                            }
                                            catch (Exception ex) {
                                                System.exit(1);
                                            }
                                            amt -= w; 
                                            w *= 2; 
                                            amt += w; 
                                            dis += "\nYou win!"; 
                                        }
                                        else
                                        { 
                                            buzz.close(); 
                                            try{
                                                buzz.open(AudioSystem.getAudioInputStream(getClass().getResource(LOSE)));
                                                buzz.start();
                                            }
                                            catch (Exception ex) {
                                                System.exit(1);
                                            }
                                            amt -= w; 
                                            dis += "\nYou lose."; 
                                        }
                                        dis += "\nYou now have $" + amt; 
                                        l4.setText("$" + amt);
                                        
                                        if(amt <= 0)
                                        {
                                            Timer timer3 = new Timer(); 
                                            timer3.schedule(new TimerTask() {
                                                public void run()
                                                {
                                                    buzz.close(); 
                                                try{
                                                    buzz.open(AudioSystem.getAudioInputStream(getClass().getResource(NO_MONEY)));
                                                    buzz.start();
                                                }
                                                catch (Exception ex) {
                                                    System.exit(1);
                                                }
                                                }
                                            }, 1000); 
                                            
                                            dis += "\nYou ran out of money!"; 
                                            b.setEnabled(false);
                                        }
                                        else if(amt >= 10000)
                                        {
                                            buzz.close(); 
                                            try{
                                                buzz.open(AudioSystem.getAudioInputStream(getClass().getResource(GAME_WIN)));
                                                buzz.start();
                                            }
                                            catch (Exception ex) {
                                                System.exit(1);
                                            }
                                            dis += "\nYou got $10000! Game over."; 
                                            b.setEnabled(false);
                                        }
                                        l1.setText(dis);
                                    }
                                }, 2000);
                                
                            }
                        } 
                    }, delay, period);
                }
            }
        });   

        exit.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            int confirmed = JOptionPane.showConfirmDialog(null, "Exit Program?","EXIT",JOptionPane.YES_NO_OPTION);
            if(confirmed == JOptionPane.YES_OPTION)
            {
                dispose();
            }
            else {
                setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });
    }

    public static int roll(JLabel l)
    {
        Random r = new Random();
        int num = r.nextInt(6) + 1; 
        l.setIcon(new ImageIcon("die" + num + ".png"));
        return num; 
    }

    public static void main(String args[])
    {
        JFrame dice = new Dice(); 
        dice.setSize(900, 440); 
        //dice.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        dice.addWindowListener( new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                JFrame frame = (JFrame)e.getSource();
        
                int result = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to exit the application?",
                    "Exit Application",
                    JOptionPane.YES_NO_OPTION);
        
                if (result == JOptionPane.YES_OPTION)
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
        dice.setVisible(true); 
        JOptionPane.showMessageDialog(null, "HOW TO PLAY\nPlace a bet on whether the dice will roll a low value (2-6), a middle value (7), or a high value (8-12).\nAfter settling on a value to wager on, click Roll.\nIf you win, the amount you bet would be doubled.\nIf you lose, you lose the amount you wagered. The game ends when you reach $10000.");
    }

}