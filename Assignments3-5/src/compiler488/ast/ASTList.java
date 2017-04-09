package compiler488.ast;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * For nodes with an arbitrary number of children.
 */
public class ASTList<E> extends AST {
	
    /*
     * Keep the list here. We delegate rather than subclass LinkedList
     * because Java won't let us override the return type for addLast.
     */
    private LinkedList<E> ll;
    public String typeName;

    /**
     * Constructor for an empty list.
     */
    public ASTList() {
        this.ll = new LinkedList<E>();
    }

    /**
     * Constructor for a list with one element.
     */
    public ASTList(E ast) {
        this.ll = new LinkedList<E>();
        this.ll.addLast(ast);
    }

    /**
     * The number of elements in the list.
     */
    public int size() {
        return this.ll.size();
    }

    public E first() {
        return this.ll.get(0);
    }

    public E get(int i) {
        return this.ll.get(i);
    }

    public LinkedList<E> getLL() {
        return this.ll;
    }

    /**
     * Append an element to the list, then return the list. This is a
     * pure-side-effect method, so it doesn't need to return anything.
     * However, we like the conciseness gained when such methods return the
     * target object.
     */
    public ASTList<E> addLast(E ast) {
        this.ll.addLast(ast);
        return this;
    }

    /**
     * Ask each element of the list to print itself using
     * <b>printOn(out,depth)</b>.  This should only be used when the
     * elements are typically printed on seperate lines, otherwise they may
     * not implement <b>printOn</b>. If the list is empty, print
     * <b>&gt;&gt;empty&lt;&lt;</b> follwed by a new-line.
     * 
     * @param out
     *            Where to print the list.
     * @param depth
     *            How much indentation to use while printing.
     */
    public void printOnSeperateLines(PrintStream out, int depth) {
        ListIterator<E> iterator = ll.listIterator();
        if (iterator.hasNext())
            while (iterator.hasNext())
                ((Indentable) iterator.next()).printOn(out, depth);
        else
            Indentable.printIndentOn(out, depth, ">>empty<<\n");
    }

    /**
     * Return the concatenations of the strings obtained by sending
     * <b>toString</b> to each element.
     */
    @Override
    public String toString() {
        if (0 == this.ll.size()) {
            return "";
        } else {
            ListIterator<E> iterator = this.ll.listIterator();

            StringBuffer result = new StringBuffer(iterator.next().toString());
            while (iterator.hasNext())
                result.append(", " + iterator.next());

            return result.toString();
        }
    }
}
