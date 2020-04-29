 import java.util.*;
import java.io.*;
public class stage_7_7_flashcards {
    public static void addCard(Map<String, String> flash, ArrayList<Integer> mistakes, ArrayList<String> log, BufferedReader reader) throws IOException{
        String temp, temp1;
        System.out.println("The card:");
        log.add("The card:");
        
        temp = reader.readLine();
        log.add(">" + temp);
        
        if(flash.containsKey(temp)){
            System.out.println("The card \"" + temp + "\" already exists.");
            log.add("The card \"" + temp + "\" already exists.");
            
            return;
        }
        System.out.println("The definition of the card:");
        log.add("The definition of the card:");
        
        temp1 = reader.readLine();
        log.add(">" + temp1);
        if(flash.containsValue(temp1)){
            System.out.println("The definition \"" + temp1 + "\"already exists.");
            log.add("The definition \"" + temp1 + "\"already exists.");
            return;
        }
        flash.put(temp, temp1);
        mistakes.add(0);
        System.out.println("The pair (\""+temp+"\":\""+temp1+"\") has been added.");
        log.add("The pair (\""+temp+"\":\""+temp1+"\") has been added.");
        return;
    }
    public static void removeCard(Map<String, String> flash,ArrayList<Integer> mistakes,ArrayList<String> log, BufferedReader reader) throws IOException{
        String temp;
        System.out.println("The card:");
        log.add("The card:");
        temp = reader.readLine();
        log.add(">" + temp);
        
        Set<String> cardKey = new LinkedHashSet<>();
        cardKey = flash.keySet();
        
        ArrayList<String> card = new ArrayList<String>(cardKey);
        
        int removeIndex = card.indexOf(temp);
        
        mistakes.remove(removeIndex);
        
        if(!(flash.containsKey(temp))){
            System.out.println("Can't remove \""+temp+"\": there is no such card.");
            log.add("Can't remove \""+temp+"\": there is no such card.");
            return;
        }
        
        
        flash.remove(temp);
        System.out.println("The card has been removed.");
        log.add("The card has been removed.");
        return;
    }
    public static void readFile(Map<String, String> flash, ArrayList<Integer> mistakes, ArrayList<String> log, String fileName){
        String temp = "", temp1="";
        File fileToRead = new File(fileName);
        int i, count=0;
        if(!(fileToRead.exists())) {
            System.out.println("File not found.");
            log.add("File not found.");
            return;
        }
        
        try(Scanner scan = new Scanner(fileToRead)){
            i = 0;
            while(scan.hasNext()) {
                if(i%3 == 0)
                    temp = scan.nextLine();
                else if(i%3 ==1){
                    temp1 = scan.nextLine();
                    if(flash.containsKey(temp)) {
                        Set<String> cardKey = new LinkedHashSet<>();
                        cardKey = flash.keySet();
                        
                        ArrayList<String> card = new ArrayList<String>(cardKey);
                        
                        int removeIndex = card.indexOf(temp);
                        
                        mistakes.remove(removeIndex);
                        flash.remove(temp);
                        flash.put(temp, temp1);
                    } else {
                        flash.put(temp, temp1);
                    }
                } else {
                    mistakes.add(Integer.parseInt(scan.nextLine()));
                    count++;
                }
                i++;
            }
            System.out.println(count + " cards have been loaded.");
            log.add(count + " cards have been loaded.");
        } catch(FileNotFoundException e){
            System.out.println("File not found.");
            log.add("File not found.");
        }
    }
    public static void importCard(Map<String, String> flash,ArrayList<Integer> mistakes, ArrayList<String> log, BufferedReader reader) throws IOException {
        String fileName, temp = "", temp1 = "";
        System.out.println("File name:");
        log.add("File name:");
        fileName = reader.readLine();
        log.add(">" + fileName);
        readFile(flash, mistakes, log, fileName);
    }
    public static void writeFile(Map<String, String> flash, ArrayList<Integer> mistakes, ArrayList<String> log, String fileName) {
        int i =0;
        
        File fileToWrite = new File(fileName);
        try(PrintWriter writer = new PrintWriter(fileToWrite)){
            
            for(var card: flash.entrySet()) {
                writer.println(card.getKey());
                writer.println(card.getValue());
                writer.println(mistakes.get(i));
                i++;
            }
            
            System.out.println(i+ " cards have been saved.");
            log.add(i+ " cards have been saved.");
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            log.add("File not found.");
        }
    }
    public static void exportCard(Map<String, String> flash,ArrayList<Integer> mistakes, ArrayList<String> log, BufferedReader reader) throws IOException {
        String fileName;
        System.out.println("File name:");
        log.add("File name:");
        fileName = reader.readLine();
        log.add(">" + fileName);
        writeFile(flash, mistakes, log, fileName);
    }
    public static void askCard(Map<String, String> flash,ArrayList<Integer> mistakes, ArrayList<String> log, BufferedReader reader) throws IOException {
        System.out.println("How many times to ask?");
        log.add("How many times to ask?");
        int number = Integer.parseInt(reader.readLine());
        log.add(">" + number);
        int count;
        String temp1, temp2;
        
        Random random = new Random();
        
        Set<String> cardKey = new LinkedHashSet<>();
        cardKey = flash.keySet();
        
        ArrayList<String> card = new ArrayList<String>(cardKey);
        
        int size = card.size();
        
        for(int i=0; i<number; i++){
            int rNo = random.nextInt(size);
            
            System.out.println("Print the definition of \"" + card.get(rNo) + "\":");
            log.add("Print the definition of \"" + card.get(rNo) + "\":");
            temp1 = reader.readLine();
            log.add(">" + temp1);
            temp2 = flash.get(card.get(rNo));
            if(temp1.equals(temp2)) {
                System.out.println("Correct answer.");
                log.add("Correct answer.");
            } else {
                count = 0;
                for(var card1: flash.entrySet()){
                    if(temp1.equals(card1.getValue())){
                        count++;
                        System.out.println("Wrong answer. The correct one is \""+temp2+
                        "\", you've just written the definition of \"" + card1.getKey() + "\".");
                        
                        log.add("Wrong answer. The correct one is \""+temp2+"\", you've just written the definition of \"" + card1.getKey() + "\".");
                        break;
                    }
                }
                if(count == 0) {
                    System.out.println("Wrong answer. The correct one is \"" + temp2 + "\".");
                    log.add("Wrong answer. The correct one is \"" + temp2 + "\".");
                }
                int t = mistakes.get(rNo);
                t++;
                mistakes.set(rNo, t);
            }
        }
    }
    public static void hardestCard(Map<String, String> flash, ArrayList<Integer> mistakes,ArrayList<String> log){
        int maxValue = 0;
        for(int i=0; i<mistakes.size(); i++){
            if(mistakes.get(i)>maxValue)
                maxValue = mistakes.get(i);
        }
        if(maxValue == 0 || mistakes.size() == 0){
            System.out.println("There are no cards with errors.");
            log.add("There are no cards with errors.");
            return;
        }
        int first = mistakes.indexOf(maxValue);
        int last = mistakes.lastIndexOf(maxValue);
        Set<String> cardKey = new LinkedHashSet<>();
        cardKey = flash.keySet();
                    
        ArrayList<String> card = new ArrayList<String>(cardKey);
        
        if(first == last){
            System.out.print("The hardest card is \"" + card.get(first) + "\".");
            System.out.println(" You have "+maxValue+" errors answering it.");
            log.add("The hardest card is \"" + card.get(first) + "\"." + " You have "+maxValue+" errors answering it.");
            return;
        }
        System.out.print("The hardest cards are ");
        String logstr = "The hardest cards are";
        for(int i=0; i<mistakes.size(); i++){
            if(mistakes.get(i).equals(maxValue)){
                System.out.print("\""+card.get(i)+"\"");
                logstr = logstr + ("\""+card.get(i)+"\"");
            }
            if(i!=last) {
                System.out.print(", ");
                logstr = logstr + (", ");
            } else{
                System.out.print(". ");
                logstr = logstr + (". ");
                break;
            }
        }
        System.out.println("You have "+maxValue+" errors answering them.");
        logstr = logstr + ("You have "+maxValue+" errors answering them.");
        log.add(logstr);
    }
    public static void reset(ArrayList<Integer> mistakes, ArrayList<String> log) {
        for(int i=0; i<mistakes.size(); i++)
            mistakes.set(i, 0);
        System.out.println("Card statistics has been reset.");
        log.add("Card statistics has been reset.");
        return;
    }
    public static void logToFile(ArrayList<String> log, BufferedReader reader) throws IOException{
        System.out.println("File name:");
        log.add("File name:");
        String fileName = reader.readLine();
        log.add(">" + fileName);
        
        File fileToWrite = new File(fileName);
        PrintWriter writer = new PrintWriter(fileToWrite);
        writer.flush();
        String temp;
        
        for(int i=0; i<log.size(); i++) {
            temp = log.get(i);
            writer.println(temp);
        }
        
        System.out.println("The log has been saved.");
        log.add("The log has been saved.");
        writer.println("The log has been saved.");
        writer.close();
        return;
    }
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Map<String, String> flash = new LinkedHashMap<>();
        ArrayList<Integer> mistakes = new ArrayList<>();
        ArrayList<String> log = new ArrayList<>();
        String opt, fileNameImport, fileNameExport="";
        int flag = 0;
        if(args.length > 0){
            for(int i=0; i<args.length; i++){
                if(args[i].equals("-import")){
                    fileNameImport = args[i+1];
                    readFile(flash, mistakes, log, fileNameImport);
                } else if(args[i].equals("-export")){
                    fileNameExport = args[i+1];
                    flag = 1;
                }
            }
        }
        do
        {
            System.out.println("Input the action (add, remove, import, export, ask, exit, log, harderst card, reset):");
            log.add("Input the action (add, remove, import, export, ask, exit, log, harderst card, reset):");
            
            opt = reader.readLine();
            log.add(">" + opt);
            
            if(opt.equals("add")){
                addCard(flash, mistakes, log, reader);
            } else if(opt.equals("remove")){
                removeCard(flash, mistakes, log, reader);
            } else if(opt.equals("import")) {
                importCard(flash, mistakes, log, reader);
            } else if(opt.equals("export")) {
                exportCard(flash, mistakes, log, reader);
            } else if(opt.equals("ask")) {
                askCard(flash, mistakes, log, reader);
            } else if(opt.equals("exit")) {
                if(flag == 1)
                    writeFile(flash, mistakes, log, fileNameExport);
                System.out.println("Bye bye!");
                break;
            } else if(opt.equals("log")) {
                logToFile(log, reader);
            } else if(opt.equals("hardest card")) {
                hardestCard(flash, mistakes, log);
            } else if(opt.equals("reset")) {
                reset(mistakes, log);
            }
        }while(true);
    }
}