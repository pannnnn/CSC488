package compiler488.ast;

import compiler488.ast.Checkable;
import compiler488.ast.SemanticException;
import compiler488.ast.expn.ConditionalExpn;
import compiler488.ast.expn.Expn;
import compiler488.ast.expn.IdentExpn;
import compiler488.ast.expn.SubsExpn;
import compiler488.ast.stmt.ExitStmt;
import compiler488.ast.stmt.Scope;
import compiler488.ast.stmt.ReturnStmt;
import compiler488.ast.stmt.ProcedureCallStmt;
import compiler488.ast.decl.ArrayDeclPart;
import compiler488.ast.decl.Declaration;
import compiler488.ast.decl.DeclarationPart;
import compiler488.ast.decl.MultiDeclarations;
import compiler488.ast.decl.RoutineDecl;
import compiler488.ast.decl.ScalarDecl;
import compiler488.ast.decl.ScalarDeclPart;
import compiler488.ast.type.BooleanType;
import compiler488.ast.type.IntegerType;
import compiler488.ast.type.Type;

import compiler488.symbol.Symbol;
import compiler488.symbol.SymbolTable;
import compiler488.symbol.ArraySymbol;
import compiler488.symbol.VariableSymbol;
import compiler488.symbol.FunctionSymbol;
import compiler488.symbol.ParameterSymbol;
import compiler488.symbol.ProcedureSymbol;

import java.util.ArrayList;

import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;

/**
 * This is a placeholder at the top of the Abstract Syntax Tree hierarchy. It is
 * a convenient place to add common behavior.
 * 
 * Authors of placeholder:
 * @author  Dave Wortman, Marsha Chechik, Danny House
 */
public class AST implements Checkable, Generatable {

    public final static String version = "Winter 2017";

    public Integer row, column;

    SemanticException exception;

    public Scope scope;

    public String getCoordinates() {
        String st = "";
        if (this.row >= 0) {     // Check line number
            st += "in line " + (this.row + 1);
            if (this.column >= 0)  // Check column number.
                st += ", column " + (this.column + 1);
        } else
           st += "at end of input " ;
        return st + ": ";
    }

    protected void setCoordinates(Integer row, Integer column) {
        this.row = row;
        this.column = column;
    }

    // ================================================================
    // Code Generation
    // ================================================================

    public void generate() throws CodeGenerationException {}

    protected void emit(Short instruction) throws CodeGenerationException {
        try {
            Machine.writeMemory(CompileTime.instructionCounter, instruction);
            CompileTime.instructionCounter ++;
        } catch (MemoryAddressException e) {
            throw new CodeGenerationException();
        }
    }

    protected void emit(Short instruction, Short arg) throws CodeGenerationException {
        try {
            Machine.writeMemory(CompileTime.instructionCounter, instruction);
            CompileTime.instructionCounter ++;
            Machine.writeMemory(CompileTime.instructionCounter, arg);
            CompileTime.instructionCounter ++;
        } catch (MemoryAddressException e) {
            throw new CodeGenerationException();
        }
    }

    protected void emit(Short instruction, Short arg, Short on) throws CodeGenerationException {
        try {
            Machine.writeMemory(CompileTime.instructionCounter, instruction);
            CompileTime.instructionCounter ++;
            Machine.writeMemory(CompileTime.instructionCounter, arg);
            CompileTime.instructionCounter ++;
            Machine.writeMemory(CompileTime.instructionCounter, on);
            CompileTime.instructionCounter ++;
        } catch (MemoryAddressException e) {
            throw new CodeGenerationException();
        }
    }

    public static void fillInstructionCountLater(short value) {
        CompileTime.fillInIC.add(new Addr(CompileTime.instructionCounter, value));
        CompileTime.instructionCounter ++;
    }

    public short placeholder() {
        return CompileTime.instructionCounter ++;
    }

    public static void fill(Short location, Short value) throws CodeGenerationException {
        if (value != null && location != null) {
            try {
                Machine.writeMemory(location, value);
            } catch (MemoryAddressException e) {
                throw new CodeGenerationException();
            }
        }
    }

