package compiler488.ast.stmt;

import compiler488.ast.SemanticExceptionList;
import compiler488.ast.SemanticException;
import compiler488.ast.expn.Expn;

/**
 * Represents the common parts of loops.
 */
public abstract class LoopingStmt extends Stmt {

	protected Stmt body;       // loop body
    protected Expn condition;  // loop condition	
		
    public LoopingStmt(Expn condition, Stmt body) {
		this.condition = condition;
		this.body = body;
	}
    
    // ================================================================
    // Checking
    // ================================================================
    
    @Override
    public void check(Scope scope, int loopCount) throws SemanticException {
    	super.check(scope, loopCount + 1);
        try {
            this.body.check(scope, loopCount + 1);
            if (body instanceof Scope) {
                this.condition.check((Scope) body, loopCount + 1);
            } else {
                this.condition.check(scope, loopCount + 1);
            }
            this.checkS30(this.condition);
        } catch (SemanticException e) {
            SemanticExceptionList.add(e);
        }
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

    public Stmt getBody() {
        return this.body;
    }

    public void setBody(Stmt body) {
        this.body = body;
    }  
}
