import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class ParseTreeNode 
{
	private String data;
	public boolean isFunction;
	private ParseTreeNode parent;
	private List<ParseTreeNode> children;

	//if given just a literal, it is just a literal
	public ParseTreeNode(String literal)
	{
		this.data = literal;
		this.isFunction = false;  
	}

	//if given children, must be a function
	public ParseTreeNode(String funName, ParseTreeNode[] children)
	{
		this.data = funName;
		this.children = Arrays.asList(children);
		this.isFunction = true;
	}

	//when a function is evaluated, it becomes a literal
	public int evaluate(String newValue)
	{

		this.data = newValue;
		this.isFunction = false;
		return 0;
	}

	public String getData()
	{
		return this.data;
	}

	//returns number of children
	public int numChildren()
	{
		if(!this.isFunction)
		{
			return 0;
		}
		return this.children.size();	
	}


	public List<ParseTreeNode> getChildren()
	{
		return children;
	}

	public ParseTreeNode getParent()
	{
		return this.parent;
	}

	//get types of children
	public String[] getTypes()
	{
		String[] types = new String[children.size()];
		for(int i = 0; i < children.size(); i++)
		{
			types[i] = (children.get(i)).getType();
		}
		return types;
	}

	//returns current type
	public String getType()
    {
    	if (isFunction == true)
    	{
    		return "function";
    	}

        if(data.startsWith("\"") && data.endsWith("\""))
        {
            
            return "String";
        }
        else if(data.matches("([+]|-)?\\d*[.]?\\d*") == true)
        {
            if(data.contains("."))
            {
                return "float";
            }
            else return "int";
        }
        else return "null";
    }

    //convert tree to a string
    public String toString()
    {
    	String string= "";
    	if(this.isFunction){string += "(";}
    	string+= data + " ";
    	if((children != null) && (children.size() != 0))
    	{
    		for(int i = 0; i<children.size();i++)
    		{
    			string += ((children.get(i)).toString());
    		}
    	}
    	if(this.isFunction)
    	{
    		string = string.substring(0, string.length()-1);
    		string += ") ";
		}
    	return string;
    }
}