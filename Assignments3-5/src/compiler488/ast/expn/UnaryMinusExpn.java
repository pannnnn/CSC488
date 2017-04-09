package compiler488.ast.expn;

import compiler488.ast.SemanticException;
import compiler488.ast.stmt.Scope;

import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;

import compiler488.ast.CompileTime;
import compiler488.ast.CodeGenerationException;

/**
 * Represents negation of an integer expression
 */
public class UnaryMinusExpn extends UnaryExpn {

    public UnaryMinusExpn(Integer row, Integer column, Expn operand, String opSymbol) {
        super(operand, opSymbol);
        this.setCoordinates(row, column);
    }

    // ================================================================
    // Code Generation
    // ================================================================
    public void generate() throws CodeGenerationException {
        operand.generate();
        if (opSymbol == "-") {
            emit(Machine.NEG);
        }
    }

    // ================================================================
    // Checking
    // ================================================================
    
    public void check(Scope scope, int loopCount) throws SemanticException {
        this.setScope(scope);
    
        this.getOperand().check(scope, loopCount);

        this.checkS31(this.getOperand());
        this.checkS21(this);
    }
}
