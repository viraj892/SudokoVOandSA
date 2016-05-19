
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class SudokuVertexOrdering {
	public class Coordinates {
		int row;
		int col;
	}

	@SuppressWarnings("resource")
	public static void main(String args[]) {
		int[][] board = new int[9][9];
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
		displayBoard(board);
		final long startTime = System.currentTimeMillis();
		if (solve(board)) {
			System.out.println("Solution:");
			displayBoard(board);
		} else {
			System.out.println("Invalid Sudoku Puzzle");

		}
		final long endTime = System.currentTimeMillis();
		System.out.println("Total execution time: " + (endTime - startTime));
	}

	public static boolean solve(int[][] board) {
		Coordinates pos = new SudokuVertexOrdering().new Coordinates();

		if (findCellVertexOrdering(board, pos) == false) {
			return true;
		}

		for (int i = 1; i <= 9; i++) {
			board[pos.row][pos.col] = i;
			if (isValid(board, pos)) {
				if (solve(board)) {
					return true;
				}
			}
		}
		board[pos.row][pos.col] = 0;
		return false;
	}

	public static boolean isValid(int[][] board, Coordinates pos) {

		for (int i = 0; i < 9; i++) {
			if (board[pos.row][i] == board[pos.row][pos.col] && i != pos.col) {
				return false;
			}
			if (board[i][pos.col] == board[pos.row][pos.col] && i != pos.row) {
				return false;
			}
		}
		int r = (pos.row / 3) * 3;
		int c = (pos.col / 3) * 3;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if ((i + r) == pos.row && (j + c) == pos.col) {
					continue;
				}
				if (board[i + r][j + c] == board[pos.row][pos.col]) {
					return false;
				}
			}
		}
		return true;
	}

	// Finds the next empty cell using the vertex ordering heuristic
	public static boolean findCellVertexOrdering(int[][] board, Coordinates pos) {
		pos.row = -1;
		pos.col = -1;
		int minUnassignedLength = 9;
		int j;
		for (int i = 0; i < 9; i++) {
			int unassignedCount = 0;
			for (j = 0; j < 9; j++) {
				if (board[i][j] == 0)
					unassignedCount++;
			}
			if (minUnassignedLength > unassignedCount && unassignedCount != 0) {
				minUnassignedLength = unassignedCount;
				pos.row = i;
			}

		}
		if (minUnassignedLength > 0 && pos.row >= 0) {
			for (int col = 0; col < 9; col++) {
				if (board[pos.row][col] == 0) {

					pos.col = col;
					return true;
				}
			}
		}
		return false;
	}

	public static void displayBoard(int[][] board) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
	}

}
