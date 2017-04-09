package compiler488.ast.expn;

import compiler488.ast.SemanticException;
import compiler488.ast.stmt.Scope;

import compiler488.runtime.Machine;
import compiler488.ast.CodeGenerationException;

/**
 * Represents the boolean negation of an expression.
 */
public class NotExpn extends UnaryExpn {
    
    public NotExpn(Integer row, Integer column, Expn operand, String opSymbol) {
        super(operand, opSymbol);
        this.setCoordinates(row, column);
    }

    // ================================================================
    // Code Generation
    // ================================================================
    public void generate() throws CodeGenerationException {
        operand.generate();
        if (opSymbol == "not") {
            emit(Machine.PUSH, Machine.MACHINE_FALSE);
            emit(Machine.EQ);
        }
    }
    
    // ================================================================
    // Checking
    // ================================================================
    
    @Override
    public void check(Scope scope, int loopCount) throws SemanticException {
        super.check(scope, loopCount);

        this.getOperand().check(scope, loopCount);
        this.checkS30(this.getOperand());
        this.checkS20(this);
    }
}
