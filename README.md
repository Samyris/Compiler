## Objetivo
O objetivo deste projeto é construir um compilador completo para uma dada linguagem de programação.

## Descrição Técnica
Esse projeto foi desenvolvido em Java e foi segmentado em três etapas:

1. Análise Léxica e Tabela de Símbolos;
2. Análise Sintática
3. Análise Semântica
Para cada etapa haverá um release específico contendo as descrições de uso e execução.

## Características da Linguagem
- As palavras-chave são reservadas.
- Toda variável deve ser declarada antes do seu uso.
- Entrada e saída de dados estão limitadas ao teclado e ao monitor.
- A linguagem possui comentários de mais de uma linha, que começa com “/*” e deve terminar com */”.
- A linguagem possui comentários de uma linha. Um comentário começa com “//” e deve terminar com a primeira ocorrência de quebra de linha.
- O comando de atribuição somente é permitido quando o tipo da expressão for igual ao da variável.
- A divisão, quando aplicada a tipos inteiros, resulta em um inteiro.
- O operador % requer que os tipos dos operandos sejam inteiros. Ele resulta do resto da divisão inteira.
- No caso do operador “+”, quando ambos os operandos forem numéricos, ocorre uma soma entre os valores, sendo o resultado também do tipo numérico. Nesse caso, se pelo menos um dos operandos for real, o resultado da soma é um real. Quando ambos os operandos forem do tipo string, o resultado é uma nova string que corresponde à concatenação dos operandos.
- A linguagem é case-sensitive.
- O compilador da linguagem deve 

## Etapas do Compilador

# 1. Análise Léxica e Tabela de Símbolos

- relop ::= "==" | ">" | ">=" | "<" | "<=" | "!="
- addop ::= "+" | "-" | "||"
- mulop ::= "*" | "/" | “%” | "&&"
- constant ::= integer_const | float_const | literal
- integer_const ::= digit+
- float_const ::= digit+ “.”digit+
- literal ::= " { " caractere* " } "
- identifier ::= (letter | _ ) (letter | digit )*
- letter ::= [A-za-z]
- digit ::= [0-9]
- caractere ::= um dos caracteres ASCII, exceto quebra de linha

# 2. Análise Sintática

program ::= start [decl-list] stmt-list exit
decl-list ::= decl {decl}
decl ::= type ident-list ";"
ident-list ::= identifier {"," identifier}
type ::= int | float | string
stmt-list ::= stmt {stmt}
stmt ::= assign-stmt ";" | if-stmt | while-stmt | read-stmt ";" | write-stmt ";"
assign-stmt ::= identifier "=" simple_expr
if-stmt ::= if condition then stmt-list end  | if condition then stmt-list else stmt-list end
condition ::= expression
while-stmt ::= do stmt-list stmt-sufix
stmt-sufix ::= while condition end
read-stmt ::= scan "(" identifier ")"
write-stmt ::= print "(" writable ")"
writable ::= simple-expr | literal
expression ::= simple-expr | simple-expr relop simple-expr
simple-expr ::= term | simple-expr addop term
term ::= factor-a | term mulop factor-a
fator-a ::= factor | "!" factor | "-" factor
factor ::= identifier | constant | "(" expression ")"

Padrões de tokens:

relop ::= "==" | ">" | ">=" | "<" | "<=" | "!="
addop ::= "+" | "-" | "||"
mulop ::= "*" | "/" | “%” | "&&"
constant ::= integer_const | float_const | literal
integer_const ::= digit+
float_const ::= digit+ “.”digit+
literal ::= " { " caractere* " } "
identifier ::= (letter | _ ) (letter | digit )*
letter ::= [A-za-z]
digit ::= [0-9]
caractere ::= um dos caracteres ASCII, exceto quebra de linha