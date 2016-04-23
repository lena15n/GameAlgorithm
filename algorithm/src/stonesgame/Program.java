package stonesgame;

import java.util.ArrayList;

public class Program {
    public static void main(String[] args) {
        System.out.println("Stones algorithm\n");

        startGame(2016);
    }

    public static void startGame(int numOfGame) {
        int countOfPlayers = 2;
        ArrayList<Operation> operations = new ArrayList<>();
        ArrayList<Integer> stonesInHeaps = new ArrayList<>();
        int endSum = 0;
        int firstPlayer = 0;
        int maxDepth = 2;


        switch(numOfGame){
            case 2016:{
                countOfPlayers = 2;

                Operation operation1 = new Operation('+', 1);
                Operation operation2 = new Operation('*', 2);
                operations.add(operation1);
                operations.add(operation2);

                stonesInHeaps.add(7);
                stonesInHeaps.add(31);

                endSum = 73;

                firstPlayer = 0;
            }
            break;

            case 2015:{

            }
            break;

            case 0:{
                countOfPlayers = 2;

                Operation operation1 = new Operation('+', 1);
                Operation operation2 = new Operation('*', 2);
                operations.add(operation1);
                operations.add(operation2);

                stonesInHeaps.add(10);
                stonesInHeaps.add(7);

                endSum = 26;

                firstPlayer = 0;
            }
            break;
        }

        Game game = new Game(countOfPlayers, operations, stonesInHeaps, endSum, firstPlayer, maxDepth);
        game.start();
    }
}
