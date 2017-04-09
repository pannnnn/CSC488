package compiler488.symbol;

import compiler488.ast.decl.RoutineBody;
import compiler488.ast.decl.RoutineDecl;
import compiler488.ast.decl.ScalarDecl;
import compiler488.ast.ASTList;
import compiler488.ast.type.Type;

/** RoutineSymbol
 *  This is a class for symbols stored inside symbol table.
 *
 *  @author  <B> Jingcheng Niu, Wen Hao Zhang, Yuhui Pan, Dmitry Kirillovich Kubikov </B>
 */

public class RoutineSymbol extends Symbol {

    protected RoutineBody body;
    protected ASTList<ScalarDecl> parameters;
    public short codeAddress;

    public RoutineSymbol(String name, Type type) {
        super(name, type);
    }

    public ASTList<ScalarDecl> getParameters() {
        return parameters;
    }

    public int getParameterCount() {
        return parameters.size();
    }

    public RoutineBody getBody() {
        return body;
    }

    public void setBody(RoutineBody body) {
        this.body = body;
    }

    public String toString() {
        return "(" +this.getClass().getName().replaceAll(".*\\.|Symbol", "") + ") " + this.name + ":" + this.body.scope + ", " + codeAddress;
    }
}