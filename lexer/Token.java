package lexer;

public class Token {
    private String word;
    private String token;
    private int line;

    public Token(String word, String token, int line) {
        setWord(word);
        setToken(token);
        setLine(line);
    }

    public Token(String word, String token) {
        setWord(word);
        setToken(token);
        setLine(0);
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }
}