    public static void fillAll() throws CodeGenerationException {
        try {
            for (Addr addr : CompileTime.fillInIC) {
                Machine.writeMemory(addr.location, (short) (addr.value + CompileTime.instructionCounter));
            }
        } catch (MemoryAddressException e) {
            throw new CodeGenerationException();
        }
    }

    // ================================================================
    // Checking
    // ================================================================

    @Override
    public void check(Scope scope, int loopCount) throws SemanticException {}
    
    /**
     * Sets result type to the given type.
     * In place of S20-28
     * 
     * S20: Set result type to boolean.
     * S21: Set result type to integer.
     * S23: Set result type to type of expression.
     * S24: Set result type to type of conditional expressions.
     */
    public void setResultType(Expn expn, Type type) throws SemanticException {
        if (expn == null) {
            exception = new SemanticException(this, "Cannot set result type: expression points to null");
            SemanticExceptionList.add(exception);
        }
        
        if (type == null) {
            exception = new SemanticException(this, "Cannot set result type: type is null");
            SemanticExceptionList.add(exception);
        }
        
        expn.setType(type);
    }
    
    /**
     * Checks if the given name exists in the symbol table.
     */
    public void nameInScope(Scope scope, String name) throws SemanticException {
        if (!scope.contains(name)) {
            exception = new SemanticException(this, name + " not defined in the current scope.");
            SemanticExceptionList.add(exception);
        } 
    }

    /**
     * Declares a routine with or without parameters.
     * Used for S11, S12, S17, S18
     */
    private void declareRoutine(Scope parentScope, Declaration routine) throws SemanticException {
        parentScope.getSymbolTable().addSymbol(routine);  // Adding function into the symbol table of the parent scope.
    }
    
    /**
     * Declares an array with specified upper and lower bounds.
     */
    private void declareArray(Scope parentScope, ArrayDeclPart a, Type type) throws SemanticException {
        parentScope.getSymbolTable().addSymbol(a, type);  // type is set later in S47
    }

    // ================================================================
    // All semantic checks
    // ================================================================

    /**
     * Start program scope.
     */
    public void checkS00(Scope mainScope) throws SemanticException {
        mainScope.initializeSymbolTable();
    } 

    /**
     * End program scope.
     */
    public void checkS01() throws SemanticException {
        SemanticExceptionList.throwExceptions();
    } 

    /**
     * Associate declaration(s) with scope
     */
    public void checkS02(Scope scope, ASTList<Declaration> declarations) throws SemanticException {
        for (Declaration decl : declarations.getLL()) {
            scope.getSymbolTable().addSymbol(decl);
        }
    } 
    
    /**
     * Start function scope.
     */
    public void checkS04(Scope scope, Scope parentScope) throws SemanticException {
        scope.initializeSymbolTable(parentScope);  // initialize new symbol table and inherit everything from parent scope
    }
    
    /**
     * NOTE REQUIRED BY OUR IMPLEMENTATION OF THE SYMBOL TABLE
     * End function scope.
     */
    public void checkS05() throws SemanticException {}

    /**
     * Start ordinary scope.
     */
    public void checkS06(Scope scope, Scope parentScope) throws SemanticException {
        scope.initializeSymbolTable(parentScope);  // initialize new symbol table and inherit everything from parent scope
    }
    
    /**
     * NOTE REQUIRED BY OUR IMPLEMENTATION OF THE SYMBOL TABLE
     * End ordinary scope.
     */
    public void checkS07() throws SemanticException {}

    /**
     * Start procedure scope.
     */
    public void checkS08(Scope scope, Scope parentScope) throws SemanticException {
        this.checkS04(scope, parentScope);  // does the same thing
    }

    /**
     * NOTE REQUIRED BY OUR IMPLEMENTATION OF THE SYMBOL TABLE
     * End procedure scope.
     */
    public void checkS09() throws SemanticException {}
    
