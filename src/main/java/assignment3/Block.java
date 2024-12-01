package assignment3;

import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;

public class Block {

	private int xCoord;
	private int yCoord;
	private int size; // height/width of the square
	private int level; // the root (outer most block) is at level 0
	private int maxDepth;
	private Color color;
	private Block[] children; // {UR, UL, LL, LR}

	public static Random gen = new Random(4);


	/*
	 * These two constructors are here for testing purposes.
	 */
	public Block() {}

	public Block(int x, int y, int size, int lvl, int  maxD, Color c, Block[] subBlocks) {
		this.xCoord=x;
		this.yCoord=y;
		this.size=size;
		this.level=lvl;
		this.maxDepth = maxD;
		this.color=c;
		this.children = subBlocks;
	}


	/*
	 * Creates a random block given its level and a max depth.
	 *
	 * xCoord, yCoord, size, and highlighted should not be initialized
	 * (i.e. they will all be initialized by default)
	 */
	public Block(int lvl, int maxDepth) {
		this.level = lvl;
		this.maxDepth = maxDepth;

		// if the level is equal to the max depth, then the block should be a leaf
		if (lvl != maxDepth && gen.nextDouble() < Math.exp(-0.25 * lvl)) {
			// Recursive case: the block is not a leaf
			this.children = new Block[4];
			// Subdivide the block
			for (int i = 0; i < 4; i++) {
				this.children[i] = new Block(lvl + 1, maxDepth);
			}
		}
		else {
			// Base case: the block is a leaf
			this.children = new Block[0];
			this.color = GameColors.BLOCK_COLORS[gen.nextInt(GameColors.BLOCK_COLORS.length)];
		}
	}


	/*
	  * Updates size and position for the block and all of its sub-blocks, while
	  * ensuring consistency between the attributes and the relationship of the
	  * blocks.
	  *
	  *  The size is the height and width of the block. (xCoord, yCoord) are the
	  *  coordinates of the top left corner of the block.
	 */
	public void updateSizeAndPosition (int size, int xCoord, int yCoord) {
		// Check for invalid arguments
		if (size != 1) {
			if (size <= 0 || size % 2 != 0) { // TODO Can size be 0?
				throw new IllegalArgumentException("Size must be a positive even number.");
			}
		}
		this.size = size;
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		// If the block is not a leaf, update the size and position of the children
		if (this.children.length != 0) {
			int half = size / 2;
			this.children[0].updateSizeAndPosition(half, xCoord + half, yCoord); // UR
			this.children[1].updateSizeAndPosition(half, xCoord, yCoord); // UL
			this.children[2].updateSizeAndPosition(half, xCoord, yCoord + half); // LL
			this.children[3].updateSizeAndPosition(half, xCoord + half, yCoord + half); // LR
		}
	}


	/*
  	* Returns a List of blocks to be drawn to get a graphical representation of this block.
  	*
  	* This includes, for each undivided Block:
  	* - one BlockToDraw in the color of the block
  	* - another one in the FRAME_COLOR and stroke thickness 3
  	*
  	* Note that a stroke thickness equal to 0 indicates that the block should be filled with its color.
  	*
  	* The order in which the blocks to draw appear in the list does NOT matter.
  	*/
	public ArrayList<BlockToDraw> getBlocksToDraw() {
		ArrayList<BlockToDraw> blocks = new ArrayList<>();
		// Base case: block is leaf
		if (this.children.length == 0) {
			blocks.add(new BlockToDraw(this.color, this.xCoord, this.yCoord, this.size, 0));
			blocks.add(new BlockToDraw(GameColors.FRAME_COLOR, this.xCoord, this.yCoord, this.size, 3));
		} else {
			// Recursive case
			for (Block b : this.children) {
				blocks.addAll(b.getBlocksToDraw());
			}
		}
		return blocks;
	}

	/*
	 * This method is provided and you should NOT modify it.
	 */
	public BlockToDraw getHighlightedFrame() {
		return new BlockToDraw(GameColors.HIGHLIGHT_COLOR, this.xCoord, this.yCoord, this.size, 5);
	}



