package compiler488.ast.stmt;

import java.io.PrintStream;

import compiler488.ast.Indentable;
import compiler488.ast.SemanticException;
import compiler488.ast.SemanticExceptionList;
import compiler488.ast.expn.Expn;

import compiler488.ast.CodeGenerationException;
import compiler488.ast.CompileTime;

import compiler488.symbol.FunctionSymbol;

import compiler488.runtime.Machine;

/**
 * The command to return from a function or procedure.
 */
public class ReturnStmt extends Stmt {

    private Expn value = null;  // The value to be returned by a function.

    /**
     * Constructor for a return statement without a value.
     */
    public ReturnStmt(Integer row, Integer column) {
        this.setCoordinates(row, column);
    }

    /**
     * Constructor for a return statement with the specified value.
     * @param exp
     */
    public ReturnStmt(Integer row, Integer column, Expn value) {
        this.setCoordinates(row, column);
        this.value = value;
    }

    public void generate() throws CodeGenerationException {
        if (value != null) {
            // function returning
            emit(Machine.ADDR, scope.lexicalLevel, (short) 0);
            value.generate();
            emit(Machine.STORE);
            emit(Machine.PUSH, (short) (scope.getARLength() - 3));
            emit(Machine.POPN);
            emit(Machine.SETD, scope.lexicalLevel);
            emit(Machine.BR);
        } else {
            // procedure returning
            scope.generateExitScope();
        }
    }

    /**
     * Print <b>return</b> or <b>return with </b> expression on a line, by itself.
     * 
     * @param out:    Where to print.
     * @param depth:  How much indentation to use while printing.
     */
    @Override
    public void printOn(PrintStream out, int depth) {
        Indentable.printIndentOn(out, depth);
        if (value == null)
            out.println("return ");
        else
            out.println("return with " + this.value);
    }

    // ================================================================
    // Checking
    // ================================================================
    
    public void check(Scope scope, int loopCount) throws SemanticException {
        super.check(scope, loopCount);
        try {
            if (this.value == null) {
                this.checkS52(this);
            } else {
                this.value.check(scope, loopCount);
                if (this.checkS51(this))
                    this.checkS35(this.value, this.getScope().getEnclosingRoutine());
            }
        } catch (SemanticException e) {
            SemanticExceptionList.add(e);
        }
    }
    
    // ================================================================
    // Getters and Setters
    // ================================================================

    public Expn getValue() {
        return this.value;
    }

    public void setValue(Expn value) {
        this.value = value;
    }
}
