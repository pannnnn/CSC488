package compiler488.ast.stmt;

import compiler488.ast.Indentable;
import compiler488.ast.SemanticException;

/**
 * A placeholder for statements.
 */
public class Stmt extends Indentable {
	
    public int loopCount = 0;
    public boolean inFunction = false;
    public boolean inProcedure = false;
	
    // ================================================================
    // Checking
    // ================================================================

    public void check(Scope scope, int loopCount) throws SemanticException {            
        setScope(scope);
        this.loopCount = loopCount;
    }
}
