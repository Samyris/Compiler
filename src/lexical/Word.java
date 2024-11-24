package lexical;

public class Word extends Token {
    private String lexeme = "";
    public static final Word and = new Word ("&&", Tag.AND);
    public static final Word or = new Word ("||", Tag.OR);
    public static final Word eq = new Word ("==", Tag.EQ);
    
    public Word (String s, int tag){
    super (tag);
    lexeme = s;
    }
    public String toString(){
    return "" + lexeme;
    }
}
