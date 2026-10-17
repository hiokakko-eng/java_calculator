import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Input{
    private Scanner cd;
    private char input;
    private List<Character> charsList = new ArrayList<>();

    /* コンストラクタ */
    public Input(Scanner cd){
        this.cd = cd;
    }

    /* 電卓仕様にするため、入力値をchar1文字に変換する。その後リストへ。 */
    public List<Character> makeInput(){
        do {
            input = this.cd.next().charAt(0);

            charsList.add(input);
            //System.out.println(charsList);

        } while (input != '=');

        return charsList;
    }

}