	/*
	 * Return the Block within this Block that includes the given location
	 * and is at the given level. If the level specified is lower than
	 * the lowest block at the specified location, then return the block
	 * at the location with the closest level value.
	 *
	 * The location is specified by its (x, y) coordinates. The lvl indicates
	 * the level of the desired Block. Note that if a Block includes the location
	 * (x, y), and that Block is subdivided, then one of its sub-Blocks will
	 * contain the location (x, y) too. This is why we need lvl to identify
	 * which Block should be returned.
	 *
	 * Input validation:
	 * - this.level <= lvl <= maxDepth (if not throw exception)
	 * - if (x,y) is not within this Block, return null.
	 */
	public Block getSelectedBlock(int x, int y, int lvl) {
		if (lvl < this.level || lvl > this.maxDepth) {
			throw new IllegalArgumentException("Invalid level.");
		}
		if (x < this.xCoord || x >= this.xCoord + this.size || y < this.yCoord || y >= this.yCoord + this.size) {
			return null;
		}
		// Base case
		if (this.level == lvl || this.children.length == 0) {
			return this;
		}
		// Recursive case
		for (Block b : this.children) {
			Block selectedBlock = b.getSelectedBlock(x, y, lvl);
			if (selectedBlock != null) {
				return selectedBlock;
			}
		}
		return null;
	}

	/*
	 * Swaps the child Blocks of this Block.
	 * If input is 1, swap vertically. If 0, swap horizontally.
	 * If this Block has no children, do nothing. The swap
	 * should be propagate, effectively implementing a reflection
	 * over the x-axis or over the y-axis.
	 *
	 */
	public void reflect(int direction) {
		if (direction < 0 || direction > 1) {
			throw new IllegalArgumentException("Invalid direction.");
		}
		// Recursive case: block not leaf
		if (this.children.length != 0) {
			// Keep track of coordinates because they are not supposed to change
			int x = this.children[0].xCoord;
			int y = this.children[0].yCoord;
			int x1 = this.children[1].xCoord;
			int y1 = this.children[1].yCoord;
			int x2 = this.children[2].xCoord;
			int y2 = this.children[2].yCoord;
			int x3 = this.children[3].xCoord;
			int y3 = this.children[3].yCoord;

			// Recursive case
			if (direction == 1) {
				// Horizontal swap
				Block tmp = this.children[0];
				this.children[0] = this.children[1];
				this.children[1] = tmp;
				Block tmp2 = this.children[2];
				this.children[2] = this.children[3];
				this.children[3] = tmp2;
			} else {
				// Vertical swap
				Block tmp = this.children[0];
				this.children[0] = this.children[3];
				this.children[3] = tmp;
				Block tmp2 = this.children[1];
				this.children[1] = this.children[2];
				this.children[2] = tmp2;
			}

			// Update coordinates
			this.children[0].xCoord = x;
			this.children[0].yCoord = y;
			this.children[1].xCoord = x1;
			this.children[1].yCoord = y1;
			this.children[2].xCoord = x2;
			this.children[2].yCoord = y2;
			this.children[3].xCoord = x3;
			this.children[3].yCoord = y3;

			for (Block b : this.children) {
				b.updateSizeAndPosition(b.size, b.xCoord, b.yCoord);
				b.reflect(direction);
			}
		}
		// Base case: block is leaf - do nothing
	}

	/*
	 * Rotate this Block and all its descendants.
	 * If the input is 1, rotate clockwise. If 0, rotate
	 * counterclockwise. If this Block has no children, do nothing.
	 */
	public void rotate(int direction) {
		if (direction < 0 || direction > 1) {
			throw new IllegalArgumentException("Invalid direction.");
		}
		// Recursive case: block not leaf
		if (this.children.length != 0) {
			// Recursive case

			// Keep track of coordinates because they are not supposed to change
			int x = this.children[0].xCoord;
			int y = this.children[0].yCoord;
			int x1 = this.children[1].xCoord;
			int y1 = this.children[1].yCoord;
			int x2 = this.children[2].xCoord;
			int y2 = this.children[2].yCoord;
			int x3 = this.children[3].xCoord;
			int y3 = this.children[3].yCoord;

			if (direction == 1) {
				// Clockwise rotation
				Block tmp = this.children[0];
				this.children[0] = this.children[1];
				this.children[1] = this.children[2];
				this.children[2] = this.children[3];
				this.children[3] = tmp;
			} else {
				// Counterclockwise rotation
				Block tmp = this.children[0];
				this.children[0] = this.children[3];
				this.children[3] = this.children[2];
				this.children[2] = this.children[1];
				this.children[1] = tmp;
			}

			// Update coordinates
			this.children[0].xCoord = x;
			this.children[0].yCoord = y;
			this.children[1].xCoord = x1;
			this.children[1].yCoord = y1;
			this.children[2].xCoord = x2;
			this.children[2].yCoord = y2;
			this.children[3].xCoord = x3;
			this.children[3].yCoord = y3;

			// Update coordinates of children

			for (Block b : this.children) {
				b.updateSizeAndPosition(b.size, b.xCoord, b.yCoord);
				b.rotate(direction);
			}
		}
	}



