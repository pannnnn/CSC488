package compiler488.ast.stmt;

import compiler488.ast.ASTList;
import compiler488.ast.Printable;
import compiler488.ast.SemanticException;
import compiler488.ast.SemanticExceptionList;
import compiler488.ast.expn.Expn;
import compiler488.ast.expn.TextConstExpn;
import compiler488.ast.expn.SkipConstExpn;

import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;

import compiler488.ast.CompileTime;
import compiler488.ast.CodeGenerationException;

/**
 * The command to write data on the output device.
 */
public class WriteStmt extends Stmt {
    
    private ASTList<Printable> outputs = new ASTList<Printable> ();  // The objects to be printed.

    /**
     * Constructor for an empty write statement.
     */
    public WriteStmt (Integer row, Integer column) {
        this.setCoordinates(row, column);
    }

    /**
     * Constructor for a write statement with specified outputs.
     * @param outputs
     */
    public WriteStmt(Integer row, Integer column, ASTList<Printable> outputs) {
        this.setCoordinates(row, column);
        this.outputs = outputs;
    }
    
    /** Returns a description of the <b>write</b> statement. */
    @Override
    public String toString() {
        return "write " + this.outputs;
    }

    public void generate() throws CodeGenerationException {
        if (this.outputs != null) {
            for (Printable out: this.outputs.getLL()) {
                if (out instanceof TextConstExpn) {
                    ((TextConstExpn) out).generate();
                } else if (out instanceof SkipConstExpn) {
                    ((SkipConstExpn) out).generate();
                } else if (out instanceof Expn) {
                    ((Expn) out).generate();
                    emit(Machine.PRINTI);
                }
            }
        }
    }

    // ================================================================
    // Checking
    // ================================================================

    public void check(Scope scope, int loopCount) throws SemanticException {
        super.check(scope, loopCount);

        try {
            if (this.outputs != null) {
                for (Printable output : this.outputs.getLL()) {
                    // Only check output if its an expression.
                    if (output instanceof Expn) {
                        ((Expn) output).check(scope, loopCount);
                    }
                }
            }
        } catch (SemanticException e) {
            SemanticExceptionList.add(e);
        }
    }
    
    // ================================================================
    // Getters and Setters
    // ================================================================

    public ASTList<Printable> getOutputs() {
        return this.outputs;
    }

    public void setOutputs(ASTList<Printable> outputs) {
        this.outputs = outputs;
    }
}
