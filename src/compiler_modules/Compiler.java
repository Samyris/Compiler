package compiler_modules;

import compiler_modules.lexer.*;
import compiler_modules.parser.Parser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import shared.errors.CompilerError;

public class Compiler {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite o nome do arquivo: ");
        String fileName = scanner.nextLine();

        scanner.close();

        System.out.println("Processando análise léxica e sintática: ");

        // Processa o arquivo usando o Lexer
        try {
            String sourceCode = new String(Files.readAllBytes(Paths.get("C:\\Users\\Samyris\\Documents\\CEFET\\Compiladores\\Compiler\\test_programs\\" + fileName)));
            Lexer lexer = new Lexer(sourceCode);
            List<Token> tokens = new ArrayList<>();
            Token token;

            while ((token = lexer.scan()) != null) {
                tokens.add(token);
            }

            // Processa os tokens usando o Parser
            Parser parser = new Parser(tokens);
            parser.parse();

            System.out.println("Parsing completed successfully.");
        } catch (IOException e) {
            System.err.println("Error reading source file: " + e.getMessage());
        } catch (CompilerError e) {
            System.err.println("Syntax error at line " + e.getLine() + ": " + e.getMessage());
        }
    }
}