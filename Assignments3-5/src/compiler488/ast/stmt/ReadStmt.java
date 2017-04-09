package compiler488.ast.stmt;

import compiler488.ast.ASTList;
import compiler488.ast.Readable;
import compiler488.ast.SemanticException;
import compiler488.ast.SemanticExceptionList;
import compiler488.ast.expn.Expn;
import compiler488.ast.expn.IdentExpn;

import compiler488.symbol.FunctionSymbol;

import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;

import compiler488.ast.CompileTime;
import compiler488.ast.CodeGenerationException;

/**
 * The command to read data into one or more variables.
 */
public class ReadStmt extends Stmt {
    
    private ASTList<Readable> inputs = new ASTList<Readable> ();  // A list of locations to put the values read.

    public ReadStmt (Integer row, Integer column) {
        this.setCoordinates(row, column);
    }

    public ReadStmt(Integer row, Integer column, ASTList<Readable> inputs) {
        this.setCoordinates(row, column);
        this.inputs = inputs;
    }
    
    /** Returns a string describing the <b>read</b> statement. */
    @Override
    public String toString() {
        return "read " + inputs;
    }

    public void generate() throws CodeGenerationException {
        if (this.inputs != null){

            for (Readable in: this.inputs.getLL()) {
                if (in instanceof IdentExpn) {
                    String ident = ((IdentExpn) in).getIdent();
                    if (scope.getSymbol(ident, this) instanceof FunctionSymbol) {
                        throw new CodeGenerationException(this, ident + " is declared as a function but not readable.");
                    }
                }
                in.generateAddr();
                emit(Machine.READI);
                emit(Machine.STORE);
            }
        }
    }
    // ================================================================
    // Checking
    // ================================================================
    
    public void check(Scope scope, int loopCount) throws SemanticException {
        super.check(scope, loopCount);
        try {
        	if (this.inputs != null) {
        		for (Readable input : this.inputs.getLL()) {
        			// Only check input if its an expression.
        			if (input instanceof Expn) {
        				((Expn) input).check(scope, loopCount);
        			}
        		}
        	}
        }  catch (SemanticException e) {
            SemanticExceptionList.add(e);
        }
    }
    
    // ================================================================
    // Getters and Setters
    // ================================================================

    public ASTList<Readable> getInputs() {
        return inputs;
    }

    public void setInputs(ASTList<Readable> inputs) {
        this.inputs = inputs;
    }
}
