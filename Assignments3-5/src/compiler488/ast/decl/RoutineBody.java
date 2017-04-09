package compiler488.ast.decl;

import java.io.PrintStream;

import compiler488.ast.ASTList;
import compiler488.ast.Indentable;
import compiler488.ast.SemanticException;
import compiler488.ast.stmt.Scope;

import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;

import compiler488.ast.CompileTime;
import compiler488.ast.CodeGenerationException;

/**
 * Represents the parameters and instructions associated with a
 * function or procedure.
 */
public class RoutineBody extends Indentable {
    
    // The formal parameters of the routine.
    private ASTList<ScalarDecl> parameters = new ASTList<ScalarDecl> ();

    public int loopCount = 0;
    public boolean inFunction = false;
    public boolean inProcedure = false;

    public void generate() throws CodeGenerationException {
        scope.generate();
    }

    public RoutineBody(Scope body) {
        CompileTime.initMajorScope(body);
        this.scope = body;
    }

    public RoutineBody(ASTList<ScalarDecl> parameters, Scope body) {
        CompileTime.initMajorScope(body);
        this.parameters = parameters;
        this.scope = body;
    }

    /**
     * Print a description of the formal parameters and the scope for this
     * routine.
     * 
     * @param out:    Where to print the description.
     * @param depth:  How much indentation to use while printing.
     */
    @Override
    public void printOn(PrintStream out, int depth) {
        if (this.parameters != null)
            out.println("(" + this.parameters.toString() + ")");
        else
            out.println(" ");
        this.scope.printOn(out, depth);
    }    
    
    // ================================================================
    // Checking
    // ================================================================

    public void check(Scope parentScope, int loopCount) throws SemanticException {
        this.loopCount = loopCount;
        this.inFunction = inFunction;
        this.inProcedure = inProcedure;

        if (this.parameters != null) {
            for (ScalarDecl param : this.parameters.getLL()) {
                param.setParam(true);
                param.check(scope, loopCount);
            }
        }
        scope.check(parentScope, 0);
    }

    // ================================================================
    // Getters and Setters
    // ================================================================

    public ASTList<ScalarDecl> getParameters() {
        return this.parameters;
    }

    public void setParameters(ASTList<ScalarDecl> parameters) {
        this.parameters = parameters;
    }
}