	/*
	 * Smash this Block.
	 *
	 * If this Block can be smashed,
	 * randomly generate four new children Blocks for it.
	 * (If it already had children Blocks, discard them.)
	 * Ensure that the invariants of the Blocks remain satisfied.
	 *
	 * A Block can be smashed iff it is not the top-level Block
	 * and it is not already at the level of the maximum depth.
	 *
	 * Return True if this Block was smashed and False otherwise.
	 *
	 */
	public boolean smash() {
		if (level == 0 || level == maxDepth) {
			return false;
		}
		this.children = new Block[4];
		for (int i = 0; i < 4; i++) {
			this.children[i] = new Block(level + 1, maxDepth);
		}
		return true;
	}


	/*
	 * Return a two-dimensional array representing this Block as rows and columns of unit cells.
	 *
	 * Return and array arr where, arr[i] represents the unit cells in row i,
	 * arr[i][j] is the color of unit cell in row i and column j.
	 *
	 * arr[0][0] is the color of the unit cell in the upper left corner of this Block.
	 */
	public Color[][] flatten() {
		int size = (int) Math.pow(2, maxDepth - this.level);
		Color[][] arr = new Color[size][size];
		if (this.children.length == 0) {
			// Base case: block is a leaf
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					arr[i][j] = this.color;
				}
			}
		} else {
			// Recursive case
			int half = size / 2;
			Color[][] UR = this.children[0].flatten();
			Color[][] UL = this.children[1].flatten();
			Color[][] LL = this.children[2].flatten();
			Color[][] LR = this.children[3].flatten();
			for (int i = 0; i < half; i++) {
				for (int j = 0; j < half; j++) {
					arr[i][j] = UL[i][j];
					arr[i][j + half] = UR[i][j];
					arr[i + half][j] = LL[i][j];
					arr[i + half][j + half] = LR[i][j];
				}
			}
        }
        return arr;
    }



	// These two get methods have been provided. Do NOT modify them.
	public int getMaxDepth() {
		return this.maxDepth;
	}

	public int getLevel() {
		return this.level;
	}


	/*
	 * The next 5 methods are needed to get a text representation of a block.
	 * You can use them for debugging. You can modify these methods if you wish.
	 */
	public String toString() {
		return String.format("pos=(%d,%d), size=%d, level=%d", this.xCoord, this.yCoord, this.size, this.level);
	}

	public void printBlock() {
		this.printBlockIndented(0);
	}

	private void printBlockIndented(int indentation) {
		String indent = "";
		for (int i=0; i<indentation; i++) {
			indent += "\t";
		}

		if (this.children.length == 0) {
			// it's a leaf. Print the color!
			String colorInfo = GameColors.colorToString(this.color) + ", ";
			System.out.println(indent + colorInfo + this);
		}
		else {
			System.out.println(indent + this);
			for (Block b : this.children)
				b.printBlockIndented(indentation + 1);
		}
	}

	private static void coloredPrint(String message, Color color) {
		System.out.print(GameColors.colorToANSIColor(color));
		System.out.print(message);
		System.out.print(GameColors.colorToANSIColor(Color.WHITE));
	}

	public void printColoredBlock(){
		Color[][] colorArray = this.flatten();
		for (Color[] colors : colorArray) {
			for (Color value : colors) {
				String colorName = GameColors.colorToString(value).toUpperCase();
				if(colorName.length() == 0){
					colorName = "\u2588";
				}
				else{
					colorName = colorName.substring(0, 1);
				}
				coloredPrint(colorName, value);
			}
			System.out.println();
		}
	}
}