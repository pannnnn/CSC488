package compiler488.ast.expn;

/**
 * The common features of unary expressions.
 */
public class UnaryExpn extends Expn {
	
    protected Expn operand;	  // operand of the unary operator
    protected String opSymbol;  // name of the operator
    
    public UnaryExpn(Expn operand) {
    	this.operand = operand;
    	this.opSymbol = null;
    }
    
    public UnaryExpn(Expn operand, String opSymbol) {
    	this.operand = operand;
    	this.opSymbol = opSymbol;
    }

    /** Returns a string that represents the unary expression. */
    @Override
    public String toString () {
    	return (this.opSymbol + "(" + this.operand + ")");
    }

    // ================================================================
    // Getters and Setters
    // ================================================================

    public Expn getOperand() {
        return this.operand;
    }

    public void setOperand(Expn operand) {
        this.operand = operand;
    }

    public String getOpSymbol() {
        return this.opSymbol;
    }

    public void setOpSymbol(String opSymbol) {
        this.opSymbol = opSymbol;
    }
}
