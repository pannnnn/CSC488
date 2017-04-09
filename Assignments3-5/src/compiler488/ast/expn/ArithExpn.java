package compiler488.ast.expn;

import compiler488.ast.SemanticException;
import compiler488.ast.stmt.Scope;

import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;

import compiler488.ast.CompileTime;
import compiler488.ast.CodeGenerationException;

/**
 * Place holder for all binary expression where both operands must be integer
 * expressions.
 */
public class ArithExpn extends BinaryExpn {
	
	/**
	 * Constructor for an arithmetic expression with the given left and right operands and operand symbol.
	 */
    public ArithExpn(Integer row, Integer column, Expn left, Expn right, String opSymbol) {
        super(left, right, opSymbol);
        this.setCoordinates(row, column);
    }

    // ================================================================
    // Code Generation
    // ================================================================
    public void generate() throws CodeGenerationException {
        left.generate();
        right.generate();
        switch (opSymbol) {
            case "+": emit(Machine.ADD); break;
            case "-": emit(Machine.SUB); break;
            case "*": emit(Machine.MUL); break;
            case "/": emit(Machine.DIV); break;
            default: throw new CodeGenerationException(this, "Unknown operand " + opSymbol);
        }
    }
    
    // ================================================================
    // Checking
    // ================================================================
    
    public void check(Scope scope, int loopCount) throws SemanticException {
    	super.check(scope, loopCount);

        this.getLeft().check(scope, loopCount);
        this.getRight().check(scope, loopCount);

    	this.checkS31(this.getLeft());
    	this.checkS31(this.getRight());
       	this.checkS21(this);
    }
}
