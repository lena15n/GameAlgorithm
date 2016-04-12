package stonesgame;

import java.util.ArrayList;

public class Game {
    public final int COUNT_OF_PLAYERS;
    public final ArrayList<Operation> OPERATIONS;
    public final ArrayList<Integer> INITIAL_STONES_IN_HEAPS;

    public final int END_OF_GAME_SUM;
    public final int FIRST_PLAYER;

    private int winner;
    private State currentState;//current StonesInHeaps




    public Game(int countOfPlayers, ArrayList<Operation> operations, ArrayList<Integer> stonesInHeaps, int endOfGameSum,
                int firstGamer){
        this.COUNT_OF_PLAYERS = countOfPlayers;
        this.OPERATIONS = operations;
        this.INITIAL_STONES_IN_HEAPS = stonesInHeaps;
        this.END_OF_GAME_SUM = endOfGameSum;
        this.FIRST_PLAYER = firstGamer;

        this.currentState = new State((ArrayList<Integer>) INITIAL_STONES_IN_HEAPS.clone());
    }


    public void start(){
        //smth test idea + git
    }


    public void calculate(){
        ArrayList<State> possibleStates = new ArrayList<>();
        State possibleState = new State(currentState);
        int winStatesCounter = 0;


        System.out.print("Current state is: ");
        currentState.printHeaps();
        System.out.println();



        for(Operation operation : OPERATIONS) {
            System.out.print("Operation: ");
            operation.print();

            ArrayList<Integer> currentStonesInHeaps = currentState.getStonesInHeaps();
            int i = 0;

            for (Integer stonesOfOneHeap : currentStonesInHeaps) {//(int i = 0; i < currentState.size(); i++){
                possibleState.setHeap(i, operation.apply(stonesOfOneHeap));

                if (possibleState.getSumm() > END_OF_GAME_SUM){
                    winStatesCounter++;

                    System.out.print("Winner state! -> ");
                    possibleState.printHeaps();
                }
                else {
                    System.out.print("Next possible state -> ");
                    possibleState.printHeaps();

                    possibleStates.add(possibleState);


                }

                //clear temp state
                possibleState.setHeap(i, stonesOfOneHeap);
                i++;
            }

            System.out.println("_____________");
        }


        //win in every possible state
        if (winStatesCounter == (INITIAL_STONES_IN_HEAPS.size() * OPERATIONS.size())){
            winner = 100;
        }
    }




    public int getWinner() {
        return winner;
    }




    private class State {
        private ArrayList<Integer> stonesInHeaps;

        public State(ArrayList<Integer> stonesInHeaps) {
            this.stonesInHeaps = stonesInHeaps;
        }

        public State(State state){
            this.stonesInHeaps = (ArrayList<Integer>) state.getStonesInHeaps().clone();
        }

        public int getSumm() {
            int result = 0;

            for (Integer stonesInOneHeap : stonesInHeaps) {
                result += stonesInOneHeap;
            }

            return result;
        }

        public void printHeaps(){
            System.out.print("(");
            int size = stonesInHeaps.size();
            int i = 0;

            for(Integer heap : stonesInHeaps) {
                if (i != size - 1) {
                    System.out.print(heap + ", ");
                }
                else{
                    System.out.print(heap);
                }

                i++;
            }

            System.out.println(")");
        }

        public void setHeap(int index, int newCountOfStones){
            stonesInHeaps.set(index, newCountOfStones);
        }

        public ArrayList<Integer> getStonesInHeaps() {
            return stonesInHeaps;
        }

        public void setStonesInHeaps(ArrayList<Integer> stonesInHeaps) {
            this.stonesInHeaps = stonesInHeaps;
        }
    }
}

class Operation {
    private final int x;
    private final char operator;

    Operation(char operator, int x){
        this.operator = operator;
        this.x = x;
    }

    public int apply (int number){
        switch (operator){
            case '+':{
                return number + x;
            }

            case '*':{
                return number * x;
            }
        }

        return -1;
    }

    public void print(){
        System.out.println("" + operator + x);
    }
}

/*enum Unit {
    KILOMETER {
        public double getLength() { return 1000; }
    },
    METER {
        public double getLength() { return 1; }
    },
    MILLIMETER {
        public double getLength() { return 0.001; }
    };
    public abstract double getLength();
}

enum BinaryOperation {

    Plus  {
        public int calculate(int a, int b){
            return a + b;
        }
    },
    Minus {
        public int calculate(int a, int b){
            return a - b;
        }
    },
    Division()  {
        public int calculate(int a, int b){
            return a / b;
        }
    },
    Times()  {
        public int calculate(int a, int b){
            return a * b;
        }
    };

    abstract public int calculate(int a, int b);
}*/

