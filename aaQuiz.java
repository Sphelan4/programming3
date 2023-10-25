//I used some chatGPT to help troubleshoot individual issues and fully understand how to use the different layout managers. I also did some troubleshooting with a class mate to have the questions call correctly.

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class aaQuiz extends JFrame 
{
    private final String[] SHORT_NAMES = { "A", "R", "N", "D", "C", "Q", "E", "G", "H", "I", "L", "K", "M", "F", "P", "S", "T", "W", "Y", "V" };
    private final String[] FULL_NAMES = { "alanine", "arginine", "asparagine", "aspartic acid", "cysteine", "glutamine", "glutamic acid", "glycine", "histidine", "isoleucine", "leucine", "lysine", "methionine", "phenylalanine", "proline", "serine", "threonine", "tryptophan", "tyrosine", "valine" };

    private int totalTime = 30;
    private JLabel timeLabel;
    private JLabel aaLabel;
    private JTextField userInputField;
    private JButton startButton;
    private JButton cancelButton;
    private JLabel correctLabel;
    private JLabel wrongLabel;
    private int correctCount = 0;
    private int wrongCount = 0;
    private Timer timer;
    private Random random = new Random();
    private String aminoAcidCode;

    public aaQuiz() 
    {
        timeLabel = new JLabel("Time left: " + totalTime + " seconds");
        aaLabel = new JLabel("");
        userInputField = new JTextField(8);
        startButton = new JButton("Start Quiz");
        cancelButton = new JButton("Cancel");
        correctLabel = new JLabel("Correct: 0");
        wrongLabel = new JLabel("Wrong: 0");

        //trying to center timer
        JPanel timePanel = new JPanel(new BorderLayout());
        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        timePanel.add(timeLabel, BorderLayout.CENTER);

        //centering aa label/quiz over message
        JPanel aaPanel = new JPanel(new BorderLayout());
        aaLabel.setHorizontalAlignment(JLabel.CENTER);
        aaPanel.add(aaLabel, BorderLayout.CENTER);        
        
        JPanel inputPanel = new JPanel();
        inputPanel.add(userInputField).setEnabled(false); //ensuring starting text field isnt usable until quiz starts 
        inputPanel.add(startButton);
        inputPanel.add(cancelButton);

        JPanel scorePanel = new JPanel();
        scorePanel.add(correctLabel);
        scorePanel.add(wrongLabel);

        JPanel quizPanel = new JPanel();
        quizPanel.setLayout(new BoxLayout(quizPanel, BoxLayout.Y_AXIS));
        quizPanel.add(aaPanel);
        quizPanel.add(inputPanel);
        quizPanel.add(timePanel);
        quizPanel.add(scorePanel);

        startButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                startQuiz();
            }
        });

        cancelButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                cancelQuiz();
            }
        });

        //adding ActionListener for userInputField here
        userInputField.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                String userInput = userInputField.getText().toUpperCase();
                if (userInput.equals(aminoAcidCode)) 
                {
                    correctCount++;
                    correctLabel.setText("Correct: " + correctCount);
                } 
                else 
                {
                    wrongCount++;
                    wrongLabel.setText("Wrong: " + wrongCount);
                }
                newQuizRound();
            }
        });

        add(quizPanel);
        setTitle("Amino Acid Quiz");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void newQuizRound() 
    {
        //selecting a new random aa
        int randomSelection = random.nextInt(FULL_NAMES.length);
        String aminoAcid = FULL_NAMES[randomSelection];
        aminoAcidCode = SHORT_NAMES[randomSelection];
        aaLabel.setText(aminoAcid); //adding label with chosen aa
        userInputField.setText(""); //resetting user input area
    }

    private void startQuiz() 
    {
        startButton.setEnabled(false); //deactivating start button
        cancelButton.setEnabled(true); //enabling cancel button
        timeLabel.setVisible(true); //enabling timer countdown
        userInputField.setEnabled(true); //ensuring input field is active

        //setting timer to count down 1 second at a time and terminate if time = 0 
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() 
        {
            @Override
            public void run() 
            {
                if (totalTime == 0) 
                {
                    endQuiz();
                } 
                else 
                {
                    timeLabel.setText("Time left: " + totalTime + " seconds");
                    totalTime--;
                }
            }
        }, 1000, 1000);

        //triggers next aa question
        newQuizRound();
    }

    private void endQuiz() 
    {
        
        timeLabel.setVisible(false); //hiding time left
        userInputField.setEnabled(false); //not letting user interact
        cancelButton.setEnabled(false); //not letting user cancel
        startButton.setEnabled(true); //leaving start button active so user can start again
        aaLabel.setText("Quiz Over!"); // end message
        userInputField.setText(""); //emptying input field so last input isnt visible
        timer.cancel(); //ending timer
    }

    private void cancelQuiz() 
    {
        endQuiz(); //ends quiz when cancelButton is used
    }

    public static void main(String[] args) 
    {
        //actually creates the instance of aaQuiz
        SwingUtilities.invokeLater(new Runnable() 
        {
            @Override
            public void run() {
                new aaQuiz().setVisible(true);
            }
        });
    }
}
