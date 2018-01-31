import java.util.*;
import java.io.*;

public class ll
{
        LinkedList<String> object = new LinkedList<String>(); 	// list creating for storing variables
	public void add(String a)				// to add a variable to list
	{
		object.add(a);
	}
	public boolean check(String a)				// to check a variable in list
	{
		return object.contains(a);
	}
	public void print()					// to show a variables in list
	{
		System.out.println("declared variabes :"+object+"\n");
	}
}
