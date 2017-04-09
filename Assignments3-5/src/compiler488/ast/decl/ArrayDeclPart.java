package compiler488.ast.decl;

import compiler488.ast.SemanticException;
import compiler488.ast.stmt.Scope;

/**
 * Holds the declaration part of an array.
 */
public class ArrayDeclPart extends DeclarationPart {

    private Integer lb, ub;  // The lower and upper boundaries of the array.
    private Integer size;    // The number of objects the array holds.

    /**
     * Constructor for an array declaration with the given array name and size.
     */
    public ArrayDeclPart(Integer row, Integer column, String name, Integer size) {
        super(name);
        this.setCoordinates(row, column);
        this.lb = 1;
        this.ub = size;
        this.size = size;
    }

    /**
     * Constructor for an array declaration with the given array name and lower and upper bounds.
     */
    public ArrayDeclPart(Integer row, Integer column, String name, Integer lb, Integer ub) {
        super(name);
        this.setCoordinates(row, column);
    	this.lb = lb;
    	this.ub = ub;
    	this.size = ub - lb + 1;
    }

    /**
     * Returns a string that describes the array.
     */
    @Override
    public String toString() {
        return this.getName() + "[" + this.lb + ".." + this.ub + "]";
    }
    
    // ================================================================
    // Checking
    // ================================================================
    
    @Override
    public void check(Scope scope, int loopCount) throws SemanticException {
    	super.check(scope, loopCount);     
        
    	if (this.lb == 0) {
    		this.checkS48(scope, this);  // declaring array
    	} else {
    		this.checkS46(this); 		 // checking that lower bound is <= upper bound
    		this.checkS19(scope, this);  // declaring array
    	}
    }
    
    // ================================================================
    // Getters and Setters
    // ================================================================

    public Integer getLowerBoundary() {
        return this.lb;
    }
    
    public void setLowerBoundary(Integer lb) {
        this.lb = lb;
    }

    public Integer getUpperBoundary() {
        return this.ub;
    }

    public void setUpperBoundary(Integer ub) {
        this.ub = ub;
    }
    
    public Short getSize() {
        return this.size.shortValue();
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
