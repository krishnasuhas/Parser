import java.io.*;
import java.util.*;

public class MyInteger extends Element
{
	int a;

	public MyInteger()
	{
		a=0;
	}
	
	public int Get()
	{
		return a;
	}
	
	public void Set(int b)
	{
		a=b;
	}

	public void Print()
	{
		if(a!=0)
		System.out.print(" "+a+" ");
	}
}
