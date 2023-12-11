import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;



public class LogAnalyzer {
    
    public static void main(String[] args) {
        int JUMP_THRESH = 10000; // Threshold for what is considered a jump
		double FJUMP_MAX = .5;  // The maximum frequency of jumps before log is flagged
		double FFLIP_MAX = .1;  // The maximum frequency of flips before log is flagged
        
        String fileInName = "input.txt";    // Input file
        String fileOutName = "output.csv";  // Output file

        try (BufferedReader reader = new BufferedReader(new FileReader(fileInName))) {
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(fileOutName))) {

            
                String line;
                int prevValue = 0;
                int prevInputNumber = 0;
                int totalJumps = 0;
                int totalInputs = 0;
                int flipCount = 0;
                int flip = 0;
			    int flag = 0;
                double jumpFrequency;
                double flipFrequency;
            
                writer.printf("Input Number, Start Value, End Value, Jump Amount%n");
                while ((line = reader.readLine()) != null) {
                    if (Character.isDigit(line.charAt(line.length()-1))) { // check if directional input line
    
                        String[] parts = line.split("\\s+");
                        int inputNumber = Integer.parseInt(parts[0]);
                        int value = Integer.parseInt(parts[parts.length - 1]);

                        if ((prevValue > 0 && value <= 0) || (prevValue <= 0 && value > 0)) {
                            flip = 1;
                            flipCount++;
                        }
                    
                        int jump = Math.abs(value - prevValue);  // Jump amount can be changed with THRESH value
                        if (jump > JUMP_THRESH) {
                            totalJumps++;
                        }   
                    
                        writer.printf("%d, %d, %d, %d, %d%n", prevInputNumber, prevValue, value, jump, flip);

                        // Update previous values for the next iteration
                        flip = 0;
                        totalInputs++;
                        prevValue = value;
                        prevInputNumber = inputNumber;
                    }
                }
            
                jumpFrequency = (double)totalJumps/(double)totalInputs;
                flipFrequency = (double)flipCount/(double)totalInputs;
			    if (jumpFrequency > FJUMP_MAX || flipFrequency > FFLIP_MAX) {
				    flag = 1;
    			}
	    		writer.printf("%nType, Threshold, Actual Value, Jump Amount%n");
                writer.printf("Jump Frequency, %f, %f%n", FJUMP_MAX, jumpFrequency);
                writer.printf("Flip Frequency, %f, %f%n", FFLIP_MAX, flipFrequency);
		    	writer.printf("Flag, %d", flag);
			
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
