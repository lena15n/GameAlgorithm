package stonesgame;

import java.util.ArrayList;

public class Program {
    public static void main(String[] args) {
        System.out.println("Stones algorithm\n");
        int countOfPlayers = 2;

        ArrayList<Operation> operations = new ArrayList<>();
        Operation operation1 = new Operation('+', 1);
        Operation operation2 = new Operation('*', 2);
        operations.add(operation1);
        operations.add(operation2);

        ArrayList<Integer> stonesInHeaps = new ArrayList<>();
        stonesInHeaps.add(10);
        stonesInHeaps.add(7);

        int endSum = 70;

        int firstPlayer = 0;


        Game game = new Game(countOfPlayers, operations, stonesInHeaps, endSum, firstPlayer);

        game.calculate();
    }
}
