import java.io.*;
import java.util.*;
public class invertedIndexSearch {
    public static Map<Integer, Integer> makeSearch(ArrayList<String> arr, Map<String, ArrayList<Integer>> searchMap, String toFind){
        ArrayList<Integer> indices;
        Map<Integer, Integer> searchHelp = new HashMap<>();
        String word = "";
        int temp, wordCount = 0;
        Scanner scanWord = new Scanner(toFind);
        while(scanWord.hasNext()){
            word=scanWord.next();
            wordCount++;
            if(searchMap.containsKey(word)){
                indices = searchMap.get(word);
                for(int index: indices){
                    if(searchHelp.containsKey(index)){
                        temp = searchHelp.get(index);
                        temp++;
                        searchHelp.remove(index);
                        searchHelp.put(index, temp);
                    } else {
                        searchHelp.put(index, 1);
                    }
                }
            }
        }
        return searchHelp;
    }
    public static int wordCounting(String toCount){
        String temp = toCount.trim();
        String[] wordArray = temp.split("\\s+");
        return wordArray.length;
    }
    public static void searchAll(ArrayList<String> arr, Map<Integer, Integer> searchHelp, int wordCount, ArrayList<Integer> found){
        for(var index: searchHelp.entrySet()){
            if(index.getValue().equals(wordCount)){
                found.add(index.getKey());
            }
        }
    }
    public static void searchAny(ArrayList<String> arr, Map<Integer, Integer> searchHelp, int wordCount, ArrayList<Integer> found){
        for(var index: searchHelp.entrySet()){
            found.add(index.getKey());
        }
    }
    public static void searchNone(ArrayList<String> arr, Map<Integer, Integer> searchHelp, int wordCount, ArrayList<Integer> found){
        int totalIndex = arr.size();
        for(int i=0; i<totalIndex; i++){
            if(!searchHelp.containsKey(i))
                found.add(i);
        }
    }
    public static void findPerson(ArrayList<String> arr, Map<String, ArrayList<Integer>> searchMap ,BufferedReader reader) throws IOException {
        String toFind, searchStrategy;
        ArrayList<Integer> indices;
        System.out.println("Select a matching strategy: ALL, ANY, NONE");
        searchStrategy = reader.readLine().toUpperCase();
        System.out.println("Enter a name or email to search all suitable people.");
        toFind = reader.readLine().toLowerCase();

        Map<Integer, Integer> searchHelp = makeSearch(arr, searchMap, toFind);
        ArrayList<Integer> found = new ArrayList<>();
        int wordCount = wordCounting(toFind);

        if(searchStrategy.equals("ALL"))    searchAll(arr, searchHelp, wordCount, found);
        else if(searchStrategy.equals("ANY"))   searchAny(arr, searchHelp, wordCount, found);
        else if(searchStrategy.equals("NONE"))  searchNone(arr, searchHelp, wordCount, found);
        else{
            System.out.println("Invalid Input!");
            return;
        }

        System.out.println(found.size() + " persons found");
        for(int index: found){
            System.out.println(arr.get(index));
        }
    }
    public static void printAll(ArrayList<String> arr){
        System.out.println("=== List of people ===");
        for(String people: arr){
            System.out.println(people);
        }
    }

    public static  void invertedIndex(ArrayList<String> arr, Map<String, ArrayList<Integer>> searchMap) {
        String temp="", people="";
        ArrayList<Integer> index;
        for(int i=0; i<arr.size(); i++){
            people = arr.get(i);
            Scanner scanWord = new Scanner(people);
            while(scanWord.hasNext()){
                temp = scanWord.next().toLowerCase();
                if(searchMap.containsKey(temp)){
                    index = searchMap.get(temp);
                    index.add(i);
                } else {
                    index = new ArrayList<>();
                    index.add(i);
                    searchMap.put(temp, index);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//        System.out.println("Enter the number of people:");
//        int num = Integer.parseInt(reader.readLine());
        int opt = 0;

        ArrayList<String> arr = new ArrayList<>();
        Map<String, ArrayList<Integer>> searchMap = new HashMap<>();
        String fileName = "";
        for(int i=0; i<args.length; i++){
            if(args[i].equals("--data"))
                fileName = args[i+1];
        }

        File fileToRead = new File(fileName);
        try(Scanner scan = new Scanner(fileToRead)){
            while(scan.hasNextLine()){
                arr.add(scan.nextLine());
            }
        } catch (FileNotFoundException e){
            System.out.println("Error: "+ e.getMessage());
        }

        invertedIndex(arr, searchMap);

        do {
            System.out.println("=== Menu ===");
            System.out.println("1. Find a person");
            System.out.println("2. Print all people");
            System.out.println("0. Exit");

            opt = Integer.parseInt(reader.readLine());

            switch(opt){
                case 1:
                    findPerson(arr, searchMap, reader);
                    break;
                case 2:
                    printAll(arr);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Incorrect option! Try again.");
            }
        }while(opt!=0);
    }
}
