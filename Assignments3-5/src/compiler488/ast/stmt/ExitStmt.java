package compiler488.ast.stmt;

import java.util.Arrays;

import compiler488.ast.SemanticException;
import compiler488.ast.SemanticExceptionList;
import compiler488.ast.stmt.*;
import compiler488.ast.expn.*;

import compiler488.symbol.SymbolTable;
import compiler488.symbol.Symbol;
import compiler488.symbol.ArraySymbol;
import compiler488.symbol.ParameterSymbol;
import compiler488.symbol.Pair;

import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;

import compiler488.ast.CompileTime;
import compiler488.ast.CodeGenerationException;

/**
 * Represents the command to exit from a loop.
 */
public class ExitStmt extends Stmt {

    // condition for 'exit when'
    private Expn condition = null;
    private Integer level = -1 ;
    
    /**
     * Constructor for an in-loop exit statement.
     */
    public ExitStmt(Integer row, Integer column) {
        this.setCoordinates(row, column);
        this.level = 1;
    }

    /**
     * Constructor for an exit statement with the specified exit level.
     */
    public ExitStmt(Integer row, Integer column, Integer level) {
        this.setCoordinates(row, column);
        this.level = level;
    }

    /**
     * Constructor for an exit statement with the specified exit condition.
     */
    public ExitStmt(Integer row, Integer column, Expn condition) {
        this.setCoordinates(row, column);
        this.condition = condition;
        this.level = 1;
    }

    /**
     * Constructor for an exit statement with the specified exit level and condition.
     */
    public ExitStmt(Integer row, Integer column, Integer level, Expn condition) {
        this.setCoordinates(row, column);
        this.level = level;
        this.condition = condition;
    }

    /** Returns the string <b>"exit"</b> or <b>"exit when e"</b>" 
            or  <b>"exit"</b> level  or  <b>"exit"</b> level  when e 
    */
    @Override
    public String toString() {
        String stmt = "exit ";
        if (this.level >= 0 )
            stmt = stmt + this.level + " ";
        if (this.condition != null )
            stmt = stmt + "when " + this.condition + " ";
        return stmt ;
    }

    // ================================================================
    // Checking
    // ================================================================
    
    public void check(Scope scope, int loopCount) throws SemanticException {
        super.check(scope, loopCount);
        try {
            this.checkS50(this);

            if (this.condition == null) {
                this.checkS53(this);
            } else if (this.level == -1) {
                this.condition.check(scope, loopCount);
                this.checkS30(this.condition);
            } else {
                this.condition.check(scope, loopCount);
                this.checkS30(this.condition);
                this.checkS53(this);
            }
        } catch (SemanticException e) {
            SemanticExceptionList.add(e);
        }
    }

    // ================================================================
    // Code Generation
    // ================================================================

    public void generate() throws CodeGenerationException {
        // Count length of total activation records
        short length = -2;
        if (level != -1) {
            int exited = 0;
            for (Loop loop : scope.loopStack) {
                if (loop.isLoop) {
                    exited ++;
                }
                length += loop.arLength;
                if (exited == level) {
                    break;
                }
            }
        }

        // Exit x when condition
        Short afterExit = null;
        if (condition != null) {
            condition.generate();
            emit(Machine.PUSH);
            afterExit = placeholder();
            emit(Machine.BF);
        }

        // Code for exit x.
        emit(Machine.PUSH, length);
        emit(Machine.POPN);
        emit(Machine.BR);
        fill(afterExit, CompileTime.instructionCounter);

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

    public Integer getLevel() {
        return this.level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

}
