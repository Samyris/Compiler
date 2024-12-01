
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import lexical.Tag;
import lexical.Token;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        // Solicita o nome do arquivo de entrada ao usuário
        System.out.println("Digite o nome do arquivo:");
        Scanner scanner = new Scanner(System.in);
        String filename = scanner.nextLine();  // Lê o nome do arquivo
        Lexer lexer = new Lexer(filename);  // Cria o Lexer para realizar a análise léxica do arquivo

        try {
            // Tenta abrir o arquivo especificado
            File file = new File(filename);
            if (!file.exists()) {
                System.out.println("Arquivo não encontrado.");
                return;
            }

        

            // Lê o arquivo e imprime o conteúdo linha por linha
            Scanner fileReader = new Scanner(file);
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                System.out.println(line);  // Exibe cada linha do arquivo
            }
            fileReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado.");
            e.printStackTrace();
            return;
        }

        // Realiza a análise léxica do arquivo
        boolean end = false;

        do {
            try {
                Token token = lexer.scan();  // Lê o próximo token do lexer

                switch (token.tag) {
                    case Tag.EOF:  // Fim do arquivo
                        System.out.println("Fim de arquivo.");
                        end = true;
                        break;
                    case Tag.INVALID_TOKEN:  // Token inválido
                        System.out.printf("Erro: Token inválido encontrado.\n");
                        end = true;
                        break;
                    default:
                        System.out.printf("Token: \"%s\", Tag: %d\n", token.toString(), token.tag);
                        break;
                }
            } catch (IOException e) {
                System.out.println("Erro ao ler o arquivo.");
                e.printStackTrace();
                break;
            }

        } while (!end);

        scanner.close();
    }
}