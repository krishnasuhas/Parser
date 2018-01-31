import java.io.*;
import java.util.*;

public class Sequence extends Element
{
	int size;
	Element e;
	Sequence next=null;

	public Sequence()
	{
		e=new MyInteger();
		size=0;
	}
	
	public Sequence(Element e)
	{
		this.e=e;
	}

	public Element first()
	{
		return e;
	}
	
	public Sequence rest()
	{
		return this.next;
	}

	public int length()
	{
		int count=0;
		Sequence ptr=this;
		while(ptr!=null)
		{
			count++;
			ptr=ptr.next;
		}
		return size;
	}

	public void add(Element b,int pos)
	{
		Sequence newele=new Sequence(b);
		Sequence ptr=this;
		Element temp;
		if(pos==0)
		{
			newele.next=ptr.next;
			ptr.next=newele;
			temp=newele.e;
			newele.e=ptr.e;
			ptr.e=temp;		
		}
		else if(pos<=size)
		{
			for(int i=0;i<pos-1;i++)
			{
				ptr=ptr.next;
				
			}
			newele.next=ptr.next;
			ptr.next=newele;		
		}
		else if(pos>size)
		{
			System.out.println("in sequence error out of range");
			System.exit(0);
		}
		size++;
	}
	public void add(Element b)
	{
		if(size==0)
		{
			e=b;		
		}
		else
		{
			Sequence newele=new Sequence(b);
			Sequence ptr=this;
			int pos=size;
			for(int i=0;i<pos-1;i++)
			{
				ptr=ptr.next;
			}
			newele.next=ptr.next;
			ptr.next=newele;
		}
		size++;
	}	
	public void delete(int pos)	
	{
		Sequence ptr=this;
		Sequence nxt=this.next;
		if(pos==0)
		{	
			{
				while(nxt.next!=null)
				{
					ptr.e=nxt.e;
					ptr=ptr.next;
					nxt=nxt.next;
				}
				pos=0;
			}
		}
		if (pos<size)
		{
			for(int i=0;i<pos-1;i++)
			{
				ptr=ptr.next;
			}
			ptr.next=ptr.next.next;
		}
		size--;
	}
	public void Print()
	{
		Sequence ptr=this;
		System.out.print("[ ");
		while(ptr.next!=null)
		{
			ptr.e.Print();
			System.out.print(" ");
			ptr=ptr.next;
		}
		System.out.print(" ]");
	}

	public Element index(int pos)
	{
		if(pos>size)
		{
			System.out.println("error out of range");
			System.exit(0);
		}
		Sequence ptr=this;
		for(int i=0;i<pos;i++)
		{
			ptr=ptr.next;
		}
		return ptr.e;			
	}

	public void flat(Sequence ptr,Sequence newseq)
	{
		while(ptr!=null)
		{
			if(ptr.e instanceof Sequence)
			{
				flat((Sequence)ptr.e,newseq);
			}
			else
			{
				newseq.add(ptr.e);
			}
			ptr=ptr.next;
		}
	}
	
	public Sequence flatten()
	{
		Sequence newseq=new Sequence(null);
		flat(this,newseq);
		return newseq;
	}

	
	public Sequence copy()
	{
		Sequence newseq=new Sequence();
		deepcopy(this,newseq);
		return newseq;
	}
	
	public void deepcopy(Sequence ptr,Sequence newseq)
	{
		while(ptr!=null)
		{
			if(ptr.e instanceof Sequence)
			{
				deepcopy((Sequence)ptr.e,newseq);
			}
			else
			{
				newseq.add(ptr.e);
			}
			ptr=ptr.next;
		}
		
	}
	
}
