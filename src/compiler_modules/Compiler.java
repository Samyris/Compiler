package compiler_modules;

import compiler_modules.lexer.*;
import java.util.Scanner;

public class Compiler {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite o nome do arquivo: ");
        String fileName = scanner.nextLine();

        scanner.close();

        System.out.println("Processando análise léxica: ");

        // Processa o arquivo usando o Lexer
        try {
            Lexer lexer = new Lexer("C:\\Users\\Samyris\\Documents\\CEFET\\Compiladores\\Compiler\\test_programs\\" + fileName);
            Token token;

            while ((token = lexer.scan()) != null) {
                // System.out.println(token); // Imprime cada token
                System.out.println(
                        String.format("Token: <\"%s\", %s>, Tag: %s", token, token.mapTagToString(),
                                token.getTagValueAsString()));

            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}