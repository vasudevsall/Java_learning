import java.io.CharConversionException;
import java.util.Scanner;
import java.util.regex.*;
import java.util.*;

public class Smart_Calc {

    private static class useStack{
        private String expression, revPolish;
        Deque<String> stack = new ArrayDeque<>();
        private Map<String,Integer> internalVar = new HashMap<>();
        private final Map<String, Integer> precedence = Map.of(
                "(", 5,
                "^",4,
                "/",3,
                "*",3,
                "+",2,
                "-",2,
                ")",1
        );

        useStack(String expression){
            this.expression = expression;
            revPolish = "";
        }

        public String makeRevPolish(){
            char[] exp = expression.toCharArray();
            String temp = "", currentVar;
            boolean numberLast = false;
            int index = 0, prec;
            for(int i=0; i<exp.length; i++){
                if(Character.isWhitespace(exp[i]))  continue;
                if(Character.isDigit(exp[i])) {
                    while (Character.isDigit(exp[i])) {
                        temp = temp + exp[i];
                        i++;
                        if (i >= exp.length)
                            break;
                    }
                    currentVar = "var" + index;
                    index++;
                    try{
                        internalVar.put(currentVar, Integer.parseInt(temp));
                        revPolish = revPolish + currentVar + " ";
                    } catch(NumberFormatException e){
                        return "invalid";
                    }
                    temp = "";
                    numberLast = true;
                    i--;
                } else {
                    String expNow = Character.toString(exp[i]);
                    if (numberLast && !precedence.containsKey(expNow)) return "invalid";
                    numberLast = false;

                    prec = precedence.get(expNow);
                    if (prec == 5) {
                        stack.offerLast(expNow);
                    } else if (prec != 1) {
                        if (stack.isEmpty())
                            stack.offerLast(expNow);
                        else if(stack.peekLast().equals("("))
                            stack.offerLast(expNow);
                        else if (prec > precedence.get(stack.peekLast())){
                            stack.offerLast(expNow);
                        }
                        else {
                            String last = stack.peekLast();
                            if(!last.equals("(")){
                                while (prec <= precedence.get(last) && !last.equals("(")) {
                                    revPolish = revPolish + stack.pollLast() + " ";
                                    if (stack.peekLast() == null)
                                        break;
                                    if(stack.peekLast().equals("("))    break;
                                }
                            }
                            stack.offerLast(expNow);
                        }
                    } else {
                        try {
                            String last = stack.removeLast();
                            if(last.equals("(")){
                                return "Invalid";
                            }
                            revPolish = revPolish + last + " ";
                            while (!last.equals("(")) {
                                last = stack.removeLast();
                                if(last.equals("(")) {
                                    break;
                                }
                                revPolish = revPolish + last + " ";
                            }
                        } catch (NoSuchElementException e) {
                            return "Invalid";
                        }
                    }
                }
            }

            try {
                String last = stack.removeLast();
                revPolish = revPolish + last + " ";
                while(!last.equals("(")){
                    last = stack.removeLast();
                    if(last.equals("(")){
                        return "Invalid";
                    }
                    revPolish = revPolish + last + " ";
                }
            } catch (NoSuchElementException e){
                return revPolish;
            }
            return revPolish;
        }

        public String solveEq(){
            Scanner scan = new Scanner(revPolish);
            String temp = "";
            while(scan.hasNext()){
                temp = scan.next();
                String t1="", t2="";
                if(precedence.containsKey(temp)){
                    t1 = stack.pollLast();
                    t2 = stack.pollLast();
                    if(temp.equals("^"))
                        temp = Integer.toString((int)Math.ceil(Math.pow((double)internalVar.get(t2),(double)internalVar.get(t1))));
                    else if(temp.equals("/"))
                        temp = Integer.toString(internalVar.get(t2)/internalVar.get(t1));
                    else if(temp.equals("*"))   temp = Integer.toString(internalVar.get(t1) * internalVar.get(t2));
                    else if(temp.equals("+"))   temp = Integer.toString(internalVar.get(t1) + internalVar.get(t2));
                    else if(temp.equals("-") && t2!=null)   temp = Integer.toString(internalVar.get(t2) - internalVar.get(t1));
                    else if(temp.equals("-") && t2==null)   temp = "-" + internalVar.get(t1);
                }
                if(t1.length() > 0){
                    internalVar.remove(t1);
                    internalVar.put(t1, Integer.parseInt(temp));
                    temp = t1;
                }
                stack.offerLast(temp);
            }
            return Integer.toString(internalVar.get(stack.pollLast()));
        }
    }

