import java.io.*;
public class Parser {

    private Stack id=new Stack();
    private Token tok; 
    private Scan scanner;
    int Plevel=-1;						//maintaining a current level(block number with out removed levels) we are in 
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

    

    private void program() {
	
	block();
	
    }

    private void block(){
	id.call();
	Plevel++;
	declaration_list();
	statement_list();
	id.close();
	Plevel--;
    }

    private void declaration_list() {
	while( is(TK.DECLARE) ) {
	    declaration();
	}
    }

    private void declaration() 
    {
	mustbe(TK.DECLARE);
	if(tok.kind == TK.ID)
	{
		if(!id.iselethislevel(tok.string))
		{
			id.push(tok.string);						//if not declared pushed into current list in stack 
		}									//gives error
		else
		System.err.print("redeclaration of variable "+tok.string+"\n");
	}
	mustbe(TK.ID);
	while( is(TK.COMMA) ) 
        {
	    scan();
	    if(tok.kind == TK.ID)
            {
	 	if(!id.isele(tok.string))						//if not declared pushed into current list in stack
		id.push(tok.string);							
		else
		System.err.print("redeclaration of variable "+tok.string+"\n");
	    }
	    mustbe(TK.ID);
	}
    }

    private void statement_list() 
    {
	while((is(TK.ID))||is(TK.SCOPING)||is(TK.PRINT)||is(TK.DO)||is(TK.IF))
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
			expr();
		}
		else if(is(TK.PRINT))
		{
			mustbe(TK.PRINT);
			expr();
		}
		else if(is(TK.DO))
		{
			mustbe(TK.DO);
			guarded_command();
			mustbe(TK.DOWHILE);
		}
		else if(is(TK.IF))
		{
			If();
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
			//gstring=tok.string;
			if(!scopflag)
	 		{
				if(!id.isele(tok.string))
				{
					System.err.print(tok.string+" is an undeclared variable on line "+tok.lineNumber+"\n");
					System.exit(1);
				}
			}
			else
			{
				if(a<=Plevel)			 //if scope is given out of range stops executing by giving error
				{
					if(!id.scoping(a,tok.string))
					{
						//System.err.print(tok.string+" is an undeclared variable on line "+tok.lineNumber);
						System.err.print("no such variable ~"+a+""+tok.string+" on line "+tok.lineNumber+"\n");
						System.exit(1);
					}
				}
				else
				System.err.print("no such variable ~"+a+""+tok.string+" on line "+tok.lineNumber+"\n");
			}
	    	}
		mustbe(TK.ID);
		//System.out.println("level : "+id.print()+"\n");
	}	

	private void If()
	{	
		mustbe(TK.IF);
		guarded_command();
		while( is(TK.ELSEIF) ) 
		{
		    mustbe(TK.ELSEIF);
		    guarded_command();
		}
		if(is(TK.ELSE))
		{
			mustbe(TK.ELSE);
			block();
		}
		mustbe(TK.ENDIF);
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
			expr();
			mustbe(TK.RPAREN);
		}
		else if(is(TK.ID)||is(TK.SCOPING))
		{
			ref_id();
		}
		else if(is(TK.NUM))
		{
			mustbe(TK.NUM);
		}
	}

	private void addop()
	{
		if(is(TK.PLUS))
		mustbe(TK.PLUS);
		else if(is(TK.MINUS))
		mustbe(TK.MINUS);
	}
	
	private void multop()
	{
		if(is(TK.TIMES))
		mustbe(TK.TIMES);
		else if(is(TK.DIVIDE))
		mustbe(TK.DIVIDE);
	}

	private void guarded_command()
	{
		expr();
		mustbe(TK.THEN);
		block();
	}

    private boolean is(TK tk) {
        return tk == tok.kind;
    }

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
