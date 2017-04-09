package compiler488.ast.expn;

import compiler488.ast.Printable;

import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;

import compiler488.ast.CompileTime;
import compiler488.ast.CodeGenerationException;


/**
 * Represents a literal text constant.
 */
public class TextConstExpn extends ConstExpn implements Printable {
	
    private String value;  // The value of this literal.

    public TextConstExpn(Integer row, Integer column, String value) {
        this.setCoordinates(row, column);
        this.value = value;
    }

    /** Returns a description of the literal text constant. */
    @Override
    public String toString() {
        return "\"" + this.value + "\"";
    }

    // ================================================================
    // Code Generation
    // ================================================================
    public void generate() throws CodeGenerationException {
        for (char c: value.toCharArray()) {
            emit(Machine.PUSH, (short) c);
            emit(Machine.PRINTC);
        }
    }

    // ================================================================
    // Getters and Setters
    // ================================================================

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
