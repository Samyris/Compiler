package compiler_modules;

import java.util.Scanner;

import compiler_modules.lexer.*;
import compiler_modules.parser.*;

public class Compiler {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite o nome do arquivo: ");
        String fileName = scanner.nextLine();

        scanner.close();

        try {
            Lexer lexer = new Lexer("test_programs/" + fileName);
            Token token;

            if (args[0].equals("0")) {
                System.out.println("Processando análise léxica . . . ");
                while ((token = lexer.scan()) != null) {
                    System.out.println(
                            String.format("Token: <\"%s\", %s>, Tag: %s", token.getStringRepresentation(),
                                    token.mapTagToString(),
                                    token.getTagValueAsString()));
                }
            }

            if (args[0].equals("1")) {
                System.out.println("Processando analise sintatica e semantica . . . ");
                Parser parser = new Parser(lexer);
                parser.parse();
                System.out.println("Analise sintatica concluida com sucesso!");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
