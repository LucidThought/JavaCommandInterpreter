/*
CPSC 449 - Java Assignment - Java Command Interpreter
Anthony Coulthard
Andrew Howell
Riley Lahd
Andrew Lata
Kendra Wannamaker

Version 1.0.0 - 02/03/2015

Our language is named Urab. This file will import a command set and
take command line input according to that command set.
*/
import java.io.*;
import java.util.Scanner;


public class Urab
{
	public static void main(String[] args)
	{   
        boolean verbose = false;
		Scanner in = new Scanner(System.in);
		String input = "";
		//printHelp();
		if (args.length == 0)
		{
			//immediately throw an error and quit
		}
		else if (args[0].equals("-v") || args[0].equals("--verbose"))
		{
			verbose = true;
			if (args.length == 2)
			{
				if (args[1].equals("-h") || args[1].equals("-?") || args[1].equals("--help"))
				{
					printHelp();
				}
			}
		}
		else if (args[0].equals("-h") || args[0].equals("-?") || args[0].equals("--help"))
		{
			printHelp();
		}
		else
			printSynopsis();
		printStartup();
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
				parse(verbose, input);
        	}
    	}
    	public static void printStartup()
    	{
        	String startup = "q           : Quit the program.\nv           : Toggle verbose mode (stack traces).\nf           : List all known functions.\n?           : Print this helpful text. <expression>:           Evaluate the expression.\nExpressions can be integers, floats, strings (surrounded in double quotes) or function calls of the form \'(identifier {expression}*)\'.\n";
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

    public static boolean parse(boolean verbose, String input)
    {
        //in future, will return a tree
        System.out.println("Parsed!\n");
        return true;
    }
}
