package compiler488.ast.stmt;

import java.io.PrintStream;

import compiler488.ast.Indentable;
import compiler488.ast.SemanticException;
import compiler488.ast.SemanticExceptionList;
import compiler488.ast.expn.Expn;
import compiler488.ast.CompileTime;
import compiler488.ast.CodeGenerationException;

import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;


/**
 * Represents an if-then or an if-then-else construct.
 */
public class IfStmt extends Stmt {

    private Expn condition;  		// The condition that determines which branch to execute.
    private Stmt whenTrue;   		// Represents the statement to execute when the condition is true.
    private Stmt whenFalse = null;  // Represents the statement to execute when the condition is false.
    
    /**
     * Constructor for an if statement without an else clause.
     */
    public IfStmt(Integer row, Integer column, Expn condition, Stmt whenTrue) {
        this.setCoordinates(row, column);
    	this.condition = condition;
    	this.whenTrue = whenTrue;
    }

    /**
     * Constructor for a complete if statement.
     */
    public IfStmt(Integer row, Integer column, Expn condition, Stmt whenTrue, Stmt whenFalse) {
        this.setCoordinates(row, column);
    	this.condition = condition;
    	this.whenTrue = whenTrue;
    	this.whenFalse = whenFalse;
    }

    // ================================================================
    // Code Generation
    // ================================================================

    public void generate() throws CodeGenerationException {
        condition.generate();
        emit(Machine.PUSH);
        short branchLocation = placeholder();

        emit(Machine.BF);
        whenTrue.generate();

        if (whenFalse != null) {
            emit(Machine.PUSH);
            short exitLocation = placeholder();
            emit(Machine.BR);
            fill(branchLocation, CompileTime.instructionCounter);
            whenFalse.generate();
            fill(exitLocation, CompileTime.instructionCounter);
        } else {
            fill(branchLocation, CompileTime.instructionCounter);
        }
    }


    /**
     * Print a description of the <b>if-then-else</b> construct. If the
     * <b>else</b> part is empty, just print an <b>if-then</b> construct.
     * 
     * @param out:    Where to print the description.
     * @param depth:  How much indentation to use while printing.
     */
    @Override
    public void printOn(PrintStream out, int depth) {
        Indentable.printIndentOnLn(out, depth, "if " + this.condition + " then ");
        this.whenTrue.printOn(out, depth + 1);
        if (this.whenFalse != null) {
            Indentable.printIndentOnLn(out, depth, "else");
            this.whenFalse.printOn(out, depth + 1);
        }
        Indentable.printIndentOnLn(out, depth, "End if");
    }

    // ================================================================
    // Checking
    // ================================================================
    
    public void check(Scope scope, int loopCount) throws SemanticException {
        super.check(scope, loopCount);
        try {
        	this.condition.check(scope, loopCount);
        	this.checkS30(this.condition);
        	this.whenTrue.check(scope, loopCount);
        	
        	if (this.whenFalse != null) {
        		this.whenFalse.check(scope, loopCount);
        	}
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

    public Stmt getWhenFalse() {
        return this.whenFalse;
    }

    public void setWhenFalse(Stmt whenFalse) {
        this.whenFalse = whenFalse;
    }

    public Stmt getWhenTrue() {
        return this.whenTrue;
    }

    public void setWhenTrue(Stmt whenTrue) {
        this.whenTrue = whenTrue;
    }
}
