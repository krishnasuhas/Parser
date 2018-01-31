import java.lang.*;
import java.io.*;
public class Stack
{
	public int level=-1;   				// how many lists currently stored count starts from 0,
	public int templevel=0;				// temporary storage used in scoping function
	ll[] vars=new ll[1000];				// array of lists creating for storing variables at each block separately
	llval[] vals=new llval[1000];			// array of lists creating for storing values at each block separately
	
	void call()					// when entered into a new block this function is invoked and link initializing happens 
	{
		level++;
		vars[level]=new ll();
		vals[level]=new llval();
	}
	void close()					// when a block executing is finished this function is invoked to erase that list
	{
		level--;
	}
	void push(String a)				// to push a new variable into respective list for that block
	{
		vars[level].add(a);	
	}	
	boolean isele(String a)				// to check a variable in a respective list for that block
	{
		return vars[level].check(a);
	}
	void pushval(String a,int b)			// to push a new value into respective list for that block
	{
		vals[level].set(vars[level].positionof(a),b);
	}
	boolean scoping(int a,String b)			// to push a new variable into specific list which is given in argument 
	{
		templevel=a;
		return vars[templevel].check(b);	
	}
	int print()					// to check how many varibles stored at a respective list of that block
	{
		vars[level].print();
		return level;
	}
	
}
