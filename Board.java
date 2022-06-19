import java.util.Random;
import java.util.ArrayList;

class Board {
    private int count = 0, queenPairs = 0;
	// 2d array of the board
	private int board[][];

	public Board(int[][] board, int boardLength) {
		this.board = board;
		heuristic(boardLength);
	}

	// makes a clean board by calling reset
	// then randomly puts queens on the board for hill climbing
	public Board(int boardLength) {
		reset(boardLength);
        if(count == 0){
            count++;
            Random rand = new Random();
			for (int i = 0; i < boardLength; i++) {
				board[i][rand.nextInt(boardLength)] = 1;
			}
        }
	}

	// creates an empty board of size n
    public void reset(int boardLength) {
        board = new int[boardLength][boardLength];
		for (int i = 0; i < boardLength; i++) {
			for (int j = 0; j < boardLength; j++) {
				board[i][j] = 0;
			}
        }
	}

	// the heurisitc function will return the number of queen pairs that are considered to be safe
	// meaning that for each queen count the number of other queens that are not being threatned
	// or threating that queen
	// so the higher the heurisitc the better
	// for a solved board the heuristic would be equal to n(n+1)/2
	// where n is n - 1
	// for example a board of size 7, with 7 queens and 7x7 board
	// we can calulate the solved heuristic to be n = 6
	// 6(6+1)/2 = 21

	public int heuristic(int boardLength) {
		int num = 0;
		queenPairs = 0;
		ArrayList<Integer> location = new ArrayList<Integer>();
		for (int i = 0; i < boardLength; i++) {
			for (int j = 0; j < boardLength; j++) {
				// if queen is in this row/column then set num to the column
				if (board[i][j] == 1) {
					num = j;
				}
			}
			// add the column that the queen is in to location
			location.add(num);
		}
		// location now has the position of each queen in every row
		for (int i = 0; i < boardLength; i++) {
			queenPairs = queenPairs + collision(location, i, boardLength);
		}
		return queenPairs;
	}

	// checks if queen pairs attack one another
	// adds to heuristic count if a pair of queens are not colliding
	public int collision(ArrayList<Integer> location, int row, int boardLength) {
		int noCollision = 0;
		//collision = 0;
		boolean check = true;

		for (int i = row; i < boardLength - 1; i++) {
			count = 0;
			check = true;
			// queens are in the same column
			if (location.get(row) == location.get(i + 1)) {
				check = false;
			}
			// diagonal
			else if ((location.get(row) + row) == (location.get(i + 1) + (i + 1))) {
				check = false;
			}
			// other diagonal
			else if ((location.get(row) - row) == (location.get(i + 1) - (i + 1))) {
				check = false;
			}
			else if (check == true) {
				noCollision++;
			}
		}
		return noCollision;
	}
	// checks to see if there is a queen in the given square, [row, column]
	public int legalMove(int row, int column, int boardLength){
        // left
        for (int j = 0; j < column; j++){
			// a queen is a 1 in the 2d array so if there is a queen return 1
            if (board[row][j] == 1){
                return 1;
			}
		}
        // diagonal 
        for (int i = row, j = column; i >= 0 && j >= 0; i--, j--){
            if (board[i][j] == 1){
                return 1;
			}
		}
       // other diagonal
        for (int i = row, j = column; j >= 0 && i < boardLength; i++, j--){
            if (board[i][j] == 1){
                return 1;
			}
		}
        return 0;
    }

	public int[][] getBoard() {
		return board;
	}

	public int getQueenPairs() {
		return queenPairs;
	}

	// gets the best neighbor and put a queen on best row where a column has no queen
	public Board getNeighbor(Board board, int row, int boardLength) {
		ArrayList<Board> neighbors = new ArrayList<Board>();
		Board max;

		for (int j = 0; j < boardLength; j++) {
			// if no queen
			if (board.getBoard()[row][j] != 1) {
				// new node board
				int node[][] = new int[boardLength][boardLength];
				// move queen
				node[row][j] = 1;
				for (int i = 0; i < boardLength; i++) {
					if (i != row) {
						node[i] = board.getBoard()[i];
					}
				}
				// add new chess board node to arraylist
				neighbors.add(new Board(node, boardLength));
			}
		} 
		for (int i = 0; i < neighbors.size(); i++) {
			 //if the next board in the arraylist is higher than the current, then switch
        }
		// finds the highest heuristic of the neightbor nodes
		max = neighbors.get(0);
		for (int i = 0; i < neighbors.size(); i++) {
			// if the next board in the arraylist is higher than the current, then switch
            if (neighbors.get(i).getQueenPairs() > max.getQueenPairs()) {
                max = neighbors.get(i);
            }
        }
		return max;
	}
	// goal is equal to n - 1 = n(n+1)/2
	public int isGoal(int boardLength) {
		int num = boardLength - 1;
		int goal = num * (num+1)/2;
		return goal;
	}

	public String printBoard(int boardLength) {
		String s = "";
		for (int i = 0; i < boardLength; i++) {
			for (int j = 0; j < boardLength; j++) {
				if (board[i][j] == 0) {
					s = s +" x ";
				} 
                
                else if (board[i][j] == 1){
					s = s +" Q ";
				}
			}
			s = s + "\n";
		}
		return s;
	}

}
