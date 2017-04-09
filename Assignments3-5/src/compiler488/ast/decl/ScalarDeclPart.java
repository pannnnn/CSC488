package compiler488.ast.decl;

import compiler488.ast.SemanticException;
import compiler488.ast.stmt.Scope;

/**
 * Represents the declaration of a simple variable.
 */
public class ScalarDeclPart extends DeclarationPart {

	/**
	 * Constructor for a declaration of a simple variable with the given variable name.
	 */
    public ScalarDeclPart(Integer row, Integer column, String name) {
        super(name);
        this.setCoordinates(row, column);
    }

    /**
     * Returns a string describing the name of the object being
     * declared.
     */
    @Override
    public String toString() {
        return this.getName();
    }
    
    // ================================================================
    // Checking
    // ================================================================

    @Override
    public void check(Scope scope, int loopCount) throws SemanticException {
    	super.check(scope, loopCount);
    	
    	this.checkS10(this);
    }
}
