package compiler488.ast.stmt;

import java.io.PrintStream;
import compiler488.ast.Indentable;
import compiler488.ast.expn.*;
import compiler488.ast.SemanticExceptionList;

import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;

import compiler488.ast.CompileTime;
import compiler488.ast.CodeGenerationException;
/**
 * Represents a loop in which the exit condition is evaluated before each pass.
 */
public class WhileDoStmt extends LoopingStmt {
	
	/**
	 * Constructor for a while loop with the specified condition and body.
	 */
    public WhileDoStmt(Integer row, Integer column, Expn condition, Stmt body) {
    	super(condition, body);
        this.setCoordinates(row, column);
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

            // Generate Condition
            short startAddress = scope.enterLoop(CompileTime.instructionCounter);
            condition.generate();
            emit(Machine.PUSH);
            short breakAddress = placeholder();
            emit(Machine.BF);

            // Generate Stmts
            minorScope.generateStatements();

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
            condition.generate();
            emit(Machine.PUSH);
            short breakAddress = placeholder();
            emit(Machine.BF);
            body.generate();
            emit(Machine.PUSH, startAddress);
            emit(Machine.BR);
            fill(breakAddress, CompileTime.instructionCounter);
        }
    }

    /**
     * Print a description of the <b>while-do</b> construct.
     * 
     * @param out:    Where to print the description.
     * @param depth:  How much indentation to use while printing.
     */
    @Override
    public void printOn(PrintStream out, int depth) {
        Indentable.printIndentOnLn(out, depth, "while " + this.condition + " do");
        this.body.printOn(out, depth + 1);
        Indentable.printIndentOnLn(out, depth, "End while-do");
    }
}
