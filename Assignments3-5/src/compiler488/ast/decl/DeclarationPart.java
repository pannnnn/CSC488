package compiler488.ast.decl;

import compiler488.ast.AST;
import compiler488.ast.SemanticException;
import compiler488.ast.stmt.Scope;

/**
 * The common features of declarations' parts.
 */
public class DeclarationPart extends AST {

    /** The name of the thing being declared. */
    private String name;

    public int loopCount = 0;
    public boolean inFunction = false;
    public boolean inProcedure = false;

    public DeclarationPart(String name) {
    	this.name = name;
    }

    // ================================================================
    // Checking
    // ================================================================

    @Override
    public void check(Scope scope, int loopCount) throws SemanticException {
    	setScope(scope);
    	this.loopCount = loopCount;
    	this.inFunction = inFunction;
    	this.inProcedure = inProcedure;
    }
    
    // ================================================================
    // Getters and Setters
    // ================================================================

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

}
