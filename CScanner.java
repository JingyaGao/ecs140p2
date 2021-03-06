/*
 *  ECS140A Nitta
 *  Project 2
 *  Part 1 : CScanner
 * 
 *  
 */

//package ecs140p2;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.nio.charset.Charset;

//import ecs140p2.CToken;

public class CScanner {
	public static int linenum = 1;
	public static int charnum = 0;
	public static int index = 1;

	public static void main(String[] args) throws IOException {

		CToken token = new CToken(0, 0, "", "");
		// BufferedReader reader = new BufferedReader(

		// instantiate the pushback reader's buffer size to 2
		// --> used when unreading 2 things in a roll
		PushbackReader reader = new PushbackReader(
				new InputStreamReader(new FileInputStream("example3.x"), Charset.forName("UTF-8")), 2);
		int c;
		while ((c = reader.read()) != -1) {
			reader.unread(c);
			token = GetNextToken(reader, token);
			token.print();
		}
	}

	// get the next in the list
	public static CToken GetNextToken(PushbackReader reader, CToken previousToken) throws IOException {
		CToken token = new CToken(0, 0, "", "");
		CToken prevToken = new CToken(previousToken); // ADDED THIS: to check
		String buffer = "";
		String type = "";

		int c = 0;
		while (type.equals("") && ((c = reader.read()) != -1)) {
			char character = (char) c;
			charnum++;

			// new line
			if (character == '\n') {
				// remember to print out a line for end of file
				if (buffer.compareTo("") != 0) {
					type = categorize(buffer);
					token.set(linenum, index, type, buffer);

					buffer = "";
				}
				linenum++;
				charnum = 0; // CHANGE TO charnum = 1; !!!!!
				index = 1; // ADDED THIS!!!!!
			}

			// leading tab
			else if (character == '\t') {
				index = charnum + 1;
			}

			// space
			else if (character == ' ') {
				if (buffer.compareTo("") != 0) {
					type = categorize(buffer);
					token.set(linenum, index, type, buffer);
					buffer = "";
				}
				index = charnum + 1;
			}

			// distinguish float from identifier
			// '.' as a decimal point
			else if (Character.isDigit(character)) {
				buffer += character;
				c = reader.read();
				char tempChar = (char) c;
				if (tempChar == '.') {
					String tempType = categorize(buffer);
					if (tempType.equals("IntConstant")) {
						buffer += tempChar;
						charnum++;
					} else {
						reader.unread(c);
					}
				} else {
					reader.unread(c);
				}
			}

			// '.' as an operator
			else if (character == '.') {
				
				// create token of identifier before .
				if (!buffer.equals("")) {
					type = categorize(buffer);
					token.set(linenum, index, type, buffer);
					reader.unread(c);
					charnum--;
					// break;
				}

				// past the first identifier --> create '.' token
				else {
					// reader.unread((int)character);
					buffer += '.';
					index = charnum;
					type = categorize(buffer);
					token.set(linenum, index, type, buffer);

					index = charnum + 1;
				}
			}

			// negative or subtraction
			// CHECK IF PREV TOKEN IS IDENTIFIER --> IF YES = OPERATOR
			else if (character == '-') {
				// no space separating previous token and '-'
				// i.e A-2
				if (!buffer.equals("")) {
					type = categorize(buffer);
					token.set(linenum, index, type, buffer);
					reader.unread((int) character);
					charnum--;
				}

				// buffer is empty, current char is '-'
				else {
					// both neg num or operator --> add '-' to buffer
					buffer += character;
					index = charnum;

					// check the previous token
					if (prevToken.toktype.equals("Identifier")) { // subtraction
						type = categorize(buffer);
						token.set(linenum, index, type, buffer);
						index = charnum + 1; // ADDED THIS!!!
					}
				}
			}

			// ==, <=, >=, != need to check 2 characters
			else if (character == '=' || character == '<' || // ==, <=, >=, !=
					character == '>' || character == '!') {

				// no space between identifier and operator (i.e A=5)
				// save the identifier
				if (!buffer.equals("")) {
					type = categorize(buffer);
					token.set(linenum, index, type, buffer);
					reader.unread((int) character);
					charnum--;
				} 
				// buffer is empty --> add operator
				else {
					buffer += character;
					c = reader.read();
					character = (char) c;
					// ==, <=, >=, !=, (+=, -=)????
					if (character == '=') {
						buffer += character;
						type = categorize(buffer);
						token.set(linenum, index, type, buffer);
						charnum++;
						index = charnum + 1;
					} else { // =, <, >, !
						type = categorize(buffer);
						token.set(linenum, index, type, buffer);
						reader.unread((int) character);
						index = charnum + 1;
					}
				}
			}

			// single operator
			else if (character == '(' || character == ')' || character == ';' || character == '+' || character == '*'
					|| character == '/' || character == '{' || character == '}' || character == ',') {
				// print identifier preceding operator
				if (buffer.compareTo("") != 0) {
					type = categorize(buffer);
					token.set(linenum, index, type, buffer);
					reader.unread(c);
					charnum--;
					break;
				}

				buffer += character;
				index = charnum;
				// print out operator
				// UNREAD THIS AND DONT PRINT OUT FOR PARSER
				// DONT WANT TO READ 2 NEXT TOKEN
				type = categorize(buffer);
				token.set(linenum, index, type, buffer);
				index = charnum + 1; // set the index to the next char
			}

			// letters and underscore
			else if (Character.isLetter(character) || character == '_') {
				buffer += character;
			}

			// Invalid Cases
			else {
				// return the current token if there is one
				if (!buffer.equals("")) {
					type = categorize(buffer);
					token.set(linenum, index, type, buffer);
					index = charnum;
					// unread the invalid token
					reader.unread(c);
					charnum--;
					break;
				}
				// else create and return invalid token
				buffer += character;
				type = categorize(buffer);
				token.set(linenum, index, type, buffer);
				index = charnum + 1;
			}
		}

		// check if the next character is EOF
		// may be bug: return an empty token instead of last token
		// override the last token
		if (c != 0 && (c = reader.read()) == -1) {// c == -1){
			type = categorize(buffer);
			token.set(linenum, index, type, buffer);
		}
		// if not EOF, put it back onto the stream
		else {
			reader.unread(c);
		}

		// reader.unread(c);
		return token;
	}

