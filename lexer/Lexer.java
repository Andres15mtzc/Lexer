package lexer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

public class Lexer {
    private String text;
    private Vector<Token> tokens;
    private static final String[] KEYWORD = {"if", "else", "while", "switch", "case", "return", 
                                            "int", "float", "void", "char", "string", "boolean", 
                                            "true", "false", "print"};
    //Constants; YOU WILL NEED TO DEFINE MORE CONSTANTS
    private static final int ZERO      =  1;
    private static final int ONE       =  2;
    private static final int B         =  0;
    private static final int OTHER     =  3;
    private static final int DELIMITER =  4;
    private static final int ERROR     =  4;
    private static final int STOP      = -2;
    // states table; THIS IS THE TABLE FOR BINARY NUMBERS; YOU SHOLD COMPLETE IT 
    private static final int[][] stateTable = {
        {    1,     4,     4,     2,     2,     2,     4,     4,     4,     4,     4,     4,     4,     4,     3,  STOP}, 
        {    6, ERROR, ERROR,     6,     6, ERROR, ERROR,     5, ERROR,    15,    14, ERROR, ERROR, ERROR,     8,  STOP}, 
        {    9, ERROR, ERROR,     9,     9,     9, ERROR, ERROR, ERROR,    15,    14, ERROR, ERROR, ERROR,     8,  STOP}, 
        {   10, ERROR, ERROR,    10,    10,    10, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR,  STOP}, 
        {   11,    11,    11,    11,    11,    11,    11,    11,    11,    11,    11,    11,    11,    11,    11,  STOP}, 
        {   12, ERROR, ERROR,    12, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR,  STOP}, 
        {ERROR, ERROR, ERROR, ERROR, ERROR}
    };

    public Lexer(String text) {
        this.text = text;
    }

    //run
    public void run () {
        tokens = new Vector<Token>();
        String line;
        int counterOfLines= 1;
        // split lines
        do {
            int eolAt = text.indexOf(System.lineSeparator());
            if (eolAt >= 0) {
                line = text.substring(0, eolAt);
                if (text.length()>0) text = text.substring(eolAt+1);
            } else {
                line = "text";
                text = "";
            }
            splitLine (counterOfLines, line);
            counterOfLines++;
        } while ( !text.equals("") );
    }

    private void splitLine(int row, String line) {
        int state = 0;
        int index = 0;
        char currentChar;
        String string="";
        if (line.equals("")) return;
        //DFA working
        do {
            currentChar = line.charAt(index);
            state = calculateNextState(state, currentChar);
            if( !isDelimeter(currentChar) && !isOperator(currentChar) )
                string = string + currentChar;
            index++;
        } while (index < line.length() && state!= STOP);
        //review final state
        if (state == 3) {
            tokens.add(new Token(string, "BINARY", row));
        } else {
            if (!string.equals(" ")) 
                tokens.add(new Token(string, "ERROR", row));
        }
        // current char
        if( isDelimeter(currentChar))
            tokens.add(new Token(currentChar+"", "DELIMETER", row));
        else if (isOperator(currentChar))
            tokens.add(new Token(currentChar+"", "OPERATOR", row));
        // loop
        if (index < line.length())
            splitLine(row, line.substring(index));
    }

    // calculate state
    private int calculateNextState(int state, char currentChar) {
        if (isSpace(currentChar) || isDelimeter(currentChar) ||
            isOperator(currentChar) || isQuotationMark(currentChar))
            return stateTable[state][DELIMITER];
        else if (currentChar == 'b')
            return stateTable[state][B];
        else if (currentChar == '0')
            return stateTable[state][ZERO];
        else if (currentChar == '1')
            return stateTable[state][ONE];
        return stateTable[state][OTHER];
    }

    // isDelimiter
    private boolean isDelimeter(char c) {
        char [] delimiters = {':', ';', '}', '{', '[', ']', '(', ')', ','};
        for (int x=0; x<delimiters.length; x++) {
            if (c == delimiters[x]) return true;
        }
        return false;
    }

    // isOperator
    private boolean isOperator(char o) {
        char [] operators = {'+', '-', '*', '/', '<', '>', '=', '!', '&', '|'};
        for (int x=0; x<operators.length; x++) {
            if (o == operators[x]) return true;
        }
        return false;
    }

    // isQuotationMark
    private boolean isQuotationMark(char o) {
        char [] quote = {'"', '\''};
        for (int x=0; x<quote.length; x++) {
            if (o == quote[x]) return true;
        }
        return false;
    }

    // isSpace
    private boolean isSpace(char o) {
        return o == ' ';
    }

    // getTokens
    public Vector<Token> getTokens() {
        return tokens;
    }
}