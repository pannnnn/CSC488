package compiler488.ast;

import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;

import compiler488.ast.stmt.Scope;

import java.util.ArrayList;

/**
 * A tuple storing the location of instructions and their value before
 * initial program counter added. The address at memeory[location] will
 * be later set to (value + initPC).
 */
class Addr {
    public Short location;
    public Short value;

    public Addr(Short location, Short value) {
        this.location = location;
        this.value = value;
    }
}

/**
 * A class storing all compile time information.
 */
public class CompileTime {

    /* Counting how many instructions are emitted */
    public static short instructionCounter = 0;
    /* A list of addresses need to increment by the initPC */
    public static ArrayList<Addr> fillInIC = new ArrayList<Addr> ();
    /* Counting how many major scopes are created */
    public static short majorScopeCount = 1;

    public static void initMajorScope(Scope scope) {
        scope.isMajor = true;
        scope.lexicalLevel = majorScopeCount ++;
    }

    public static void finish() throws CodeGenerationException {
        try {
            Machine.writeMemory(CompileTime.instructionCounter, Machine.HALT);
            CompileTime.instructionCounter ++;
        } catch (MemoryAddressException e) {
            throw new CodeGenerationException();
        }

        Machine.setPC((short) 0) ;                         /* where code to be executed begins */
        Machine.setMSP(instructionCounter);         /* where memory stack begins */
        Machine.setMLP((short) (Machine.memorySize - 1));
    }
}