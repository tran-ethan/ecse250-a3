package assignment3;

public class Main {
    public static void main(String[] args) {
        Block blockDepth = new Block(0,4);
        blockDepth.updateSizeAndPosition(32, 0, 0);

        System.out.println("=================");
        blockDepth.printColoredBlock();
        System.out.println(new PerimeterGoal(GameColors.GREEN).score(blockDepth));
    }
}
