
import java.io.*;
import java.util.*;
class tic_Tac_Toe{
    public static char checkRowAndCol(char[][] arr){
        String temp="", temp1="";
        int x = 0, o = 0;
        for(int i=0; i<3; i++){
            temp = "";
            temp1 = "";
            for(int j=0; j<3; j++){
                temp = temp + arr[i][j];
                temp1 = temp1 + arr[j][i];
            }

            if(temp.equals("XXX")){
                x++;
            } else if(temp.equals("OOO")){
                o++;
            }

            if(temp1.equals("XXX"))
                x++;
            else if(temp1.equals("OOO"))
                o++;
        }
        if(x==1 && o==0)
            return 'x';
        else if(x==0 && o==1)
            return 'o';
        else
            return 'n';
    }

    public static char checkDiagonal(char[][] arr){
        String temp = "", temp1="";
        int x = 0, o = 0;
        for(int i=0; i<3; i++){
            temp = temp + arr[i][i];
            temp1 = temp1 + arr[3-i-1][i];
        }

        if(temp.equals("XXX"))
            x++;
        else if(temp.equals("OOO"))
            o++;

        if(temp1.equals("XXX"))
            x++;
        else if(temp1.equals("OOO"))
            o++;

        if(x==1 && o==0)
            return 'x';
        else if (x==0 && o==1)
            return 'o';
        else
            return 'n';
    }
    public static String checkWin(char[][] arr){
        char out1, out2;
        String output = "";
        out1 = checkRowAndCol(arr);
        out2 = checkDiagonal(arr);
        if(out1=='x' || out2=='x')
            output = "X wins";
        else if(out1=='o'||out2=='o')
            output = "O wins";
        else if(out1=='n' && out2=='n')
            output = "Draw";

        return output;
    }
    public static int getValidInput(char[][] arr, BufferedReader reader) throws IOException{
        int inp = Integer.parseInt(reader.readLine());
        int x = (inp-1)/3;
        int y = (inp-1)%3;
        if(x>2 || y>2){
            x =0;
            y =0;
        }
        while(arr[x][y] != '_' || inp<0 || inp>9){
            System.out.println("Invalid Input!!");
            System.out.print("Try again:\t");
            inp = Integer.parseInt(reader.readLine());
            x = (inp-1)/3;
            y = (inp-1)%3;
            if(x>2 || y>2){
                x = 0;
                y = 0;
            }
        }
        return inp;
    }
    public static int getValidRandom(char[][] arr, Random random){
        int out, x, y;
        do{
            out = random.nextInt(9);
            x = out/3;
            y = out%3;
        }while (arr[x][y]!='_');
        return out;
    }
    public static void main(String args[]) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Random random = new Random();
        char[][] arr = new char[3][3];
        String output="";
        char last_turn = 'O';
        int point, x, y, flag = 0;

        for(int i=0, t = 0; i<3; i++){
            System.out.print("  ");
            for(int j=0; j<3; j++, t++){
                if(j<2)
                    System.out.print((t+1)+"|");
                else
                    System.out.println((t+1)+" ");
                arr[i][j] = '_';
            }
            if(i<2)
                System.out.println(" --+-+--");
            else
                System.out.println();
        }
        System.out.println("############################");
        System.out.println("        TIC TAC TOE");
        System.out.println("############################\n\n");
        System.out.println("Enter::\n1. 1 player\n2. 2 players\n3. Exit");
        int ch = Integer.parseInt(reader.readLine());
        
        while(ch!=1 && ch!=2){
            System.out.println("Invalid Input!");
            System.out.println("__________________________");
            System.out.println("Enter::\n1. 1 player\n2. 2 players");
            ch = Integer.parseInt(reader.readLine());
            if(ch==3){
                flag = 1;
                break;
            }
        }
        
        for(int a=0; (a<9 && ch!=3) ; a++){
            if(last_turn == 'X')
                last_turn = 'O';
            else
                last_turn = 'X';
            System.out.println("\n________________________\n");
            if(a%2 == 0){
                System.out.println("Player X's turn:");
                System.out.print("Enter a block from 1 to 9:\t");
                point = getValidInput(arr, reader);
            } else {
                if(ch == 1){
                    System.out.println("Computer O's turn:");
                    point = getValidRandom(arr, random) + 1;
                } else {
                    System.out.println("Player O's turn:");
                    System.out.print("Enter a block from 1 to 9:\t");
                    point = getValidInput(arr, reader);
                }
            }
            
            x = (point-1)/3;
            y = (point-1)%3;
            arr[x][y] = last_turn;
            
            for(int i=0; i<3; i++){
                System.out.print("  ");
                for(int j=0; j<3; j++){
                    if(j<2){
                        if(arr[i][j] == '_') 
                            System.out.print(" |");
                        else
                            System.out.print(arr[i][j]+"|");
                    }else{
                        if(arr[i][j] == '_') 
                            System.out.print("  ");
                        else
                            System.out.print(arr[i][j]+" ");
                    }
                }
                if(i<2)
                    System.out.println("\n --+-+--");
                else
                    System.out.println();
            }
            
            output = checkWin(arr);
            if(output.equals("X wins") || output.equals("O wins")){
                output = (output.equals("X wins"))? "Player \'X\' wins!" : ((ch==1)? "Computer wins!!" : "Player \'O\' Wins");
                System.out.println("****\t" + output + "\t****");
                flag = 1;
                break;
            }
        }
        if(flag==0)
            System.out.println("Draw");
    }
}