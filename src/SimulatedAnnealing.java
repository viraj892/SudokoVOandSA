import java.util.Random;

public class SimulatedAnnealing {

	double prob;
	double exp;
	int bestscore = -162;
	int column;
	int rows;
	float temperature;
	int prevscore;
	int globalScore;
	int maxcount, checkscore, first;
	Random rand, rand1, rand2, rand3;
	int[][] sudokuBoardArray;
	RunSudokuSA oldBoard, newBoard;

	public SimulatedAnnealing(RunSudokuSA board) {

		oldBoard = board;
		newBoard = new RunSudokuSA(board);
		temperature = 0.9f;

		sudokuBoardArray = newBoard.getBoard();
		System.out.println("Initial Board :");
		System.out.println(" ");
		System.out.println(oldBoard.toString());

		maxcount = 3000;
		first = maxcount;
		globalScore = prevscore = 0;
		rand1 = new Random();
		rand2 = new Random();
		rand3 = new Random();

		playSudoku();

	}

	private void playSudoku() {
		boolean validSwap;
		int colmn, t;
		int[] columvalues = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
		int temp, selectedcol;

		calculateScore(sudokuBoardArray);

		long lStartTime = System.currentTimeMillis();

		while (maxcount > 0 && prevscore != bestscore) {
			for (t = columvalues.length - 1; t < 0; t--) {
				selectedcol = rand3.nextInt(t);
				temp = columvalues[t];
				columvalues[t] = columvalues[selectedcol];
				columvalues[selectedcol] = temp;

			}

			for (int colIndex = 0; colIndex < columvalues.length; colIndex++) {
				colmn = columvalues[colIndex];

				for (int numOfSwaps = 0; numOfSwaps < 21; numOfSwaps++) {
					newBoard = new RunSudokuSA(oldBoard);

					if (prevscore == bestscore) {
						System.out.println("REACHED BEST SCORE");
						break;
					}

					int r1 = rand1.nextInt(9);
					int r2 = rand1.nextInt(9);

					if (r1 == r2) {
					} else {

						validSwap = newBoard.swap(colmn, r1, r2);
						if (validSwap) {
							sudokuBoardArray = newBoard.getBoard();
							globalScore = 0;
							globalScore = calculateScore(sudokuBoardArray);
							temperature -= 0.001;

							exp = (-(double) (globalScore - prevscore) / (double) maxcount);
							prob = Math.pow(2.718, exp);

							if (prob > 0.8) {

								prevscore = globalScore;
								oldBoard = new RunSudokuSA(newBoard);
								sudokuBoardArray = oldBoard.getBoard();

							} else {

							}
						} else {
						}
					}

				}

			}
			if (prevscore > globalScore)
				maxcount -= 80;

			else
				maxcount--;

		}

		sudokuBoardArray = oldBoard.getBoard();

		System.out.println("Final Board :");
		System.out.println(" ");
		System.out.println(oldBoard.toString());
		System.out.println(" ");
		System.out.println("Final score:" + prevscore);
		System.out.print("Best Score:" + -168);
		System.out.println(" ");

		long lEndTime = System.currentTimeMillis();
		long difference = lEndTime - lStartTime;

		System.out.print("Elapsed milliseconds: " + difference);

	}

	private int scoreRows(int[][] board) {
		int[] duplicates;
		int currScore = 0;

		for (int row = 0; row < board.length; row++) {
			duplicates = new int[10];

			for (int col = 0; col < board[row].length; col++) {
				duplicates[board[row][col]]++;
			}

			for (int x = 1; x < duplicates.length; x++) {
				if (duplicates[x] == 1) {
					currScore--;
				}
			}

		}
		return currScore;
	}

	private int calculateScore(int[][] board) {
		int scorerow, scorecol;

		scorerow = scoreRows(board);

		scorecol = scoreCols(board);
		return scorerow + scorecol;
	}

	private int scoreCols(int[][] board) {
		int[] duplicates;
		int currScore = 0;
		int row = 0;
		int col = 0;
		int lim1 = row + 3;
		int lim2 = col + 3;

		while (row < board.length) {
			duplicates = new int[10];

			while (row < lim1) {
				col = lim2 - 3;
				while (col < lim2) {
					duplicates[board[row][col]]++;
					++col;
				}
				++row;
			}

			for (int x = 1; x < duplicates.length; x++) {
				if (duplicates[x] == 1) {
					currScore--;
				}
			}

			if (lim2 < board.length) {
				row = lim1 - 3;
				col = lim2;
				lim2 += 3;
			} else {
				col = 0;
				lim2 = 3;
				row = lim1;
				lim1 = row + 3;
			}
		}
		return currScore;
	}
}