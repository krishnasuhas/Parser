import java.io.*;
public class Parser {

    private Stack id=new Stack();				//reference to stack is created for storing variables for various blocks
    private Token tok;
    private Scan scanner;
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
	id.close();
    }

    private void block(){
	id.call();	
	declaration_list();
	statement_list();
    }

    private void declaration_list() 
    {
	while( is(TK.DECLARE) ) 
	{
	    declaration();
	}
    }

    private void declaration() 
    {
	mustbe(TK.DECLARE);
	if(tok.kind == TK.ID)				
	{
	 	id.push(tok.string);
	}
	mustbe(TK.ID);
	while( is(TK.COMMA) ) 				
        {
	    scan();
	    if(tok.kind == TK.ID)
            {
	 	id.push(tok.string);
	    }
	    mustbe(TK.ID);
	}
    }

    private void statement_list() 
    {
	while((is(TK.ID))||is(TK.SCOPING)||is(TK.PRINT)||is(TK.DO)||is(TK.IF))	// from the grammar rules it has to be one of this option to add statements 
	{
	   statement();
	}
    }

	private void statement() 
	{
		if(is(TK.ID)||is(TK.SCOPING))			// if identifier with or with out scoping calls ref function followed by 
		{
			ref_id();				// assignment and expression as given in grammar
			mustbe(TK.ASSIGN);
			expr();
		}
		else if(is(TK.PRINT))
		{
			mustbe(TK.PRINT);			// if print token then expr function
			expr();
		}
		else if(is(TK.DO))
		{
			mustbe(TK.DO);				//if todo token calls guarder_command funtion 
			guarded_command();
			mustbe(TK.DOWHILE);
		}
		else if(is(TK.IF))
		{
			If();					//if  "if"  token calls if funtion 
		}
	}
	private void ref_id()
	{
		boolean scopflag=false;					//whether scoping is present or not just a flag
		int a=0;						//for storing number in scoping
		if(is(TK.SCOPING))
		{
			scopflag=true;					
			mustbe(TK.SCOPING);				
			if(is(TK.NUM))
			{
				a=Integer.parseInt(tok.string);
				mustbe(TK.NUM);
			}
		}
		if(tok.kind == TK.ID)
		{
			
			if(!scopflag)
	 		{
				
			}
			else
			{
				
			}
	    	}
		mustbe(TK.ID);
	}	

	private void If()					//according to grammar checks the respective tokens and calls needed functions
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
	private void expr() 					//according to grammar checks the respective tokens and calls needed functions
	{
		term();
		while(is(TK.PLUS)||is(TK.MINUS)) 
		{
		    addop();
		    term();
		}
	}

	private void term() 					//according to grammar checks the respective tokens and calls needed functions
	{
		factor();
		while(is(TK.TIMES)||is(TK.DIVIDE)) 
		{
		    multop();
		    factor();
		}
	}

	private void factor() 					//according to grammar checks the respective tokens and calls needed functions
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

	private void addop()				//according to grammar checks the respective tokens and calls needed functions
	{
		if(is(TK.PLUS))
		mustbe(TK.PLUS);
		else if(is(TK.MINUS))
		mustbe(TK.MINUS);
	}
	
	private void multop()				//according to grammar checks the respective tokens and calls needed functions
	{
		if(is(TK.TIMES))
		mustbe(TK.TIMES);
		else if(is(TK.DIVIDE))
		mustbe(TK.DIVIDE);
	}

	private void guarded_command()			//according to grammar checks the respective tokens and calls needed functions
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
