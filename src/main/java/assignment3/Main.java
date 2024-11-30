package assignment3;

public class Main {
    public static void main(String[] args) {
        Block blockDepth = new Block(0,3);
        blockDepth.updateSizeAndPosition(8, 0, 0);

        System.out.println("=================");
        blockDepth.printColoredBlock();
    }
}
