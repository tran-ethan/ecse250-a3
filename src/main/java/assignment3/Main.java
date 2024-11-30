package assignment3;

public class Main {
    public static void main(String[] args) {
        Block blockDepth = new Block(0,3);
        blockDepth.updateSizeAndPosition(16, 0, 0);
        blockDepth.printBlock();
        blockDepth.getSelectedBlock(0, 0, 1).smash();

        System.out.println("=================");
        blockDepth.printBlock();
    }
}
