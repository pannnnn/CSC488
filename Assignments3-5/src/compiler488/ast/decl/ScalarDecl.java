package compiler488.ast.decl;

import compiler488.ast.SemanticException;
import compiler488.ast.stmt.Scope;
import compiler488.ast.type.Type;

/**
 * Represents the declaration of a simple variable.
 */
public class ScalarDecl extends Declaration {
	
	private boolean isParam = false;

	/**
	 * Constructor for a simple variable with a variable name and type.
	 */
    public ScalarDecl(Integer row, Integer column, String name, Type type) {
        super(name, type);
        this.setCoordinates(row, column);
    }

    /**
     * Returns a string representation of this object.
     * Includes the name and type of the object being declared.
     */
    @Override
    public String toString() {
        return this.getName() + " : " + this.getType();
    }
    
    // ================================================================
    // Checking
    // ================================================================
    
    @Override
    public void check(Scope scope, int loopCount) throws SemanticException {
        super.check(scope, loopCount);
        
    	if (!this.isParam) {
            this.checkS10(this);
            this.checkS26(this, this.getName());
    	} else {
            // this.checkS16();
            this.checkS15(this);
            this.checkS25(this, this.getName());
    		
    	}
    }
    
    // ================================================================
    // Getters and Setters
    // ================================================================

	public boolean isParam() {
		return isParam;
	}

	public void setParam(boolean isParam) {
		this.isParam = isParam;
	}
}
