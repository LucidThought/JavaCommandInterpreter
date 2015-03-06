
import java.net.JarURLConnection;
import java.io.FileNotFoundException;

public class Spyglass
{
	private JarURLConnection jarConnection;

	public Spyglass(String jarfile, String classfile) throws FileNotFoundException
	{
		//URL comJar = new URL("jar", "", jarfile);
		//jarConnection = (JarURLConnection)comJar.openConnection();
		if (verifyClass(jarfile, classfile) == true)
		{
			URL comJar = new URL("jar", "", jarfile);
			jarConnection = (JarURLConnection)comJar.openConnection();
		}
		else
		{
			throw new FileNotFoundException("Class name given is invalid.");
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
