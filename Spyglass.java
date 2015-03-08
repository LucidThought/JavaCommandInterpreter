import java.lang.Exception;
import java.net.URLClassLoader;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
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

public class Spyglass
{
	private JarURLConnection jarConnection;
	private Class lookAtThis;

	public Spyglass(String jarFile, String classFile) throws FileNotFoundException, IOException, MalformedURLException, ClassNotFoundException
	{
		if (verifyClass(jarFile, classFile) == true)
		{
			JarFile jar = new JarFile(jarFile);
			Enumeration e = jar.entries();

			URL[] urls = { new URL("jar:file:" + jarFile+"!/") };
			URLClassLoader cl = new URLClassLoader(urls);
			URLClassLoader child = new URLClassLoader (new URL[] {new URL("file://./"+jarFile)}, this.getClass().getClassLoader());

			lookAtThis = Class.forName(classFile,true,cl);
/*
			while (e.hasMoreElements()) 
			{
				JarEntry je = (JarEntry) e.nextElement();
				if(je.isDirectory() || !je.getName().endsWith(".class"))
				{
					continue;
				}
				// -6 because of .class
				String className = je.getName().substring(0,je.getName().length()-6);
				className = className.replace('/', '.');
				if (className.contains(classFile))
				{
//					System.out.println(className);
					lookAtThis = cl.loadClass(className);  //This line is not working...
					break;
				}
			}
*/
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


	public boolean verifyFunction( String func, String arguments)
	{
		Method[] classMethods = lookAtThis.getMethods();
		for(Method method : classMethods)
		{
			System.out.println(method.getName());
			if (method.getName().equals( func ))
			{
				if (verifyParameters( method, func, arguments))
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
			if (parameter.getName().equals( argArray[index]) == false)
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
}
