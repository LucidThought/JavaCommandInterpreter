
import java.net.URLClassLoader;
import java.net.JarURLConnection;
import java.io.FileNotFoundException;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;
import java.util.Enumeration;

public class Spyglass
{
	private JarURLConnection jarConnection;
	private Class lookAtThis;

	public Spyglass(String jarFile, String classFile) throws FileNotFoundException
	{
		//URL comJar = new URL("jar", "", jarfile);
		//jarConnection = (JarURLConnection)comJar.openConnection();
		if (verifyClass(jarFile, classFile) == true)
		{
			JarFile jar = new JarFile(jarFile);
			Enumeration e = jarFile.entries();

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
			throw new FileNotFoundException("Could not load class: " + classFile);
		}
	}
	
	public boolean verifyClass(String jar, String commandObject)
	{
		ZipInputStream zip = new ZipInputStream(new FileInputStream(jar));
		for(ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry())
		{
			if(entry.getName().endsWith(".class") && !entry.isDirectory() && entry.substring(entry.getName().lastIndexOf("/") ,entry.getName().indexOf(".class")) 
			{
				return true;
			}
		}
		return false;
	}

}
