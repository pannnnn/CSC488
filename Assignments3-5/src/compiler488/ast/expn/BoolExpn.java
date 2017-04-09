package compiler488.ast.expn;

import compiler488.ast.SemanticException;
import compiler488.ast.stmt.Scope;

import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;

import compiler488.ast.CompileTime;
import compiler488.ast.CodeGenerationException;


/**
 * Place holder for all binary expression where both operands must be boolean
 * expressions.
 */
public class BoolExpn extends BinaryExpn {  

    /**
     * Constructor for a boolean expression with the given left and right operands and the operand symbol.
     */
    public BoolExpn(Integer row, Integer column, Expn left, Expn right, String opSymbol) {
        super(left, right, opSymbol);
        this.setCoordinates(row, column);
    }

    // ================================================================
    // Code Generation
    // ================================================================
    public void generate() throws CodeGenerationException {
        switch(opSymbol) {
            case "or":
                left.generate();
                right.generate();
                emit(Machine.OR);
                break;
            case "and":
                left.generate();
                emit(Machine.PUSH, Machine.MACHINE_FALSE);
                emit(Machine.EQ);
                right.generate();
                emit(Machine.PUSH, Machine.MACHINE_FALSE);
                emit(Machine.EQ);
                emit(Machine.OR);
                emit(Machine.PUSH, Machine.MACHINE_FALSE);
                emit(Machine.EQ);
                break;
        }
    }

    // ================================================================
    // Checking
    // ================================================================
    
    public void check(Scope scope, int loopCount) throws SemanticException {
    	super.check(scope, loopCount);

        this.getLeft().check(scope, loopCount);
        this.getRight().check(scope, loopCount);

        this.checkS30(this.getLeft());
        this.checkS30(this.getRight());
   	    this.checkS20(this);
    }
}
