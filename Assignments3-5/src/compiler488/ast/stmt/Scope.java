package compiler488.ast.stmt;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import compiler488.ast.ASTList;
import compiler488.ast.AST;
import compiler488.ast.Indentable;
import compiler488.ast.SemanticException;
import compiler488.ast.decl.Declaration;
import compiler488.ast.decl.RoutineDecl;
import compiler488.ast.SemanticExceptionList;

import compiler488.symbol.SymbolTable;
import compiler488.symbol.Symbol;
import compiler488.symbol.ArraySymbol;
import compiler488.symbol.RoutineSymbol;
import compiler488.symbol.ParameterSymbol;
import compiler488.symbol.Pair;

import compiler488.ast.type.IntegerType;
import compiler488.ast.type.BooleanType;

import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;

import compiler488.ast.CompileTime;
import compiler488.ast.CodeGenerationException;

/**
 * Represents the declarations and instructions of a scope construct.
 */
public class Scope extends Stmt {

    private ASTList<Declaration> declarations;      // The declarations at the top.
    private ASTList<Stmt> statements;               // The statements to execute.
    private SymbolTable symbolTable;                // The symbol table.
    private RoutineDecl enclosingRoutine = null;    // The routine enclosing.
    public boolean isMajor = false;                 // Is a major scope.
    public boolean isMain = false;                  // Is a main scope.
    public Scope parentScope;                       // Parent scope.
    public Short lexicalLevel = 0;                  // Lexical level of the scope. 0 for minor scopes and 1~31 for major scopes
    public static ArrayList<Loop> loopStack = null; // A stack tracing every loops

    // An array storing addresses need to be fill in current loop's (if any) breaking address
    public ArrayList<Short> fillInBreakAddrLater = new ArrayList<Short> ();

    /**
     * Return the total activation record length of this scope.
     */
    public short getARLength() {
        return (short) (symbolTable.offsetCount + getScopeOffset());
    }

    // ================================================================
    // Loop Stack API
    // ================================================================

    /**
     * Return loop at the top of the stack without removing it.
     */
    public Loop peek() {
        return loopStack.get(loopStack.size() - 1);
    }

    /**
     * Push a new loop into the stack.
     */
    public void push(Loop loop) {
        loopStack.add(0, loop);
    }

    /**
     * Pop the top.
     */
    public Loop pop() {
        Loop top = loopStack.get(0);
        loopStack.remove(0);
        return top;
    }

    // ================================================================
    // Loop Entrance and Exit API
    // ================================================================

    public short enterLoop(short startAddress) {
        peek().isLoop = true;
        return startAddress;
    }

    public Loop exitLoop(int level) {
        int exited = 0;
        while (exited < level) {
            if (peek().isLoop)
                exited++;
            pop();
        }
        return peek();
    }

    // ================================================================
    // Constructors
    // ================================================================

    public Scope() {
        this.declarations = new ASTList<Declaration>();
        this.statements = new ASTList<Stmt>();
        this.loopStack = new ArrayList<Loop> ();
    }

    public Scope(Integer row, Integer column) {
        this.setCoordinates(row, column);
        this.declarations = new ASTList<Declaration>();
        this.statements = new ASTList<Stmt>();
        this.loopStack = new ArrayList<Loop> ();
    }

    public Scope(Integer row, Integer column, ASTList<Declaration> declList, ASTList<Stmt> stmtList) {
        this.setCoordinates(row, column);
        this.declarations = declList;
        this.statements = stmtList;
        this.loopStack = new ArrayList<Loop> ();
    }

    public Scope(Integer row, Integer column, ASTList<Stmt> stmtList) {
        this.setCoordinates(row, column);
        this.declarations = new ASTList<Declaration>();
        this.statements = stmtList;
        this.loopStack = new ArrayList<Loop> ();
    }

