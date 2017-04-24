

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.nio.charset.Charset;

//import package.CToken;

public class CScanner {

	public static void main(String[] args) throws IOException{
			String buffer = "";
			int linenum = 1;
			int charnum = 0;
			int index = 1;
			String type = "";
		
	        //BufferedReader reader = new BufferedReader(
			PushbackReader reader = new PushbackReader(
	            new InputStreamReader(
	                new FileInputStream("example1.x"),
	                Charset.forName("UTF-8")));
	        int c;
	        while((c = reader.read()) != -1) {
	          char character = (char) c;
	          charnum++;
	          //System.out.println(character);
	          
	          // check if - is negative or minus sign; check next token 
	          //  -- if next token is const literal == neg
	          //  -- else minus
	          
	          if(character == '\n'){
	        	  // remember to print out a line for end of file
	        	  linenum++;
	        	  charnum = 0;
	          }

	          // ==, <=, >= need to check 2 characters
	          else if (character == '.') {
	        	  c  = reader.read();
	        	  character = (char) c;
	        	  if (!Character.isDigit(character)){ // identifier.identifier i.e class.type
	        		  System.out.printf("Token: %s, line: %d, index: %d%n", buffer, linenum, index);
	        		  buffer = "";
	        		  reader.unread((int)character);
	        		  
	        		  buffer += '.';
	        		  index = charnum;
	        		  System.out.printf("Token: %s, line: %d, index: %d%n", buffer, linenum, index);
	        		  buffer = "";
	        		  //index = charnum;
	        	  }
	        	  else if (Character.isDigit(character)){ //float
	        		  buffer += character;
	        		  charnum++;
	        	  }   		  
	          }
	          else if ( character == '-'){
	        	  buffer += character;
	        	  c  = reader.read();
	        	  character = (char) c;
	        	  if (character == ' '){		// subtraction
	        		  // move printing to later????
	        		  System.out.printf("Token: %s, line: %d, index: %d%n", buffer, linenum, index);
	        		  buffer = "";
	        		  reader.unread((int)character);
	        	  }
	        	  else{							// number
	        		  buffer += character;
	        		  charnum++;
	        	  }
	          }
	          else if (character == '=' || character == '<' ||
	        	  character == '>' || character == '!') {
	        	  buffer += character;
	        	  c  = reader.read();
	        	  character = (char) c;
	        	  if (character == '='){
	        		  buffer += character;
	        		  //simpler way of printing??? exit here??
	        		  System.out.printf("Token: %s, line: %d, index: %d%n", buffer, linenum, index);
	        		  buffer = "";
	        		  charnum++;
	        		  index = charnum + 2; // identifier += digit; index should be at 'd'; charnum at '='
	        	  }
	        	  else{
	        		  System.out.printf("Token: %s, line: %d, index: %d%n", buffer, linenum, index);
	        		  buffer = "";
	        		  reader.unread((int)character);
	        		  index = charnum + 2;
	        	  }
	          }
	          else if (character == '(' || character == ')' || character == ';' ||
	        	  character == '+' || character == '*' || character == '/' ||
	        	  character == '{' || character == '}' || character == ',') {
	        	  if(buffer.compareTo("") != 0){
	        		  // print identifier preceding operator
	        		  System.out.printf("Token: %s, line: %d, index: %d%n", buffer, linenum, index);
		        	  buffer = "";
	        	  }
	        	  
	        	  buffer += character;
	        	  index = charnum;
	        	  // print out operator
	        	  System.out.printf("Token: %s, line: %d, index: %d%n", buffer, linenum, index);
	        	  buffer = "";
	          }
	          else if(!Character.isWhitespace(character)) {
	        	  buffer += character;
	          }
	          else if(character != '\t' && buffer.compareTo("") != 0) {
	        	  System.out.printf("Token: %s, line: %d, index: %d%n", buffer, linenum, index);
	        	  buffer = "";
	        	  index = charnum + 1;
	        	  //call CToken to initialize a new object
	        	  //clear buffer
	          }
	        }
	        
	}
	
	// get the next in the list
	public static CToken GetNextToken(){
		CToken token = null;
		return token;
	}
	
	public static CToken PeekNextToken() {
		CToken token = null;
		// uses push back reader's unread option
		return token;
	
	}

}
