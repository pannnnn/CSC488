package compiler488.ast.type;

import compiler488.ast.AST;
import compiler488.ast.expn.Expn;

/**
 * A placeholder for types.
 */
public class Type extends AST {
    public static boolean sameType(Expn object1, Expn object2) {
        return (object1.getType().toString().equals(object2.getType().toString()));
    }

    public static boolean sameType(Type t1, Type t2) {
        return (t1.toString().equals(t2.toString()));
    }
}