	public static CToken PeekNextToken() {
		CToken token = null;
		// uses push back reader's unread option
		return token;

	}

	private static String categorize(String buffer) {
		String type = "None";

		if (buffer.equals("=") || buffer.equals("==") || buffer.equals("!") || buffer.equals("!=") || buffer.equals("<")
				|| buffer.equals("<=") || buffer.equals(">") || buffer.equals(">=") || buffer.equals("(")
				|| buffer.equals(")") || buffer.equals("{") || buffer.equals("}") || buffer.equals("+")
				|| buffer.equals("-") || buffer.equals(",") || buffer.equals(";") || buffer.equals(".")
				|| buffer.equals("/") || buffer.equals("*")) {
			type = "Operator";
		} else if (buffer.equals("interface") || buffer.equals("int") || buffer.equals("double")
				|| buffer.equals("void") || buffer.equals("main") || buffer.equals("instance") || buffer.equals("let")
				|| buffer.equals("new") || buffer.equals("storage") || buffer.equals("implementation")
				|| buffer.equals("return") || buffer.equals("self") || buffer.equals("if") || buffer.equals("global")
				|| buffer.equals("nil") || buffer.equals("while") || buffer.equals("unsigned") || buffer.equals("short")
				|| buffer.equals("long") || buffer.equals("char") || buffer.equals("float")) {
			type = "Keyword";
		} else if (buffer.compareTo("") == 0) {
			type = "None";
		} else if (checkInt(buffer)) {
			type = "IntConstant";
		} else if (checkFloat(buffer)) {
			type = "FloatConstant";
		} else if (checkIdentifier(buffer)) {
			type = "Identifier";
		} else {
			type = "Invalid";
		}

		return type;
	} // catergorize

	private static boolean checkInt(String buffer) {
		boolean isIntConstant = true;
		if (!(buffer.charAt(0) == '-' || Character.isDigit(buffer.charAt(0))))
			isIntConstant = false;
		else {
			for (int i = 1; i < buffer.length(); i++) {
				if (!(Character.isDigit(buffer.charAt(i)))) {
					isIntConstant = false;
					break;
				} // if
			} // for
		}

		return isIntConstant;
	}

	private static boolean checkFloat(String buffer) {
		boolean isFloatConstant = false;
		int periodCounter = 0;
		if (!(buffer.charAt(0) == '-' || Character.isDigit(buffer.charAt(0))))
			isFloatConstant = false;
		else {
			for (int i = 1; i < buffer.length(); i++) {
				// check if char = period
				// float only true when there is one period
				if (buffer.charAt(i) == '.') {
					if (periodCounter == 0)
						isFloatConstant = true;
					periodCounter++;
				}
				// if char is not digit or more than one period
				else if (!(Character.isDigit(buffer.charAt(i))) || periodCounter > 1) {
					isFloatConstant = false;
					break;
				}

			} // for
		}

		return isFloatConstant;
	}

	private static boolean checkIdentifier(String buffer) {
		boolean isIdentifier = true;
		if (!(buffer.charAt(0) == '_' || Character.isLetter(buffer.charAt(0))))
			isIdentifier = false;
		else {
			for (int i = 1; i < buffer.length(); i++) {
				if (!(Character.isLetter(buffer.charAt(i))) && !(Character.isDigit(buffer.charAt(i)))
						&& !(buffer.charAt(i) == '_')) {
					isIdentifier = false;
					break;
				} // if
			} // for
		} // else
		return isIdentifier;
	}

}