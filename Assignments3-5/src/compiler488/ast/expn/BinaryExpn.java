package compiler488.ast.expn;

import compiler488.ast.SemanticException;
import compiler488.ast.stmt.Scope;

/**
 * The common features of binary expressions.
 */
public class BinaryExpn extends Expn {
	
    protected Expn left, right;  // Left and right operands of the binary operator.
    protected String opSymbol;   // Name of the operator.
    
    /**
     * Constructor for a binary expression with the given left and right operands and the operand symbol.
     */
    public BinaryExpn(Expn left, Expn right, String opSymbol) {
    	this.left = left;
    	this.right = right;
    	this.opSymbol = opSymbol;
    }

    /** Returns a string that represents the binary expression. */
    @Override
    public String toString () {
    	return ("(" + this.left + ")" + this.opSymbol + "(" + this.right + ")");
    }
    
    // ================================================================
    // Checking
    // ================================================================
    
    @Override
    public void check(Scope scope, int loopCount) throws SemanticException {
    	super.check(scope, loopCount);
    }
    
    // ================================================================
    // Getters and Setters
    // ================================================================

    public Expn getLeft() {
        return this.left;
    }

    public void setLeft(Expn left) {
        this.left = left;
    }

    public String getOpSymbol() {
        return this.opSymbol;
    }

    public void setOpSymbol(String opSymbol) {
        this.opSymbol = opSymbol;
    }

    public Expn getRight() {
        return this.right;
    }

    public void setRight(Expn right) {
        this.right = right;
    }
}
