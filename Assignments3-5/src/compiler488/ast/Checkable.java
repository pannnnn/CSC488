package compiler488.ast;
import compiler488.ast.stmt.Scope;

public interface Checkable {

    /*
     * Classes that extend this interface can be used
     * as arguments to all checkable AST nodes.
     */

    public void check(Scope scope, int loopCount) throws SemanticException;

}
