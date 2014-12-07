package com.phoenix.common.property.parser.std;

public interface StandardParserConstants {
    int      EOF                           = 0;
    int      _SPACE                        = 1;
    int      _NEW_LINE                     = 2;
    int      _SPACE_OR_NEW_LINE            = 3;
    int      _ESCAPED_CHAR                 = 4;
    int      _IDENTIFIER                   = 5;
    int      _LETTER                       = 6;
    int      _DIGIT                        = 7;
    int      _DECIMAL_LITERAL              = 8;
    int      _HEX_LITERAL                  = 9;
    int      _OCTAL_LITERAL                = 10;
    int      _SQ                           = 11;
    int      _DQ                           = 12;
    int      _REF                          = 13;
    int      ENCODING                      = 14;
    int      UESC                          = 15;
    int      INTEGER_LITERAL               = 16;
    int      IDENTIFIER                    = 17;
    int      DOT                           = 18;
    int      LBRACKET                      = 19;
    int      RBRACKET                      = 20;
    int      VAR                           = 21;
    int      EQUALS                        = 22;
    int      COMMA                         = 23;
    int      CR                            = 24;
    int      SQ_STRING                     = 35;
    int      DQ_STRING                     = 39;
    int      DIRECTIVE_DECLARE             = 41;
    int      DIRECTIVE_INCLUDE             = 42;
    int      DIRECTIVE_BASE                = 43;
    int      DIRECTIVE_NAME                = 44;
    int      DQ_STRING_PART                = 51;
    int      SQ_BLOCK                      = 57;
    int      DQ_BLOCK_PART                 = 65;
    int      DQ_BLOCK                      = 66;
    int      SIMPLE_STRING_PART            = 76;
    int      SIMPLE_STRING                 = 77;
    int      DEFAULT                       = 0;
    int      IN_DIRECTIVE                  = 1;
    int      IN_REFERENCE                  = 2;
    int      IN_VALUES                     = 3;
    int      IN_SIMPLE_STRING_PRE_NEW_LINE = 4;
    int      IN_PRE_SQ_BLOCK               = 5;
    int      IN_PRE_DQ_BLOCK               = 6;
    int      IN_COMMENTS                   = 7;
    int      IN_PRE_VALUES                 = 8;
    int      IN_SQ_STRING                  = 9;
    int      IN_DQ_STRING                  = 10;
    int      IN_DQ_STRING_EX               = 11;
    int      IN_PRE_DIRECTIVE              = 12;
    int      IN_SQ_BLOCK                   = 13;
    int      IN_SQ_BLOCK_END               = 14;
    int      IN_DQ_BLOCK                   = 15;
    int      IN_DQ_BLOCK_END               = 16;
    int      IN_SIMPLE_STRING              = 17;
    int      IN_SIMPLE_STRING_NEW_LINE     = 18;
    String[] tokenImage                    = {
        "<EOF>", "<_SPACE>", "<_NEW_LINE>", "<_SPACE_OR_NEW_LINE>", "<_ESCAPED_CHAR>",
        "<_IDENTIFIER>", "<_LETTER>", "<_DIGIT>", "<_DECIMAL_LITERAL>", "<_HEX_LITERAL>",
        "<_OCTAL_LITERAL>", "\"\\\'\"", "\"\\\"\"", "\"${\"", "\"encoding\"", "\"uesc\"",
        "<INTEGER_LITERAL>", "<IDENTIFIER>", "\".\"", "\"[\"", "\"]\"", "<VAR>", "\"=\"", "\",\"",
        "<CR>", "<token of kind 25>", "<token of kind 26>", "<token of kind 27>",
        "<token of kind 28>", "<token of kind 29>", "<token of kind 30>", "<token of kind 31>",
        "<token of kind 32>", "<token of kind 33>", "\"\\\\\"", "<SQ_STRING>", "<token of kind 36>",
        "<token of kind 37>", "<token of kind 38>", "<DQ_STRING>", "<token of kind 40>",
        "<DIRECTIVE_DECLARE>", "<DIRECTIVE_INCLUDE>", "<DIRECTIVE_BASE>", "<DIRECTIVE_NAME>",
        "<token of kind 45>", "<token of kind 46>", "<token of kind 47>", "<token of kind 48>",
        "<token of kind 49>", "<token of kind 50>", "<DQ_STRING_PART>", "<token of kind 52>",
        "<token of kind 53>", "<token of kind 54>", "<token of kind 55>", "<token of kind 56>",
        "<SQ_BLOCK>", "<token of kind 58>", "<token of kind 59>", "<token of kind 60>",
        "<token of kind 61>", "<token of kind 62>", "<token of kind 63>", "<token of kind 64>",
        "<DQ_BLOCK_PART>", "<DQ_BLOCK>", "<token of kind 67>", "<token of kind 68>",
        "<token of kind 69>", "<token of kind 70>", "\"\\\\\"", "<token of kind 72>",
        "<token of kind 73>", "<token of kind 74>", "<token of kind 75>", "<SIMPLE_STRING_PART>",
        "<SIMPLE_STRING>", "<token of kind 78>", "<token of kind 79>", "\"}\"",
    };
}

