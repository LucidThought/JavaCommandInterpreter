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
			URLClassLoader cl = URLClassLoader.newInstance(urls);

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
			if (className.substring(className.lastIndexOf(".")).equalsIgnoreCase(classFile))
			lookAtThis = cl.loadClass(className);
			break;
			}
		}
		else
		{
			throw new FileNotFoundException("The indended class \"" + classFile + "\" does not exist");
		}

	}
	
	public boolean verifyClass(String jar, String commandObject) throws FileNotFoundException, IOException
	{
		ZipInputStream zip = new ZipInputStream(new FileInputStream(jar));
		for(ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry())
		{
			if(entry.getName().endsWith(".class") && !entry.isDirectory() && (entry.getName().substring(entry.getName().lastIndexOf("/") ,entry.getName().indexOf(".class"))==commandObject)) 
			{
				return true;
			}
		}
		return false;
	}

}
