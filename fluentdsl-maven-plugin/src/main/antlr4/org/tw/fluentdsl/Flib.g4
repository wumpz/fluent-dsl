grammar Flib;

start: 
    options?
    rules
    ;

options: OPTIONS '{'
    ( option ';')+  
    '}'
    ;

option: name=ID '=' (value=CHARLITERAL | value=ID);

rules: (interfaceRule | methodRule)+;

interfaceRule locals [boolean hidden=false]: 
            ( '#' {$hidden=true;} )? ID ':' methodExpr ';';

methodExpr:  '(' methodExpr ')'                             #Parenthesis
            /* list needs an interface between. one can add a name for it */
             | methodExpr ( CHARLITERAL? methodExpr )+      #List 
             | left=methodExpr '|' right=methodExpr         #Or
             | expr=methodExpr '+'                          #OneOrMore
             | singleMethod                                 #Simple
            ;

singleMethod: ID methodSignature                        #Word
             | '#' ID                                   #Interface
             ;

methodRule:
        methodDefinition ';';

//definition of method inludig name
methodDefinition:
        name=ID methodSignature 
        ;

//signature of a method (parameters and return type)
methodSignature: 
        //(paramType1 param, ...) returnType
        methodParameters? (RETURNS returnType=ID)?
        ;

//parameters of a method
methodParameters:
        '(' methodParam (',' methodParam)* ')' ;

methodParam: typeName=ID name=ID;

OPTIONS: 'options';
RETURNS: 'returns';

NUMBER: DIGIT+;
ID: LETTER (LETTER | DIGIT)*;
CHARLITERAL: '\'' ~[']* '\''; 

fragment DIGIT: [0-9];
fragment LETTER: [a-zA-Z_];

LINE_COMMENT: '--' ~[\r\n]* -> channel(HIDDEN);
MULTI_LINE_COMMENT: '/*' .*? '*/' -> channel(HIDDEN);

WHITESPACE:  [ \t\r\n] -> channel(HIDDEN);