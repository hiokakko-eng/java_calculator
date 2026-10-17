import java.util.ArrayList;
import java.util.List;

/* record */
class Token {
    public final Tokenizer.TokenType type;
    private final String value;

    public Token(Tokenizer.TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public Tokenizer.TokenType type(){ return type; }
    public String value(){ return value; }

    @Override
    public String toString(){
        return "Token(" + type + ",\"" + value + "\")";
    }
}

/* 入力charのリストをもとに、トークンリストを作成 */
public class Tokenizer {
    final String CALC_KEY_NUMBER = "0123456789.";
    private List<Character> charsList;

    public enum TokenType {
        NUMBER,
        PLUS,
        MINUS,
        MULTIPLY,
        DIVIDE,
        LEFT_PAREN,
        RIGHT_PAREN,
        EQUAL
    }

    //public record Token(TokenType type, String value){}

    public Tokenizer(List<Character> charsList){
        this.charsList = charsList;
    };

    /* Tokenをリストにして返す */
    public List<Token> toTokenList(){
        List<Token> tokens = new ArrayList<>();
        Token token = null;
        int i = 0;
        String c = "";
        String num = "";
        
        do {
            do {
                c = charsList.get(i).toString();
                if (CALC_KEY_NUMBER.contains(c)){
                    num = num + c;
                } else {
                    break;
                }
                i++;
                if (i < charsList.size()) c = charsList.get(i).toString();
            } while (i < charsList.size() && CALC_KEY_NUMBER.contains(c));

            if(!num.isEmpty()){
                token = new Token(TokenType.NUMBER, num);
                tokens.add(token);
            }

            switch (c){
                case "+" :
                    token = new Token(TokenType.PLUS, c);
                    break;
                case "-" :
                    token = new Token(TokenType.MINUS, c);
                    break;
                case "*" :
                    token = new Token(TokenType.MULTIPLY, c);
                    break;
                case "/" :
                    token = new Token(TokenType.DIVIDE, c);
                    break;
                case "(" :
                    token = new Token(TokenType.LEFT_PAREN, c);
                    break;
                case ")" :
                    token = new Token(TokenType.RIGHT_PAREN, c);
                    break;
                case "=" :
                    token = new Token(TokenType.EQUAL, c);
                    break;
            }

            tokens.add(token);
            i++;
            num = "";
            if (i < charsList.size()) c = charsList.get(i).toString();
        } while (i < charsList.size());

        return tokens;

    }
}