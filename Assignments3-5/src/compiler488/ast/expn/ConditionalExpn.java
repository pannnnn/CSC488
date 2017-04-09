package compiler488.ast.expn;

import compiler488.ast.SemanticException;
import compiler488.ast.stmt.Scope;

import compiler488.runtime.Machine;
import compiler488.ast.CodeGenerationException;
import compiler488.ast.CompileTime;

/** Represents a conditional expression (i.e., x>0?3:4). */
public class ConditionalExpn extends Expn {
	
    private Expn condition;   // Evaluate this to decide which value to yield.
    private Expn trueValue;   // The value is this when the condition is true.
    private Expn falseValue;  // Otherwise, the value is this.

    public ConditionalExpn(Integer row, Integer column, Expn condition, Expn trueValue, Expn falseValue) {
        this.setCoordinates(row, column);
    	this.condition = condition;
    	this.trueValue = trueValue;
    	this.falseValue = falseValue;
    }

    /** Returns a string that describes the conditional expression. */
    @Override
    public String toString() {
        return "(" + this.condition + " ? " + this.trueValue + " : " + this.falseValue + ")";
    }
    
    // ================================================================
    // Getters and Setters
    // ================================================================
    
    @Override
    public void check(Scope scope, int loopCount) throws SemanticException {
    	super.check(scope, loopCount);

        this.condition.check(scope, loopCount);
        this.trueValue.check(scope, loopCount);
        this.falseValue.check(scope, loopCount);

    	this.checkS30(this.condition);
    	this.checkS33(this.trueValue, this.falseValue);
       	this.checkS24(this);
    }

    // ================================================================
    // Code Generation
    // ================================================================
    public void generate() throws CodeGenerationException {
        condition.generate();
        emit(Machine.PUSH);
        short falseAddr = placeholder();
        emit(Machine.BF);
        trueValue.generate();
        emit(Machine.PUSH);
        short endingAddr = placeholder();
        emit(Machine.BR);
        fill(falseAddr, CompileTime.instructionCounter);
        falseValue.generate();
        fill(endingAddr, CompileTime.instructionCounter);
    }

    // ================================================================
    // Getters and Setters
    // ================================================================

    public Expn getCondition() {
        return this.condition;
    }

    public void setCondition(Expn condition) {
        this.condition = condition;
    }

    public Expn getFalseValue() {
        return this.falseValue;
    }

    public void setFalseValue(Expn falseValue) {
        this.falseValue = falseValue;
    }

    public Expn getTrueValue() {
        return this.trueValue;
    }

    public void setTrueValue(Expn trueValue) {
        this.trueValue = trueValue;
    }
}
