import java.io.*;
import java.util.*;

public class MyChar extends Element
{
	char a;

	public MyChar()
	{
		a='0';
	}
	
	public char Get()
	{
		return a;
	}
	
	public void Set(char b)
	{
		a=b;
	}

	public void Print()
	{
		System.out.print("'"+a+"'");
	}
}