    /**
     * Declare scalar variable.
     */
    public void checkS10(AST decl) throws SemanticException {
        if (decl instanceof ScalarDecl) {
            SymbolTable st = ((ScalarDecl) decl).getScope().getSymbolTable();
            st.addSymbol((ScalarDecl) decl);
        } else if (decl instanceof ScalarDeclPart) {
            SymbolTable st = ((ScalarDeclPart) decl).getScope().getSymbolTable();
            st.addSymbol((ScalarDeclPart) decl, null);
        }
    }

    /**
     * Declare a function with NO parameters and specified type.
     */
    public void checkS11(Scope scope, Declaration declaration) throws SemanticException {
        this.declareRoutine(scope, declaration);
    }
    
    /**
     * Declare a function with parameters and specified type.
     */
    public void checkS12(Scope scope, Declaration declaration) throws SemanticException {
        this.declareRoutine(scope, declaration);
    }
    
    /**
     * Associate scope with function/procedure.
     */
    public void checkS13(Scope scope, RoutineDecl routine, ASTList<ScalarDecl> declarations) throws SemanticException {
        scope.setEnclosingRoutine(routine);                // set enclosing function/procedure
        for (ScalarDecl decl : declarations.getLL()) {     // add passed in parameters into the scope so they are visible
            scope.getSymbolTable().addSymbol(decl);
        }
    }

    /**
     * NOT REQUIRED SINCE WE'RE USING A FOR LOOP INSTEAD.
     * Set parameter count to zero.
     */
    public void checkS14() throws SemanticException {}
    
    /**
     * Declare a parameter with the specified type.
     */
    public void checkS15(ScalarDecl parameter) throws SemanticException {
        SymbolTable st = parameter.getScope().getSymbolTable();
        st.addSymbol((Declaration) parameter);
    }
    
    /**
     * NOT REQUIRED SINCE WE'RE USING A FOR LOOP INSTEAD.
     * Increment parameter count by one.
     */
    public void checkS16() throws SemanticException {}
    
    /**
     * Declare a procedure with NO parameters.
     */
    public void checkS17(Scope scope, Declaration declaration) throws SemanticException {
        this.declareRoutine(scope, declaration);
    }
    
    /**
     * Declare a procedure with parameters.
     */
    public void checkS18(Scope scope, Declaration declaration) throws SemanticException {
        this.declareRoutine(scope, declaration);
    }  

    /**
     * Declare an array variable with specified lower and upper bounds.
     */
    public void checkS19(Scope parentScope, ArrayDeclPart a) throws SemanticException {
        this.declareArray(parentScope, a, null);
    }
    
    /**
     * Set result type to boolean.
     */
    public void checkS20(Expn expn) throws SemanticException {
        this.setResultType(expn, new BooleanType());
    }
   
    /**
     * Set result type to integer.
     */
    public void checkS21(Expn expn) throws SemanticException {
        this.setResultType(expn, new IntegerType());
    }    
   
    /**
     * Set result type to type of expression.
     */
    public void checkS23(Expn expn) throws SemanticException {
        this.setResultType(expn, expn.getType());
    }
    
    /**
     * Set result type to type of conditional expressions.
     */
    public void checkS24(Expn expn) throws SemanticException {
        if (expn instanceof ConditionalExpn) {
            this.setResultType(expn, ((ConditionalExpn) expn).getTrueValue().getType());
        } else {
            exception = new SemanticException(this, "Cannot set type: not a conditional expression");
            SemanticExceptionList.add(exception);
        }
    }
    
    /**
     * Set result type to type of parameter name.
     */
    public void checkS25(Declaration decl, String ident) throws SemanticException {
        Symbol symbol = decl.getScope().getSymbolTable().get(ident, this);
        
        // Retrieving array type from symbol table given its identifier.
        if (symbol instanceof ParameterSymbol) {
            decl.setType(((ParameterSymbol) symbol).getType());
        } else {
            exception = new SemanticException(this, ident + " is not declared as a parameter.");
            SemanticExceptionList.add(exception);
        }
    }
    
