/*
I was not sure how to report on increase in speed when threads were increased/ decreased but you can manpiulate the threads used and see the
prime nums found and time taken in the gui. Sorry, I couldnt figure this one out all the way. When I manually run the program 2x with 1 thread and 2 threads
the difference is visible with 1 thread finding 2,025,824 and 2 threads finding 3,412,554 in the 15ish seconds allotted. 
*/

//getting all the bits
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//main class
public class PrimeFinder extends JFrame 
{
    //intializing all the bits
    private ExecutorService executorService;
    private JTextArea intermediateTextArea;
    private JTextArea finalTextArea;
    private JButton startButton;
    private JButton cancelButton;
    private JTextField threadCountField;
    private volatile boolean isCancelled = false;
    private int threadCount = 1; //starting my threadcount at 1 bc i assume less than that would break it


    //constructor for gui
    public PrimeFinder() 
    {
        //setting up main JFrame with title and size
        setTitle("Prime Finder");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //making a centered main title because I thought it looked nice
        JLabel titleLabel = new JLabel("Prime Finder", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        //initializing text fields for both result areas
        intermediateTextArea = new JTextArea();
        intermediateTextArea.setLineWrap(true);
        intermediateTextArea.setWrapStyleWord(true);
        intermediateTextArea.setEditable(false);

        finalTextArea = new JTextArea();
        finalTextArea.setLineWrap(true);
        finalTextArea.setWrapStyleWord(true);
        finalTextArea.setEditable(false);

        //initializing buttons and thread text field
        startButton = new JButton("Start");
        cancelButton = new JButton("Cancel");
        threadCountField = new JTextField("1");

        //action listeners for buttons
        startButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                startPrimeFinding();
            }
        });
        cancelButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                cancelPrimeFinding();
            }
        });

        //major panel everything goes on
        JPanel panel = new JPanel(new BorderLayout());

        //top panel with title label
        JPanel topPanel = new JPanel();
        topPanel.add(titleLabel);
        //spacing around top of title label
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        //bottom panel with thread count field and buttons
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(new JLabel("Thread Count:"));
        bottomPanel.add(threadCountField);
        bottomPanel.add(startButton);
        bottomPanel.add(cancelButton);

        //result panel with both result fields
        JPanel resultPanel = new JPanel(new GridBagLayout());

        //intermediate result panel
        JPanel intermediatePanel = new JPanel(new BorderLayout());
        intermediatePanel.setBorder(BorderFactory.createTitledBorder("Intermediate Results"));
        intermediatePanel.add(intermediateTextArea, BorderLayout.CENTER);

        //final result panel
        JPanel finalPanel = new JPanel(new BorderLayout());
        finalPanel.setBorder(BorderFactory.createTitledBorder("Final Results"));
        finalPanel.add(finalTextArea, BorderLayout.CENTER);

        //using GridBagLayout to make the intermediate panel 0.5x the width of the final panel
        GridBagConstraints gbc = new GridBagConstraints();
        //intermediate panel in left col 0,0 with equal height/ width 
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;

        //setting both boxes to expand to fill given space as it looked nicer
        gbc.fill = GridBagConstraints.BOTH;
        resultPanel.add(intermediatePanel, gbc); //adding intermediate panel to result panel

        //final panel in right col 1,0 with 2x the width of the intermediate panel
        gbc.gridx = 1;
        gbc.weightx = 2;
        resultPanel.add(finalPanel, gbc);

        //adding all panels to major panel
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(resultPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        //adding major panel to JFrame
        add(panel);

        //making visible
        setVisible(true);

        //initializing the ExecutorService with a single thread initially
        executorService = Executors.newFixedThreadPool(threadCount);
    }

    //method to start prime finding process
    private void startPrimeFinding() 
    {
        //resetting text areas when cancelled/time is up
        isCancelled = false;
        intermediateTextArea.setText("");
        finalTextArea.setText("");
    
        //getting thread count
        threadCount = Integer.parseInt(threadCountField.getText());
        
        //intializing executor service with specified thread count
        executorService = Executors.newFixedThreadPool(threadCount);
    
        //start time and list to store primes found
        long startTime = System.currentTimeMillis();
        List<Integer> primes = new ArrayList<>();
    
        //timer to cancel prime finding after 15 seconds if user hasnt canceled already
        Timer timeoutTimer = new Timer(15000, new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                cancelPrimeFinding();
            }
        });
        timeoutTimer.setRepeats(false);
        timeoutTimer.start();
    
        //looping through all threads
        for (int i = 0; i < threadCount; i++) 
        {
            final int currentNumber = i;
            executorService.submit(() -> 
            {
                int number = currentNumber;
                long intermediateStartTime = startTime;
    
                //calling prime finding method until cancelled/ time up
                while (!isCancelled) 
                {
                    if (isPrime(number)) 
                    {
                        primes.add(number);
                    }
                    number++;
    
                    //updating gui with intermediate results every 2ish seconds
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - intermediateStartTime >= 2000) 
                    {
                        final int primeCount = primes.size();
                        //using setText to overwrite previus intermediate results for a cleaner look (instead of keeping all intermediate results)
                        SwingUtilities.invokeLater(() -> 
                        {
                            intermediateTextArea.setText("Primes found: " + primeCount + "\n");
                        });
                        intermediateStartTime = currentTime;
                    }
                }
                //pushing info to final results method once canceled/times up
                updateResults(primes, startTime);
            });
    
        }
    }
    
    //method to update final results
    private void updateResults(List<Integer> primes, long startTime) 
    {
        SwingUtilities.invokeLater(new Runnable() 
        {
            @Override
            //pushing final results to gui - only showing tim ein milliseconds bc you lose precision when converting to seconds
            public void run() 
            {
                finalTextArea.setText("Primes found: " + primes.size() + "\nTotal Time: " + (System.currentTimeMillis() - startTime) + " milliseconds");
            }
        });
    }
    

    //method to stop finding prime nums
    private void cancelPrimeFinding() 
    {
        isCancelled = true;
        executorService.shutdown(); //shutting down thread pool
    }

    //method to check for prime nums - found this on stack overflow bc I was struggling with the math of prime nums
    //https://stackoverflow.com/questions/46536431/writing-an-isprime-function-in-java-and-using-math-sqrt
    private boolean isPrime(int num) 
    {
        if (num <= 1) 
        {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(num); i++) 
        {
            if (num % i == 0) 
            {
                return false;
            }
        }
        return true;
    }

    //main method - intiailizing gui
    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(() -> new PrimeFinder());
    }
}

