/* lab 01

How often would you expect to see this 3mer by chance?

probability of getting A = 0.25
p of AAA = 0.25^3 = 0.015625
p of AAA in 1000 gen = 0.015625 * 1000 = 15.625
p of AAA in 1000 gen is 16~

Is Java’s number close to the number that you would expect?
Yes. My java output was consistently around 16 - after running 3x the actual nums were: 17, 15, 14


(3) Modify your code so that the frequency of A,C,G and T is

		p(A) = 0.12
		p(C) = 0.38
		p(G) = 0.39
		p(T) = 0.11

What is the expected frequency now of “AAA”?  Does Java produce “AAA” at close to the expected frequency?

p of getting A = 0.12
p of AAA = 0.12^3 = 0.001728
p of AAA in 1000 gen = 0.001728 * 1000 = 1.728
P of AAA in 1000 gen is 2~

Yes. My java output was consistently around 2 - after running 3x the actual nums were: 1, 2, 1


*/


import java.util.Random;

public class lab1 
{
    public static void main(String[] args) 
    {
        // nucleotide array & codon variable
        String[] nuc = {"A","T","C","G"};
        String codon = "";

        // counter
        int aaa_c = 0;

        Random random = new Random();

        // q1 - uniform prob loop
        for (int c = 0; c < 1000; c++) 
        {
            //looping through nuc array 3x to create 3mer
            for (int i = 0; i < 3; i++) 
            {
                codon += (nuc[random.nextInt(nuc.length)]);
            }

            // tracking frequency of AAA
            if (codon.equals("AAA")) 
            {
                aaa_c++;
            }

            // resetting codon val for next iteration
            codon = "";
        }

        // ouput
        System.out.println("Actual Frequency of AAA: " + aaa_c);
    

        // q3 weighted loop
      
        // resetting counter
        aaa_c = 0;
  
        for (int c = 0; c < 1000; c++) 
        {
            //looping through nuc array 3x to create 3mer
            for (int i = 0; i < 3; i++) 
            {
              //genning rand num between 0-99
              int p = (random.nextInt(100));
              
              //A 0-12 (12%)
              if (p <12)
                codon += (nuc[0]);
              
              //T 12-23 (11%)
              else if (p <23)
                codon += (nuc[1]);
              
              //C 23-61 (38%)
              else if (p<61)
               codon += (nuc[2]);
              
              //G 61-99 (39%)
              else
               codon += (nuc[3]); 
            }
            
            // tracking frequency of AAA
            if (codon.equals("AAA")) 
                aaa_c++;
    
            // resetting codon val for next iteration
            codon = "";
        } 

    //output  
    System.out.println("Actual Frequency of AAA with weighted values: " + aaa_c);

    }
}