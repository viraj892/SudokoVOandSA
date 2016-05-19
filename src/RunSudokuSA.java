import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class RunSudokuSA {
	private boolean[][] invalidMove;
	private int[][] originalBoard;
	private Random rand;
	private int[][] board;
	private ArrayList<ArrayList<Integer>> numList;

	public RunSudokuSA(RunSudokuSA sb) {
		board = new int[9][9];
		originalBoard = sb.getOriginalBoard();
		copyBoard(sb);
	}

	public RunSudokuSA(boolean preFill) {
		numList = new ArrayList<ArrayList<Integer>>();
		invalidMove = new boolean[9][9];

		rand = new Random();

		for (int i = 0; i < 9; i++) {
			numList.add(new ArrayList<Integer>());

			for (int j = 1; j < 10; j++) {
				numList.get(i).add(new Integer(j));
			}
		}

		if (preFill) {
			defaultBoard();
		} else
			board = new int[9][9];
	}

	public void copyBoard(RunSudokuSA sb) {
		int[][] arr = sb.getBoard();

		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				this.board[i][j] = arr[i][j];
			}
		}

		this.invalidMove = sb.getInvalidMove();
	}

	@SuppressWarnings("resource")
	private void defaultBoard() {
		board = new int[9][9];
		try {
			File file = new File("E:\\Google Drive\\Eclipse Workspace\\SudokoVertexOrdering\\src\\grid_hard5.txt");
			FileReader fr = new FileReader(file);
			Scanner in = new Scanner(fr);
			int row = 0;
			while (in.hasNextLine() && row < 9) {
				String line = in.nextLine();
				Scanner sc = new Scanner(line);
				for (int col = 0; col < 9; col++) {
					board[row][col] = sc.nextInt();
				}
				row++;
			}
		} catch (IOException e) {
			System.out.println("File I/O Error");
		}

		originalBoard = board;

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] != 0) {
					invalidMove[i][j] = true;
					numList.get(j).remove(new Integer(board[i][j]));
				}
			}
		}
		fillEmpty();
	}

	public boolean swap(int col, int r1, int r2) {
		int temp;

		if (isInvalid(r1, col))
			return false;

		if (isInvalid(r2, col))
			return false;

		temp = board[r1][col];
		board[r1][col] = board[r2][col];
		board[r2][col] = temp;

		return true;
	}

	public boolean isInvalid(int row, int col) {
		return invalidMove[row][col];
	}

	public void fillEmpty() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == 0) {
					board[i][j] = numList.get(j).remove(rand.nextInt(numList.get(j).size()));
				}
			}
		}
	}

	public int[][] getOriginalBoard() {
		return originalBoard;
	}

	public int[][] getBoard() {
		return board;
	}

	public void insert(int row, int col, int value) {
		if (value > 0) {
			invalidMove[row][col] = true;
			board[row][col] = value;
			numList.get(col).remove(new Integer(board[row][col]));
		}
	}

	public void changeValueAt(int row, int col, int value) {
		board[row][col] = value;
	}

	public int getValueAt(int row, int col) {
		return board[row][col];
	}

	public boolean[][] getInvalidMove() {
		return invalidMove;
	}

	public String toString() {
		StringBuilder out = new StringBuilder();

		for (int i = 0; i < board.length; i++) {
			if (i == 3 || i == 6) {
				out.append("-------------------\n");
			}

			for (int j = 0; j < board[i].length; j++) {
				if (j == 3 || j == 6) {
					out.append('|');
				}

				if (board[i][j] > 0) {
					out.append(board[i][j]);
				} else
					out.append('_');

				out.append(' ');
			}
			out.append('\n');
		}
		return out.toString();
	}

	public void printnumList() {
		for (int i = 0; i < numList.size(); i++) {
			for (int j = 0; j < numList.get(i).size(); j++) {
				System.out.println("I: " + i + " J: " + j + " Value: " + numList.get(i).get(j));
			}
		}
	}

	public static void main(String args[]) {
		RunSudokuSA sdkBrd = new RunSudokuSA(true);
		SimulatedAnnealing sa = new SimulatedAnnealing(sdkBrd);
	}

}