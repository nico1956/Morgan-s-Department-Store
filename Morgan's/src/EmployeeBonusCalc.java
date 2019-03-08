
/*
 * This program allows the user to input how many full weeks an employee has worked and
 * how many positive review he or she received. These 2 entries will lookup on a pre loaded array
 * the bonus rate that the employee will get. Each rate will be accumulated in an empty array that
 * is the same size of the pre loaded one.
 * 
 *  Nico Busatto, 03/07/2019
 */

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.NumberFormat;

public class EmployeeBonusCalc {
	
	static NumberFormat nf; //Used to format currency
	static int iWeeks;      //Used to store the weeks enter by user
	static int iReviews;    //Used to store the positive reviews enter by user
	static double cRate;    //Used to store the looked up rate
	static int cCounter = 0;    	//Total number bonuses
	static double cTotBonuses = 0;  //Total value of bonuses
	static Scanner rateScanner;
	static Scanner menuScanner;
	static double[][] payRateTab = new double[7][];
	static double[][] payRateAcc = new double[payRateTab.length][payRateTab.length];
	
	public static void main(String[] args) {
		
		boolean ok = true;
		char iAgain = 'Y';
		char fAgain = 'Y';
		boolean goAgain = false;
		
		init();
		do {
			iAgain = input();
			if (iAgain != 'Y') { //Print Accumulator array if user enter option 2
				printArray();
				do {
					try { //Try catch for Again input after printing Summary report
						System.out.println("Again? Y/N");
						fAgain = menuScanner.next().trim().toUpperCase().charAt(0);
					} catch (Exception e1) {
						System.out.println("Invalid entry. Only Y or N allowed.");
						ok = false;
					}
					if(fAgain == 'Y') {  //If user prompt Y, restart the program
						goAgain = true;
						ok = true;
					} else if (fAgain == 'N') {
						goAgain = false;
						ok = true;
					} else {
						System.out.println("Invalid entry. Only Y or N allowed.");
						ok = false;
					}
				} while (!ok);	
			}
		} while (goAgain == true);  //Program restart checking
		
		if (goAgain == false) {  //Terminate program if user prompt N
			exit();
		}		
	}
	
    public static void init() {
    	//Number formatter
    	nf = NumberFormat.getCurrencyInstance(java.util.Locale.US);
    	
    	//Menu scanner
    	menuScanner = new Scanner(System.in);
    	menuScanner.useDelimiter(System.getProperty("line.separator"));
    	
    	//Rate array scannner
    	rateScanner = new Scanner(System.in);
    	rateScanner.useDelimiter(System.getProperty("line.separator"));
    	
    	//Try catch for missing file issue
    	try {
    		rateScanner = new Scanner(new File("rates.dat"));
    		rateScanner.useDelimiter(System.getProperty("line.separator"));
    		loadArray();
    	} catch (FileNotFoundException e1) {
    		System.out.println("File error");
    		System.exit(1);
    	}
    	//Accumulator array initialization
    	for (int x = 0; x < payRateAcc.length; x++ ) {
    		for (int y = 0; y < payRateAcc[x].length; y++) {
    			payRateAcc[x][y] = 0;
    		}
    	}	
    }
    
    public static void loadArray() {
    	
    	String rateRecord = "";
    	String rateRow[];
    	int row = 0;
    	
    	//While there are still records
    	while(rateScanner.hasNextLine()) {
    		
    		try {
    			//Read a record
    			rateRecord = rateScanner.nextLine();	
    			//Allocate a row in the rate table
    			rateRow = rateRecord.split(",");
    			//Determine row length
    			payRateTab[row] = new double[rateRow.length];	
    			//Populate each cell in the row with the rates
    			for (int i = 0; i < rateRow.length; i++) {
    					payRateTab[row][i] = Double.parseDouble(rateRow[i]);
    				}	 
    				row++;  //Advance to the next row
    		}	
    		catch(Exception e) { //Try catch for illegal characters in .dat file
    			System.out.println("Error reading rate file, program terminated");
    			System.exit(1);
    		}
    	}	 
    }
    
