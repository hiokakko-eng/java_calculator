import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

class Position {
    private final int depthNumber;
    private final int parenNumber;

    Position(int depthNumber, int parenNumber) {
        this.depthNumber = depthNumber;
        this.parenNumber = parenNumber;
    }

    public int depth(){
        return this.depthNumber;
    }

    public int parenNumber(){
        return this.parenNumber;
    }

    @Override
    public String toString(){
        return "Position(" + this.depthNumber + ",\"" +this.parenNumber + "\")";
    }
}

class ParsedToken {
    public Token token;
    Position currentPosition;
    Position parentPosition;
//    public Token parentGroup;

    public ParsedToken(Token token, Position currentPosition, Position parentPosition){
        this.token = token;
        this.currentPosition = currentPosition;
        this.parentPosition = parentPosition;
    }

    @Override
    public String toString(){
        return "Token(" + token + ",\"" + currentPosition + ",\"" + parentPosition + "\")";
    }
}

public class Parser {

    private List<Token> tokenList;
    private static final Position ROOT_POSITION = new Position(0, 0);

    public Parser(List<Token> tokenList){
        this.tokenList = tokenList;
    }

    public List<ParsedToken> parse(){
        List<ParsedToken> calcList = new ArrayList<>();
        int depth = 0;   //かっこの階層
        int count = 0;   //ある階層内のかっこの順番

        for (int tokenIndex=0; tokenIndex<tokenList.size(); tokenIndex++){
            Token token = tokenList.get(tokenIndex);
            
            switch (token.type()){
                case LEFT_PAREN:{
                    depth++;
                    count = 0;
                    for (ParsedToken p :calcList){
                        if(p.token.type() == Tokenizer.TokenType.LEFT_PAREN && p.currentPosition.depth() == depth){
                            count++;
                        }
                    }
                    Position currentPosition = new Position(depth, count);

                    Position parentPosition = ROOT_POSITION;
                    int balance = 0;
                    //tokenIndexは、Tokenizerでのリストのインデックスであり、ParsedTokenListのインデックスではない。
                    for (int j = tokenIndex-1; j>=0; j--){
                        Tokenizer.TokenType type = tokenList.get(j).type();
                        if(type == Tokenizer.TokenType.RIGHT_PAREN){
                            balance++;
                        } else if (type == Tokenizer.TokenType.LEFT_PAREN){
                            if(balance == 0){
                                parentPosition = calcList.get(j).currentPosition;
                                break;
                            }
                            balance--;
                        }
                    }
                    
                    calcList.add(new ParsedToken(token, currentPosition, parentPosition));
                    break;
                }
                case RIGHT_PAREN:{
                    int currentParenNumber = -1;
                    for (ParsedToken p :calcList){
                        if(p.token.type() == Tokenizer.TokenType.LEFT_PAREN && p.currentPosition.depth() == depth){
                            currentParenNumber = p.currentPosition.parenNumber();
                        }
                    }
                    Position currentPosition = new Position(depth, currentParenNumber);
                    calcList.add(new ParsedToken(token, currentPosition, null));
                    depth--;
                    break;
                }
                case NUMBER:
                case MULTIPLY:
                case DIVIDE:
                case PLUS:
                case MINUS:
                case EQUAL:{
                    int currentParen = -1;

                    for (ParsedToken p :calcList){
                        if(p.token.type() == Tokenizer.TokenType.LEFT_PAREN && p.currentPosition.depth() == depth){
                            currentParen = p.currentPosition.parenNumber();
                        }
                    }
                    Position currentPosition = new Position(depth, currentParen);
                    calcList.add(new ParsedToken(token, currentPosition, null));
                    break;
                }
            }
        }

        //System.out.println(calcList);

        //BigDecimal BG = new BigDecimal("1");
        return calcList;
    }







}