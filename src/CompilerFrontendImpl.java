public class CompilerFrontendImpl extends CompilerFrontend {
    public CompilerFrontendImpl() {
        super();
    }

    public CompilerFrontendImpl(boolean debug_) {
        super(debug_);
    }

    @Override
    protected void init_lexer() {
        lex = new LexerImpl();

        AutomatonImpl numAutomaton = new AutomatonImpl();
        numAutomaton.addState(0, true, false);
        numAutomaton.addState(1, false, false);
        numAutomaton.addState(2, false, true);
        numAutomaton.addTransition(0, '.', 1);
        for (char c = '0'; c <= '9'; c++) {
            numAutomaton.addTransition(0, c, 0);
            numAutomaton.addTransition(1, c, 2);
            numAutomaton.addTransition(2, c, 2);
        }
        lex.add_automaton(TokenType.NUM, numAutomaton);

        // Add automata for other token types
        lex.add_automaton(TokenType.PLUS, singleCharAutomaton('+'));
        lex.add_automaton(TokenType.MINUS, singleCharAutomaton('-'));
        lex.add_automaton(TokenType.TIMES, singleCharAutomaton('*'));
        lex.add_automaton(TokenType.DIV, singleCharAutomaton('/'));
        lex.add_automaton(TokenType.LPAREN, singleCharAutomaton('('));
        lex.add_automaton(TokenType.RPAREN, singleCharAutomaton(')'));

        AutomatonImpl whitespaceAutomaton = new AutomatonImpl();
        whitespaceAutomaton.addState(0, true, true);
        whitespaceAutomaton.addTransition(0, ' ', 0);
        whitespaceAutomaton.addTransition(0, '\n', 0);
        whitespaceAutomaton.addTransition(0, '\r', 0);
        whitespaceAutomaton.addTransition(0, '\t', 0);
        lex.add_automaton(TokenType.WHITE_SPACE, whitespaceAutomaton);
    }

    private Automaton singleCharAutomaton(char c) {
        AutomatonImpl automaton = new AutomatonImpl();
        automaton.addState(0, true, false);
        automaton.addState(1, false, true);
        automaton.addTransition(0, c, 1);
        return automaton;
    }
}
