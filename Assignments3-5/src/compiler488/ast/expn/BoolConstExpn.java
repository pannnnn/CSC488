package compiler488.ast.expn;

import compiler488.ast.SemanticException;
import compiler488.ast.stmt.Scope;

import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;

import compiler488.ast.CompileTime;
import compiler488.ast.CodeGenerationException;


/**
 * Boolean literal constants.
 */
public class BoolConstExpn extends ConstExpn {
    
    private boolean value;  // value of the constant

    // ================================================================
    // Code Generation
    // ================================================================
    public void generate() throws CodeGenerationException {
        if (value) {
            emit(Machine.PUSH, Machine.MACHINE_TRUE);
        } else {
            emit(Machine.PUSH, Machine.MACHINE_FALSE);
        }
    }

    public BoolConstExpn(Integer row, Integer column, boolean value) {
        this.setCoordinates(row, column);
    	this.value = value;
    }
    
    /** Returns the value of the boolean constant */
    @Override
    public String toString () { 
    	return (this.value ? "(true)" : "(false)");
    }
    
    // ================================================================
    // Checking
    // ================================================================
    
    @Override
    public void check(Scope scope, int loopCount) throws SemanticException {
        super.check(scope, loopCount);
        
       	this.checkS20(this);
    }
    
    // ================================================================
    // Getters and Setters
    // ================================================================

    public boolean getValue() {
        return this.value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
