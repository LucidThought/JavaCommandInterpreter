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
import java.lang.reflect.*;
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
					System.out.println(je.getName());
					lookAtThis = cl.loadClass(className);  
					break;
				}
			}

		}

		else
		{
			throw new FileNotFoundException("Cannot find the indended class \"" + classFile + "\" in the specified jar");
		}

	}
	
	public boolean verifyClass(String jar, String commandObject) throws FileNotFoundException, IOException
	{
		ZipInputStream zip = new ZipInputStream(new FileInputStream(jar));
		for(ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry())
		{
//			System.out.println(entry.getName());
			if(entry.getName().endsWith(".class") && !entry.isDirectory() && (entry.getName().contains(commandObject+".class"))) 
			{
				return true;
			}
		}
		return false;
	}


	public boolean verifyFunction(String func, String arguments) throws SecurityException
	{
		Method[] classMethods = lookAtThis.getMethods();
		for(Method method : classMethods)
		{
			System.out.println(method.getName());
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
				
	public boolean verifyParameters(Method method, String func, String arguments)
	{
		Class[] parameterType = method.getParameterTypes();
		String[] argArray = arguments.split(" ");
		int index = 0;
		boolean isLegit = false;
		for( Class parameter : parameterType)
		{
			if (parameter.getName().equals(argArray[index]) == false)
			{
				return false;
			}
			else
			{
			isLegit = true;
			}
			index++;
		}
		return isLegit;
	}

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

	public Method summonMethod(String function, String arguments) throws SecurityException
	{
		Method[] classMethods = lookAtThis.getMethods();
		for(Method method : classMethods)
		{
			System.out.println(method.getName());
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

	/*
	* See http://stackoverflow.com/questions/1857775/getting-a-list-of-accessible-methods-for-a-given-class-via-reflection
	*/

	public Method[] getAccessibleMethods()
	{
		List<Method> result = new ArrayList<Method>();
		
			for (Method method : lookAtThis.getDeclaredMethods()) {
			    int modifiers = method.getModifiers();
			    if (Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers)) {
				result.add(method);
			    }
			}

			
		

		return result.toArray(new Method[result.size()]);
	}




	public static void printMethods(Method[] methods) {
		for (Method method : methods) {
			System.out.print("(" + method.getName());
			returnParams(method);
			System.out.println(") : " + method.getReturnType().getName());
		}
	}




	public static void returnParams(Method method) {
		Class[] parameterType = method.getParameterTypes();
		for (Class type : parameterType) {
			if (type.getName().equals("java.lang.String")){
				System.out.print(" String");
			} else
				System.out.print(" " + type.getName());
		}
	}






	public Object invokeMethod (String methodName, Class[] paramTypes, Object[] params) {
// throws NoSuchMethodException, IllegalAccessException, InvocationTargetException??????

/*
* See http://www.oracle.com/technetwork/articles/java/javareflection-1536171.html
*/
		Object returnObj = new Object();

		System.out.println(methodName);

		try {
			Method method = lookAtThis.getMethod(methodName, paramTypes);
			returnObj = method.invoke(lookAtThis, params);
		}
			catch (Throwable e) {
			System.err.println(e);
		}

		return returnObj;
	}
}
