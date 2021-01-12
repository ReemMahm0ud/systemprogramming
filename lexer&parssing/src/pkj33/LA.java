package pkj33;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import pkj33.Token.TOKENTYPE;
 public  class LA {
	 public ArrayList<Token.TOKENTYPE> tokenName = new ArrayList<>();
	 public ArrayList<String> tokens = new ArrayList<>();
	 public HashSet<String> identifiers = new HashSet<>();

	 public String programName;
	 public String[] variables;

   

    public void printIdentifiers() {
        System.out.println("identifers");
        for (String item : identifiers) {
            System.out.println(item);
        }

    }

    public void printTokens() {
        for (int i = 0; i < tokens.size(); i++) {
            System.out.printf("%-20s", tokenName.get(i));

            System.out.println(tokens.get(i));
        }
    }

  

    public String getProgramName() {
        return programName;
    }

    public  void setProgramName(String programName) {
        this.programName = programName;
        addToken(TOKENTYPE.prog_name, programName);
    }
    public ArrayList<TOKENTYPE> getTokenNameList() {
        return tokenName;
    }


    public boolean isStartOfProgram(String line) {
        boolean flag;
        String pattern = "(?i)\\s*PROGRAM\\s+(\\w+)\\s*";
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(line);
        flag = matcher.find();
        if (flag) {
            addToken(TOKENTYPE.keyword, "PROGRAM");
            setProgramName(matcher.group(1));
        }

        return flag;
    }

  public void setVariables(String line) {
        variables = line.trim().split("[,\\s+]");

        identifiers.add(variables[0]);
        addToken(Token.TOKENTYPE.identifier, variables[0]);
        for (int i = 1; i < variables.length; i++) {
            addToken(TOKENTYPE.seperator, ",");
            addToken(TOKENTYPE.identifier, variables[i]);

        }
    }

  
  public boolean checkOpenningAndClosingBrackets(String line) {
      char[] charArray = line.toCharArray();
      int openingBrackets = 0;
      int closingBrackets = 0;
      for (char item : charArray) {
          if (item == '(') {
              openingBrackets++;
          } else if (item == ')') {
              closingBrackets++;
          }
      }
      if (closingBrackets != openingBrackets) {
          return false;
      }
      return true;
  }
   
   
   public ArrayList<String> getTokensList() {
       return tokens;
   }
    public boolean isReadMethod(String line) {
        boolean flag;
        String pattern = "(?i)\\s*READ\\s*\\((\\s*[a-zA-Z_]\\w*\\s*(,\\s*[a-zA-Z_]\\w*\\s*)*)\\)\\s*;";
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(line);
        flag = matcher.find();
        if (flag) {
            addToken(TOKENTYPE.stmt, "READ");
            addToken(TOKENTYPE.opening_brackets, "(");
            readArgumentsAndAddTokens(matcher.group(1));
            addToken(TOKENTYPE.close_brackets, ")");
            addToken(TOKENTYPE.end_stmt, ";");
        }

        return flag;
    }

    public void readArgumentsAndAddTokens(String line) {

        String[] methodArguments = line.replaceAll(" ", "").split(",");
        addToken(TOKENTYPE.identifier, methodArguments[0]);
        for (int i = 1; i < methodArguments.length; i++) {
            addToken(TOKENTYPE.seperator, ",");
            addToken(TOKENTYPE.identifier, methodArguments[i]);

        }
    }
    public String[] getVariables() {
        return variables;
    }
    public void tokenizeAssignment(String line) {
        ArrayList<String> tokens = new ArrayList<>();
        String token = "";
        line = line.replaceAll(" ", "");
        char[] charArray = line.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] == '+' || charArray[i] == '*') {
                addToken(TOKENTYPE.identifier, token);
                addToken(TOKENTYPE.op, String.valueOf(charArray[i]));
                token = "";
            } else if (charArray[i] == '(') {
                addToken(TOKENTYPE.opening_brackets, String.valueOf(charArray[i]));
            } else if (charArray[i] == ')') {
                addToken(Token.TOKENTYPE.identifier, token);
                token = "";
                addToken(Token.TOKENTYPE.close_brackets, String.valueOf(charArray[i]));
            } else {
                token += charArray[i];
            }
        }
        if (token != "") {
            addToken(Token.TOKENTYPE.identifier, token);
        }
    }

    public void read(String filePath) throws SyntaxError {
        FileReader fileReader = null;
        try {
            File file = new File(filePath);
            fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);
            String newLine = null;
            if (!isStartOfProgram(reader.readLine())) {
                throw new SyntaxError("Start of program is not correct");
            }
            if (isVar(reader.readLine())) {
                setVariables(reader.readLine());
            } else {
                System.out.println("There are no variables in the program");
            }
            if (!isBegin(reader.readLine())) {
                throw new SyntaxError("Begin statement missing");
            }

            while ((newLine = reader.readLine()) != null && !"END".equals(newLine.trim())) {
                if (isReadMethod(newLine)) {

                } else if (isWriteMethod(newLine)) {

                } else if (isAssignment(newLine)) {

                } else {
                    throw new SyntaxError("Undefined Instruction");
                }
            }
            addToken(Token.TOKENTYPE.keyword, "END");

            printTokens();
            reader.close();

            new PA(tokenName, tokens, identifiers).init();
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        } finally {
            try {
                fileReader.close();
            } catch (IOException ex) {

            }
        }
    }
   public void addToken(TOKENTYPE key, String token) {

        tokenName.add(key);
        tokens.add(token);
    }

   
   public boolean isBegin(String line) {
       boolean flag;
       String pattern = "(?i)\\s*BEGIN\\s*";
       Pattern r = Pattern.compile(pattern);
       Matcher matcher = r.matcher(line);
       flag = matcher.find();
       if (flag) {
           addToken(TOKENTYPE.keyword, "BEGIN");
           System.out.println("Start of the program logic.");
       }
       return flag;
   }

 

  public boolean isWriteMethod(String line) {
        boolean flag;
        String pattern = "(?i)\\s*WRITE\\s*\\((\\s*[a-zA-Z_]\\w*\\s*(,\\s*[a-zA-Z_]\\w*\\s*)*)\\)\\s*;";
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(line);
        flag = matcher.find();

        if (flag) {
            addToken(TOKENTYPE.stmt, "WRITE");
            addToken(TOKENTYPE.opening_brackets, "(");
            readArgumentsAndAddTokens(matcher.group(1));
            addToken(TOKENTYPE.close_brackets, ")");
            addToken(TOKENTYPE.end_stmt, ";");
        }

        return flag;

    }

    public boolean isVar(String line) {
        boolean flag;
        String pattern = "(?i)\\s*VAR\\s*";
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(line);
        flag = matcher.find();
        if (flag) {
            addToken(TOKENTYPE.keyword, "VAR");
            System.out.println("Next line is the decalaration of variables");
        }
        return flag;
    }

    public boolean isNumber(String line) {
        String pattern = "(?i)\\d+";
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(line);
        return matcher.find();
    }
  public class SyntaxError extends  Exception{
        
        public SyntaxError(String message){
            super(message);
        }
        
    }

    public boolean isidentifier(String line) {
        String pattern = "(?i)[_$\\w]+";
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(line);
        return matcher.find();
    }

    public boolean isAssignment(String line) {
        String pattern1 = "\\s*([a-zA-Z]\\w*)\\s*:=\\s*(\\(?([a-zA-Z]\\w*|\\d+)\\s*([+*]\\s*\\(?([a-zA-Z]+\\w*|\\d+)\\s*\\)?)+)\\s*;";
        String pattern2 = "\\s*([a-zA-Z]\\w*)\\s*:=\\s*([a-zA-Z]\\w*|\\d+)\\s*;";

        Pattern r1 = Pattern.compile(pattern1);
        Pattern r2 = Pattern.compile(pattern2);
        Matcher matcher1 = r1.matcher(line);
        Matcher matcher2 = r2.matcher(line);
        if (matcher1.find() && checkOpenningAndClosingBrackets(line)) {

            addToken(TOKENTYPE.stmt, matcher1.group(1));
            addToken(TOKENTYPE.op, ":=");
            tokenizeAssignment(matcher1.group(2));
            addToken(TOKENTYPE.end_stmt, ";");
            return true;
        } else if (matcher2.find() && checkOpenningAndClosingBrackets(line)) {

            addToken(TOKENTYPE.stmt, matcher2.group(1));
            addToken(TOKENTYPE.op, ":=");
            addToken(TOKENTYPE.identifier, matcher2.group(2));
            addToken(TOKENTYPE.end_stmt, ";");
            return true;
        }
        return false;
    }
    
  
}

 