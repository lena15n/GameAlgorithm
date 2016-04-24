package stonesgame;

import java.util.*;

public class Game {
    public final int COUNT_OF_PLAYERS;
    public final ArrayList<Operation> OPERATIONS;
    public final ArrayList<Integer> INITIAL_STONES_IN_HEAPS;
    public final int END_OF_GAME_SUM;
    public final int FIRST_PLAYER;
    public final int MAX_DEPTH;

    private final int COUNT_OF_BRANCHES;

    private int winner;
    private State startState;



    public Game(int countOfPlayers, ArrayList<Operation> operations, ArrayList<Integer> stonesInHeaps, int endOfGameSum,
                int firstGamer, int maxDepth) {
        this.COUNT_OF_PLAYERS = countOfPlayers;
        this.OPERATIONS = sort(operations);
        this.INITIAL_STONES_IN_HEAPS = stonesInHeaps;
        this.END_OF_GAME_SUM = endOfGameSum;
        this.FIRST_PLAYER = firstGamer;
        this.MAX_DEPTH = maxDepth;
        this.startState = new State((ArrayList<Integer>) INITIAL_STONES_IN_HEAPS.clone());
        startState.setStep(0);//вершина дерева игры, номер сделанного хода
        startState.setPlayer(FIRST_PLAYER);
        winner = -1;
        COUNT_OF_BRANCHES = INITIAL_STONES_IN_HEAPS.size() * OPERATIONS.size();
    }

    private ArrayList<Operation> sort(ArrayList<Operation> operations){//удалить к херам мб
        Collections.sort(operations,new Comparator<Operation>(){
            public int compare(Operation o1, Operation o2){
                return o2.compareTo(o1);//по "убыванию"
            }});

        return operations;
    }

    public void start() {
        ArrayDeque<State> queue = new ArrayDeque<>();
        queue.add(startState);
        buildGameTreeBranch(queue);

       // findWinSolutionAndWinner();

        System.out.println("\n\n--end creating--\n\n");
    }

    private void buildGameTreeBranch(ArrayDeque<State> queue) {
        if (!queue.isEmpty()) {//можно и без очереди, просто контроль по step
            State currentState = queue.remove();
            /*if (currentState.getStep() >= MAX_DEPTH){//шоб было на всякий случай
                return;
            }*/

            if (!currentState.isItWinState()) {
                currentState.setNextStates(calculateAllPossibleStatesOnIter(currentState));

                ArrayList<State> states = currentState.getNextStates();

                for (State state : states) {
                    queue.add(state);
                }
            }

            buildGameTreeBranch(queue);
        }

    }

    private ArrayList<State> calculateAllPossibleStatesOnIter(State currentState) {
        ArrayList<State> possibleStates = new ArrayList<>();

        System.out.print("Current state is: ");
        currentState.printHeaps();
        System.out.println();

        ArrayList<State> winStates = new ArrayList<>();

        for (Operation operation : OPERATIONS) {
            System.out.print("Operation: ");
            operation.print();

            ArrayList<Integer> currentStonesInHeaps = currentState.getStonesInHeaps();
            int heapNum = 0;

            for (Integer stonesOfOneHeap : currentStonesInHeaps) {
                State possibleState = new State(currentState, operation);
                possibleState.setHeap(heapNum, operation.apply(stonesOfOneHeap));

                if (possibleState.getSumm() >= END_OF_GAME_SUM) {
                    possibleState.setWin();
                    possibleStates.add(possibleState);
                    winStates.add(possibleState);

                    System.out.print("Winner state! -> ");
                    possibleState.printHeaps();
                } else {
                    possibleStates.add(possibleState);

                    System.out.print("Next possible state -> ");
                    possibleState.printHeaps();
                }

                heapNum++;
            }

            System.out.println("--------------");
        }

        if (winStates.size() > 0) {
            possibleStates.retainAll(winStates);
        }


        System.out.println("______________________________");

        return possibleStates;
    }

    private void findWinSolutionAndWinner(){
        HashSet<State> leadStates = new HashSet<>();

        isPlayerIsWinner(startState, 0);// мб не 0 игрок




        /*//win in every possible state
            winner = 100;//идет присвоение победителя
        }*/
    }

    private boolean isPlayerIsWinner(State currentState, int player){//запуск из корня или хода 2-4-6 игрока, ищем победу 1-3-5 игрока
        //TODO: ПЕРЕПИСАТЬ,
        // проверить, что длина ветки более трех
        // проверять, что состояния не листья
        int count = 0;
        ArrayList<State> states = currentState.getNextStates();

        if (currentState.getPlayer() != player) {
            for (State state : states) {
                if (state.isItWinState()) {//дописать для случая когда победа на 0-1 ходах
                    count++;
                } else {
                    if (isPlayerIsWinner(state, player)){
                        count++;
                    }
                }
            }
        }
        else {
            for (State state : states) {
                if (isPlayerIsWinner(state, player)) {
                    count++;
                }
            }
        }


        return count == states.size();




        /*ArrayList<State> states = currentState.getNextStates();
        int opponentLosses = 0;

        for(State state : states){
            if (!state.isItWinState()){
                opponentLosses++;
            }
        }

        if (opponentLosses == states.size()){//каждый ход противника НЕ приводит его к победе
           int winStatesCount = 0;

           for (State state : states){
               ArrayList<State> nextStates = state.getNextStates();
               int localWinStateCount = 0;

               for (State childState : nextStates){
                   if (childState.isItWinState()){
                       localWinStateCount++;
                   }
               }

               if (localWinStateCount > 0){
                    winStatesCount++;
               }
           }

            if (winStatesCount == states.size()){
                leadStates.add(currentState);
            }
        }

        for (State state : states){
            isPlayerIsWinner(state, leadStates);
        }*/
    }

