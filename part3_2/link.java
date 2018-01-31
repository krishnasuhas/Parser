class link
{
	public Element e;
	public link nextlink;


	public link(int a,int pos)
	{
		e=a;
	}
	
	public link(char a,int pos)
	{
		e=a;
	}
	
	public void print()
	{
		System.out.println("["+e+"]");
	}
	
}