    public Scope(Integer row, Integer column, ASTList<Declaration> declList, ASTList<Stmt> stmtList, ArrayList<Loop> loopStack) {
        // Create minor scope, inherits parent's loopStack
        this.setCoordinates(row, column);
        this.declarations = declList;
        this.statements = stmtList;
        this.loopStack = loopStack;
    }

    public Scope(Integer row, Integer column, ASTList<Stmt> stmtList, ArrayList<Loop> loopStack) {
        this.setCoordinates(row, column);
        this.declarations = new ASTList<Declaration>();
        this.statements = stmtList;
        this.loopStack = loopStack;
    }

    // ================================================================
    // Code Generation
    // ================================================================

    /**
     * Generate code to create all the new declared routines.
     */
    public void generateRoutines() throws CodeGenerationException {
        for (RoutineSymbol rs : symbolTable.getNewRoutines()) {
            rs.codeAddress = CompileTime.instructionCounter;
            rs.getBody().generate();
        }
    }

    /**
     * Generate code entering the scope.
     */
    public void generateEnterScope() throws CodeGenerationException {
        // Init activation record
        if (isMain) {
            emit(Machine.PUSHMT);
            emit(Machine.SETD, lexicalLevel);
            emit(Machine.PUSH, Machine.UNDEFINED);
            emit(Machine.PUSH, Machine.UNDEFINED);
        } else if (!isMajor) {
            push(new Loop(CompileTime.instructionCounter, getARLength(), false));
            emit(Machine.ADDR, parentScope.lexicalLevel, (short) 0);
            emit(Machine.PUSH, Machine.UNDEFINED);
        } 
        generateEnterScopeHelper();
    }

    /**
     * Generate code entering the scope. 
     */
    public void generateEnterScopeHelper() throws CodeGenerationException {
        // Create space in activation record for newly declared variables and inherit parent scope's variables
        for (Pair pair : symbolTable.getPairs()) {
            if (pair.inherited) {
                if (pair.symbol instanceof ArraySymbol) {
                    ArraySymbol array = (ArraySymbol) pair.symbol;
                    for (int i=0; i<array.getSize(); i++) {
                        emit(Machine.ADDR, parentScope.lexicalLevel, (short) (parentScope.getOffset(pair.symbol) + parentScope.getScopeOffset() + i));
                        emit(Machine.LOAD);
                    }
                } else {
                    emit(Machine.ADDR, parentScope.lexicalLevel, (short) (parentScope.getOffset(pair.symbol) + parentScope.getScopeOffset()));
                    emit(Machine.LOAD);
                }
            } else {
                if (!(pair.symbol instanceof ParameterSymbol)) {
                    if (pair.symbol.getType() instanceof BooleanType) {
                        emit(Machine.PUSH, Machine.MACHINE_FALSE);
                    } else if (pair.symbol.getType() instanceof IntegerType) {
                        emit(Machine.PUSH, (short) 0);
                    } else {
                        throw new CodeGenerationException(this, "Invalid type");
                    }
                    if (pair.symbol instanceof ArraySymbol) {
                        emit(Machine.PUSH, ((ArraySymbol) pair.symbol).getSize());
                        emit(Machine.DUPN);
                    }
                }
            }
        }

        // Store the address to the beginning of activation record to display
        if (!isMajor) {
            emit(Machine.PUSHMT);
            emit(Machine.PUSH, getARLength());
            emit(Machine.SUB);
            emit(Machine.SETD, lexicalLevel);
        }
    }

    /**
     * Generate every statements inside.
     */
    public void generateStatements() throws CodeGenerationException {
        if (this.statements != null) {
            for (Stmt stmt: this.statements.getLL()) {
                stmt.generate();
            }
        }
    }

    /**
     * Fill in breaking addresses for spots needed.
     */
    public void fillBreakAddresses(short breakAddr) throws CodeGenerationException {
        for (Short addr : fillInBreakAddrLater) {
            fill(addr, breakAddr);
        }
    }

