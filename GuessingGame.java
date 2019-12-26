/**
 * This program plays a guessing game with the user. 
 * 
 * @author John Cody
 * @created 02019.02.19
 */

import java.util.*;

public class GuessingGame implements I_GG{
    public static void main(String[] args) {
        
        Random r = new Random();
        int randomNum, resultNum = 10;
        int numGuesses = 0, input, guessNumber = 0;
        int numGames = 0, numWon = 0, numLost = 0, numQuit = 0;
        boolean goodInput = false, sameWrong = false, backDoored = false;
        Scanner scanner = new Scanner(System.in);
        char newGame = 'n';
        
        gameData[] game = new gameData[4];
        
        System.out.println(WELCOME_MSG);
        
        do{
            if(args.length > 0) 
                randomNum = DFLT_NUMBER;
            else 
                randomNum = r.nextInt(MAX_NUMBER-1)+1;
            numGuesses = 0;
            guessNumber = 0;
            int[] guesses = new int[20];
            goodInput = false;
            sameWrong = false;
            backDoored = false; 
            
            do {
                goodInput = false;
                do{
                    input = -5;
                    
                    do {
                        System.out.println(ENTER_GUESS_PROMPT);
                        try {
                            
                            input = scanner.nextInt();
                            scanner.nextLine();
                        } 
                        catch (InputMismatchException e) {
                            System.out.println(INPUT_NOT_INT_MSG);
                            scanner.nextLine();
                        }
                    } while(input == -5); 
                    
                    if(input > MAX_NUMBER) {
                        System.out.println(INPUT_TOO_HIGH_MSG);
                    }
                    else if(input == QUIT_VALUE || input == BACKDOOR_NUMBER) {
                        goodInput = true;
                        break;
                    }
                    else if(input < MIN_NUMBER) {
                        System.out.println(INPUT_TOO_LOW_MSG);
                    }
                    
                    else{
                        goodInput = true;
                    }
                } while(!goodInput);
            
            
                if(input == BACKDOOR_NUMBER) {
                    System.out.println("The random number is " + randomNum);
                    backDoored = true; 
                }
                else if(input == randomNum) {
                    System.out.println(WINNER_MSG + (numGuesses+1)); 
                    resultNum = WON_VALUE;
                    guesses[guessNumber] = input;
                    guessNumber++; 
                    numWon++;
                    break;
                }
                else if(input == QUIT_VALUE) {
                    System.out.println(QUITTER_MSG + randomNum);
                    resultNum = QUIT_VALUE;
                    numQuit++;
                    break;
                }
                else {
                    for(int i = 0; i < guessNumber; i++) {
                        if(input == guesses[i]) {
                            sameWrong = true;
                            break;
                        }
                    }
                    guesses[guessNumber] = input;
                    guessNumber++; 
                    if(sameWrong) {
                        System.out.println(NOPE_NOPE_MSG);
                    }
                    else {
                        System.out.print(NOPE_MSG);
                        if(numGuesses >= HINT_THRESHOLD){
                            if(input < randomNum) 
                                System.out.print(HIGHER_MSG);
                            if(input > randomNum) 
                                System.out.print(LOWER_MSG);
                        }
                        System.out.println();
                        numGuesses++;
                    }
                
                }
            } while (numGuesses < 10);
        
            if(numGuesses > 8) {
                System.out.println(LOSER_MSG + randomNum);
                resultNum = LOSE_VALUE;
                numLost++;
            }
            
            game[numGames] = new gameData(resultNum, randomNum,numGuesses, backDoored, guesses);
            numGames++;
        
            if(numGames < MAX_GAMES) {
                do {
                    System.out.println(PLAY_AGAIN_PROMPT);
                    newGame = scanner.next().charAt(0);
                    if(newGame == NO || newGame == NO_UPPER) 
                        newGame = NO;
                    if(newGame == YES || newGame == YES_UPPER)
                        newGame = YES;
                    if(newGame != YES && newGame != NO)
                        System.out.println(NOT_YN_MSG);
                } while(newGame != YES && newGame != NO);
            }
        
        } while(numGames < MAX_GAMES && newGame == YES);
        
        System.out.println(THANKS_MSG);
        
        System.out.printf(GAME_STATS_FMT, numGames, numWon, numLost, numQuit, (numWon*1.0)/numGames);
        for(int i = 0; i < numGames; i++) {
            System.out.printf(GAME_DUMP_FMT, i+1, game[i].result, game[i].number, game[i].numberOfGuesses, game[i].backdoored);
            System.out.print(GUESSES_DUMP);
            SelectionSort(game[i].guesses);
            
            for(int j = 0; j < game[i].guesses.length; j++) {
                if(game[i].guesses[j] > 0)
                    System.out.print(game[i].guesses[j] + ",");
            }
            System.out.println();
        }
        
    }
    
