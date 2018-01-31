/* *** This file is given as part of the programming assignment. *** */
import java.io.*;
public class Parser {

    private Stack id=new Stack();
    // tok is global to all these parsing methods;
    // scan just calls the scanner's scan method and saves the result in tok.
    private Token tok; // the current token
    private Scan scanner;
    int Plevel=-1;
    Parser(Scan scanner) {
	this.scanner = scanner;
	scan();
	program();
	if( tok.kind != TK.EOF )
	    parse_error("junk after logical end of program");
    }

    private void scan() {
	tok = scanner.scan();
    }

    private void program() 
    {
	System.out.println("void main()\n{\n");
	block();
	System.out.print("return;\n}\n");
    }

    private void block(){
	id.call();
	Plevel++;
	if(Plevel>0)
	System.out.print("{\n");
	declaration_list();
	statement_list();
	id.close();
	if(Plevel>0)
	System.out.print("}\n");
	Plevel--;	
    }

    private void declaration_list() {
	// below checks whether tok is in first set of declaration.
	// here, that's easy since there's only one token kind in the set.
	// in other places, though, there might be more.
	// so, you might want to write a general function to handle that.
	while( is(TK.DECLARE) ) {
	    declaration();
	}
    }

    private void declaration() 
    {
	mustbe(TK.DECLARE);
	if(tok.kind == TK.ID)
	{
		if(!id.iselethislevel(tok.string))//part4changed
		{
			id.push(tok.string);//part4changed
			System.out.print("\n\tint x_"+tok.string);	
		}
		else
		System.err.print("redeclaration of variable "+tok.string+"\n");
	}
	mustbe(TK.ID);
	while( is(TK.COMMA) ) 
        {
		System.out.print(", ");
	    scan();
	    if(tok.kind == TK.ID)
            {
	 	if(!id.iselethislevel(tok.string))//part4changed
		{
			id.push(tok.string);//part4 changed
			System.out.print("x_"+tok.string);
		}
		else
		System.err.print("redeclaration of variable "+tok.string+"\n");
	    }
	    mustbe(TK.ID);
	}
	System.out.print(";\n\t");
    }

    private void statement_list() 
    {
	while((is(TK.ID))||is(TK.SCOPING)||is(TK.PRINT)||is(TK.DO)||is(TK.IF)||is(TK.FOR))
	{
	   statement();
	}
    }

	private void statement() 
	{
		if(is(TK.ID)||is(TK.SCOPING))
		{
			ref_id();
			mustbe(TK.ASSIGN);
			System.out.print(" = ");
			expr();			
			System.out.print(";\n\t");
		}
		else if(is(TK.PRINT))
		{
			mustbe(TK.PRINT);			
			System.out.print("printf( \"%d\\n\", ");
			expr();			
			System.out.print(" );\n\t");
		}
		else if(is(TK.DO))
		{
			mustbe(TK.DO);			
			System.out.print("while(");
			guarded_command();
			mustbe(TK.DOWHILE);
		}
		else if(is(TK.IF))
		{
			If();
		}
		else if(is(TK.FOR))
		{
			For();
		}
	}
	private void ref_id()
	{
		boolean scopflag=false;
		int a=0;
		if(is(TK.SCOPING))
		{
			scopflag=true;
			mustbe(TK.SCOPING);
			if(is(TK.NUM))
			{
				a=Integer.parseInt(tok.string);
				mustbe(TK.NUM);
			}
			else
			a=Plevel;
		}
		if(tok.kind == TK.ID)
		{
			if(!scopflag)
	 		{
				if(!id.isele(tok.string))
				{
					System.err.print(tok.string+" is an undeclared variable on line "+tok.lineNumber);
					System.exit(1);
				}
			}
			else
			{
				if(a<=Plevel)
				{
					if(!id.scoping(a,tok.string))
					{
						//System.err.print(tok.string+" is an undeclared variable on line "+tok.lineNumber);
						System.err.print("no such variable ~"+a+""+tok.string+" on line "+tok.lineNumber);
						System.exit(1);
					}
				}
				else
				System.err.print("no such variable ~"+a+""+tok.string+" on line "+tok.lineNumber);
			}
	    	}
		System.out.print("x_"+tok.string);
		mustbe(TK.ID);
		//System.out.println("level : "+id.print()+"\n");
	}	