    public static char input() {
    	
    	int iOption = 0;       // Var for menu input
    	boolean ok = true;     // Input loop controller
    	boolean ok2 = true;    // Input loop controller
    	boolean ok3 = true;    // Input loop controller
    	char iAgain = 'N';     // Returning var to main for program loop control 
    	
    	do {
    		do {
    			try { //Main menu controller
    				System.out.format("%40s%25s%25s", " ", "Morgan’s Department Store", " ");
    				System.out.println("");
    				System.out.println("");
    				System.out.println("1 - Data Entry");			
    				System.out.println("2 - Display Summary");			
    				System.out.println("3 - Exit");
    				System.out.println("");
    				iOption = Integer.parseInt(menuScanner.next());		
    					if (iOption == 1) {
    						weekValidation();  //Call week input
    						reviewValidation(); //Call reviews input
    						calcRate();         //Look up rate and do calcs
    						iAgain = 'Y';
    					}
    					else if (iOption == 2) {
    						iAgain = 'N';
    						ok = true;
    					}
    					else if (iOption == 3) {
    						exit();				//Call exit if option 3 is prompted
    					}
    					else {    //Input validation
    						System.out.println("Option can only be 1, 2 or 3. Re-entry");
    						ok3 = false;
    					}
    			} catch (Exception e) {   //Catch illegal characters
    				System.out.println("Option can only be 1, 2 or 3. Re-entry");
    				ok3 = false;	
    			}
    		} while (!ok3);	 //Main menu loop check
    		do {
    			try {
    				if (iAgain == 'Y') {
    					System.out.println("Do you want to insert another entry? Y/N");
    					iAgain = menuScanner.next().trim().toUpperCase().charAt(0);
    					if (iAgain == 'Y') {
    						weekValidation();        //Call week input
    						reviewValidation();      //Call reviews input
    						calcRate();              //Look up rate and do calcs
    						ok2 = false;
    					} else if (iAgain == 'N') {
    						ok2 = true;
    						iAgain = 'N';	
    					}
    					else {	
    						System.out.println("Invalid entry. Wrong answer. Only Y or N allowed.");
    						ok2 = false;
    						iAgain = 'Y';	
    					}	
    				}
    			} catch (Exception e1) {
    				System.out.println("Invalid entry. Only Y or N allowed.");
        			ok2 = false;
    			}
    		} while (!ok2);     //Next entry loop check
    	} while (!ok);    //Input() loop check
    	
    	return iAgain;	
    }	
    public static void weekValidation() {
    	
    	boolean ok = true;
    	
    	do {
    		ok = true;
    		try {
    			System.out.println("Enter number of full weeks worked: ");		
    			iWeeks = Integer.parseInt(menuScanner.next());
    			if(iWeeks < 0) {
    				ok = false;
    			}
    			else {
    				ok = true;
    			}
    			if(!ok) {
    				System.out.println("Invalid entry. Weeks must be a number greater or equal 0");	
    			}
    		} catch (Exception e) {
    			System.out.println("Invalid entry. Weeks must be a number greater or equal 0");
    			ok = false;
    		}
    	} while (!ok);		
    }
    
    public static void reviewValidation() {
    	
    	boolean ok = true;
    	
    	do {
    		ok = true;
        	try {
    			System.out.println("Enter number of positive reviews received: ");
    			iReviews = Integer.parseInt(menuScanner.next());
    			if(iReviews < 0) {
    				ok = false;	
    			}
    			else {
    				ok = true;
    			}
    			if(!ok) {
    				System.out.println("Invalid entry. Reviews must be a number greater or equal 0");
    			}	
    		} catch (Exception e) {
    			System.out.println("Invalid entry. Reviews must be a number greater or equal 0");
    			ok = false;
    		}
    	} while (!ok);	
    }
    
