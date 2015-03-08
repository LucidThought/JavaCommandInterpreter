import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Exception;
import java.net.MalformedURLException;

public class TestMain
{

	public static void main(String[] args)
	{
		try
		{
			Spyglass test = new Spyglass("commands.jar", "Commands");
		}
		catch (FileNotFoundException ef)
		{
			ef.printStackTrace();
		}
		catch (IOException eio)
		{
			eio.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

}
