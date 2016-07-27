package br.cefetrj.sagitarii.parser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import br.cefetrj.sagitarii.parser.SagitariiParser.ActivitySentenceContext;

// http://www.theendian.com/blog/antlr-4-lexer-parser-and-listener-with-example-grammar/

public class Main {

	
	private static void parseExpression(String drinkSentence) {
	    // Get our lexer
	    SagitariiLexer lexer = new SagitariiLexer(new ANTLRInputStream(drinkSentence) );
	 
	    // Get a list of matched tokens
	    CommonTokenStream tokens = new CommonTokenStream(lexer);
	 
	    // Pass the tokens to the parser
	    SagitariiParser parser = new SagitariiParser(tokens);
	 
	    // Specify our entry point
	    ActivitySentenceContext activitySentenceContext = parser.activitySentence();
	 
	    // Walk it and attach our listener
	    ParseTreeWalker walker = new ParseTreeWalker();
	    SagitariiParserListener listener = new SagitariiParserListener();
	    walker.walk(listener, activitySentenceContext);
	    
	    listener.showList();
	    
	}	
	
	
	public static void main(String[] args) {
		String sentence1 = "map myExecutor1 ( input2 ) to outputtable as activity";
		String sentence2 = "reduce myReduce ( reducein ) to reduceout as reduceAct";
		String sentence3 =  "select myExecutor2 ( " + sentence1 + ", "+sentence2+" ) to selectOutTable as selectActivity";
		String sentence4 = "map parallel1 ( inputtable ) to par1out as parallel1; " + sentence3;
		
		// Worked
		//parseExpression("map myExecutor1 ( inputtable ) to outputtable as activity");
		
		// Worked
		//parseExpression( sentence1 );
		
		//Worked
		//parseExpression("select myExecutor2 ( "+sentence1+" ) to selectOutTable as selectActivity");
	
		// Worked
		//parseExpression("select myExecutor2 ( inputtable, input2, "+sentence1+" ) to selectOutTable as selectActivity");
		
		//Worked
		// parseExpression("select myExecutor2 ( "+sentence1+", tablenoy ) to selectOutTable as selectActivity");
		
		parseExpression( sentence4 );
		
	}

}
