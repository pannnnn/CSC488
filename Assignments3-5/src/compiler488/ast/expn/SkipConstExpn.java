package compiler488.ast.expn;

import compiler488.ast.Printable;

import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;

import compiler488.ast.CompileTime;
import compiler488.ast.CodeGenerationException;

/**
 * Represents the special literal constant associated with writing a new-line
 * character on the output device.
 */
public class SkipConstExpn extends ConstExpn implements Printable {

    public SkipConstExpn(Integer row, Integer column) {
        this.setCoordinates(row, column);
    }

    // ================================================================
    // Code Generation
    // ================================================================
    public void generate() throws CodeGenerationException {
        emit(Machine.PUSH, (short) System.lineSeparator().charAt(0));
        emit(Machine.PRINTC);
    }

    /** Returns the string <b>"skip"</b>. */
    @Override
    public String toString() {
        return " newline ";
    }
}
