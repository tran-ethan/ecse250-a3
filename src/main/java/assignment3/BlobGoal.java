package assignment3;

import java.awt.Color;

public class BlobGoal extends Goal{

	public BlobGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {
		/*
		 * ADD YOUR CODE HERE
		 */
		return 0;
	}

	@Override
	public String description() {
		return "Create the largest connected blob of " + GameColors.colorToString(targetGoal) 
		+ " blocks, anywhere within the block";
	}


	public int undiscoveredBlobSize(int i, int j, Color[][] unitCells, boolean[][] visited) {
		if (unitCells[i][j] != targetGoal || visited[i][j]) {
			// Base case: cell is not the target color or has already been visited
			return 0;
		} else {
			// Recursive case: check all neighboring cells
			visited[i][j] = true;
			// Recursively check orthogonal cells
			int size = 1;
			if (i > 0) {
				// Check cell to the left
				size += undiscoveredBlobSize(i - 1, j, unitCells, visited);
			}
			if (i < unitCells.length - 1) {
				// Check cell to the right
				size += undiscoveredBlobSize(i + 1, j, unitCells, visited);
			}
			if (j > 0) {
				// Check cell above
				size += undiscoveredBlobSize(i, j - 1, unitCells, visited);
			}
			if (j < unitCells[0].length - 1) {
				// Check cell below
				size += undiscoveredBlobSize(i, j + 1, unitCells, visited);
			}
			return size;
		}
	}

}
