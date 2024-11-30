package assignment3;

import java.awt.Color;

public class PerimeterGoal extends Goal{

	public PerimeterGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {
		Color[][] grid = board.flatten();
		int size = grid.length;
		int score = 0;

		for (int i = 0; i < size; i++) {
			// First row
			if (grid[0][i].equals(targetGoal)) {
				score++;
			}
			// Last row
			if (grid[size-1][i].equals(targetGoal)) {
				score++;
			}
			// First column
			if (grid[i][0].equals(targetGoal)) {
				score++;
			}
			// Last column
			if (grid[i][size-1].equals(targetGoal)) {
				score++;
			}
		}
		return score;
	}

	@Override
	public String description() {
		return "Place the highest number of " + GameColors.colorToString(targetGoal) 
		+ " unit cells along the outer perimeter of the board. Corner cell count twice toward the final score!";
	}

}
