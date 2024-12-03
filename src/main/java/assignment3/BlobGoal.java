package assignment3;

import java.awt.Color;

public class BlobGoal extends Goal{

	public BlobGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {
		Color[][] grid = board.flatten();
		boolean[][] visited = new boolean[grid.length][grid.length];
		int maxBlobSize = 0;
		// Iterate through each cell in the grid, keeping track of unvisited cells
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid.length; j++) {
				// Check max blob size only if cell is target color and has not been visited yet
				if (grid[i][j].equals(targetGoal) && !visited[i][j]) {
					int blobSize = undiscoveredBlobSize(i, j, grid, visited);
					// Update max blob size
					if (blobSize > maxBlobSize) {
						maxBlobSize = blobSize;
					}
				}
			}
		}
		return maxBlobSize;
	}

	@Override
	public String description() {
		return "Create the largest connected blob of " + GameColors.colorToString(targetGoal)
				+ " blocks, anywhere within the block";
	}


	public int undiscoveredBlobSize(int i, int j, Color[][] unitCells, boolean[][] visited) {
		if (i < 0 || i >= unitCells.length || j < 0 || j >= unitCells.length || unitCells[i][j] != targetGoal || visited[i][j]) {
			// Base case: cell is out of bounds, not the target color, or has already been visited
			return 0;
		} else {
			// Recursive case: check all neighboring cells
			visited[i][j] = true;
			// Check orthogonal cells
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
