package hadoop;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lena on 15.06.2016.
 */
public class Program1 {
    public static void main(String[] args) throws FileNotFoundException {
        //task1();
        //task2();
        //task3();
        task4();
    }

    static void task1() throws FileNotFoundException{
        File file = new File("src/hadoop/task11");
        BufferedReader reader = new BufferedReader(new FileReader(file));//new InputStreamReader(System.in));
        String string;
        String USER = "user10";

        try {
            while ((string = reader.readLine()) != null) {
                String[] userInfo = string.split("\t");

                if (userInfo[1].equals(USER)){
                    System.out.println(string);
                }
            }
        } catch (IOException e) {

        }
    }

    static void task2() throws FileNotFoundException{
        File file = new File("src/hadoop/task12_2");
        BufferedReader reader = new BufferedReader(new FileReader(file));//new InputStreamReader(System.in));
        String string;

        try {
            while ((string = reader.readLine()) != null) {
                String[] userInfo = string.split("\t");

                System.out.println(userInfo[2]);

            }
        } catch (IOException e) {

        }
    }

    static void task3() throws FileNotFoundException{
        File file = new File("src/hadoop/task13");
        BufferedReader reader = new BufferedReader(new FileReader(file));//new InputStreamReader(System.in));
        String string;
        ArrayList<String> associativeArray = new ArrayList<>();

        try {
            while ((string = reader.readLine()) != null) {
                String[] element = string.split("\t");

                if (!associativeArray.contains(element[0])){
                    associativeArray.add(element[0]);
                }
            }

            for (String element : associativeArray) {
                System.out.println(element);
            }

        } catch (IOException e) {

        }
    }

    static void task4() throws FileNotFoundException{
        File file = new File("src/hadoop/task14");
        BufferedReader reader = new BufferedReader(new FileReader(file));//new InputStreamReader(System.in));
        String string;
        HashMap<String, Integer> associativeArray = new HashMap<>();

        try {
            while ((string = reader.readLine()) != null) {
                String[] element = string.split("\t");
                String key = element[0];
                if (associativeArray.containsKey(key)){
                    associativeArray.put(key, associativeArray.get(key) + 1);
                }
                else {
                    associativeArray.put(key, 1);
                }
            }

            for (Map.Entry<String, Integer> element : associativeArray.entrySet()) {
                if (element.getValue() == 2) {
                    System.out.println(element.getValue());
                }
            }

        } catch (IOException e) {

        }

        /*File file = new File("src/hadoop/task14");
        BufferedReader reader = new BufferedReader(new FileReader(file));//new InputStreamReader(System.in));
        String string;
        HashMap<String, Integer> associativeArray = new HashMap<>();

        try {
            while ((string = reader.readLine()) != null) {
                String[] element = string.split("\t");
                String key = element[0];
                if (associativeArray.containsKey(key)){
                    associativeArray.put(key, associativeArray.get(key) + 1);
                }
                else {
                    associativeArray.put(key, 1);
                }
            }

            for (Map.Entry<String, Integer> element : associativeArray.entrySet()) {
                if (element.getValue() == 2) {
                    System.out.println(element.getValue());
                }
            }

        } catch (IOException e) {

        }*/
    }
}
