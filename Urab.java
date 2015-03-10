/*
CPSC 449 - Java Assignment - Java Command Interpreter
Anthony Coulthard
Andrew Howell
Riley Lahd
Andrew Lata
Kendra Wannamaker

Version 1.0.1 - 04/03/2015

Our language is named Urab. This file will import a command set and
take command line input according to that command set.
*/
import java.io.*;
import java.util.Scanner;
import java.util.Arrays;
import java.io.File;
import java.lang.Object;

public class Urab
{
    static final int OPEN_BRACKET_ERROR = 1;
    static final int CLOSE_BRACKET_ERROR = 2;
    static final int INVALID_TYPE = 3;
    static final int INVALID_SPACE_IN_LITERAL = 4;

	public static void main(String[] args)
	{   
        boolean verbose = false;
        boolean helpMode = false;
        String jarName = "commands.jar";
        String className = "commands";
		//printHelp();
		if (args.length == 0)
		{
			//immediately print synopsis
            printSynopsis();
		}
        for(int i = 0; i<args.length;i++)
        {
            if((args[i].toLowerCase()).equals("--help") || (args[i].toLowerCase()).equals("--h") || args[i].equals("-h") || args[i].equals("-hv") || args[i].equals("-vh") || args[i].equals("-?"))
            {
                if(args.length>1)
                {
                    //if -h -v is allowed, this is wrong
                    //if -h doesnt exit program, this is stupid
                    System.out.println("Qualifier --help (-h, -?) should not appear with any command-line arguments.");
                    printSynopsis();
                    System.exit(-4);
                }
                helpMode = true;
                //System.out.print("h\n");
            }
            if( ((args[i].toLowerCase())).equals("--verbose") || (args[i].toLowerCase()).equals("--v") || args[i].equals("-v") || args[i].equals("-hv") || args[i].equals("-vh"))
            {
                verbose = true;
                //System.out.print("v\n");
            }
            if((args[i].indexOf('.') >= 0) && (args[i].substring(args[i].lastIndexOf('.'))).equals(".jar"))
            {
                if(jarName.equals("commands.jar"))
                {
                    jarName = args[i];
                }   
                else
                {
                    System.out.println("Could not load jar file: " + args[i]);
                    System.exit(-5);
                }
            }
            else if(!(args[i].startsWith("-")))
            {
                if(className.equals("commands") && !jarName.equals("commands.jar"))
                {
                    className = args[i];
                }
                else
                {
                    System.out.println("Could not find class: " + args[i]);
                    System.exit(-6);
                }
            }

        }

        if(helpMode == true)
        {
            printHelp();
            System.exit(0);            
        }

        File f = new File(jarName);
        if(!f.exists() || f.isDirectory())
        {
            System.out.println("Could not load jar file: " + jarName);
            System.exit(-5);
        }
		printStartup();
        inputLoop(verbose, jarName, className);
        //System.out.print("\nJar name: " + jarName + "\nClass name: " + className + "\n");
    }

    public static void printStartup()
    {
    	String startup = "q           : Quit the program.\nv           : Toggle verbose mode (stack traces).\nf           : List all known functions.\n?           : Print this helpful text.\n<expression>:           Evaluate the expression.\nExpressions can be integers, floats, strings (surrounded in double quotes) or function calls of the form \'(identifier {expression}*)\'.\n";
    	System.out.print(startup);
    }
	public static void printHelp()
	{
		String help = "Synopsis:\n  methods\n  methods { -h | -? | --help }+\n  methods {-v --verbose}* <jar-file> [<class-name>]\nArguments:\n  <jar-file>:   The .jar file that contains the class to load (see next line).\n  <class-name>: The fully qualified class name containing public static command methods to call. [Default=\"Commands\"]\nQualifiers:\n  -v --verbose: Print out detailed errors, warning, and tracking.\n  -h -? --help: Print out a detailed help message.\nSingle-char qualifiers may be grouped; long qualifiers may be truncated to unique prefixes and are not case sensitive.\n\nThis program interprets commands of the format \'(<method> {arg}*)\' on the command line, finds corresponding methods in <class-name>, and executes them, printing the result to sysout.\n";
		System.out.print(help);
	}
	public static void printSynopsis()
	{
		String synopsis = "Synopsis:\n  methods\n  methods { -h | -? | --help }+\n  methods {-v --verbose}* <jar-file> [<class-name>]\nArguments:\n  <jar-file>:   The .jar file that contains the class to load (see next line).\n  <class-name>: The fully qualified class name containing public static command methods to call. [Default=\"Commands\"]\nQualifiers:\n  -v --verbose: Print out detailed errors, warning, and tracking.\n  -h -? --help: Print out a detailed help message.\nSingle-char qualifiers may be grouped; long qualifiers may be truncated to unique prefixes and are not case sensitive.\n";
		System.out.print(synopsis);
	}
	public static void printFunctions()
	{
		String functions = "(add string string) : string\n(add float float) : float\n(add int int) : int\n(sub float float) : float\n(sub int int) : int\n(div int int) : int\n(div float float) : float\n(mul float float) : float\n(mul int int) : int\n(inc float) : float\n(inc int) : int\n(dec int) : int\n(dec float) : float\n(len string) : int\n";
		System.out.print(functions);
	}

