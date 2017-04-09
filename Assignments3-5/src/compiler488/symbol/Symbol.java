package compiler488.symbol;

import compiler488.ast.type.Type;

/** Symbol
 *  This is a class for symbols stored inside symbol table.
 *
 *  @author  <B> Jingcheng Niu, Wen Hao Zhang, Yuhui Pan, Dmitry Kirillovich Kubikov </B>
 */

public abstract class Symbol {

    /** String used by Main to print symbol table
     *  version information.
     */

    public final static String version = "Winter 2017" ;

    protected String name;
    private Type type;
    public boolean inherited = false;

    public Symbol(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    // ================================================================
    // Getters and Setters
    // ================================================================

    public String getName() {
        return this.name;
    }

    public String toString() {
        return "(" +this.getClass().getName().replaceAll(".*\\.|Symbol", "") + ") " + this.name + " : " + type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
