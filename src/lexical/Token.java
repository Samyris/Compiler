package lexical;

public class Token {

    public final int tag; // Identificador do tipo do token

    public Token(int tag) {
        this.tag = tag;
    }

    private String getAsciiValue(int tag1) {
        if (tag >= 0 && tag <= 127) { // Apenas para valores no intervalo ASCII
            return String.valueOf((char) tag);
        }
        return null; // Não aplicável fora do intervalo ASCII
    }

    @Override
    public String toString() {
        // Retorna o nome da tag usando o método estático Tag.getName
        return "< " +  getAsciiValue(tag) + " , " +Tag.getName(tag) + " >";
    }
}
