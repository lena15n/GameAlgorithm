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
    private int currentPlayer;


    public Game(int countOfPlayers, ArrayList<Operation> operations, ArrayList<Integer> stonesInHeaps, int endOfGameSum,
                int firstGamer) {
        this.COUNT_OF_PLAYERS = countOfPlayers;
        this.OPERATIONS = operations;
        this.INITIAL_STONES_IN_HEAPS = stonesInHeaps;
        this.END_OF_GAME_SUM = endOfGameSum;
        this.FIRST_PLAYER = firstGamer;

        this.currentState = new State((ArrayList<Integer>) INITIAL_STONES_IN_HEAPS.clone());
        currentState.setIndex(0);//вершина дерева игры
        currentPlayer = FIRST_PLAYER;
        winner = -1;
    }


    public void start() {
        currentState.setNextStates(calculateAllPossibleStatesOnIter(currentState));

        ArrayList<State> states = currentState.getNextStates();

        for (State state : states) {
            buildGameTreeBranch(state);
        }

    }

    private void buildGameTreeBranch(State startState) {
        if (startState.isItWinState()) {
            startState.setNextStates(calculateAllPossibleStatesOnIter(startState));

            ArrayList<State> states = startState.getNextStates();

            for (State state : states) {
                buildGameTreeBranch(startState);
            }
        }
    }

    //TODO: добавлять все вершины, даже win
    public ArrayList<State> calculateAllPossibleStatesOnIter(State startState) {
        ArrayList<State> possibleStates = new ArrayList<>();
        State possibleState = new State(startState);
        int winStatesCounter = 0;


        System.out.print("Current state is: ");
        currentState.printHeaps();
        System.out.println();


        for (Operation operation : OPERATIONS) {
            System.out.print("Operation: ");
            operation.print();

            ArrayList<Integer> currentStonesInHeaps = currentState.getStonesInHeaps();
            int i = 0;

            for (Integer stonesOfOneHeap : currentStonesInHeaps) {//(int i = 0; i < currentState.size(); i++){
                possibleState.setHeap(i, operation.apply(stonesOfOneHeap));

                if (possibleState.getSumm() > END_OF_GAME_SUM) {
                    winStatesCounter++;

                    System.out.print("Winner state! -> ");
                    possibleState.printHeaps();
                } else {
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
        if (winStatesCounter == (INITIAL_STONES_IN_HEAPS.size() * OPERATIONS.size())) {
            winner = 100;//идет присвоение победителя
        }

        return possibleStates;
    }


    public int getWinner() {
        return winner;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }


    private class State {
        private int index;//номер хода в игре (несколько состояний могут иметь один и тот же номер хода)
        private ArrayList<Integer> stonesInHeaps;
        private boolean win;
        private int player;//сделавший данный ход - тот, кто получил такое состояние, а не тот, кто только начнет ходить
        private ArrayList<State> nextStates;//то, почему Ход/State - дерево

        public State(ArrayList<Integer> stonesInHeaps) {
            this.stonesInHeaps = stonesInHeaps;
            win = false;
        }

        public State(State state) {
            this.stonesInHeaps = (ArrayList<Integer>) state.getStonesInHeaps().clone();
            win = false;
        }

        public int getSumm() {
            int result = 0;

            for (Integer stonesInOneHeap : stonesInHeaps) {
                result += stonesInOneHeap;
            }

            return result;
        }

        /* TODO: add state in nextStates(int i)
             set state in nextStates(int i)
         */


        public void printHeaps() {
            System.out.print("(");
            int size = stonesInHeaps.size();
            int i = 0;

            for (Integer heap : stonesInHeaps) {
                if (i != size - 1) {
                    System.out.print(heap + ", ");
                } else {
                    System.out.print(heap);
                }

                i++;
            }

            System.out.println(")");
        }

        public boolean isItWinState() {
            return win;
        }

        public void setWin() {
            win = true;
        }

        public void setHeap(int index, int newCountOfStones) {
            stonesInHeaps.set(index, newCountOfStones);
        }


        public ArrayList<Integer> getStonesInHeaps() {
            return stonesInHeaps;
        }

        public void setStonesInHeaps(ArrayList<Integer> stonesInHeaps) {
            this.stonesInHeaps = stonesInHeaps;
        }

        public ArrayList<State> getNextStates() {
            return nextStates;
        }

        public void setNextStates(ArrayList<State> nextStates) {
            this.nextStates = nextStates;
        }

        public int getPlayer() {
            return player;
        }

        public void setPlayer(int player) {
            this.player = player;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
}

class Operation {
    private final int x;
    private final char operator;

    Operation(char operator, int x) {
        this.operator = operator;
        this.x = x;
    }

    public int apply(int number) {
        switch (operator) {
            case '+': {
                return number + x;
            }

            case '*': {
                return number * x;
            }
        }

        return -1;
    }

    public void print() {
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
        public int calculateAllPossibleStatesOnIter(int a, int b){
            return a + b;
        }
    },
    Minus {
        public int calculateAllPossibleStatesOnIter(int a, int b){
            return a - b;
        }
    },
    Division()  {
        public int calculateAllPossibleStatesOnIter(int a, int b){
            return a / b;
        }
    },
    Times()  {
        public int calculateAllPossibleStatesOnIter(int a, int b){
            return a * b;
        }
    };

    abstract public int calculateAllPossibleStatesOnIter(int a, int b);
}*/

