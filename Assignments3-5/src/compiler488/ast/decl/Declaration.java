package compiler488.ast.decl;

import compiler488.ast.Indentable;
import compiler488.ast.SemanticException;
import compiler488.ast.stmt.Scope;
import compiler488.ast.type.Type;


/**
 * The common features of declarations.
 */
public class Declaration extends Indentable {
	
    private Type type = null;    // The type of thing being declared.
    private String name = null;  //  The name of the thing being declared.
    
    public int loopCount = 0;
    public boolean inFunction = false;
    public boolean inProcedure = false;
    
    /**
     * Constructor for a declaration with a name.
     */
    public Declaration(String name) {
    	this.name = name;
    }
    
    /**
     * Constructor for a declaration without a name but with a type.
     * This is for declaring multiple variable of the same type on one line.
     */
    public Declaration(Type type) {
    	this.type = type;
    }
    
    /**
     * Constructor for a declaration with a name and type.
     */
    public Declaration(String name, Type type) {
    	this.name = name;
    	this.type = type;
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

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}
}
