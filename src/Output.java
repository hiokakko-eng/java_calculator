import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.math.MathContext;

public class Output {
    private ParsedToken answerParsedToken;

    public Output(ParsedToken answerParsedToken){
        this.answerParsedToken = answerParsedToken;
    }

    public void toTerminal(){
        BigDecimal answer = new BigDecimal(answerParsedToken.token.value());
        System.out.println("Answer: " + answer.toPlainString());
    }

}