    public static void SelectionSort(int[] unsorted) {
        
        int min, minLocation, i;
        
        for(int cursor = 0; cursor < unsorted.length; cursor++) {
            min = unsorted[cursor];
            minLocation = cursor;
            for(i = cursor; i < unsorted.length; i++) {
                if(min > unsorted[i]) {
                    min = unsorted[i];
                    minLocation = i;
                }
            }
            
            unsorted[minLocation] = unsorted[cursor];
            unsorted[cursor] = min;
        }
    }
    
}

class gameData implements I_GG{
    String result;
    int number;
    int numberOfGuesses;
    boolean backdoored;
    int[] guesses;
    
    public gameData(int resultNum, int num, int numGuesses, boolean backDoor, int[] Guesses)  {
        if(resultNum == WON_VALUE) 
            result = "Won";
        else if(resultNum == LOSE_VALUE)
            result = "Lost";
        else if(resultNum == QUIT_VALUE)
            result = "Quit";
        number = num;
        numberOfGuesses = numGuesses;
        backdoored = backDoor;
        guesses = Guesses;
    }
}

interface I_GG {
   int MIN_NUMBER = 1;
   int MAX_NUMBER = 205;
   int BACKDOOR_NUMBER = -314;
   int DFLT_NUMBER = 60;
   int WON_VALUE = 0;
   int QUIT_VALUE = -1;
   int LOSE_VALUE = -2;
   int MAX_GAMES = 4;
   int MAX_GUESSES = 10;
   int HINT_THRESHOLD = 5;
   char NO = 'n', NO_UPPER = 'N';
   char YES = 'y', YES_UPPER = 'Y';

   String NOPE_MSG = "nope...";
   String NOPE_NOPE_MSG = "you've already guessed that wrong guess...";
   String HIGHER_MSG = "higher";
   String LOWER_MSG = "lower";
   String INVALID_INPUT_BEGIN = "*** invalid input -- ";
   String INPUT_TOO_LOW_MSG = INVALID_INPUT_BEGIN + "must be greater than " + 
                              (MIN_NUMBER - 1);
   String INPUT_TOO_HIGH_MSG = INVALID_INPUT_BEGIN + "must be less than " + 
                               (MAX_NUMBER + 1);
   String INPUT_NOT_INT_MSG = INVALID_INPUT_BEGIN + "must be an whole number"; 
   String NOT_YN_MSG = INVALID_INPUT_BEGIN + "must be " + NO + " or " + YES;
   String WINNER_MSG = "you're a winner... # of guesses: ";
   String LOSER_MSG = "too many guesses entered... the number was ";
   String QUITTER_MSG = "you're a quitter... the number was ";
   String MAX_GAMES_PLAYED_MSG = "\nMaximum number (" + MAX_GAMES + 
                                 ") of games played.";
   String ENTER_GUESS_PROMPT = "enter a guess between " + MIN_NUMBER + 
                               " and " + MAX_NUMBER + " (" + QUIT_VALUE + 
                               " to quit): ";
   String PLAY_AGAIN_PROMPT = "\nDo you want to play again (" + NO + 
                              " or " + YES + ")? ";

   String BOLD_BEGIN = "*** ";
   String BOLD_END = " ***";
   String PLAY_MSG = " playing the CSC205AA" + " guessing game." + BOLD_END;
   String WELCOME_MSG = BOLD_BEGIN + "Hello! Have fun" + PLAY_MSG; 
   String THANKS_MSG = BOLD_BEGIN + "Thanks for" + PLAY_MSG;
   String GUESSES_DUMP = "...guesses in ascending order: "; 
   String WINNER_RESULT = "Won";
   String QUITTER_RESULT = "Quit";
   String LOSER_RESULT = "Lost";

   // printf() format strings...
   String GAME_STATS_FMT = "games played: %d; won: %d; lost: %d; quit: %d;" +
                           " winning pct.: %.2f%%\n";
   String GAME_DUMP_FMT = "game %d: %s; the number was: %d; " +
                      "#guesses: %d; backdoored: %s\n";
}