    public static void inputLoop(boolean verbose, String jarName, String className)
    {
        Scanner in = new Scanner(System.in);
        String input = "";
        while(!input.equals("q"))
                {
                    System.out.print("> ");
                    input = in.nextLine();
                    if (input.equals("?"))
                        printHelp();
                    else if (input.equals("f"))
                        printFunctions();
                    else if (input.equals("v"))
                    {
                        if (verbose == false)
                        {
                            verbose = true;
                            System.out.print("Verbose on\n");
                        }
                        else
                        {
                            verbose = false;
                            System.out.print("Verbose off\n");
                        }
                    }
                    else if (input.equals("q"))
                    {
                        System.out.print("bye.\n");
                        System.exit(0);
                    }
                    else
                        parse(verbose, input, input);
                    }
        
    }
    public static boolean parse(boolean verbose, String input, String fullInput)
    {
        //in future, will return a tree
        //final int MAX_ARGS = 5;
        input = input.trim();
        if(input.startsWith("(")) //then must be a funcall
        {
            String funName = "";
            if(input.lastIndexOf(')') > 0)
            {
                input = input.substring(1, input.lastIndexOf(')'));
                String[] args = {};
                int brackets = 0;
                String substring = "";
                for(int i = 0; i<input.length(); i++)
                {
                    //if a space is found outside of brackets, add substring to array
                    substring = substring + input.charAt(i);
                    if(input.charAt(i) == '(')
                    {
                        brackets++;
                    }
                    else if(input.charAt(i) == ')')
                    {
                        brackets--;
                    }
                    else if((input.charAt(i) == ' ' && brackets == 0) || (i == input.length()-1))
                    {
                        args = addElement(args, substring.trim());
                        //System.out.print(substring + " ");
                        substring = "";
                    }

                    if(brackets < 0)
                    {
                        //error out, mismatched bracket
                        error(fullInput, CLOSE_BRACKET_ERROR);
                    }
                    else if((i == input.length()-1) && (brackets != 0))
                    {
                        //error out, mismatched bracket
                        error(fullInput, OPEN_BRACKET_ERROR);
                    }
                }
                /* 
                for(int i = 0; i< args.length; i++)
                {
                    System.out.print(args[i] + " ");
                }
                System.out.print("\n");
                */
            }
        }
        else
         {
            //get value type
            if(input.contains(" "))
            {
                //error
                error(fullInput, INVALID_SPACE_IN_LITERAL);
            }
            else if(getType(input).equals("null"))
            {
                //error
                error(fullInput, INVALID_TYPE, fullInput.indexOf(input));
            }
            else
            {
                System.out.println(input);
            }
        }
        return true;
    }
    public static String[] addElement(String[] args, String newArg)
    {
        String[] newArray = Arrays.copyOf(args, args.length+1);
        newArray[args.length] = newArg;
        return newArray;
    }
    public static String getType(String val)
    {
        if(val.startsWith("\"") && val.endsWith("\""))
        {
            return "String";
        }
        else if(val.matches("([+]|-)?\\d*[.]?\\d*") == true)
        {
            if(val.contains("."))
            {
                return "float";
            }
            else return "int";
        }
        else return "null";
    }
    public static void error(String input, int errorCode)
    {
        if(errorCode == OPEN_BRACKET_ERROR)
        {
            System.out.println("Unmatched open bracket at offset " + input.indexOf("(") + ".");
            System.out.println(input);
            for(int i = 0; i < input.indexOf("(");i++)
            {
                System.out.print("-");
            }
            System.out.println("^");
        }
        if(errorCode == CLOSE_BRACKET_ERROR)
        {
            System.out.println("Unmatched close bracket at offset " + input.lastIndexOf(")") + ".");
            System.out.println(input);
            for(int i = 0; i < input.lastIndexOf(")");i++)
            {
                System.out.print("-");
            }
            System.out.println("^");
        }
        if(errorCode == INVALID_SPACE_IN_LITERAL)
        {
            System.out.println("Invalid space in literal at offset " + input.indexOf(" ") + ".");
            System.out.println(input);
            for(int i = 0; i < input.indexOf(" ");i++)
            {
                System.out.print("-");
            }
            System.out.println("^");
        }
    }
    public static void error(String input, int errorCode, int errorIndex)
    {
        if(errorCode == INVALID_TYPE)
        {
            System.out.println("Unrecognized variable type at offset " + errorIndex + ".");
            System.out.println(input);
            for(int i = 0; i < errorIndex;i++)
            {
                System.out.print("-");
            }
            System.out.println("^");
        }
    }
}
