// lab 3
//special thanks to Alex for talking me through some of this as I was completely at a loss for how to do some of this

//importing all the bits
import java.util.List;
import java.util.ArrayList; 
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;

 
public class FastaSequence 
{
    
    //only accessible in the FastaSequence class 
    private String header;
    private String sequence;

    
    //composing headers & seqs
    public FastaSequence(String header, String sequence) 
    {
        this.header = header;
        this.sequence = sequence;
    }


    //getting header - first ch (should be the <)
    public String getHeader() 
    {
        return header.substring(1);
    }


    //getting seq
    public String getSequence() 
    {
        return sequence;
    }


    //getting gc ratio
    public float getGCRatio() 
    {
        int counter = 0;
        float ratio;
        //for ch in nuc if ch == g/c then increase counter
        for (char nucleotide : sequence.toCharArray()) 
        {
            if (nucleotide == 'G' || nucleotide == 'C') 
            {
                counter++;
            }
        }
        
        //trying to round to two decimals
        ratio = (float) counter / sequence.length();
        ratio = (float)(Math.round((ratio) * 100.0) / 100.0);
        
        return ratio;
    }

    
    //reading in fasta file
    public static List<FastaSequence> readFastaFile(String filePath) throws Exception
    {
        //making a list to store the FastaSequence objects
        List<FastaSequence> seqList = new ArrayList<>();
        
        //storing headers and building seqs from fasta file
        String header = null;
        StringBuilder seq = new StringBuilder();

        //reading in fasta file
        try (Scanner reader = new Scanner(new File(filePath))) 
        {
            //reading every line in given file until null is produced (file ends)
            while (reader.hasNextLine()) 
            {
                //current line in file
                String line = reader.nextLine();
                
                //identifying headers by searching for >
                if (line.startsWith(">")) 
                {
                    //when a header is identified start collecting header and seq to send to FastaSequence
                    if (header != null) 
                    {
                        //Sending any current seqs to FastaSequence before a new header starts
                        seqList.add(new FastaSequence(header, seq.toString()));
                        
                        //clearing seqs for next set of header:seqs
                        seq.setLength(0);
                    }

                    //next header
                    header = line;
                } 
                
                //if next line is not a header, appending line to previous line to handle multiline seqs
                else 
                {
                    seq.append(line);
                }
            }
        } 

        //printing specific error message for user in case an innapropriate file is called
        catch (Exception e) 
        {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
        }
       
        //making sure last seq is grabbed also
        if (header != null) 
        {
            seqList.add(new FastaSequence(header, seq.toString()));
        }

        //returning list of FastaSequence objects containing parsed data from the FASTA file
        return seqList;

    }


    //required writeTableSummary method - wondering if I should throw a specific exception? Like Exception exception vs general exception in python?
    public static void writeTableSummary(List<FastaSequence> list, File outputFile) throws Exception 
    {
        //writing output to specified file using BufferedWriter in case a large fasta file is ever used
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) 
        {
            //header row per instructions, \t sep and \n at end
            writer.write("sequenceID\tnumA\tnumC\tnumG\tnumT\tsequence\n");

            //iterating through all FastaSequence in list
            for (FastaSequence sequence : list) 
            {
                //getting header and seq
                String sequenceID = sequence.getHeader();
                String sequenceData = sequence.getSequence();

                //counting nuc occurrences
                int numA = countNuc(sequenceData, 'A');
                int numC = countNuc(sequenceData, 'C');
                int numG = countNuc(sequenceData, 'G');
                int numT = countNuc(sequenceData, 'T');

                //writing out the collected data, had to add double /t after sequenceID to fix formatting issues
                writer.write(sequenceID + "\t\t" + numA + "\t" + numC + "\t" + numG + "\t" + numT + "\t" + sequenceData + "\n");
            }
        }
    }

    
    //sep method to actually count nuc bc trying to seperate my functions more per previous feedback
    private static int countNuc(String sequence, char nucleotide) 
    {
        //using 'counter' again, if i understand this variable is local to the method and should be unique
        int counter = 0;

        //iterating through each ch in sequence and adding to counter
        for (char ch : sequence.toCharArray()) 
        {
            if (ch == nucleotide) 
            {
                counter++;
            }
        }
        return counter;
    }


    //calling FastaSequence and writeTableSummary, trying to mimic format in pp but some of my stuff is slightly different.
    public static void main(String[] args) 
    {
        try 
        {
            List<FastaSequence> fastaSequences = readFastaFile("C:\\Users\\slphe\\Desktop\\p3\\ex.fasta");
            File outputFile = new File("output.txt");
            
            //writing output 
            writeTableSummary(fastaSequences, outputFile);
            System.out.println("\nTable summary written to output.txt\n");

            //iterating through all fastaseqs
            for (FastaSequence sequence : fastaSequences) 
            {
                System.out.println("Header: " + sequence.getHeader());
                System.out.println("Sequence: " + sequence.getSequence());
                System.out.println("GC Ratio: " + sequence.getGCRatio() + "\n");
            }
        } 
        
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

}


/*
Extra Credit:
ChatGPT generated a very similar format to me, which makes sense given the specificity. 
The ChatGPT included a feature that ensured all files read in had the ACTG in uppercase 
which seems nice but irrelevant and I actually revised my writeTableSummary method based 
on its input to use BufferedWriter as it is a better fit for larger documents (my test 
document was not large, but FASTA files can definitely get large so it seemed like a solid 
improvement). 


The ChatGPT generated output did not handle multiline fasta sequences - I think this was likely 
due to how I put in the instructions - I think it read the first few commands and then basically 
ignored everything else but I dont use chatGPT often and I am not used to how finnicky it is when it comes to input
*/