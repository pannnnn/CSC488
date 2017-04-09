package compiler488.ast.stmt;

import java.io.PrintStream;
import compiler488.ast.Indentable;
import compiler488.ast.SemanticException;
import compiler488.ast.SemanticExceptionList;

import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;

import compiler488.ast.CompileTime;
import compiler488.ast.CodeGenerationException;

/**
 * Placeholder for the scope that is the entire program
 */
public class Program extends Scope {
	
    public Scope mainScope;

    public Program(Integer row, Integer column, Scope s) {
        this.setCoordinates(row, column);
        s.isMajor = true;
        s.isMain = true;
        this.mainScope = s;
    }

    public void generate() throws CodeGenerationException {
        mainScope.generate();
        fillAll();
        CompileTime.finish();
    }

    /**
     * Print a description of the <b>scope</b> construct.
     * 
     * @param out:    Where to print the description.
     * @param depth:  How much indentation to use while printing.
     */
    @Override
    public void printOn(PrintStream out, int depth) {
        Indentable.printIndentOnLn(out, depth, "Program:");
        this.mainScope.printOn(out, depth + 1);
        Indentable.printIndentOnLn(out, depth, "End Program");
    }

    // ================================================================
    // Checking
    // ================================================================
    
    public void check(Scope scope, int loopCount) throws SemanticException {
        this.checkS00(this.mainScope);
        this.mainScope.check(mainScope, 0);
        this.checkS01();
    }
}
