package compiler488.symbol;

import compiler488.ast.type.Type;

/** VariableSymbol
 *  This is a class for symbols stored inside symbol table.
 *
 *  @author  <B> Jingcheng Niu, Wen Hao Zhang, Yuhui Pan, Dmitry Kirillovich Kubikov </B>
 */

public class VariableSymbol extends Symbol {

    /** Variable Symbol constructor
     *  Create and initialize a symbol table 
     */
    public VariableSymbol (String name, Type type) {
    	super(name, type);
    }
}
