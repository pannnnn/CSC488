package compiler488.ast;

public class SemanticException extends Exception {

    public SemanticException(AST node, String message) {
        super(node.getCoordinates() + message);
    }

    public SemanticException() {
        super("Semantic exception occured during semantic analysis");
    }
}