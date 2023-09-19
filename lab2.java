import java.util.Random;

public class lab2
{

	public static void main(String[] args) //throws Exception
	{
		String[] SHORT_NAMES = 
		{ 
			"A","R", "N", "D", "C", "Q", "E", 
			"G",  "H", "I", "L", "K", "M", "F", 
			"P", "S", "T", "W", "Y", "V" 
		};

		String[] FULL_NAMES = 
		{
			"Alanine","Arginine", "Asparagine", 
			"Aspartic acid", "Cysteine",
			"Glutamine", "Glutamic acid",
			"Glycine","Histidine","Isoleucine",
			"Leucine", "Lysine", "Methionine", 
			"Phenylalanine", "Proline", 
			"Serine","Threonine","Tryptophan", 
			"Tyrosine", "Valine"
		};

		//used later to gen random int
		Random random = new Random();
		
		//letting user select time limit
		int allotted_time = timer();

		//tracking wrong answers
		int wrong = 0;

		//system start time
		long start = System.currentTimeMillis();
		long end = start + allotted_time;
		int score = 0;

		//while timer is still running
		while (System.currentTimeMillis() < end)
		{
			//determining which aa is selected
			int aaSelection = random.nextInt(FULL_NAMES.length);
			String aa = FULL_NAMES[aaSelection];
			String aaCode = SHORT_NAMES[aaSelection];
			System.out.println(aa);

			//let user guess aaCode
			// making all inputs upper at input
			String guess = System.console().readLine().toUpperCase();

			//isolating first ch of input in case user screws around
			char first_ch = guess.charAt(0);

			//letting user quit
			if (guess.equals("QUIT"))
			{
				terminate(score, wrong);
			}

			//checking if user is right/wrong
			if (first_ch == aaCode.charAt(0))
			{
				score++;
				correct(score, end);
			}
			else
			{
				incorrect(aaCode, score, end);
				wrong++;
			}
		}

		terminate(score, wrong);
	
	}

		//Trying to include more methods in my code as instructed in previous lab (wasnt really sure what to break off here as everything seemed so simple?)

		//method to adjust timer (took way longer than it should have)
		private static int timer()
		{
			//thought about asking in milliseconds, but thought this would be difficult for some users
			System.out.println("Enter Desired Time Limit in Seconds:");
			int input;

			//using try to catch in user inputs string or something
			try
			{
				input = Integer.parseInt(System.console().readLine());
				
				//capping at 5 mins bc Idk how stressful this could be to someones computer otherwise
				if (input <= 0 || input >= 300)
				{
				throw new IllegalArgumentException();
				}
			}
			catch (IllegalArgumentException e)
			{
				System.out.println("Error. Time Limit must be an integer larger than 0 and less than 300 \n");
				return timer();
			}
			int allotted_time = input * 1000;
			System.out.println("");
			return allotted_time;
		}

		//method to terminate program early
		private static void terminate(int score, int wrong)
		{
			if (wrong == 0)
			{
			System.out.println("\nFinal Score: " + score + " Correct! \n" + wrong + " Incorrect! \n");
			System.exit(0);
			}
			else
			{
			System.out.println("\nFinal Score: " + score + " Correct! \n" + wrong + " Incorrect :( \n");
			System.exit(0);				
			}
		}

		//method to report correct answers
		private static void correct(int score, long end)
		{
			System.out.println("Correct! Score: " + score + "\n" + ((end - System.currentTimeMillis()) / 1000) + " seconds remaining\n");
		}

		//method to report incorrect answers
		private static void incorrect(String aaCode, int score, long end)
		{
			System.out.println("Incorrect, the correct answer was: " + aaCode + "\nScore: " + score + "\n" + ((end - System.currentTimeMillis()) / 1000) + " seconds remaining\n");
		}


}
