package compiler488.ast.stmt;

import compiler488.ast.SemanticException;
import compiler488.ast.SemanticExceptionList;
import compiler488.ast.expn.Expn;
import compiler488.ast.expn.SubsExpn;
import compiler488.ast.expn.IdentExpn;

import compiler488.runtime.Machine;

import compiler488.ast.Readable;
import compiler488.ast.CodeGenerationException;

import compiler488.symbol.Symbol;
/**
 * Holds the assignment of an expression to a variable.
 */
public class AssignStmt extends Stmt {
    
    private Expn var;    // location being assigned to
    private Expn value;  // value being assigned
    
    /**
     * Constructor for an assignment of a value to location.
     */
    public AssignStmt(Integer row, Integer column, Expn location, Expn value) {
        this.setCoordinates(row, column);
        this.var = location;
        this.value = value;
    }

    /** Returns a string that describes the assignment statement. */
    @Override
    public String toString() {
        return "Assignment: " + this.var + " := " + this.value;
    }

    // ================================================================
    // Code Generation
    // ================================================================

    public void generate() throws CodeGenerationException {
        ((Readable) var).generateAddr();
        value.generate();
        emit(Machine.STORE);
    }

    // ================================================================
    // Checking
    // ================================================================

    public void check(Scope scope, int loopCount) throws SemanticException {
        super.check(scope, loopCount);
        try {
            this.var.check(scope, loopCount);
            this.value.check(scope, loopCount);
            this.checkS60(var);
            this.checkS34(var, value);
        } catch (SemanticException e) {
            SemanticExceptionList.add(e);
        }
    }

    // ================================================================
    // Getters and Setters
    // ================================================================

    public Expn getVar() {
        return this.var;
    }

    public void setVar(Expn var) {
        this.var = var;
    }

    public Expn getValue() {
        return this.value;
    }

    public void setValue(Expn value) {
        this.value = value;
    }
}
