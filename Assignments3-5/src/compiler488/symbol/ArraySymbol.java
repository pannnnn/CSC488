package compiler488.symbol;

import compiler488.ast.type.Type;

import compiler488.ast.decl.ArrayDeclPart;

/** ArraySymbol
 *  This is a class for symbols stored inside symbol table.
 *
 *  @author  <B> Jingcheng Niu, Wen Hao Zhang, Yuhui Pan, Dmitry Kirillovich Kubikov </B>
 */

public class ArraySymbol extends Symbol {

    private Integer lb, ub;  // The lower and upper boundaries of the array.
    private Short size;    // The number of objects the array holds.

    /** Variable Symbol constructor
     *  Create and initialize a symbol table 
     */
    public ArraySymbol (ArrayDeclPart decl, Type type) {
        super(decl.getName(), type);
        this.setLb(decl.getLowerBoundary());
        this.setUb(decl.getUpperBoundary());
        this.setSize(decl.getSize());
    }

    // ================================================================
    // Getters and Setters
    // ================================================================

    public Short getSize() {
        return size;
    }

    public void setSize(Short size) {
        this.size = size;
    }

    public short getLb() {
        return lb.shortValue();
    }

    public void setLb(Integer lb) {
        this.lb = lb;
    }

    public void setUb(Integer ub) {
        this.ub = ub;
    }
}
