package compiler488.ast;

import java.util.LinkedList;
import compiler488.ast.SemanticException;
import compiler488.ast.AST;

public class SemanticExceptionList {
    private static LinkedList <SemanticException> list = new LinkedList<SemanticException> ();

    public static void add(SemanticException e) {
        list.add(e);
    }

    public static void throwExceptions() throws SemanticException {
        if (list.size() > 0) {

            for (SemanticException e : list) {
                try {
                    throw e;
                } catch (SemanticException ex) {
                    System.out.println(ex);
                }
            }

            throw new SemanticException();
        }
    }
}