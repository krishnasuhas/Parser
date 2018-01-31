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
	void close()
	{
		vars[level].makeempty();
		level--;
	}
	void push(String a)
	{
		vars[level].add(a);
	}	
	boolean isele(String a)
	{
		for(int i=level;i>=0;i--)
		{
			if(vars[i].check(a))
			{
				return true;
			}
		}
		return false;
	}
	boolean iselethislevel(String a)
	{
		if(vars[level].check(a))
		{
			return true;
		}
		return false;
	}
	boolean scoping(int a,String b)
	{
		return vars[level-a].check(b);
	}
	int print()
	{
		vars[level].print();
		return level;
	}
	
}