    public static String removePlus(String inp) {
        String regex = "(\\+)+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(inp);
        return matcher.replaceAll("+");
    }
    public static String removeMinus(String inp) {
        String regex = "(--)+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(inp);
        return matcher.replaceAll("+");
    }
    public static String removeVar(Map<String, Integer> variableMap, String inp){
        char[] exp = inp.toCharArray();
        String cleanString = "", variable = "";
        boolean lastSpace = false;

        for(int i=0; i<exp.length; i++){
            if(Character.isWhitespace(exp[i])){
                continue;
            }
            if(lastSpace){
                lastSpace = false;
                if(exp[i]!='^' && exp[i]!='/' && exp[i]!='*' && exp[i]!='+' && exp[i]!='-' && exp[i]!='(' && exp[i]!=')')   return "Invalid";
            }
            if(i==0){
                if(exp[i]=='^' || exp[i]=='/' || exp[i]=='*'|| exp[i]==')')   return "Invalid";
            }
            if(Character.isLetter(exp[i])){
                while(Character.isLetter(exp[i])){
                    variable = variable + exp[i];
                    i++;
                    if(i>=exp.length)   break;
                }
                i--;
                if(variableMap.containsKey(variable))
                    cleanString = cleanString + variableMap.get(variable);
                else return "Unknown";
                variable = "";
                lastSpace = true;
                continue;
            }
            if(Character.isDigit(exp[i])){
                while(Character.isDigit(exp[i])){
                    variable = variable + exp[i];
                    i++;
                    if(i>=exp.length)   break;
                }
                i--;
                cleanString = cleanString + variable;
                variable = "";
                lastSpace = true;
                continue;
            }
            cleanString = cleanString + Character.toString(exp[i]);
        }
        return cleanString;
    }

    public static void arithmetic(Map<String, Integer> variableMap, String inp){
        inp = removeMinus(inp);
        inp = removePlus(inp);
        inp = removeVar(variableMap, inp);
        if(inp.matches(".*\\*{2,}.*") || inp.matches(".*/{2,}.*") || inp.matches(".*\\^{2,}.*"))  inp = "invalid";
        if(inp.equalsIgnoreCase("invalid")){
            System.out.println("Invalid expression");
            return;
        }
        if(inp.equalsIgnoreCase("unknown")){
            System.out.println("Unknown variable");
            return;
        }
        useStack sol = new useStack(inp);
        String revPol = sol.makeRevPolish();
        if(revPol.equalsIgnoreCase("invalid")){
            System.out.println("Invalid expression");
            return;
        }
        String output = sol.solveEq();
        System.out.println(output);
    }

    public static void variableFunc(Map<String, Integer> variableMap, String inp){
        Scanner scan = new Scanner(inp);
        scan.useDelimiter("=");
        String varName = scan.next().trim();
        int varValueInt = 0;
        String varValue = "";
        if(!varName.matches("[a-zA-Z]+")){
            System.out.println("Invalid identifier");
            return;
        }

        if(!scan.hasNext()){
            System.out.println("Invalid assignment");
            return;
        }

        varValue = scan.next().trim();
        if (scan.hasNext()){
            System.out.println("Invalid assignment");
            return;
        }

        try{
            varValueInt = Integer.parseInt(varValue);
        } catch (NumberFormatException e) {
            if(!varValue.matches("[a-zA-Z]+")){
                System.out.println("Invalid assignment");
                return;
            }
            if(!variableMap.containsKey(varValue)){
                System.out.println("Unknown variable");
                return;
            }

            varValueInt = variableMap.get(varValue);
        }

        variableMap.remove(varName);
        variableMap.put(varName, varValueInt);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> variableMap = new HashMap<>();
        String inp;
        System.out.println("If you don't know what to do try typing \"/help\":");
        do {
            inp = scanner.nextLine();
            inp = inp.trim();
            if(inp.equals("/help")) {
                System.out.println("== Help ==");
                System.out.println("This is a smart calculator, the features are:");
                System.out.println("1. You can use variable (containing only alphabets), like a=2, b=3");
                System.out.println("2. You can use the following operations: ^, *, /, +, -");
                System.out.println("3. You can input an equation like: (a+b)*5, the answer to which is 25");
                System.out.println("4. You can use multiple + and - operations like, 2 -- 2, which is equivalent to 2 -(-2).");
                System.out.println("5. Use \"/exit\" to exit");
            } else if(inp.equals("/exit")) {
                System.out.println("Bye!");
                break;
            }else if(inp.length()>0 && inp.charAt(0) == '/'){
                System.out.println("Unknown command");
            } else if(inp.matches(".*=.*")){
                variableFunc(variableMap, inp);
            }else if(inp.length() > 0){
                arithmetic(variableMap, inp);
            }
        }while(true);
    }
}