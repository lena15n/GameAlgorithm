package hadoop;

import java.io.*;
import java.util.*;

public class Program {
    public static void main(String[] args) throws FileNotFoundException {
        //task2();
        //task3();
        //task4_combiner();
        //task5_phase1_mapper();
        //task5_phase1_reducer();
        //task5_phase1_second_mapper();
        //task6();
        //task7();
        task7_stripes();
    }

    static void task1() throws FileNotFoundException {
        File file = new File("src/hadoop/task12.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));//new InputStreamReader(System.in));

        String string;

        try {
            while ((string = reader.readLine()) != null) {
                String[] words = string.split(" ");
                HashMap<String, Integer> associativeArray = new HashMap<>();


                for (String word : words) {
                    if (associativeArray.containsKey(word)) {
                        associativeArray.put(word, associativeArray.get(word) + 1);
                    } else {
                        associativeArray.put(word, 1);
                    }
                }

                for (Map.Entry<String, Integer> keyValue : associativeArray.entrySet()) {
                    System.out.println(keyValue.getKey() + "\t" + keyValue.getValue());
                }

            }


        } catch (IOException e) {

        }
    }

    static void task2() throws FileNotFoundException {
        File file = new File("src/hadoop/task12.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));//new InputStreamReader(System.in));
        HashMap<String, Integer> associativeArray = new HashMap<>();
        String string;

        try {
            while ((string = reader.readLine()) != null) {
                String[] words = string.split(" ");

                for (String word : words) {
                    if (associativeArray.containsKey(word)) {
                        associativeArray.put(word, associativeArray.get(word) + 1);
                    } else {
                        associativeArray.put(word, 1);
                    }
                }
            }


            for (Map.Entry<String, Integer> keyValue : associativeArray.entrySet()) {
                System.out.println(keyValue.getKey() + "\t" + keyValue.getValue());
            }

        } catch (IOException e) {

        }
    }

    static void task3() throws FileNotFoundException {
        File file = new File("src/hadoop/task3");
        BufferedReader reader = new BufferedReader(new FileReader(file));//new InputStreamReader(System.in));
        HashMap<String, List<Integer>> associativeArray = new HashMap<>();
        String string;
        String page = "";

        try {
            while ((string = reader.readLine()) != null) {
                String[] pageInfo = string.split("\t");

                if (page.equals(pageInfo[0])) {
                    associativeArray.get(page).add(Integer.valueOf(pageInfo[1]));
                } else {
                    printPageInfo(page, associativeArray);

                    page = pageInfo[0];
                    List<Integer> time = new ArrayList<>();
                    time.add(Integer.valueOf(pageInfo[1]));
                    associativeArray.put(page, time);
                }
            }

            printPageInfo(page, associativeArray);

        } catch (IOException e) {

        }
    }

    private static void printPageInfo(String page, HashMap<String, List<Integer>> associativeArray) {
        if (!page.isEmpty()) {
            Integer result = 0;
            List<Integer> times = associativeArray.get(page);

            for (Integer time : times) {
                result += time;
            }

            if (times.size() != 0) {
                result = result / times.size();

                System.out.println(page + "\t" + result);
            }
        }
    }

        /*File file = new File("src/hadoop/task3");
        BufferedReader reader = new BufferedReader(new FileReader(file));//new InputStreamReader(System.in));
        HashMap<String, List<Integer>> associativeArray = new HashMap<>();
        String string;

        try {
            while ((string = reader.readLine()) != null) {
                String[] pageInfo = string.split("\t");
                String page = pageInfo[0];
                Integer time = Integer.valueOf(pageInfo[1]);

                if (associativeArray.containsKey(page)){
                    associativeArray.get(page).add(time);
                }
                else {
                    ArrayList<Integer> values = new ArrayList<>();
                    values.add(time);
                    associativeArray.put(page, values);
                }
            }

            for (Map.Entry<String, List<Integer>> keyValue : associativeArray.entrySet()){
                List<Integer> values = keyValue.getValue();
                Integer result = 0;
                Integer count = values.size();

                for (Integer value : values){
                    result += value;
                }

                result = result / count;

                System.out.println(keyValue.getKey() + "\t" + result);
            }

        } catch (IOException e) {

        }*/


    static void task4_combiner() throws FileNotFoundException {
        File file = new File("src/hadoop/task4");
        BufferedReader reader = new BufferedReader(new FileReader(file));//new InputStreamReader(System.in));
        HashMap<String, List<Integer>> associativeArray = new HashMap<>();
        String string;
        String page = "";
        Integer sum = 0;
        Integer count = 0;

        try {
            while ((string = reader.readLine()) != null) {
                String[] pageInfo = string.split("\t|;");

                if (page.equals(pageInfo[0])) {
                    Integer newSum = Integer.valueOf(pageInfo[1]);
                    Integer newCount = Integer.valueOf(pageInfo[2]);

                    sum += newSum;
                    count += newCount;

                    associativeArray.get(page).set(0, sum);
                    associativeArray.get(page).set(1, count);
                } else {
                    if (!page.isEmpty()) {
                        System.out.println(page + "\t" + associativeArray.get(page).get(0) + ";" +
                                associativeArray.get(page).get(1));
                    }

                    page = pageInfo[0];
                    sum = Integer.valueOf(pageInfo[1]);
                    count = Integer.valueOf(pageInfo[2]);

                    List<Integer> info = new ArrayList<>();
                    info.add(sum);
                    info.add(count);
                    associativeArray.put(page, info);
                }
            }

            if (!page.isEmpty()) {
                System.out.println(page + "\t" + associativeArray.get(page).get(0) + ";" +
                        associativeArray.get(page).get(1));
            }

        } catch (IOException e) {

        }
    }

    static void task5_phase1_mapper() throws FileNotFoundException {
        File file = new File("src/hadoop/task5_map");
        BufferedReader reader = new BufferedReader(new FileReader(file));//new InputStreamReader(System.in));
        //HashMap<String, List<Integer>> associativeArray = new HashMap<>();
        String string;
        String f;
        //F     g1,g2,g3

        try {
            while ((string = reader.readLine()) != null) {
                String[] keyValue = string.split("\t");
                f = keyValue[0];
                List<String> categories = new ArrayList<>();
                categories.addAll(Arrays.asList(keyValue[1].split(",")));

                for (String g : categories) {
                    System.out.println(f + "," + g + "\t" + "1");
                }
            }
        } catch (IOException e) {

        }
    }

    static void task5_phase1_reducer() throws FileNotFoundException{
        /*File file = new File("src/hadoop/task5_reduce");
        BufferedReader reader = new BufferedReader(new FileReader(file));//new InputStreamReader(System.in));
        String string;
        HashMap<Integer, String> associativeArray = new HashMap<>();
        //f,g       1

        try {
            while ((string = reader.readLine()) != null) {
                String[] keyValue = string.split("\t")[0].split(",");
                Integer key = Integer.valueOf(keyValue[0]);

                if (!associativeArray.containsKey(key)){
                    associativeArray.put(key, keyValue[1]);
                }

            }

            for (Map.Entry<Integer, String> pair : associativeArray.entrySet()){
                System.out.println(pair.getKey() + "," + pair.getValue());
            }

        } catch (IOException e) {

        }*/
        File file = new File("src/hadoop/task5_reduce");
        BufferedReader reader = new BufferedReader(new FileReader(file));//new InputStreamReader(System.in));
        String string;
        List<String> associativeArray = new ArrayList<>();
        //f,g       1

        try {
            while ((string = reader.readLine()) != null) {
                String[] keyValue = string.split("\t");
                String key = keyValue[0];

                if (!associativeArray.contains(key)){
                    associativeArray.add(key);
                }

            }

            for (String result : associativeArray){
                System.out.println(result);
            }

        } catch (IOException e) {

        }

    }

    static void task5_phase1_second_mapper() throws FileNotFoundException{
        File file = new File("src/hadoop/task5_map2");
        BufferedReader reader = new BufferedReader(new FileReader(file));//new InputStreamReader(System.in));
        String string;
        String g;
        //f,g

        try {
            while ((string = reader.readLine()) != null) {
                String[] keyValue = string.split(",");
                g = keyValue[1];

                System.out.println(g + "\t1");
            }
        } catch (IOException e) {

        }
    }

    static void task6() throws FileNotFoundException{
        File file = new File("src/hadoop/task6");
        BufferedReader reader = new BufferedReader(new FileReader(file));//new InputStreamReader(System.in));
        String string;
        HashMap<String, Integer> associativeArray = new HashMap<>();
        Integer f = 0;
        String g = "";
        //f     g

        try {
            while ((string = reader.readLine()) != null) {
                String[] keyValue = string.split("\t");

                Integer newF = Integer.valueOf(keyValue[0]);
                String newG = keyValue[1];

                if (f.equals(newF)){
                    if (!g.equals(newG)){
                        g = newG;

                        if (associativeArray.containsKey(g)){
                            associativeArray.put(g, associativeArray.get(g) + 1);
                        }
                        else {
                            associativeArray.put(g, 1);
                        }

                    }
                }
                else {
                    f = newF;
                    g = newG;

                    if (associativeArray.containsKey(g)){
                        associativeArray.put(g, associativeArray.get(g) + 1);
                    }
                    else {
                        associativeArray.put(g, 1);
                    }
                }
            }

            SortedSet<String> keys = new TreeSet<>(associativeArray.keySet());
            for (String key : keys) {
                Integer value = associativeArray.get(key);
                System.out.println(key + "\t" + value);
            }

        } catch (IOException e) {

        }
    }

    static void task7() throws FileNotFoundException {
        File file = new File("src/hadoop/task7");
        BufferedReader reader = new BufferedReader(new FileReader(file));//new InputStreamReader(System.in));
        String string;
        String g;
        //f,g

        try {
            while ((string = reader.readLine()) != null) {
                String[] objects = string.split(" ");

                for (String object1 : objects){
                    for (String object2 : objects){
                        if (!object1.equals(object2)){
                            System.out.println(object1 + "," + object2 + "\t1");
                        }
                    }
                }
            }
        } catch (IOException e) {

        }
    }

    static void task7_stripes() throws FileNotFoundException{
        File file = new File("src/hadoop/task7");
        BufferedReader reader = new BufferedReader(new FileReader(file));//new InputStreamReader(System.in));
        String string;

        try {
            while ((string = reader.readLine()) != null) {
                String[] objects = string.split(" ");

                for (String object1 : objects){
                    ArrayList<String> stripes = new ArrayList<>();
                    ArrayList<Integer> counts = new ArrayList<>();

                    for (String object2 : objects){
                        if (!object1.equals(object2)){
                            if (stripes.contains(object2)){
                                int i = stripes.indexOf(object2);
                                counts.set(i, counts.get(i) + 1);
                            }
                            else {
                                stripes.add(object2);
                                counts.add(1);
                            }
                        }
                    }

                    System.out.print(object1 + "\t");
                    int length = stripes.size();

                    for (int i = 0; i < length - 1; i++){
                        System.out.print(stripes.get(i) + ":" + counts.get(i) + ",");
                    }

                    System.out.println(stripes.get(length - 1) + ":" + counts.get(length - 1));

                }


            }
        } catch (IOException e) {

        }
    }

}
