package compiler488.ast.expn;

import compiler488.ast.SemanticException;
import compiler488.ast.stmt.Scope;

import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;

import compiler488.ast.CompileTime;
import compiler488.ast.CodeGenerationException;

/**
 * Represents a literal integer constant.
 */
public class IntConstExpn extends ConstExpn
    {
    private Integer value;  // The value of this literal.

    // ================================================================
    // Code Generation
    // ================================================================
    public void generate() throws CodeGenerationException {
        emit(Machine.PUSH, value.shortValue());
    }

    public IntConstExpn(Integer row, Integer column, Integer value) {
        this.setCoordinates(row, column);
        this.value = value;
    }

    /** Returns a string representing the value of the literal. */
    @Override
    public String toString () { return value.toString (); }
    
    // ================================================================
    // Checking
    // ================================================================
    
    @Override
    public void check(Scope scope, int loopCount) throws SemanticException {
    	super.check(scope, loopCount);
    	
        this.checkS21(this);
    }
    
    // ================================================================
    // Getters and Setters
    // ================================================================

    public Integer getValue() {
        return this.value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
