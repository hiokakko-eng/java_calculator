import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class CalculatorApp{

    public static void main(String[] args){
        System.out.println("これはJavaによる電卓プログラムです。\r\n0123456789.+-*/=を、ターミナル上で一文字ずつEnterし、計算式を記述してください。\r\n最後に=を入力しEnterすると計算結果が表示されます。");

        /* 入力処理 */
        Scanner cd = new Scanner(System.in);   //戻り値は配列。のちにcd.close()。

        Input input = new Input(cd);
        List<Character> charsList = input.makeInput();


        /* 配列の数字部分を数値に変換する */
        Tokenizer tokenList = new Tokenizer(charsList);
        var test = tokenList.toTokenList();

        //System.out.println(test);

        //
        Parser parser = new Parser(test);
        List<ParsedToken> parsedTokenList = parser.parse();

        Calculator calculator = new Calculator(parsedTokenList);
        List<ParsedToken> totalList = null;
        while(calculator.findCalcRange().startIndex != -1){
            calculator.calculateRange();
            totalList = calculator.reduceRange();
        }
        calculator.calculateRange();
        totalList = calculator.reduceRange();

        if(totalList.get(1).token.type() == Tokenizer.TokenType.EQUAL){
            totalList.remove(1);
        }
        
        Output output = new Output(totalList.get(0));
        output.toTerminal();

        //System.out.println(totalList);
        cd.close();

    }

}