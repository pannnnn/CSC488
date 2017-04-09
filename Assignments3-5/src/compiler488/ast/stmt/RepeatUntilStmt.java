package compiler488.ast.stmt;

import java.io.PrintStream;
import compiler488.ast.Indentable;
import compiler488.ast.expn.*;

import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;

import compiler488.ast.CompileTime;
import compiler488.ast.CodeGenerationException;
/**
 * Represents a loop in which the exit condition is evaluated after each pass.
 */
public class RepeatUntilStmt extends LoopingStmt {
    
    /**
     * Constructor for a repeat statement with the specified condition and body.
     */
    public RepeatUntilStmt(Integer row, Integer column, Stmt body, Expn condition) {
        super(condition, body);
        this.setCoordinates(row, column);
    }
    
    /**
     * Print a description of the <b>repeat-until</b> construct.
     * 
     * @param out:    Where to print the description.
     * @param depth:  How much indentation to use while printing.
     */
    @Override
    public void printOn(PrintStream out, int depth) {
        Indentable.printIndentOnLn(out, depth, "repeat");
        body.printOn(out, depth + 1);
        Indentable.printIndentOnLn(out, depth, " until "  + this.condition);
    }

    public void generate() throws CodeGenerationException {
        if (body instanceof Scope) {
            Scope minorScope = (Scope) body;

            // Enter Scope
            scope.push(new Loop(CompileTime.instructionCounter, minorScope.getARLength(), true));
            emit(Machine.ADDR, minorScope.lexicalLevel, (short) 0);
            emit(Machine.PUSH);
            short breakAddress0 = placeholder();
            minorScope.generateEnterScopeHelper();

            // Generate Stmts
            short startAddress = scope.enterLoop(CompileTime.instructionCounter);
            minorScope.generateStatements();

            // Generate Condition
            condition.generate();
            emit(Machine.PUSH, Machine.MACHINE_FALSE);
            emit(Machine.EQ);
            emit(Machine.PUSH);
            short breakAddress = placeholder();
            emit(Machine.BF);

            // Go Back or Exit
            emit(Machine.PUSH, startAddress);
            emit(Machine.BR);
            fill(breakAddress0, (short) (CompileTime.instructionCounter + 3));       // Skip POPN
            fill(breakAddress, CompileTime.instructionCounter);
            minorScope.fillBreakAddresses(CompileTime.instructionCounter);
            minorScope.generateExitScope();


        } else {
            // No Scope, Only Single Stmt
            short startAddress = CompileTime.instructionCounter;
            body.generate();

            condition.generate();
            emit(Machine.PUSH, Machine.MACHINE_FALSE);
            emit(Machine.EQ);
            emit(Machine.PUSH);
            short breakAddress = placeholder();
            emit(Machine.BF);

            emit(Machine.PUSH, startAddress);
            emit(Machine.BR);
            fill(breakAddress, CompileTime.instructionCounter);
        }
    }
}
