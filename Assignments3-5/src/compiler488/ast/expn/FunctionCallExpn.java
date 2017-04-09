package compiler488.ast.expn;

import compiler488.ast.ASTList;
import compiler488.ast.SemanticException;
import compiler488.ast.stmt.Scope;

import compiler488.ast.CodeGenerationException;
import compiler488.ast.CompileTime;

import compiler488.symbol.FunctionSymbol;

import compiler488.runtime.Machine;
/**
 * Represents a function call with or without arguments.
 */
public class FunctionCallExpn extends Expn {
    
    private String ident;             // The name of the function.
    private ASTList<Expn> arguments;  // The arguments passed to the function.

    public FunctionCallExpn(Integer row, Integer column, String ident, ASTList<Expn> arguments) {
        this.setCoordinates(row, column);
        this.ident = ident;
        this.arguments = arguments;
    }

    public FunctionCallExpn(IdentExpn identExpn) {
        this.setCoordinates(identExpn.row, identExpn.column);
        this.ident = identExpn.getIdent();
        this.arguments = new ASTList<Expn> ();
        setScope(identExpn.scope);
    }

    // ================================================================
    // Code Generation
    // ================================================================
    public void generate() throws CodeGenerationException {
        FunctionSymbol symbol = (FunctionSymbol) scope.getSymbol(ident, this);

        // return value
        emit(Machine.PUSH, Machine.UNDEFINED);

        // return address
        emit(Machine.PUSH);
        Short returnAddress = placeholder();

        // dynamic link to previous display with same lexical level
        emit(Machine.ADDR, symbol.getBody().scope.lexicalLevel, (short) 0);

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

    /** Returns a string describing the function call. */
    @Override
    public String toString() {
        if (this.arguments != null) {
            return this.ident + " (" + this.arguments + ")";
        } else {
            return this.ident + " ";
        }
    }
    
    // ================================================================
    // Checking
    // ================================================================
    
    public void check(Scope scope, int loopCount) throws SemanticException {
    	super.check(scope, loopCount);
        this.checkS40(this, this.ident);
        if (this.arguments != null) {
            for (Expn arg : this.arguments.getLL()) {
                arg.check(scope, loopCount);
            }
            this.checkS44();
            this.checkS43(scope, this.ident, this.arguments.size());
            this.checkS36(scope, this.ident, this.arguments);
        } else {
            this.checkS42(this.ident, scope);
        }
        this.checkS28(this, this.ident);
    }
    
    // ================================================================
    // Getters and Setters
    // ================================================================

    public ASTList<Expn> getArguments() {
        return this.arguments;
    }

    public void setArguments(ASTList<Expn> args) {
        this.arguments = args;
    }

    public String getIdent() {
        return this.ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

}
