import java.lang.*;
import java.io.*;
public class Stack
{
	public int top=0,level=-1,templevel=0;
	String sample;
	ll[] vars=new ll[1000];
	llval[] vals=new llval[1000];
	
	void call()
	{
		level++;
		vars[level]=new ll();
		vals[level]=new llval();
	}
	void close()					// calls makeempty to remove traces of old list of variables which is now removed 
	{
		vars[level].makeempty();
		level--;
	}
	void push(String a)
	{
		vars[level].add(a);
	}	
	boolean isele(String a)				// checks if a variable present a top level if not goes down the stack and checks if 
	{						//not goes again  and so on upto global area
		for(int i=level;i>=0;i--)
		{
			if(vars[i].check(a))
			{
				return true;
			}
		}
		return false;
	}
	boolean iselethislevel(String a)		// checks if a variable presence at only current or top level 
	{
		if(vars[level].check(a))
		{
			return true;
		}
		return false;
	}
	boolean scoping(int a,String b)			//to check at specific level by using scope
	{
		return vars[level-a].check(b);
	}
	int print()
	{
		vars[level].print();
		return level;
	}
}
