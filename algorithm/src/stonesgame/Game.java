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
        winner = -1;
        COUNT_OF_BRANCHES = INITIAL_STONES_IN_HEAPS.size() * OPERATIONS.size();
    }

    private ArrayList<Operation> sort(ArrayList<Operation> operations) {//удалить к херам мб
        Collections.sort(operations, new Comparator<Operation>() {
            public int compare(Operation o1, Operation o2) {
                return o2.compareTo(o1);//по "убыванию"
            }
        });

        return operations;
    }

    public void start() {
        ArrayDeque<State> queue = new ArrayDeque<>();
        queue.add(startState);
        buildGameTreeBranch(queue);

        findWinSolutionAndWinner();

        cutBranches(startState);

        System.out.println("\n\n--end creating--\n\n");
    }

    private void buildGameTreeBranch(ArrayDeque<State> queue) {
        //TODO: сделать итерационным до максимальной глубины, т к рекурсия будет более долгой
        if (!queue.isEmpty()) {//можно и без очереди, просто контроль по step
            State currentState = queue.remove();
            /*if (currentState.getStep() >= MAX_DEPTH){//шоб было на всякий случай
                return;
            }*/

            if (!(currentState.getWin() == 1)) {
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
        //TODO: set prevState to all States
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
                if (possibleState.getPlayer() == -1) {
                    possibleState.setPlayer(FIRST_PLAYER);
                }

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

    private void findWinSolutionAndWinner() {
        setWinAndLooseStates(startState);//запуск покраски для всех веток

        if (startState.getWin() == 0) {
            winner = FIRST_PLAYER;
        } else {
            winner = FIRST_PLAYER == 0 ? 1 : 0;//'противоположный первому игроку' игрок
        }

        cutBranches(startState);
    }

    private void setWinAndLooseStates(State currentState) {// запуск из уровня 1 (не корень)
        int count = 0;
        ArrayList<State> states = currentState.getNextStates();

        if (currentState.getWin() == -1) {//если победа уже на первом ходе
            for (State state : states) {
                if (state.getWin() == 1) {//вершина листовая
                    currentState.setLoose();//при хотя бы одной победе противника помещаем состояние проигрышным
                } else if (state.getWin() == 0) {//смотрим сколько проигрышных состояний противника
                    count++;
                } else if (state.getWin() == -1) {
                    setWinAndLooseStates(state);

                    if (state.getWin() == 1) {
                        currentState.setLoose();
                    } else {
                        count++;
                    }
                }
            }

            if (count == states.size()) {
                currentState.setWin();
            } else {
                currentState.setLoose();
            }
        }
    }

    private void cutBranches(State currentState) {
        //знаем выигрышные и проигрышные вершины,
        //устраняем случаи: из проигрышной ветки вдруг приходим к выигрышу
        //т е из проигрышной вершины (player = x) выходит проигрышная вершина (player = y)
        if (currentState.getNextStates() != null) {
            ArrayList<State> nextStates = currentState.getNextStates();
            ArrayList<State> excessStates = new ArrayList<>();

            //собираем индексы детей, которых надо удалить из текущей
            for (State nextState : nextStates) {
                if (currentState.getWin() == 0 && nextState.getWin() == 0) {
                    excessStates.add(nextState);
                }
                else {
                    cutBranches(nextState);
                }

            }
            //удаляем
            nextStates.removeAll(excessStates);

        }
    }

    private boolean isItExcessBranch(State currentState) {// если эта вершина проигрышная, а из нее идет проигрышная, player разный у них
        boolean result = false;


        return result;
    }
    //private boolean isFirstPlayerIsWinner1(State currentState){
        /*
         суть алгоритма:
         цель: проверить, что выигрывает 0 игрок.

         запуск из состояния -1 корня или из состояния 1 игрока,

         просматриваем его детей  (состояния 0 игрока),             результат будет - операция | над результатом от всех детей
         просматриваем всех внуков (состояния 1 игрока)  из детей,  результат будет - операция & над результатом от всех внуков

         запуск из любого следующего состояния рекурсивно(было: запуск из состояния 1 игрока рекурсивно)
         */


        /*int count = 0;
        ArrayList<State> states = currentState.getNextStates();

        if (currentState.getPlayer() != FIRST_PLAYER) {
            for (State state : states) {//просмотр детей (состояния 0 игрока)
                if (state.getWin()) {
                    count++;
                } else {
                    if (setWinAndLooseStates(state)){
                        count++;
                    }
                }
            }

            return count > 0;//хотя бы одна победа на какой-то из ветвей
        }
        else {
            for (State state : states) {
                if (state.getWin()){//выходим сразу, т к есть хотя бы одна возможность выигрыша у противника
                    return false;
                }
                if (setWinAndLooseStates(state)) {
                    count++;
                }
            }

            return count == states.size();
        }*/



        /*ArrayList<State> states = currentState.getNextStates();
        int opponentLosses = 0;

        for(State state : states){
            if (!state.getWin()){
                opponentLosses++;
            }
        }

        if (opponentLosses == states.size()){//каждый ход противника НЕ приводит его к победе
           int winStatesCount = 0;

           for (State state : states){
               ArrayList<State> nextStates = state.getNextStates();
               int localWinStateCount = 0;

               for (State childState : nextStates){
                   if (childState.getWin()){
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
            setWinAndLooseStates(state, leadStates);
        }*/
    //}

    /*private void check(State currentState, HashSet<State> leadStates){
        ArrayList<State> opponentStates = currentState.getNextStates();
        int opponentLosses = 0;

        for(State state : opponentStates){
            if (!state.getWin()){
                opponentLosses++;
            }
        }

        if (opponentLosses == opponentStates.size()) {//каждый ход противника НЕ приводит его к победе
            int winStatesCount = 0;

            for (State state : opponentStates) {
                ArrayList<State> nextStates = state.getNextStates();
                int localWinStateCount = 0;

                for (State childState : nextStates) {
                    if (childState.getWin()) {
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
    }*/

    public int getWinner() {
        return winner;
    }
}

class State {
    private static long counter;
    private long index;//номер просто вершины в дереве
    private long step;//номер хода в игре (несколько состояний могут иметь один и тот же номер хода)
    //TODO: сделать обычным массивом константной длины, т к в течение всей игры его размер не изменится
    private ArrayList<Integer> stonesInHeaps;
    private int win;//-1 0 1
    private int player;//сделавший данный ход - тот, кто получил такое состояние, а не тот, кто только начнет ходить
    private int summ;//сумма всех камней в кучах
    private State prevState;//TODO: или удалить или присваивать родителя сюда
    private ArrayList<State> nextStates;//то, почему Ход/State - дерево
    private Operation operation;

    public State(ArrayList<Integer> stonesInHeaps) {
        index = counter;
        counter++;
        this.stonesInHeaps = stonesInHeaps;
        updateSumm();
        win = -1;
        player = -1;
        prevState = null;
    }

    public State(State state, Operation operation) {
        index = counter;
        counter++;
        this.stonesInHeaps = (ArrayList<Integer>) state.getStonesInHeaps().clone();
        updateSumm();

        if (state.getPlayer() == 0) {        //противоположный игрок теперь
            player = 1;
        } else if (state.getPlayer() == 1) {   //противоположный игрок теперь
            player = 0;
        } else {//значение игрока еще не было установлено - корневое состояние
            player = -1;
        }

        step = state.getStep() + 1;
        win = -1;
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


    public int getWin() {
        return win;
    }

    public void setWin() {
        win = 1;
    }

    public void setLoose() {
        win = 0;
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

class Operation implements Comparable<Operation> {
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
        if (operator == '+') {
            if (o.operator == '+') {
                return x - o.x;
            } else {
                return -1; // условно '+' < '*'
            }
        } else if (operator == '*') {
            if (o.operator == '*') {
                return x - o.x;
            } else {
                return 1; // условно '*' > '+'
            }
        }

        throw new IllegalArgumentException();
    }
}
