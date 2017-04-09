package compiler488.ast.decl;

import java.io.PrintStream;

import compiler488.ast.ASTList;
import compiler488.ast.Indentable;
import compiler488.ast.SemanticException;
import compiler488.ast.stmt.Scope;
import compiler488.ast.type.Type;
import compiler488.ast.CompileTime;

/**
 * Represents the declaration of a function or procedure.
 */
public class RoutineDecl extends Declaration {
    
    /*
     * The formal parameters of the function/procedure and the
     * statements to execute when the procedure is called.
     */
    private ASTList<ScalarDecl> parameters = new ASTList<ScalarDecl> ();
    private RoutineBody routineBody;
    
    // NOTE: no type -> procedure, otherwise function.

    /**
     * Constructor for a PROCEDURE without parameters.
     */
    public RoutineDecl(Integer row, Integer column, String name, RoutineBody routineBody) {
        super(name);
        this.setCoordinates(row, column);
        this.routineBody = routineBody;
    }
    
    /**
     * Constructor for a PROCEDURE with parameters.
     */
    public RoutineDecl(Integer row, Integer column, String name, ASTList<ScalarDecl> params, RoutineBody routineBody) {
        super(name);
        this.setCoordinates(row, column);
        this.parameters = params;
        this.routineBody = routineBody;
        this.routineBody.setParameters(this.parameters);
    }
    
    /**
     * Constructor for a FUNCTION without parameters.
     */
    public RoutineDecl(Integer row, Integer column, String name, Type type, RoutineBody routineBody) {
        super(name, type);
        this.setCoordinates(row, column);
        this.routineBody = routineBody;
    }

    /**
     * Constructor for a FUNCTION with parameters.
     */
    public RoutineDecl(Integer row, Integer column, String name, Type type, ASTList<ScalarDecl> params, RoutineBody routineBody) {
        super(name, type);
        this.setCoordinates(row, column);
        this.parameters = params;
        this.routineBody = routineBody;
        this.routineBody.setParameters(this.parameters);
    }

    /**
     * Returns a string indicating that this is a function with
     * return type or a procedure, name, Type parameters, if any,
     * are listed later by routineBody
     */
    @Override
    public String toString() {
        return (this.getType() == null) ? ("procedure " + this.getName()) : ("function " + this.getName() + " : " + this.getType());
    }

    /**
     * Prints a description of the function/procedure.
     * 
     * @param out:    Where to print the description.
     * @param depth:  How much indentation to use while printing.
     */
    @Override
    public void printOn(PrintStream out, int depth) {
        Indentable.printIndentOn(out, depth, this + " ");
        routineBody.printOn(out, depth + 1);
    }
    
    // ================================================================
    // Checking
    // ================================================================

    @Override
    public void check(Scope scope, int loopCount) throws SemanticException {
        setScope(scope);
        routineBody.scope.setEnclosingRoutine(this);

        if (this.getType() == null) {  // procedure
            this.checkProcedure(scope, routineBody.scope);
        } else {                       // function
            this.checkFunction(scope, routineBody.scope);;
        }

        this.routineBody.check(scope, 0);
    }
    
    private void checkFunction(Scope parentScope, Scope functionScope) throws SemanticException {
    	if (this.parameters == null) {
    		this.checkS11(parentScope, this);
    	} else {
    		this.checkS12(parentScope, this);
    	}
    	
    	// Initialize new scope.
    	this.checkS04(functionScope, parentScope);
    	this.checkS05();
    	
    	// Checking that the identifier has been declared as a function.
//    	this.checkS40(this.getName(), parentScope);
    }
    
    private void checkProcedure(Scope parentScope, Scope procedureScope) throws SemanticException {    	
    	if (this.parameters == null) {
    		this.checkS17(parentScope, this);
    	} else {
    		this.checkS18(parentScope, this);
    	}
    	
    	// Initialize new scope.
    	this.checkS08(procedureScope, parentScope);
    	this.checkS09();
    	
    	// Checking that the identifier has been declared as a procedure.
//    	this.checkS41(this.getName(), parentScope);
    }        

    // ================================================================
    // Getters and Setters
    // ================================================================

    public RoutineBody getRoutineBody() {
        return routineBody;
    }

    public void setRoutineBody(RoutineBody routineBody) {
        this.routineBody = routineBody;
    }

    public int getParameterCount() {
        return parameters.size();
    }

    public ASTList<ScalarDecl> getParameters() {
        return parameters;
    }
}
