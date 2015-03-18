/*
CPSC 449 - Java Assignment - Java Command Interpreter
Anthony Coulthard
Andrew Howell
Riley Lahd
Andrew Lata
Kendra Wannamaker


Spyglass is the class that handles all of the Reflection operations necessary for our program.

*/
import java.lang.Exception;
import java.lang.ClassLoader;
import java.lang.NullPointerException;
import java.net.URLClassLoader;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;
import java.util.Enumeration;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;
import java.lang.Object;
import java.net.URL;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
//import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.lang.SecurityException;
import java.lang.reflect.InvocationTargetException;

public class Spyglass
{
	private JarURLConnection jarConnection;
	private Class lookAtThis;

//	The constructor here verifies and loads the class file from the given jar,
//	then assigns the class to operate reflection on in the varible 'lookAtThis'
	public Spyglass(String jarFile, String classFile) throws FileNotFoundException, IOException, MalformedURLException, ClassNotFoundException
	{
		if (verifyClass(jarFile, classFile) == true)
		{
			JarFile jar = new JarFile(new File(jarFile));
			Enumeration e = jar.entries();

			URL[] urls = { new URL("jar:file:" + jarFile+"!/") };
			URLClassLoader cl = new URLClassLoader(urls);

			while (e.hasMoreElements()) 
			{
				JarEntry je = (JarEntry) e.nextElement();
				if(je.isDirectory() || !je.getName().endsWith(".class"))
				{
					continue;
				}
				// -6 because of '.class'
				String className = je.getName().substring(0,je.getName().length()-6);
				className = className.replace('/', '.');
				if (className.contains(classFile))
				{
//					System.out.println(je.getName());
					lookAtThis = cl.loadClass(className);  
					break;
				}
			}

		}

		else
		{
			throw new FileNotFoundException();
		}

	}

// verifyClass returns a boolean verifying whether the intended class exists inside of the defined jar
	public boolean verifyClass(String jar, String commandObject) throws FileNotFoundException, IOException
	{
		ZipInputStream zip = new ZipInputStream(new FileInputStream(jar));
		for(ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry())
		{
			if(entry.getName().endsWith(".class") && !entry.isDirectory() && (entry.getName().contains(commandObject+".class"))) 
			{
				return true;
			}
		}
		return false;
	}

// verifyFunction returns a boolean verifying whether the intended function exists inside the defined class
	public boolean verifyFunction(String func, String arguments) throws SecurityException
	{
		Method[] classMethods = lookAtThis.getMethods();
		for(Method method : classMethods)
		{
			if (method.getName().equals(func))
			{
				if (verifyParameters(method, func, arguments))
				{
					return true;
				}
				
			}
		}
		return false;
	}

// verifyParameters returns a boolean reflecting whether or not the intended function accepts the given parameters				
	public boolean verifyParameters(Method method, String func, String arguments)
	{
		Class[] parameterType = method.getParameterTypes();
		String[] argArray = arguments.split(" ");
		int index = 0;
		for( Class parameter : parameterType)
		{
			if (parameter.getName().equals(argArray[index]) == false)
			{
				if ( (parameter.getName().equals("java.lang.Integer")==true) && (argArray[index].equals("int")==true) )
				{
					index++;
					continue;
				}
				else if ( (parameter.getName().equals("java.lang.Float")==true) && (argArray[index].equals("float")==true) )
				{
					index++;
					continue;
				}
				else if ((parameter.getName().equals("java.lang.String")==true) && (argArray[index].equals("String")==true))
				{
					index++;
					continue;
				}
				else
				{
					return false;
				}
			}

			index++;
		}
		return true;
	}

// verifyReturns returns a string representation of the given methods return type
	public String verifyReturns(String function, String arguments)
	{
		Method theMethod = summonMethod(function, arguments);
		if (theMethod == null)
		{
			throw new NullPointerException();
		}
		else
		{
			return theMethod.getReturnType().getName();
		}
	}

// summonMethod returns a pointer to the method on which reflection is intended
	public Method summonMethod(String function, String arguments) throws SecurityException
	{
		Method[] classMethods = lookAtThis.getMethods();
		for(Method method : classMethods)
		{
			if (method.getName().equals(function))
			{
				if (verifyParameters(method, function, arguments))
				{
					return method;
				}
				
			}
		}
		return null;
	}



// getAccessibleMethods returns a list of pointers to all of the public methods inside the class
	/*
	* See http://stackoverflow.com/questions/1857775/getting-a-list-of-accessible-methods-for-a-given-class-via-reflection
	*/
	public Method[] getAccessibleMethods()
	{
		List<Method> result = new ArrayList<Method>();
		
			for (Method method : lookAtThis.getDeclaredMethods()) {
			    int modifiers = method.getModifiers();
			    if (Modifier.isPublic(modifiers)) {
				result.add(method);
			    }
			}
		

		return result.toArray(new Method[result.size()]);
	}

// printMethods prints a list of available functions, their input types, and their return types
	public static void printMethods(Method[] methods) {
		for (Method method : methods) {
			System.out.print("(" + method.getName());
			returnParams(method);
			System.out.print(") : ");
			returnReturns(method);
			System.out.println();
		}
	}

// returnParams prints the appropriate parameter data types on a given method
	public static void returnParams(Method method) {
		Class[] parameterType = method.getParameterTypes();
		for (Class type : parameterType) {
			if (type.getName().equals("java.lang.String"))
			{
				System.out.print(" string");
			} else if (type.getName().equals("java.lang.Float"))
			{
				System.out.print(" float");
			} else if (type.getName().equals("java.lang.Integer"))
			{
				System.out.print(" int");
			} else
				System.out.print(" " + type.getName());
		}
	}

// returnReturns prints the appropriate method's return type
	public static void returnReturns(Method method) {
		String returnType = method.getReturnType().getName();

		if (returnType.equals("java.lang.String"))
		{
			System.out.print("string");
		} else if (returnType.equals("java.lang.Float"))
		{
			System.out.print("float");
		} else if (returnType.equals("java.lang.Integer"))
		{
			System.out.print("int");
		} else
			System.out.print(returnType);
	}





// invokeMethod finds the intended method with summonMethod, then executes and returns a string of the output of that function
public String invokeMethod (String function, String arguments)
	{
// throws NoSuchMethodException, IllegalAccessException, InvocationTargetException??????

/*
* See http://www.oracle.com/technetwork/articles/java/javareflection-1536171.html
*/
		Object returnObj = null;

		Object[] params = makeParams(arguments);
		Method method = summonMethod(function, paramsToTypes(arguments));

		if (method != null)
		{
		//  "try" statement should be restructured to throw an exception
			try
			{
				returnObj = method.invoke(lookAtThis, params);
			}
			catch (Throwable e)
			{
				System.err.println(e);
			}
		}

		return returnObj.toString();
	}


// paramsToTypes takes the string containing the intended arguments and return a string of their intended types
	public String paramsToTypes(String arguments)
	{
		StringBuilder values = new StringBuilder();
		String[] argArray = arguments.split(" ");

		int i = 0;

		for (String arg : argArray) {
			if (checkInteger(arg) == true)
			{
				values.append("int ");
			}
			else if (checkFloat(arg) == true)
			{
				values.append("float ");
			}
			else
			{
				values.append("java.lang.String ");
			}

			i++;
		}

		return values.toString();
	}


// makeParams takes the string containing teh intended arguments and returns an array of the Objects for each argument
	public Object[] makeParams(String arguments)
	{
		String[] argArray;
		if (arguments.contains("\""))
		{
			argArray = removeQuotes(arguments);
		}
		else
		{
			argArray = arguments.split(" ");
		}
		removeQuotes(arguments);
		Object [] resultArray = new Object [argArray.length];
		int i = 0;


		for (String arg : argArray)
		{
			if (checkInteger(arg) == true)
			{
				resultArray[i] = new Integer(Integer.parseInt(arg));
			}
			else if (checkFloat(arg) == true)
			{
				resultArray[i] = new Float(Float.parseFloat(arg));
			}
			else
			{
				resultArray[i] = new String(arg);
			}

			i++;
		}

		return resultArray;
	}

