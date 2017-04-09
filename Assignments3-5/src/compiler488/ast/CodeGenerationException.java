package compiler488.ast;

public class CodeGenerationException extends Exception {

    public CodeGenerationException(AST node, String message) {
        super(node.getCoordinates() + message);
    }

    public CodeGenerationException() {
        super("Code generation exception occured during semantic analysis");
    }
}