package compiler488.ast.decl;

import java.io.PrintStream;

import compiler488.ast.ASTList;
import compiler488.ast.Indentable;
import compiler488.ast.SemanticException;
import compiler488.ast.stmt.Scope;
import compiler488.ast.type.Type;


/**
 * Holds the declaration of multiple elements.
 */
public class MultiDeclarations extends Declaration {
	
    /* The elements being declared */
    private ASTList<DeclarationPart> elements = new ASTList<DeclarationPart> ();
    
    /**
     * Constructor a list with specified elements.
     */
    public MultiDeclarations(Integer row, Integer column, ASTList<DeclarationPart> elements, Type type) {
        super(type);
        this.setCoordinates(row, column);
    	this.elements = elements;
    }

    /**
     * Returns a string representation of the array.
     * Includes the type.
     */
    @Override
    public String toString() {
        return  this.elements +  " : " + this.getType() ;
    }

    /**
     * Print the multiple declarations of the same type.
     * 
     * @param out:    Where to print the description.
     * @param depth:  How much indentation to use while printing.
     */
    @Override
    public void printOn(PrintStream out, int depth) {
        Indentable.printIndentOnLn(out, depth, this.toString());
    }
    
    // ================================================================
    // Checking
    // ================================================================
    
    @Override
    public void check(Scope scope, int loopCount) throws SemanticException {
        super.check(scope, loopCount);
        
        for (DeclarationPart element : this.elements.getLL()) {
        	element.check(scope, loopCount);
        }        
       
        this.checkS47(this);
    }

    // ================================================================
    // Getters and Setters
    // ================================================================

    public ASTList<DeclarationPart> getElements() {
        return this.elements;
    }

    public void setElements(ASTList<DeclarationPart> elements) {
        this.elements = elements;
    }
}
