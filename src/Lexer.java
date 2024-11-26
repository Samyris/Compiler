
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import lexical.LiteralInteger;
import lexical.LiteralFloat;
import lexical.LiteralString;
import lexical.Tag;
import lexical.Token;
import lexical.Word;

public class Lexer {

    public static int line = 1; //contador de linhas
    private char ch = ' '; //caractere lido do arquivo
    private FileReader file;
    private Hashtable words = new Hashtable();

    /* Método para inserir palavras reservadas na HashTable */
    private void reserve(Word w) {
        //words.put(w.getLexeme(), w);
    }

    public Lexer(String fileName) throws FileNotFoundException {
        try {
            file = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado");
            throw e;
        }
        //Insere palavras reservadas na HashTable
        reserve(new Word("if", Tag.IF));
        reserve(new Word("end", Tag.END));
        //reserve(new Word("type", Tag.TYPE));
        //reserve(new Word("int", Tag.INT));
        /*Lê o próximo caractere do arquivo*/
    }

    private void readch() throws IOException {
        ch = (char) file.read();
    }

    /* Lê o próximo caractere do arquivo e verifica se é igual a c*/
    private boolean readch(char c) throws IOException {
        readch();
        if (ch != c) {
            return false;
        }
        ch = ' ';
        return true;
    }

    public Token scan() throws IOException {
//Desconsidera delimitadores na entrada
        for (;; readch()) {
            if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\b') {
                continue; 
            }else if (ch == '\n') {
                line++; //conta linhas
             }else {
                break;
            }
        }
        switch (ch) {
//Operadores
            case '&':
                if (readch('&')) {
                    return Word.and; 
                }else {
                    return new Token('&');
                }
            case '|':
                if (readch('|')) {
                    return Word.or; 
                }else {
                    return new Token('|');
                }
            case '=':
                if (readch('=')) {
                    return Word.eq; 
                }else {
                    return new Token('=');
                }

        }
//Números
        if (Character.isDigit(ch)) {
            int value = 0;
            do {
                value = 10 * value + Character.digit(ch, 10);
                readch();
            } while (Character.isDigit(ch));
            return new IntegerNumber(value);
            //FALTA DO FLOAT
        }
//Identificadores
        if (Character.isLetter(ch)) {
            StringBuffer sb = new StringBuffer();
            do {
                sb.append(ch);
                readch();
            } while (Character.isLetterOrDigit(ch));
            String s = sb.toString();
            Word w = (Word) words.get(s);
            if (w != null) {
                return w; //palavra já existe na HashTable

            }w = new Word(s, Tag.ID);
            words.put(s, w);
            return w;
        }
        //Caracteres não especificados
        Token t = new Token(ch);
        ch = ' ';
        return t;
    }
}
