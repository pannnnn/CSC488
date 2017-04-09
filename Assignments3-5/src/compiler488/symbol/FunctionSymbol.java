package compiler488.symbol;

import compiler488.ast.decl.RoutineBody;
import compiler488.ast.decl.RoutineDecl;
import compiler488.ast.decl.ScalarDecl;
import compiler488.ast.ASTList;

/** FunctionSymbol
 *  This is a class for symbols stored inside symbol table.
 *
 *  @author  <B> Jingcheng Niu, Wen Hao Zhang, Yuhui Pan, Dmitry Kirillovich Kubikov </B>
 */

public class FunctionSymbol extends RoutineSymbol {

    /** Variable Symbol constructor
     *  Create and initialize a symbol table 
     */
    public FunctionSymbol (RoutineDecl decl) {
    	super(decl.getName(), decl.getType());
        this.setBody(decl.getRoutineBody());
        this.parameters = decl.getParameters();
    }
}