	private void If()
	{	
		System.out.print("if(int ");
		mustbe(TK.IF);
		guarded_command();
		while( is(TK.ELSEIF) ) 
		{
		    System.out.print("else if(");
		    mustbe(TK.ELSEIF);
		    guarded_command();
		}
		if(is(TK.ELSE))
		{
			System.out.print("else");
			mustbe(TK.ELSE);
			block();
		}
		mustbe(TK.ENDIF);
	}

	private void For()
	{	
		System.out.print("for( ");
		mustbe(TK.FOR);
		if(is(TK.ID))
		System.out.print("x_"+tok.string+" ");
		mustbe(TK.ID);
		if(is(TK.ASSIGN))
		System.out.print("=");
		mustbe(TK.ASSIGN);
		expr();
		if(is(TK.INFOR))
		System.out.print(";");
		mustbe(TK.INFOR);
		condition();
		if(is(TK.INFOR))
		System.out.print(";");
		mustbe(TK.INFOR);
		expr();
		if(is(TK.ASSIGN))
		System.out.print("=");
		mustbe(TK.ASSIGN);
		expr();
		if(is(TK.ENDFOR))
		System.out.print(")\n");
		mustbe(TK.ENDFOR);
		block();
		mustbe(TK.ENDFORBLK);
	}
	
	private void condition()
	{
		expr();
		if(!(is(TK.GRTTHAN)||is(TK.LESSTHAN)))
		{
			System.err.println( "can't parse: line "+ tok.lineNumber + " for loop violation" );
			System.exit(1);
		}
		if(is(TK.GRTTHAN)||is(TK.LESSTHAN)) 
		{
		    grtless();
		    expr();
		}
	}

	private void expr() 
	{
		term();
		while(is(TK.PLUS)||is(TK.MINUS)) 
		{
		    addop();
		    term();
		}
	}

	private void term() 
	{
		factor();
		while(is(TK.TIMES)||is(TK.DIVIDE)) 
		{
		    multop();
		    factor();
		}
	}

	private void factor() 
	{
		if(is(TK.LPAREN))
		{
			mustbe(TK.LPAREN);
			System.out.print(" (");
			expr();
			mustbe(TK.RPAREN);
			System.out.print(" )");
		}
		else if(is(TK.ID)||is(TK.SCOPING))
		{
			ref_id();
		}
		else if(is(TK.NUM))
		{
			System.out.print(" "+tok.string);
			mustbe(TK.NUM);
		}
	}

	private void addop()
	{
		if(is(TK.PLUS))
		{
			mustbe(TK.PLUS);
			System.out.print("+");
		}
		else if(is(TK.MINUS))
		{
			mustbe(TK.MINUS);
			System.out.print("-");
		}
	}
	
	private void multop()
	{
		if(is(TK.TIMES))
		{
			mustbe(TK.TIMES);
			System.out.print("*");
		}
		else if(is(TK.DIVIDE))
		{
			mustbe(TK.DIVIDE);
			System.out.print("/");
		}
	}

	private void grtless()
	{
		if(is(TK.GRTTHAN))
		{
			mustbe(TK.GRTTHAN);
			System.out.print(">");
		}
		else if(is(TK.LESSTHAN))
		{
			mustbe(TK.LESSTHAN);
			System.out.print("<");
		}
	}

	private void guarded_command()
	{
		System.out.print(" (");
		expr();
		mustbe(TK.THEN);
		System.out.print(")<=0 )\n");
		block();
	}

    // is current token what we want?
    private boolean is(TK tk) {
        return tk == tok.kind;
    }

    // ensure current token is tk and skip over it.
    private void mustbe(TK tk) 
    {
	if( tok.kind != tk ) 
	{
	    System.err.println( "mustbe: want " + tk + ", got " +tok);
	    parse_error( "missing token (mustbe)" );
	}
	scan();
    }

    private void parse_error(String msg) {
	System.err.println( "can't parse: line "+ tok.lineNumber + " " + msg );
	System.exit(1);
    }
}
