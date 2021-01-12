package pkj33;

import java.util.ArrayList;
import java.util.HashSet;


public class PA {

	 public final Token token;

    public PA(ArrayList<Token.TOKENTYPE> tokenName, ArrayList<String> tokens, HashSet<String> identifiers) {
        token = new Token(tokenName, tokens, identifiers);
    }
    public void checkExp() {
        checkFactor();
        if (token.getCurrentTokenType() == Token.TOKENTYPE.op) {
            checkFactor();
            return;
        }
    }
    public void init() {
        if ("PROGRAM".equals(token.getCurrentToken()) && token.getNextTokenType() == Token.TOKENTYPE.prog_name && "VAR".equals(token.getNextToken())) {
            CG.generateStartOfProgram(token.peakPreviousToken());
            checkIdList();
            CG.generateIdList();
        } else {
            return;
        }
        if ("BEGIN".equals(token.getNextToken())) {
            checkStmt();
        }
    }
    public void checkRead() {
        if ("READ".equals(token.getCurrentToken()) && token.getNextTokenType() == Token.TOKENTYPE.opening_brackets) {
            CG.readSubRoutine();
            checkIdList();
        }
        if (token.getNextTokenType() == Token.TOKENTYPE.close_brackets) {
            CG.generateIdListForMethod();
            return;
        }
    }

    public void checkWrite() {
        if ("WRITE".equals(token.getCurrentToken()) && token.getNextTokenType() == Token.TOKENTYPE.opening_brackets) {
            CG.writeSubRoutine();
            checkIdList();
        }
        if (token.getNextTokenType() == Token.TOKENTYPE.close_brackets) {
            CG.generateIdListForMethod();
            return;
        }
    }

   



   
  
    public void checkFactor() {
        if (token.getNextTokenType() == Token.TOKENTYPE.identifier && token.getNextTokenType() == Token.TOKENTYPE.op) {
            checkExp();
            return;
        }
        if (token.getPreviousTokenType() == Token.TOKENTYPE.identifier) {
            return;
        }
    }

    public void checkAssign() {
        if (token.getNextTokenType() == Token.TOKENTYPE.identifier && ":=".equals(token.getNextToken())) {
            CG.loadDestination(token.peakPreviousToken());
            checkExp();
        }
    }

  

    public void checkIdList() {
        if (token.getNextTokenType() == Token.TOKENTYPE.identifier && token.getNextTokenType() == Token.TOKENTYPE.seperator) {
            CG.addToListCount(token.peakPreviousToken());
            checkIdList();
            return;
        }
        if (token.getPreviousTokenType() == Token.TOKENTYPE.identifier) {
            CG.addToListCount(token.getCurrentToken());
            return;
        }
    }
    public void checkStmt() {

        if ("END".equals(token.getNextToken())) {
            CG.generateEndOfProgram();
            return;
        }
        if (token.getCurrentTokenType()== Token.TOKENTYPE.end_stmt) {
            checkStmt();
            return;
        }
        if ("READ".equals(token.getCurrentToken())) {
            checkRead();
            checkStmt();
        } else if ("WRITE".equals(token.getCurrentToken())) {
            checkWrite();
            checkStmt();
        } else if (token.isDefinedIdentifier(token.getCurrentToken())) {
            checkAssign();
            checkStmt();
        }else{
            checkStmt();
        }
    }  
}
