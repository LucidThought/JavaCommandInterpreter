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
    String HELP = ""
    public static void main(String[] args)
    {   
        boolean verbose = false
        Scanner in = new Scanner(System.in);
        String input = "";
        while(!input.equals("q"))
        {
            System.out.print("> ");
            input = in.nextLine();
            //System.out.print(input + "\n");
            
        }
    }
}