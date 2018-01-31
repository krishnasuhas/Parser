import java.util.*;

public class llval
{
        List<Integer> object = new ArrayList<Integer>();
	public void add(int a)
	{
		object.add(a);
	}
	public int getat(int a)
	{
		return object.get(a);
	}
	public void set(int pos,int a)
	{
		object.set(pos, a);
	}
}
