package compiler488.symbol;

import compiler488.ast.decl.RoutineBody;
import compiler488.ast.decl.RoutineDecl;
import compiler488.ast.decl.ScalarDecl;
import compiler488.ast.ASTList;

/** ProcedureSymbol
 *  This is a class for symbols stored inside symbol table.
 *
 *  @author  <B> Jingcheng Niu, Wen Hao Zhang, Yuhui Pan, Dmitry Kirillovich Kubikov </B>
 */

public class ProcedureSymbol extends RoutineSymbol {

    /** Variable Symbol constructor
     *  Create and initialize a symbol table 
     */
    public ProcedureSymbol (RoutineDecl decl) {
    	super(decl.getName(), null);
        this.setBody(decl.getRoutineBody());
        this.parameters = decl.getParameters();
    }
}