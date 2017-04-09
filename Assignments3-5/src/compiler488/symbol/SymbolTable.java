package compiler488.symbol;

import java.util.HashMap;
import java.util.Map;

import compiler488.ast.SemanticException;

import compiler488.ast.type.Type;
import compiler488.symbol.ArraySymbol;

import compiler488.ast.decl.Declaration;
import compiler488.ast.decl.RoutineDecl;
import compiler488.ast.decl.DeclarationPart;
import compiler488.ast.decl.ArrayDeclPart;
import compiler488.ast.decl.ScalarDeclPart;
import compiler488.ast.decl.ScalarDecl;
import compiler488.ast.decl.MultiDeclarations;
import compiler488.ast.AST;
import compiler488.ast.ASTList;
import compiler488.ast.SemanticExceptionList;
import compiler488.ast.expn.Expn;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;

/** Symbol Table
 *  This almost empty class is a framework for implementing
 *  a Symbol Table class for the CSC488S compiler
 *  
 *  Each implementation can change/modify/delete this class
 *  as they see fit.
 *
 *  @author  <B> Jingcheng Niu, Wen Hao Zhang, Yuhui Pan, Dmitry Kirillovich Kubikov </B>
 */

public class SymbolTable {
    
    /** String used by Main to print symbol table
     *  version information.
     */

    public final static String version = "Winter 2017" ;

    public Map<String, Pair> table;
    public short offsetCount = 0;

    SemanticException exception;

    /** Symbol Table  constructor
     *  Create and initialize a symbol table 
     */
    public SymbolTable() {
        table = new HashMap<String, Pair> ();
    }

    public SymbolTable(SymbolTable symbolTable) {
        table = new HashMap<String, Pair> ();

        for (Object pair : symbolTable.getTable().values()) {
            Symbol symbol = ((Pair) pair).symbol;
            table.put(symbol.getName(), new Pair(symbol, true));
        }
    }

    public void setParameters(ASTList<Expn> parameterValues) {
        ArrayList<Pair> pairs = new ArrayList();
        for (Object p : table.values()) {
            if (((Pair) p).symbol instanceof ParameterSymbol) {
                pairs.add((Pair) p);
            }
        }
        for (int i=0; i<parameterValues.size(); i++) {
            ((ParameterSymbol) pairs.get(i).symbol).value = parameterValues.get(i);
        }
    }

    public boolean contains(String name) {
        return table.containsKey(name);
    }

    public ArrayList<RoutineSymbol> getNewRoutines() {
        ArrayList<RoutineSymbol> routines = new ArrayList();
        for (Object p : table.values()) {
            if (!((Pair) p).inherited) {
                if (((Pair) p).symbol instanceof RoutineSymbol) {
                    routines.add(((RoutineSymbol) ((Pair) p).symbol));
                }
            }
        }
        return routines;
    }

    public Symbol get(String name, AST ast) throws SemanticException {
        if (contains(name)) {
            return table.get(name).symbol;
        } else {
            throw new SemanticException(ast, "Symbol " + name + " has not been declared yet.");
        }
    }

    public Pair getPair(String name) {
        return table.get(name);
    }

    private void updateOffsetCount(Symbol symbol) {
        if (symbol instanceof ArraySymbol) {
            offsetCount += ((ArraySymbol) symbol).getSize();
        } else {
            offsetCount ++;
        }
    }

    private void put(String name, Symbol symbol) {
        table.put(name, new Pair(symbol, false));
    }

    /** The rest of Symbol Table
     *  Data structures, public and private functions
     *  to implement the Symbol Table.         
     */
    public void addSymbol(Declaration decl) throws SemanticException {
        if (decl instanceof RoutineDecl) {
            if (decl.getType() == null) {
                put(decl.getName(), new ProcedureSymbol((RoutineDecl) decl));
            } else {
                put(decl.getName(), new FunctionSymbol((RoutineDecl) decl));
            }
        } else if (decl instanceof ScalarDecl) {
        	if (((ScalarDecl) decl).isParam()) {
                put(decl.getName(), new ParameterSymbol(decl.getName(), decl.getType()));
        	} else {
        		put(decl.getName(), new VariableSymbol(decl.getName(), decl.getType()));
        	}
        } else if (decl instanceof MultiDeclarations) {
            for (DeclarationPart declPart : ((MultiDeclarations) decl).getElements().getLL()) {
                addSymbol(declPart, decl.getType());
            }
        } else {
            exception = new SemanticException(decl, "Unknown kind of symbol");
            SemanticExceptionList.add(exception);
        }
    }

    public void addSymbol(DeclarationPart decl, Type type) throws SemanticException {
        if (decl instanceof ScalarDeclPart) {
            put(decl.getName(), new VariableSymbol(decl.getName(), type));
        } else if (decl instanceof ArrayDeclPart) {
            put(decl.getName(), new ArraySymbol((ArrayDeclPart) decl, type));
        } else {
            exception = new SemanticException(decl, "Unknown kind of symbol");
            SemanticExceptionList.add(exception);
        }
    }

    public Map getTable() {
        return table;
    }

    public ArrayList<Pair> getPairs() {
        Collection values = new ArrayList<Pair>(table.values());
        values.removeIf(p -> ((Pair) p).symbol instanceof RoutineSymbol);
        ArrayList<Pair> pairs = new ArrayList(values);
        return pairs;
    }

    public ArrayList<Pair> getRawPairs() {
        ArrayList<Pair> pairs = new ArrayList();
        ArrayList<Pair> attrPairs = new ArrayList();
        for (Object p : table.values()) {
            if (!(((Pair) p).symbol instanceof RoutineSymbol)) {
                // pairs.add((Pair) p);
                if (((Pair) p).symbol instanceof ParameterSymbol) {
                    attrPairs.add((Pair) p);
                } else {
                    pairs.add((Pair) p);
                }
            }
        }
        attrPairs.addAll(pairs);
        return attrPairs;
    }

    public String toString() {
        String str = "[\n";
        for (Pair p : table.values()) {
            str += "\t" + p.toString();
        }
        return str + "]";
    }

    public void setOffsets() {
        offsetCount = 0;
        for (Pair p : getRawPairs()) {
            p.offset = offsetCount;
            updateOffsetCount(p.symbol);
        }
    }
}
