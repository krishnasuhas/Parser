import java.util.*;
import java.io.*;

public class ll
{
        LinkedList<String> object = new LinkedList<String>();
	public void add(String a)
	{
		object.add(a);
	}
	public boolean check(String a)
	{
		return object.contains(a);
	}
	public void print()
	{
		System.out.println("declared variabes :"+object+"\n");
	}
	public void makeempty()				// clear is useful when one list s removed from stack its traces has to removed
	{
		object.clear();
	}
}
