import java.util.Scanner;

/*
Joseph Milici
This program uses the board class to constuct a chess board of user specified length.
Then uses two different methods, hill climbing and a backtracking search to solve the puzzle.
Each step throughout the program is documented.
*/

public class queensPuzzle{
	public int track = 0, solutions = 0;
	public static void main(String[] args) {
		// puzzle object
		queensPuzzle puzzle = new queensPuzzle();
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter number of queens/board size(must be greater than 3): ");
		int boardLength = sc.nextInt();
		// boards of length that are less than 4 do not have solutions.
		while(boardLength < 4){
			System.out.println("Your number must be larger than 3!");
			System.out.print("Enter a new number: ");
			boardLength = sc.nextInt();
		}
		// close the scanner object
		sc.close();
		// create a board object
		Board board = new Board(boardLength);
		System.out.println("Board size: " + boardLength + "x" + boardLength);
		System.out.println("Starting Board:");
		System.out.println(board.printBoard(boardLength));
		System.out.println("----------------");
		System.out.println("Hill Climbing with random restart: ");
		long time = System.currentTimeMillis();
		// calls hill climb
		puzzle.hillClimb(board, boardLength);
		long elapsedTimeMillis = System.currentTimeMillis()-time;
		float elapsedTimeSec = elapsedTimeMillis/1000F;
		System.out.println("Time to solution: " + elapsedTimeSec + " seconds.");
		System.out.println("----------------\n");
		// resets the board to an empty board
		board.reset(boardLength);
		System.out.println("Backtracking Search: ");
		long time2 = System.currentTimeMillis();
		// calls backtrack
		puzzle.backTrack(board, 0, boardLength);
		long elapsedTimeMillis2 = System.currentTimeMillis()-time2;
		float elapsedTimeSec2 = elapsedTimeMillis2/1000F;
		System.out.println("Time to solution: " + elapsedTimeSec2 + " seconds.");
		System.out.println("----------------\n");
	}

	public void hillClimb(Board board, int boardLength) {
		Board current = new Board(board.getBoard(), boardLength);
		int restarts = 0;
		int goal = current.isGoal(boardLength);
		boolean check = true, localMax = false;
		
		while (check == true) {
			if (current.getQueenPairs() != goal) {
				for (int i = 0; i < boardLength; i++) {
					Board neighbor = current.getNeighbor(current, i, boardLength);
					// if neighbor is less than the current amount of safe current
					// then local max has not been hit yet, set current equal to neighbor
					if (neighbor.getQueenPairs() > current.getQueenPairs()) {
						current = neighbor;
						localMax = false;
					}
					else {
						// local max has been hit
						localMax = true;
					}
				}
				// if the local max has been hit then completely restart
				if (localMax == true) {
					restarts++;
					current = new Board(boardLength);
				}
			}
			// else the board is solved
			else {
				System.out.println("Heuristic Function: " + current.getQueenPairs());
				System.out.println("Number of Restarts: " + restarts);
				System.out.println("Solution:");
				System.out.println(current.printBoard(boardLength));
				check = false;
			}
		}
	}

    public int backTrack(Board board, int j, int boardLength) {
		Board current = new Board(board.getBoard(), boardLength);
		int goal = current.isGoal(boardLength);
		// if the current board is not equal to the goal
		if(current.getQueenPairs() != goal){
			for (int i = 0; i < boardLength; i++) {
            	// checks to see if there is not a queen in that position
            	if (current.legalMove(i, j, boardLength) == 0) {
                	// places the queen
                	current.getBoard()[i][j] = 1;
					// if you uncomment out this print statment below you can see all of the
					// backtracks happening
					//System.out.println(current.printBoard());
                	// do again to place the rest of the queens
                	if (backTrack(current, j + 1, boardLength) == 1){
                    	return 1;
					}
                	// if that is not the solution then remove the queen
					// or "backtrack"
					track++;
                	current.getBoard()[i][j] = 0;
            	}
        	}
			return 0;
		}
		// else the board is solved
		else{
        	solutions++;
			System.out.println("Number of backtracks: " + track);
			System.out.println("Solution:");
			System.out.println(current.printBoard(boardLength));
			/*
			if you uncomment out the print statment below and change the return 1 to 0
			this backtracking function will print out all possible solutions
			for larger n's it is proablly best to comment out the above board and number of
			backtracks
			For example the eight queens board has 92 possible solutions
			*/
			//System.out.println("There are: " + solutions + " different solutions.");
        	return 1;
		}
	}

}

