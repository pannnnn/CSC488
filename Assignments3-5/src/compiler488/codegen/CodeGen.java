package compiler488.codegen;

import java.io.*;
import java.util.*;
import compiler488.compiler.Main;
import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;

/**     CodeGenerator.java 
 *      NOT USED IN OUR IMPLEMENTATION!
 *      WE PERFORMED ALL NEEDED FUNCTIONALITIES BY class CompileTime
 */

public class CodeGen
    {
      /** version string for Main's -V */
      public static final String version = "Winter 2017" ;

    /** initial value for memory stack pointer */
    private short startMSP;
    /** initial value for program counter */
    private short startPC;
    /** initial value for memory limit pointer */
    private short startMLP;

    /** flag for tracing code generation */
    private boolean traceCodeGen = Main.traceCodeGen ;

    /**  
     *  Constructor to initialize code generation
     */
    public CodeGen()
    {
    // YOUR CONSTRUCTOR GOES HERE.
    }

    // Utility procedures used for code generation GO HERE.

    /** 
     *  Additional intialization for gode generation.
     *  Called once at the start of code generation.
     *  May be unnecesary if constructor does everything.
     */

   /** Additional initialization for Code Generation (if required) */
   void Initialize()
    {
    /********************************************************/
    /* Initialization code for the code generator GOES HERE */
    /* This procedure is called once before codeGeneration  */      
    /*                                                      */
    /********************************************************/

    return;
    }

    
    /**  
     *  Perform any requred cleanup at the end of code generation.
     *  Called once at the end of code generation.
     *  @throws MemoryAddressException
     */
    void Finalize()
        throws MemoryAddressException     // from Machine.writeMemory 
    {
    /********************************************************/
    /* Finalization code for the code generator GOES HERE.  */      
    /*                                                      */
    /* This procedure is called once at the end of code     */
    /* generation                                           */
    /********************************************************/

    //  REPLACE THIS CODE WITH YOUR OWN CODE
    //  THIS CODE generates a single HALT instruction 
        //  as an example.
     Machine.setPC( (short) 0 ) ;        /* where code to be executed begins */
    Machine.setMSP((short)  1 );       /* where memory stack begins */
    Machine.setMLP((short) ( Machine.memorySize -1 ) );    
                    /* limit of stack */
        Machine.writeMemory((short)  0 , Machine.HALT );

    return;
    }

    /** Procedure to implement code generation based on code generation
     *  action number
     * @param actionNumber  code generation action to perform
     */
    void generateCode( int actionNumber )
    {
    if( traceCodeGen )
        {
        //output the standard trace stream
        Main.traceStream.println("CodeGen: C" +  actionNumber );
        }

    /****************************************************************/
    /*  Code to implement the code generation actions GOES HERE     */
    /*  This dummy code generator just prints the actionNumber      */
    /*  In Assignment 5, you'll implement something more interesting */
        /*                                                               */
        /*  FEEL FREE TO ignore or replace this procedure                */
    /****************************************************************/

        System.out.println("Codegen: C" + actionNumber ); 
    return;
    }

     //  ADDITIONAL FUNCTIONS TO IMPLEMENT CODE GENERATION GO HERE

}
