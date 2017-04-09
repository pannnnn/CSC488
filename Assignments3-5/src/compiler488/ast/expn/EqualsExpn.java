package compiler488.ast.expn;

import compiler488.ast.SemanticException;
import compiler488.ast.stmt.Scope;

import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;

import compiler488.ast.CompileTime;
import compiler488.ast.CodeGenerationException;

/**
 * Place holder for all binary expression where both operands could be either
 * integer or boolean expressions. e.g. = and not = comparisons
 */
public class EqualsExpn extends BinaryExpn {
	
    public EqualsExpn(Integer row, Integer column, Expn left, Expn right, String opSymbol) {
    	super(left, right, opSymbol);
        this.setCoordinates(row, column);
    }

    // ================================================================
    // Code Generation
    // ================================================================
    public void generate() throws CodeGenerationException {
        left.generate();
        right.generate();
        switch(opSymbol) {
            case "=":
                emit(Machine.EQ);
                break;
            case "not =":
                emit(Machine.EQ);
                emit(Machine.PUSH, Machine.MACHINE_FALSE);
                emit(Machine.EQ);
                break;
            default:
                throw new CodeGenerationException(this, "unexpected operation symbol " + opSymbol);
        }
    }

    // ================================================================
    // Checking
    // ================================================================

    public void check(Scope scope, int loopCount) throws SemanticException {
    	super.check(scope, loopCount);

        this.getLeft().check(scope, loopCount);
        this.getRight().check(scope, loopCount);

        this.checkS32(this.getLeft(), this.getRight());
   	    this.checkS20(this);
    }    
}
