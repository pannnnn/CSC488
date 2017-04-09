package compiler488.ast.type;

/**
 * The type of things that may be true or false.
 */
public class BooleanType extends Type {
    static Integer id = 1;

    public BooleanType(Integer row, Integer column) {
        this.setCoordinates(row, column);
    }

    public BooleanType() {}

    /** Returns the string <b>"Boolean"</b>. */
    @Override
    public String toString() {
        return "boolean";
    }
}
