package compiler488.ast;

public interface Readable {

    /*
     * Generate address of the current node
     */
    public void generateAddr() throws CodeGenerationException;

}
