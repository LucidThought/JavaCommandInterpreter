import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class ParseTreeNode 
{
	private String data;
	public boolean isFunction;
	private ParseTreeNode parent;
	private List<ParseTreeNode> children;

	public ParseTreeNode(String literal)
	{
		this.data = literal;
		this.isFunction = false;  
		//this.children = new List<ParseTreeNode>(); 
	}

	public ParseTreeNode(String funName, ParseTreeNode[] children)
	{
		this.data = funName;
		this.children = Arrays.asList(children);
		this.isFunction = true;
	}

	public int evaluate(String newValue)
	{

		this.data = newValue;
		this.children.clear();
		return 0;
	}

	public String getData()
	{
		return this.data;
	}

	public int numChildren()
	{
		return this.children.size();	
	}

	public List<ParseTreeNode> getChildren()
	{
		/*
		//ParseTreeNode<T>[] childArray = new ParseTreeNode<T>[children.size()];
		ParseTreeNode<T>[] childArray = children.toArray(new ParseTreeNode<T>[children.size()]);
		return childArray;
		*/
		return children;
	}

	public ParseTreeNode getParent()
	{
		return this.parent;
	}

	public String[] getTypes()
	{
		String[] types = new String[children.size()];
		for(int i = 0; i < children.size(); i++)
		{
			types[i] = (children.get(i)).getType();
		}
		return types;
	}

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

    public String toString()
    {
    	String string= "";
    	string += data;
    	if((children != null) && (children.size() != 0))
    	{
    		string += "(";
    		for(int i = 0; i<children.size();i++)
    		{
    			string += ((children.get(i)).toString());
    			if(i!=children.size()-1)
    			{
    				string+=", ";
    			}

    		}
   			string += ")";
    	}
    	return string;
    }
}