    public static void calcRate() {
    	
    	int col = 0;
    	int row = 0;
    	
    	if (iWeeks > 6) {    //If weeks more than 6, assign max value that array can hold
    		iWeeks = 6;
    	}
    	if (iReviews > 4) {  //If reviews more than 4, assign max value that array can hold
    		iReviews = 4;
    	}   	
    	for(int x = 0; x <= iWeeks; x++) {
    		row = x;                               
    		for (int y = 0; y <= iReviews; y++) {     //Look up rate from pre loaded array
    			col = y;
    			cRate = payRateTab[row][col];
    		}
    	}
    	payRateAcc[row][col] += cRate;   //Accumulate rate in appropriate cell
    	cCounter++;                      //Increase bonus counter
    	cTotBonuses += cRate;	         //Accumulate total bonus value
    }
    
    public static void printArray() {
    	
    	String oTotBonuses;
    	String oAverage;
    	double cAverage = 0;
    	cAverage = cTotBonuses/cCounter;    //Calculate final average bonus amount
    	
    	oTotBonuses = nf.format(cTotBonuses);     //Format in $
    	oAverage = nf.format(cAverage);           //Format in $
    	
    	System.out.format("%30s%25s%5s\n", " " , "Positive reviews received", " ");
    	System.out.println("");
    	System.out.format("%17s%5s%1s%8s%1s%8s%1s%8s%1s%5s%9s\n", "Full Weeks Worked", " ", "0" , " ",
    						"1", " ", "2", " ", "3", " ", "4 or more");
    	System.out.println("");
    	System.out.format("%7s%1s%-13s%-9.2f%-9.2f%-9.2f%-9.2f%-9.2f\n", " ", "0", " ", payRateAcc[0][0],
    			payRateAcc[0][1], payRateAcc[0][2], payRateAcc[0][3], payRateAcc[0][4]);
    	System.out.format("%7s%1s%-13s%-9.2f%-9.2f%-9.2f%-9.2f%-9.2f\n", " ", "1", " ", payRateAcc[1][0],
				payRateAcc[1][1], payRateAcc[1][2], payRateAcc[1][3], payRateAcc[1][4]);
    	System.out.format("%7s%1s%-13s%-9.2f%-9.2f%-9.2f%-9.2f%-9.2f\n", " ", "2", " ", payRateAcc[2][0],
				payRateAcc[2][1], payRateAcc[2][2], payRateAcc[2][3], payRateAcc[2][4]);
    	System.out.format("%7s%1s%-13s%-9.2f%-9.2f%-9.2f%-9.2f%-9.2f\n", " ", "3", " ", payRateAcc[3][0],
				payRateAcc[3][1], payRateAcc[3][2], payRateAcc[3][3], payRateAcc[3][4]);
    	System.out.format("%7s%1s%-13s%-9.2f%-9.2f%-9.2f%-9.2f%-9.2f\n", " ", "4", " ", payRateAcc[4][0],
				payRateAcc[4][1], payRateAcc[4][2], payRateAcc[4][3], payRateAcc[4][4]);
    	System.out.format("%7s%1s%-13s%-9.2f%-9.2f%-9.2f%-9.2f%-9.2f\n", " ", "5", " ", payRateAcc[5][0],
				payRateAcc[5][1], payRateAcc[5][2], payRateAcc[5][3], payRateAcc[5][4]);
    	System.out.format("%4s%9s%8s%-9.2f%-9.2f%-9.2f%-9.2f%-9.2f\n", " ", "6 or more", " ", payRateAcc[6][0],
				payRateAcc[6][1], payRateAcc[6][2], payRateAcc[6][3], payRateAcc[6][4]);		
    	System.out.println("");
    	System.out.format("%14s%1s%3d\n", "Total entries: ", " ", cCounter);
    	System.out.format("%20s%1s%5s\n", "Total paid bonuses: ", " ", oTotBonuses);
    	System.out.format("%15s%1s%5s\n", "Average bonus: ", " ", oAverage);
    	System.out.println("");
    }
    
    public static void exit() {
    
    	System.out.println("Thank you! Program ending!");     //Print closing message and kill process.
    	System.exit(0);
    }	    
}