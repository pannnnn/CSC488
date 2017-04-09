package compiler488.ast.expn;

import compiler488.ast.Readable;
import compiler488.ast.SemanticException;
import compiler488.ast.SemanticExceptionList;
import compiler488.ast.CodeGenerationException;
import compiler488.ast.stmt.Scope;
import compiler488.symbol.Symbol;
import compiler488.symbol.VariableSymbol;
import compiler488.symbol.ArraySymbol;
import compiler488.symbol.FunctionSymbol;
import compiler488.symbol.ParameterSymbol;
import compiler488.symbol.ProcedureSymbol;

import compiler488.runtime.Machine;

/**
 *  References to a scalar variable or a function call without parameters.
 */
public class IdentExpn extends Expn implements Readable {
    
    private String ident;   // name of the identifier
    private Symbol symbol;  // identifier symbol

    public IdentExpn(Integer row, Integer column, String ident) {
        this.setCoordinates(row, column);
        this.ident = ident;
    }

    public void generateAddr() throws CodeGenerationException {
        Symbol s = scope.getSymbol(ident, this);
        Short realOffset = (short) (scope.getScopeOffset() + scope.getOffset(s));
        emit(Machine.ADDR, scope.lexicalLevel, realOffset);
    }

    // ================================================================
    // Code Generation
    // ================================================================
    public void generate() throws CodeGenerationException {
        if (scope.getSymbol(ident, this) instanceof FunctionSymbol) {
            FunctionCallExpn functionCallExpn = new FunctionCallExpn(this);
            functionCallExpn.generate();
        } else {
            generateAddr();
            emit(Machine.LOAD);
        }
    }

    /**
     * Returns the name of the variable or function.
     */
    @Override
    public String toString () { return scope + "." + this.ident; }
    
    // ================================================================
    // Checking
    // ================================================================

    @Override
    public void check(Scope scope, int loopCount) throws SemanticException {
        super.check(scope, loopCount);
        
        // Checking if identifier exists in the symbol table.
        this.symbol = scope.getSymbolTable().get(this.ident, this);
        this.setType(this.symbol.getType());

        if (this.symbol instanceof VariableSymbol) {
            this.checkS37(this);
        } else if (this.symbol instanceof ParameterSymbol) {
            this.checkS39(this);
        } else if (this.symbol instanceof FunctionSymbol) {
            this.checkS40(this, this.ident);
            this.checkS42(this.ident, scope);
        } else {
            throw new SemanticException(this, "Identifier " + this.ident + " has not been declared as an scalar or a function call.");
        }
    }
    
    // ================================================================
    // Getters and Setters
    // ================================================================

    public String getIdent() {
        return this.ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public Symbol getSymbol() {
        return this.symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }
}