    private void check(State currentState, HashSet<State> leadStates){
        ArrayList<State> opponentStates = currentState.getNextStates();
        int opponentLosses = 0;

        for(State state : opponentStates){
            if (!state.isItWinState()){
                opponentLosses++;
            }
        }

        if (opponentLosses == opponentStates.size()) {//каждый ход противника НЕ приводит его к победе
            int winStatesCount = 0;

            for (State state : opponentStates) {
                ArrayList<State> nextStates = state.getNextStates();
                int localWinStateCount = 0;

                for (State childState : nextStates) {
                    if (childState.isItWinState()) {
                        localWinStateCount++;
                    }
                    //рекурсия поиска winstate
                }

                if (localWinStateCount > 0) {
                    winStatesCount++;
                }
            }

            if (winStatesCount == opponentStates.size()) {
                leadStates.add(currentState);
            }
        }
    }

    public int getWinner() {
        return winner;
    }
}

class State {
    private static long counter;
    private long index;//номер просто вершины в дереве
    private long step;//номер хода в игре (несколько состояний могут иметь один и тот же номер хода)
    private ArrayList<Integer> stonesInHeaps;
    private boolean win;
    private int player;//сделавший данный ход - тот, кто получил такое состояние, а не тот, кто только начнет ходить
    private int summ;//сумма всех камней в кучах
    private State prevState;
    private ArrayList<State> nextStates;//то, почему Ход/State - дерево
    private Operation operation;

    public State(ArrayList<Integer> stonesInHeaps) {
        index = counter;
        counter++;
        this.stonesInHeaps = stonesInHeaps;
        updateSumm();
        win = false;
        player = -1;
        prevState = null;
    }

    public State(State state, Operation operation) {
        index = counter;
        counter++;
        this.stonesInHeaps = (ArrayList<Integer>) state.getStonesInHeaps().clone();
        updateSumm();
        player =  ((state.getPlayer() == 0) ? 1 : 0);//противоположный игрок теперь
        step = state.getStep() + 1;
        win = false;
        this.operation = operation;
        this.prevState = state.getPrevState();
    }



    public void updateSumm() {
        int result = 0;

        for (Integer stonesInOneHeap : stonesInHeaps) {
            result += stonesInOneHeap;
        }

        summ = result;
    }

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



    /*   ARE NEVER USED


    public void addNextState(State state) {
        nextStates.add(state);
    }

    public void addSomeState(long index, State state){//номер родителя

        findState(index).addNextState(state);
    }

        /*public ArrayList<State> categoryTasks(String category){// throws TaskException {//все задания определенной категории(во всей иерархии)
            ArrayList<Task> foundTasks = new ArrayList<>();
            if(this.category != null && this.category.equals(category))
                foundTasks.add(this);

            for (Task subtask : subtasks) {
                foundTasks.addAll(subtask.categoryTasks(category));
            }

            return foundTasks;
            // TaskException("Заданий с таким именем нет ни в одном дереве!");

        }

    public State findState(long index){
        if (this.index == index)
            return this;

        for (State someState : nextStates) {
            State res = someState.findState(index);
            if (res != null)
                return res;
        }

        return null;
    }

    public void remove(long id){
        for(State someState: nextStates){
            if(someState.index == id){
                nextStates.remove(someState);
                return;
            }
            else
                someState.remove(id);
        }
    }*/


    public boolean isItWinState() {
        return win;
    }

    public void setWin() {
        win = true;
    }

    public void setHeap(int index, int newCountOfStones) {
        stonesInHeaps.set(index, newCountOfStones);
        updateSumm();
    }


    public ArrayList<Integer> getStonesInHeaps() {
        return stonesInHeaps;
    }

    public void setStonesInHeaps(ArrayList<Integer> stonesInHeaps) {
        this.stonesInHeaps = stonesInHeaps;
        updateSumm();
    }

    public ArrayList<State> getNextStates() {
        return nextStates;
    }

    public void setNextStates(ArrayList<State> nextStates) {
        this.nextStates = nextStates;
    }

    public int getSumm() {
        return summ;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public long getStep() {
        return step;
    }

    public void setStep(long step) {
        this.step = step;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public State getPrevState() {
        return prevState;
    }

    public void setPrevState(State prevState) {
        this.prevState = prevState;
    }
}

class Operation implements Comparable<Operation>{
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

    @Override
    public int compareTo(Operation o) {
        if (operator == '+'){
            if(o.operator == '+'){
                return x - o.x;
            }
            else {
                return -1; // условно '+' < '*'
            }
        }
        else if (operator == '*'){
            if(o.operator == '*'){
                return x - o.x;
            }
            else {
                return 1; // условно '*' > '+'
            }
        }

        throw new IllegalArgumentException();
    }
}