    /**
     * Set result type to type of variable name.
     */
    public void checkS26(Declaration decl, String ident) throws SemanticException {
        Symbol symbol = decl.getScope().getSymbolTable().get(ident, this);
        
        // Retrieving array type from symbol table given its identifier.
        if (symbol instanceof VariableSymbol) {
            decl.setType(((VariableSymbol) symbol).getType());
        } else {
            exception = new SemanticException(this, ident + " is not declared as a variable.");
            SemanticExceptionList.add(exception);
        }
    }    

    /**
     * Set result type to type of array element.
     */
    public void checkS27(SubsExpn expn) throws SemanticException {
        Symbol symbol = expn.getScope().getSymbolTable().get(expn.getVariable(), this);
        expn.setType(symbol.getType());
    }
    
    /**
     *  Set result type to result type of function.
     */
    public void checkS28(Expn expn, String ident) throws SemanticException {
        Symbol symbol = expn.getScope().getSymbolTable().get(ident, this);
        
        // Retrieving function type from symbol table given its identifier.
        if (symbol instanceof FunctionSymbol)
            expn.setType(((FunctionSymbol) symbol).getType());
        else {
            exception = new SemanticException(this, ident + " is not a function.");
            SemanticExceptionList.add(exception);
        }
    }
    
    /**
     * Checks that the type of the expression is boolean.
     */
    public void checkS30(Expn expn) throws SemanticException {
        if (!(expn.getType() instanceof BooleanType)) {
            exception = new SemanticException(this, "Expression type " + expn.getType().toString() + " is not boolean.");
            SemanticExceptionList.add(exception);
        }
    }
    
    /**
     * Checks that the type of the expression is integer.
     */
    public void checkS31(Expn expn) throws SemanticException {
        if (!(expn.getType() instanceof IntegerType)) {
            exception = new SemanticException(this, "Expression type " + expn.getType().toString() + " is not integer.");
            SemanticExceptionList.add(exception);
        }
    }
    
    /**
     * Checks that left and right operand expressions are the same type.
     */
    public void checkS32(Expn expn1, Expn expn2) throws SemanticException {
        if (!(Type.sameType(expn1, expn2))) {
            exception = new SemanticException(this, "Type mismatch");
            SemanticExceptionList.add(exception);
        }
    }    

    /**
     * Checks that both result expressions in conditional are the same type.
     */
    public void checkS33(Expn expn1, Expn expn2) throws SemanticException {
        this.checkS32(expn1, expn2);
    }    
    
    /**
     * Checks whether or not the left and the right side have matching types.
     */
    public void checkS34(Expn expn1, Expn expn2) throws SemanticException {
        if (!(Type.sameType(expn1, expn2))) {
            exception = new SemanticException(this, "Can't perform assignment, " + expn1.getType() + " does not match with " + expn2.getType()); 
            SemanticExceptionList.add(exception);
        }
    }
    
    /**
     * Checks if the expression type matches the return type of enclosing function.
     */
    public void checkS35(Expn returnValue, RoutineDecl enclosingFunction) throws SemanticException {
        if (!(returnValue.getType().toString().equals(enclosingFunction.getType().toString()))) {
            exception = new SemanticException(this, "Type of return value " + returnValue.getType().toString() + " does not match function type " + enclosingFunction.getType().toString());
            SemanticExceptionList.add(exception);
        }
    }
    
    /**
     * Checks that the type of argument expression matches the type of corresponding formal parameter.
     */
    public void checkS36(Scope scope, String ident, ASTList<Expn> arguments) throws SemanticException {
        Symbol symbol = scope.getSymbolTable().get(ident, this);
        ASTList<ScalarDecl> parameters = ((FunctionSymbol) symbol).getParameters();
        
        // Retrieving the types of parameters of the called function from symbol table given its identifier.        
        for (int i = 0; i < arguments.size(); i++) {
            if (!(Type.sameType(parameters.get(i).getType(), arguments.get(i).getType()))){
               throw new SemanticException(this, "Type of parameters of " + ident + " does not match type of given arguments.");
            }
        }
    }
    
