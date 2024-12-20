
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import lexical.LiteralFloat;
import lexical.LiteralInteger;
import lexical.LiteralString;
import lexical.Tag;
import lexical.Token;
import lexical.Word;

public class Lexer {

    public static int line = 1; //contador de linhas
    private char currentChar = ' '; //caractere lido do arquivo
    private FileReader file;
    private static Hashtable<String, Word> words = new Hashtable<>(); //Armazena palavras reseradas previamente 
    public static Hashtable<Token, Integer> errors = new Hashtable<Token, Integer>(); // tabela de erros


    /* Método para inserir palavras reservadas na HashTable */
    private void reserve(Word w) {
        words.put(w.getLexeme(), w);
    }

    public Lexer(String fileName) throws FileNotFoundException {
        try {
            file = new FileReader(fileName);
            initializeReservedWords();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado");
            throw e;
        }
    }

    private void initializeReservedWords() {
        //Insere palavras reservadas na HashTable
        reserve(new Word("start", Tag.START));
        reserve(new Word("exit", Tag.EXIT));
        reserve(new Word("end", Tag.END));
        reserve(new Word("if", Tag.IF));
        reserve(new Word("then", Tag.THEN));
        reserve(new Word("else", Tag.ELSE));
        reserve(new Word("do", Tag.DO));
        reserve(new Word("while", Tag.WHILE));
        reserve(new Word("scan", Tag.SCAN));
        reserve(new Word("print", Tag.PRINT));
        reserve(new Word("int", Tag.TYPE_INT));
        reserve(new Word("float", Tag.TYPE_FLOAT));
        reserve(new Word("string", Tag.TYPE_STRING));
        
        /*Lê o próximo caractere do arquivo*/
    }

    private void readch() throws IOException {
        currentChar = (char) file.read();
    }

    /* Lê o próximo caractere do arquivo e verifica se é igual a c*/
    private boolean readch(char c) throws IOException {
        readch();
        if (currentChar != c) {
            return false;
        }
        currentChar = ' ';
        return true;
    }

    public Token scan() throws IOException {
        //Desconsidera delimitadores na entrada
        for (;; readch()) {
            if (currentChar == ' ' || currentChar == '\t' || currentChar == '\r' || currentChar == '\b') {
                continue;
            } else if (currentChar == '\n') {
                line++; // Conta as linhas
            } else {
                break;
            }
        }
    
        int line_aux = 0;
        while (currentChar == '/') {
            readch(); // Avança para o próximo caractere após '/'
            if (currentChar == '/') { // Comentário de uma linha
                do {
                    readch(); // Lê até o fim do comentário
                    if (currentChar == '\n') {
                        readch();
                        line++;
                        break;
                    }
                } while (currentChar != '\n' && currentChar != (char) -1); // Vai até a quebra de linha ou fim do arquivo
            } else if (currentChar == '*') { // Comentário de múltiplas linhas
                int lineAux = line;
                boolean closed = false;
                do {
                    readch();
                    if (currentChar == '\n') {
                        line++;
                    }
                    if (currentChar == '*') {
                        readch();
                        if (currentChar == '/') {
                            closed = true;
                            readch();
                            break;
                        }
                    }
                } while ((int) currentChar != 65535);


                if (!closed) {
                    Token t = new Token('F');
                    errors.put(t, lineAux);
                    return t;
                }
            } else {
                return Word.div; // Operador de divisão
            }
    
            if (currentChar == 65535) {
                Token t = new Token('F');
                errors.put(t, line_aux);
                return t;
            }
            if (currentChar == ' ' || currentChar == '\t' || currentChar == '\r' || currentChar == '\b') {
                currentChar = '/';
            }
        }

        switch (currentChar) {
            case ';':
                readch();
                return Word.semicolon;
            case ',':
                readch();
                return Word.comma;
            case '(':
                readch();
                return Word.opar;
            case ')':
                readch();
                return Word.cpar;

            //Operadores
            case '&':
                if (readch('&')) {
                    return Word.and; //&&
                } else {
                    Token t = new Token('&');
                    errors.put(t, line);
                    return t;
                }
            case '|':
                if (readch('|')) {
                    return Word.or;
                } else {
                    Token t = new Token('|');
                    errors.put(t, line);
                    return t;
                }
            case '=':
                if (readch('=')) {
                    return Word.eq;
                } else {
                    return Word.assign;
                }
            case '<':
                if (readch('=')) {
                    return Word.lt; //<=
                } else {
                    return Word.lt;
                }
            case '>':
                if (readch('=')) {
                    return Word.ge; //>=
                } else {
                    return Word.gt;
                }
            case '!':
                if (readch('=')) {
                    return Word.dif; //!=
                } else {
                    return Word.not;
                }
            case '+':
                readch();
                return Word.add; // +
            case '-':
                readch();
                return Word.sub; // -
            case '*':
                readch();
                return Word.mul; // *
            case '/':
                readch();
                return Word.div; // /
            case '%':
                readch();
                return Word.module; // %

        }
//Números
        if (Character.isDigit(currentChar)) {
            int value = 0;
            do {
                value = 10 * value + Character.digit(currentChar, 10);
                readch();
            } while (Character.isDigit(currentChar));

            if (currentChar == '.') { // Ponto decimal encontrado.
                readch();
                float floatValue = value;
                float divisor = 10;
                while (Character.isDigit(currentChar)) {
                    floatValue += Character.digit(currentChar, 10) / divisor;
                    divisor *= 10;
                    readch();
                }
                return new LiteralFloat(floatValue); // Retorna como número de ponto flutuante.
            }

            return new LiteralInteger(value); // Retorna como inteiro.
        }

//String
        if (currentChar == '{') {
            StringBuilder sb = new StringBuilder();
            readch(); // Avança o caractere.
            while (currentChar != '}' && currentChar != (char) -1) { // Lê até encontrar '}'
                sb.append(currentChar);
                readch();
            }
            if (currentChar == '}') {
                readch(); // Avança após o fechamento.
                return new LiteralString(sb.toString());
            } else {
                throw new IOException("Literal de string não fechado.");
            }
        }
//Identificadores
        if (Character.isLetter(currentChar) || currentChar == '_') {
            StringBuilder sb = new StringBuilder();
            do {
                sb.append(currentChar);
                readch();
            } while (Character.isLetterOrDigit(currentChar) || currentChar == '_');
            String lexeme = sb.toString();
            Word w = words.get(lexeme);
            if (w != null) {
                return w; // Retorna palavra reservada, se existir.

            }
            w = new Word(lexeme, Tag.ID); // Caso contrário, cria identificador.
            words.put(lexeme, w);
            return w;
        }

        //Caracteres não especificados
        Token t = new Token(currentChar);
        currentChar = ' ';
        return t;

    }
}
