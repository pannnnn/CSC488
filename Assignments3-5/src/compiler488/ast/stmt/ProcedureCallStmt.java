package compiler488.ast.stmt;

import compiler488.ast.ASTList;
import compiler488.ast.SemanticException;
import compiler488.ast.SemanticExceptionList;
import compiler488.ast.expn.Expn;
import compiler488.symbol.ProcedureSymbol;

import compiler488.ast.CodeGenerationException;
import compiler488.ast.CompileTime;

import compiler488.runtime.Machine;

/**
 * Represents calling a procedure.
 */
public class ProcedureCallStmt extends Stmt {

    private String name;                     // The name of the procedure being called.
    private ASTList<Expn> arguments = new ASTList<Expn> ();  // The arguments passed to the procedure.

    public void generate() throws CodeGenerationException {

        ProcedureSymbol symbol = (ProcedureSymbol) scope.getSymbol(name, this);

        // return address
        emit(Machine.PUSH);
        Short returnAddress = placeholder();

        // dynamic link
        emit(Machine.ADDR, symbol.getBody().scope.lexicalLevel, (short) 0);

        // padding
        emit(Machine.PUSH, Machine.UNDEFINED);

        // arguments
        for (Expn arg : arguments.getLL()) {
            arg.generate();
        }

        emit(Machine.PUSHMT);
        emit(Machine.PUSH, (short) (arguments.size() + symbol.getBody().scope.getScopeOffset()));
        emit(Machine.SUB);
        emit(Machine.SETD, symbol.getBody().scope.lexicalLevel);

        emit(Machine.PUSH, symbol.codeAddress);
        emit(Machine.BR);
        fill(returnAddress, CompileTime.instructionCounter);
    }

    /**
     * Constructor for a procedure call statement with given procedure name.
     */
    public ProcedureCallStmt(Integer row, Integer column, String name) {
        this.setCoordinates(row, column);
        this.name = name;
    }

    /**
     * Constructor for a procedure call statement with the given procedure name and arguments.
     */
    public ProcedureCallStmt(Integer row, Integer column, String name, ASTList<Expn> arguments) {
        this.setCoordinates(row, column);
        this.name = name;
        this.arguments = arguments;
    }

    /** Returns a string describing the procedure call. */
    @Override
    public String toString() {
        return (this.arguments != null) ? "Procedure call: " + this.name + " (" + this.arguments + ")" : "Procedure call: " + this.name + " ";
    }

    // ================================================================
    // Checking
    // ================================================================
    
    public void check(Scope scope, int loopCount) throws SemanticException {
        super.check(scope, loopCount);
        try {
            this.checkS41(this);
            if (this.arguments == null) {
                this.checkS42(this.name, scope);
            } else {
                this.checkS44();
                for (Expn arg : this.arguments.getLL()) {
                    arg.check(scope, loopCount);
                }
                this.checkS43(scope, name, arguments.size());
            }
        } catch (SemanticException e) {
            SemanticExceptionList.add(e);
        }
    }
    
    // ================================================================
    // Getters and Setters
    // ================================================================

    public ASTList<Expn> getArguments() {
        return arguments;
    }

    public void setArguments(ASTList<Expn> args) {
        this.arguments = args;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
