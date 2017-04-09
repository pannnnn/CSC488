package compiler488.symbol;

import compiler488.ast.type.Type;
import compiler488.ast.expn.Expn;

/** VariableSymbol
 *  This is a class for symbols stored inside symbol table.
 *
 *  @author  <B> Jingcheng Niu, Wen Hao Zhang, Yuhui Pan, Dmitry Kirillovich Kubikov </B>
 */

public class ParameterSymbol extends VariableSymbol {

    public Expn value;

    /** Parameter Symbol constructor
     *  Create and initialize a symbol table 
     */
    public ParameterSymbol (String name, Type type) {
    	super(name, type);
    }
}