    /**
     * Checks that identifier has been declared as a scalar variable.
     */
    public void checkS37(IdentExpn ident) throws SemanticException {
        SymbolTable st = ident.getScope().getSymbolTable();
        if (!(st.get(ident.getIdent(), this) instanceof VariableSymbol)) {
            throw new SemanticException(this, "Identifier " + ident.getIdent() + " has not been declared as a scalar variable");
        }
    }

    /**
     * Checks that identifier has been declared as an array
     */
    public void checkS38(SubsExpn array) throws SemanticException {
        SymbolTable st = array.getScope().getSymbolTable();
        if (!(st.get(array.getVariable(), this) instanceof ArraySymbol)) {
            throw new SemanticException(this, "Identifier " + array.getVariable() + " has not been declared as an array");
        }
    }
    
    /**
     * Checks that the identifier has been declared as a parameter.
     */
    public void checkS39(IdentExpn ident) throws SemanticException {
        SymbolTable st = ident.getScope().getSymbolTable();
        if (!(st.get(ident.getIdent(), this) instanceof ParameterSymbol)) {
            throw new SemanticException(this, "Identifier " + ident.getIdent() + " has not been declared as parameter");
        }
    }
    
    /**
     * Checks that identifier has been declared as a function.
     */
    public void checkS40(Expn expn, String ident) throws SemanticException {
        SymbolTable st = expn.getScope().getSymbolTable();
        if (!(st.get(ident, this) instanceof FunctionSymbol)) {
            throw new SemanticException(this, "Identifier " + ident + " has not been declared as a function");
        }
    }

    /**
     * Checks that identifier has been declared as a procedure.
     */
    public void checkS41(ProcedureCallStmt call) throws SemanticException {
        SymbolTable st = call.getScope().getSymbolTable();
        if (!(st.get(call.getName(), this) instanceof ProcedureSymbol)) {
            exception = new SemanticException(this, "Identifier " + call.getName() + " has not been declared as a procedure");
            SemanticExceptionList.add(exception);
        }
    }

    /**
     * Checks that the function or procedure has no parameters.
     */
    public void checkS42(String ident, Scope scope) throws SemanticException {
        Symbol symbol = scope.getSymbolTable().get(ident, this);
        
        // Retrieving the procedure from the symbol table.
        if (symbol instanceof ProcedureSymbol) {
            if (((ProcedureSymbol) symbol).getParameterCount() != 0) {
                throw new SemanticException(this, ident + " requires " + ((ProcedureSymbol) symbol).getParameterCount() + " parameters, but was called with none");
            }
        } else if (symbol instanceof FunctionSymbol) {
            if (((FunctionSymbol) symbol).getParameterCount() != 0) {
                throw new SemanticException(this, ident + " requires " + ((FunctionSymbol) symbol).getParameterCount() + " parameters, but was called with none");
            }
        } else {
            throw new SemanticException(this, ident + " is not callable as a function/procedure");
        }
    }
    
    /**
     * Checks that the number of arguments is equal to the number of formal parameters.
     */
    public void checkS43(Scope scope, String ident, int parameterCount) throws SemanticException {
        Symbol symbol = scope.getSymbolTable().get(ident, this);
        // Retrieving number of parameters of the called function from symbol table given its identifier.
        if (symbol instanceof ProcedureSymbol) {
            if (((ProcedureSymbol) symbol).getParameterCount() != parameterCount) {
                throw new SemanticException(this, ident + " requires " + ((ProcedureSymbol) symbol).getParameterCount() + " arguments but you have " + parameterCount + ".");
            }
        } else if (symbol instanceof FunctionSymbol) {
            if (((FunctionSymbol) symbol).getParameterCount() != parameterCount) {
                throw new SemanticException(this, ident + " requires " + ((FunctionSymbol) symbol).getParameterCount() + " arguments but you have " + parameterCount + ".");            
            }
        } else {
            throw new SemanticException(this, ident + " is not callable as a function/procedure.");
        }
    }
    
