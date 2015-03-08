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
import java.lang.reflection.Constructor;
import java.lang.reflection.Field;
import java.lang.reflection.Method;
import java.lang.reflection.Modifier;

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

			lookAtThis = cl.loadClass(classFile);
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
					lookAtThis = cl.loadClass(className+".class");  //This line is not working...
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

//? not sure where this goes
	Class reflectClass = classFile.class;

	public boolean verifyFunction( String func, String arguments)
	{
		Method[] classMethods = reflectClass.getMethods();
		for(Method method : classMethod)
		{
			if (method.getName().equals( func ))
			{
				if (verifyParameters( func, arguments))
				{
					return true;
				}
				
			}
			index++;
		}
		return false;
	}
				
	public boolean verifyParameters(String func, String arguments)
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
		}
		return isLegit;
	}
}