	public String[] removeQuotes(String arguments)
	{
		StringBuilder maker;
		ArrayList buildingArray;
		boolean inQuotes = false;
		for(int i=0;i<arguments.length();i++)
		{
			if((arguments.charAt(i) == '"') && inQuotes == false)
			{
				inQuotes = true;
				continue;
			}
			else if((arguments.charAt(i) == '"') && inQuotes == true)
			{
				inQuotes = false;
				continue;
			}
			if(inQuotes == false && (arguments.charAt(i) == ' '))
			{
				buildingArray.add(maker.toString());
				maker.delete(0,maker.length());
				continue;
			}
			else
			{
				maker.append(i);
			}
			if(inQuotes == true)
			{
				maker.append(arguments.charAt(i));
			}
		}
		String[] finished = buildingArray.toArray(new String[buildingArray.size()]);
		return finished;
	}

// checkInteger checks if the given string is an integer and returns true or false accordingly
	public Boolean checkInteger(String s)
	{
		boolean isInteger = true;
		try {
			Integer.parseInt(s);
		}
		catch(Exception ex) {
			isInteger = false;
		}
		return isInteger;
	}

// checkFloat checks if the given string is a floating point number and returns true or false accordingly
	public Boolean checkFloat(String s)
	{
		boolean isFloat = true;
		try {
			Float.parseFloat(s);
		}
		catch(Exception ex) {
			isFloat = false;
		}
		return isFloat;
	}
}




