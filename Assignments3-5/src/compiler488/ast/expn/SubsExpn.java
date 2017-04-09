package compiler488.ast.expn;

import compiler488.ast.Readable;
import compiler488.ast.SemanticException;
import compiler488.ast.CodeGenerationException;
import compiler488.ast.stmt.Scope;
import compiler488.symbol.Symbol;
import compiler488.symbol.ArraySymbol;
import compiler488.symbol.FunctionSymbol;

import compiler488.runtime.Machine;

/**
 * References to an array element variable
 * 
 * Treat array subscript operation as a special form of unary expression.
 * operand must be an integer expression
 */
public class SubsExpn extends UnaryExpn implements Readable {

    private String variable;  // name of the array variable

    public SubsExpn(Integer row, Integer column, String variable, Expn operand) {
        super(operand);
        this.setCoordinates(row, column);
        this.variable = variable;
    }


    // ================================================================
    // Code Generation
    // ================================================================
    public void generate() throws CodeGenerationException {
        if (scope.getSymbol(variable, this) instanceof FunctionSymbol) {
            throw new CodeGenerationException(this, variable + " is declared as a function, not an array.");
        } else {
            generateAddr();
            emit(Machine.LOAD);
        }
    }


    /**
     * Calculates the address of the array subscript
     */
    public void generateAddr() throws CodeGenerationException {
        ArraySymbol array = (ArraySymbol) scope.getSymbol(variable, this);
        operand.generate();
        Short realOffset = (short) (scope.getScopeOffset() + scope.getOffset(array));
        emit(Machine.ADDR, scope.lexicalLevel, realOffset);
        emit(Machine.ADD);
        emit(Machine.PUSH, array.getLb());
        emit(Machine.SUB);
    }


    /** Returns a string that represents the array subscript. */
    @Override
    public String toString() {
        return (this.variable + "[" + this.getOperand() + "]");
    }
    
    // ================================================================
    // Checking
    // ================================================================

    @Override
    public void check(Scope scope, int loopCount) throws SemanticException {
        super.check(scope, loopCount);

        this.getOperand().check(scope, loopCount);
        this.checkS38(this);
        this.checkS31(this.getOperand());
        this.checkS27(this);
    }
    
    // ================================================================
    // Getters and Setters
    // ================================================================

    public String getVariable() {
        return this.variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }
}
