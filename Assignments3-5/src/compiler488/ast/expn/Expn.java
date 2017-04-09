package compiler488.ast.expn;

import compiler488.ast.AST;
import compiler488.ast.Printable;
import compiler488.ast.Readable;
import compiler488.ast.SemanticException;
import compiler488.ast.type.Type;

import compiler488.ast.stmt.Scope;

/**
 * A placeholder for all expressions.
 */
public class Expn extends AST implements Printable {

	private Type type;  // expression type
    
    public int loopCount;
    public boolean inFunction, inProcedure;
    
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

    public Type getType() {
    	return this.type;
    }
    
    public void setType(Type type) {
    	this.type = type;
    }

    public Scope getScope() {
        return this.scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }    
}
