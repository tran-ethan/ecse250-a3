package assignment3;

public class Main {
    public static void main(String[] args) {
        Block blockDepth = new Block(0,3);
        blockDepth.updateSizeAndPosition(8, 0, 0);

        blockDepth.printColoredBlock();
        System.out.println("=====================================");
        Block selected1 = blockDepth.getSelectedBlock(2, 0, 3);
        selected1.printColoredBlock();
        // System.out.printf("Coordinates: (%d, %d)\n", selected1.xCoord, selected1.yCoord);
        System.out.println("=====================================");
        blockDepth.rotate(1);
        blockDepth.printColoredBlock();
        System.out.println("=====================================");
        selected1.printColoredBlock();
        // System.out.printf("Coordinates: (%d, %d)\n", selected1.xCoord, selected1.yCoord);
//        Block selected2 = blockDepth.getSelectedBlock(0, 4, 2);
//        selected2.printColoredBlock();
//        System.out.printf("Coordinates: (%d, %d)\n", selected2.xCoord, selected2.yCoord);
    }
}
