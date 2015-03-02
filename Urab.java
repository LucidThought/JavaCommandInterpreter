import java.io.*;
import java.util.Scanner;


public class Urab
{
    public static void main(String[] args)
    {   
        Scanner in = new Scanner(System.in);
        String input = "";
        while(!input.equals("q"))
        {
        System.out.print("> ");
        input = in.nextLine();
        System.out.print(input + "\n");
        }
    }
}