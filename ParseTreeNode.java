import java.util.List;
import java.util.ArrayList;

public class ParseTreeNode <T> 
{
	private String data;
	public boolean isFunction;
	private ParseTreeNode<T> parent;
	private List<TreeNode<T>> children;

	public ParseTreeNode(T literal)
	{
		this.data = literal;
		this.isFunction = true;   
	}

	public ParseTreeNode(T funName, ParseTreeNode[] children)
	{
		this.data = funName;
		this.children = new ArrayList<ParseTreeNode<T>>(Arrays.asList(children));
		this.isFunction = false;
	}

	public int evaluate(String newValue)
	{
		this.data = newValue;
		this.children = this.children.clear();
	}

	public String getData()
	{
		return this.data;
	}

	public int numChildren()
	{
		return this.children.size();	
	}

	public ParseTreeNode<T>[] getChildren()
	{
		ParseTreeNode<T>[] childArray = new ParseTreeNode<T>[children.size()];
		childArray = children.toArray(childArray);
		return childArray;
	}

	public static String getType()
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