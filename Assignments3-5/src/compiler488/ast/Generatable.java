package compiler488.ast;

public interface Generatable {

    /*
     * Classes that extend this interface can be used
     * as arguments to all generatable AST nodes.
     */

    public void generate() throws CodeGenerationException;

}
