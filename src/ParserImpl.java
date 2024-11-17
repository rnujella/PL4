public class ParserImpl extends Parser {

    @Override
    public Expr do_parse() throws Exception {
        return parseT();
    }

    private Expr parseT() throws Exception {
        Expr l = parseF();
        if (peek(TokenType.PLUS, 0) || peek(TokenType.MINUS, 0)) {
            Token op = consume(peek(TokenType.PLUS, 0) ? TokenType.PLUS : TokenType.MINUS);
            Expr right = parseT();
            return op.ty == TokenType.PLUS ? new PlusExpr(l, right) : new MinusExpr(l, right);
        }
        return l;
    }

    private Expr parseF() throws Exception {
        Expr l = parseLit();
        if (peek(TokenType.TIMES, 0) || peek(TokenType.DIV, 0)) {
            Token op = consume(peek(TokenType.TIMES, 0) ? TokenType.TIMES : TokenType.DIV);
            Expr right = parseF();
            return op.ty == TokenType.TIMES ? new TimesExpr(l, right) : new DivExpr(l, right);
        }
        return l;
    }

    private Expr parseLit() throws Exception {
        if (peek(TokenType.NUM, 0)) {
            Token num = consume(TokenType.NUM);
            return new FloatExpr(Float.parseFloat(num.lexeme));
        } else if (peek(TokenType.LPAREN, 0)) {
            consume(TokenType.LPAREN);
            Expr expr = parseT();
            consume(TokenType.RPAREN);
            return expr;
        }
        throw new Exception("Expected literal");
    }
}