    /**
     * Generate code entering the scope.
     */
    public void generateExitScope() throws CodeGenerationException {
        if (!isMajor) {
            emit(Machine.PUSH, (short) (getARLength() - 1));
            emit(Machine.POPN);
            emit(Machine.SETD, lexicalLevel);
        } else if (isMain) {
            emit(Machine.PUSHMT);
            emit(Machine.PUSH);
            fillInstructionCountLater((short) (1));
            emit(Machine.SUB);
            emit(Machine.POPN);
        } else {
            // Major scopes
            if (enclosingRoutine.getType() == null) {
                // Procedure
                emit(Machine.PUSH, (short) (getARLength() - 2));
                emit(Machine.POPN);
                emit(Machine.SETD, lexicalLevel);
                emit(Machine.BR);
            }
        }
    }

    /**
     * Generate code for the whole scope.
     */
    public void generate() throws CodeGenerationException {
        generateEnterScope();
        if (!symbolTable.getNewRoutines().isEmpty()) {
            emit(Machine.PUSH);
            short afterRoutine = placeholder();
            emit(Machine.BR);
            generateRoutines();
            fill(afterRoutine, CompileTime.instructionCounter);
        }
        generateStatements();
        generateExitScope();
    }

    // ================================================================
    // Symbol Table API
    // ================================================================

    public Symbol getSymbol(String name, AST ast) {
        try {
            return symbolTable.get(name, ast);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean contains(String name) {
        return symbolTable.contains(name);
    }

    public void initializeSymbolTable(Scope parentScope) {
        this.parentScope = parentScope;
        if (symbolTable == null)
            symbolTable = new SymbolTable(parentScope.getSymbolTable());
    }

    public void initializeSymbolTable() {
        if (symbolTable == null)
            symbolTable = new SymbolTable();
    }

    public short getOffset(Symbol symbol) {
        Pair pair = symbolTable.getPair(symbol.getName());
        return pair.offset;
    }

    /**
     * Print a description of the <b>scope</b> construct.
     * 
     * @param out:    Where to print the description.
     * @param depth:  How much indentation to use while printing.
     */
    @Override
    public void printOn(PrintStream out, int depth) {
        Indentable.printIndentOnLn(out, depth, toString());

        Indentable.printIndentOnLn(out, depth, "declarations");

        this.declarations.printOnSeperateLines(out, depth + 1);

        Indentable.printIndentOnLn(out, depth, "statements");

        this.statements.printOnSeperateLines(out, depth + 1);

        Indentable.printIndentOnLn(out, depth, "End Scope");
    }
    
    // ================================================================
    // Checking
    // ================================================================

    public void check(Scope scope, int loopCount) throws SemanticException {
        this.loopCount = loopCount;
        this.inFunction = inFunction;
        this.inProcedure = inProcedure;
        try {
            this.checkS06(this, scope);
            if (scope.getEnclosingRoutine() != null)
                this.setEnclosingRoutine(scope.getEnclosingRoutine());
            
            if (this.declarations != null) {
                for (Declaration decl: this.declarations.getLL()) {
                    decl.check(this, loopCount);
                }
            }

            if (this.statements != null) {
                for (Stmt stmt: this.statements.getLL()) {
                    stmt.check(this, loopCount);
                }   
            }
            symbolTable.setOffsets();
        } catch (SemanticException e) {
            SemanticExceptionList.add(e);
        }
    }
    
    // ================================================================
    // Getters and Setters
    // ================================================================

    public ASTList<Declaration> getDeclarations() {
        return this.declarations;
    }

    public ASTList<Stmt> getStatements() {
        return this.statements;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public RoutineDecl getEnclosingRoutine() {
        return this.enclosingRoutine;
    }

    public void setEnclosingRoutine(RoutineDecl enclosingRoutine) {
        this.enclosingRoutine = enclosingRoutine;
    }

    public short getScopeOffset() {
        if (isMain) return 2;
        else if (isMajor) return 3;
        else return 2;
    }

    // ================================================================
    // To String method
    // ================================================================

    public String toString() {
        return "{Scope " + lexicalLevel + super.toString() + "}";
    }
}