    /**
     * NOT REQUIRED SINCE WE'RE USING A FOR LOOP INSTEAD.
     * Sets the argument count to 0.
     */
    public void checkS44() throws SemanticException {}
    
    /**
     * NOT REQUIRED SINCE WE'RE USING A FOR LOOP INSTEAD.
     * Increments the argument count by one.
     */
    public void checkS45() throws SemanticException {}
    
    /**
     * Checks that lower bound is <= upper bound.
     */
    public void checkS46(ArrayDeclPart a) throws SemanticException {
        if (a.getLowerBoundary() > a.getUpperBoundary()) {
            exception = new SemanticException(this, "Invalid lower bound for " + a.getName());
            SemanticExceptionList.add(exception);
        }
    }
    
    /**
     * Associate type with variables.
     */
    public void checkS47(MultiDeclarations md) throws SemanticException {
        SymbolTable st = md.getScope().getSymbolTable();
        for (DeclarationPart element : md.getElements().getLL()) {
            st.get(element.getName(), this).setType(md.getType());
        }
    }
    
    /**
     * Declare an array variable with the specified upper bound.
     */
    public void checkS48(Scope parentScope, ArrayDeclPart a) throws SemanticException {
        this.declareArray(parentScope, a, null);
    }
    
    /**
     * Checks that the exit statement is inside a loop.
     */
    public void checkS50(ExitStmt e) throws SemanticException {
        if (e.loopCount == 0) {
            exception = new SemanticException(this, "Exit statements must be inside a loop");
            SemanticExceptionList.add(exception);
        }
    }
    
    /**
     * Checks that the return statement is inside a function.
     */
    public boolean checkS51(ReturnStmt r) throws SemanticException {

        RoutineDecl enclosingRoutine = r.getScope().getEnclosingRoutine();

        if (enclosingRoutine == null) {
            exception = new SemanticException(this, "Return with statements must be inside a function");
            SemanticExceptionList.add(exception);
            return false;
        } else if (enclosingRoutine.getType() == null) {
            exception = new SemanticException(this, "Return with statements must be inside a function");
            SemanticExceptionList.add(exception);
            return false;
        }
        return true;
    }
    
    /**
     * Checks that the return statement is inside a procedure.
     */
    public void checkS52(ReturnStmt r) throws SemanticException {

        RoutineDecl enclosingRoutine = r.getScope().getEnclosingRoutine();

        if (enclosingRoutine == null) {
            exception = new SemanticException(this, "Return statements must be inside a procedure");
            SemanticExceptionList.add(exception);
        } else if (enclosingRoutine.getType() != null) {
            exception = new SemanticException(this, "Return statements must be inside a procedure");
            SemanticExceptionList.add(exception);
        }
    }
    
    /**
     * Checks that the exit level is > 0 and <= the number of containing loops.
     */
    public void checkS53(ExitStmt e) throws SemanticException {
        if (e.getLevel() <= 0) {
            exception = new SemanticException(this, "Exit level is has to be greater or equals to zero");
            SemanticExceptionList.add(exception);
        }        
        if (e.getLevel() > e.loopCount) {
            exception = new SemanticException(this, "Exit level exceeds loop count");
            SemanticExceptionList.add(exception);
        }
    }

    // ================================================================
    // Custom semantic checks
    // ================================================================

    /**
     * Checks if the expression is assignable.
     */
    public void checkS60(Expn expn) throws SemanticException {
        if (expn instanceof SubsExpn) {
            return;
        } else if (expn instanceof IdentExpn) {
            try {
                expn.checkS37((IdentExpn) expn);
            } catch (SemanticException e) {
                exception = new SemanticException(this, "Can only assign variables or array entries.");
                SemanticExceptionList.add(exception);
            }
        } else {
            exception = new SemanticException(this, "Can only assign variables or array entries.");
            SemanticExceptionList.add(exception);
        }
    }

    public Scope getScope() {
        return this.scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }
}
