import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.math.MathContext;

class CalcRange {
    final int startIndex;
    final int endIndex;

    public CalcRange(int startIndex, int endIndex){
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }
}

public class Calculator {

    private List<ParsedToken> parsedTokenList;

    public Calculator(List<ParsedToken> parsedTokenList) {
        this.parsedTokenList = new ArrayList<>(parsedTokenList);
    }

    public CalcRange findCalcRange(){
        int size = parsedTokenList.size();

        //もっとも深くもっとも右側の左カッコをさがす。
        int maxDepth = -1;
        int calcStartIndex = -1;
        for (int i=size-1; i>-1; i--){
            ParsedToken pt = parsedTokenList.get(i);
            if (pt.token.type() == Tokenizer.TokenType.LEFT_PAREN){
                if(pt.currentPosition.depth()>maxDepth){
                    maxDepth = pt.currentPosition.depth();
                    calcStartIndex = i;
                }
            }
        }

        //calcStartIndexをキーにして右かっこも得る。
        int calcEndIndex = -1;
        if (calcStartIndex != -1){
            Position targetPosition = parsedTokenList.get(calcStartIndex).currentPosition;
            for (int i=calcStartIndex+1; i<size; i++){
                ParsedToken pt = parsedTokenList.get(i);
                if (pt.token.type() == Tokenizer.TokenType.RIGHT_PAREN &&
                    pt.currentPosition.depth() == targetPosition.depth() &&
                    pt.currentPosition.parenNumber() == targetPosition.parenNumber()){
                    calcEndIndex = i;
                    break;   //ひとつ目が見つかればとめる。
                }
            }
        }

        return new CalcRange(calcStartIndex, calcEndIndex);
    }

    public List<ParsedToken> calculateRange(){
        CalcRange range = this.findCalcRange();
        int start = range.startIndex;
        int end = range.endIndex;

        //カッコがないときに先頭からカウント
        if (start == -1){
            start = -1;
            end = parsedTokenList.size()-1;
        }
        
        int i = start+1;
        while (i<end){

            BigDecimal result;
            ParsedToken pt = parsedTokenList.get(i);

            //かけ算割り算
            if(pt.token.type()==Tokenizer.TokenType.MULTIPLY || pt.token.type()==Tokenizer.TokenType.DIVIDE){
                if(pt.token.type()==Tokenizer.TokenType.MULTIPLY){
                    BigDecimal leftNum = new BigDecimal(parsedTokenList.get(i-1).token.value());
                    BigDecimal rightNum = new BigDecimal(parsedTokenList.get(i+1).token.value());
                    result = leftNum.multiply(rightNum);
                } else {
                    BigDecimal leftNum = new BigDecimal(parsedTokenList.get(i-1).token.value());
                    BigDecimal rightNum = new BigDecimal(parsedTokenList.get(i+1).token.value());
                    result = leftNum.divide(rightNum, MathContext.DECIMAL64);   //割り切れない小数に対応。
                }
                String num = result.toPlainString(); 
                ParsedToken resultNumber = new ParsedToken(new Token(Tokenizer.TokenType.NUMBER, num), parsedTokenList.get(i-1).currentPosition, null);
                parsedTokenList.set(i-1, resultNumber);
                parsedTokenList.remove(i);
                parsedTokenList.remove(i);
                i = Math.max(start+1, i-1);
                end = end-2;
                continue;
            }
            i++;
        }

        //初期化
        if (start == -1){
            start = -1;
            end = parsedTokenList.size()-1;
        }
        i = start+1;

        while (i<end){
            //if(start==-1){break;}

            BigDecimal result;
            ParsedToken pt = parsedTokenList.get(i);

            //たし算引き算
            if(pt.token.type()==Tokenizer.TokenType.PLUS || pt.token.type()==Tokenizer.TokenType.MINUS){
                if(pt.token.type()==Tokenizer.TokenType.PLUS){
                    BigDecimal leftNum = new BigDecimal(parsedTokenList.get(i-1).token.value());
                    BigDecimal rightNum = new BigDecimal(parsedTokenList.get(i+1).token.value());
                    result = leftNum.add(rightNum);
                } else {
                    BigDecimal leftNum = new BigDecimal(parsedTokenList.get(i-1).token.value());
                    BigDecimal rightNum = new BigDecimal(parsedTokenList.get(i+1).token.value());
                    result = leftNum.subtract(rightNum);
                }
                String num = result.toPlainString(); 
                ParsedToken resultNumber = new ParsedToken(new Token(Tokenizer.TokenType.NUMBER, num), parsedTokenList.get(i-1).currentPosition, null);
                parsedTokenList.set(i-1, resultNumber);
                parsedTokenList.remove(i);
                parsedTokenList.remove(i);
                i = Math.max(start+1, i-1);
                end = end-2;
                continue;
            }
            i++;
        }
        
        //System.out.println(parsedTokenList);
        //System.out.println("---CALC---");
        return parsedTokenList;
    }

    public List<ParsedToken> reduceRange(){
        CalcRange range = this.findCalcRange();
        int start = range.startIndex;
        int end = range.endIndex;

        List<ParsedToken> pt = parsedTokenList;
        if(start != -1){
            pt.get(start+1).currentPosition = pt.get(start).parentPosition;
            pt.remove(end);
            pt.remove(start);
            //System.out.println("---REDUCE---");
        }

        return pt;
    }
}