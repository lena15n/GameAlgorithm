package hadoop;

import java.io.*;

/**
 * Created by Lena on 12.06.2016.
 */
public class Hadoop {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("src/hadoop/example.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));//new InputStreamReader(System.in));
        String string;

        try {
            String key = "";
            int counter = 1;

            while ((string = reader.readLine()) != null) {
                String[] values = string.split("\t");

                if (key.equals(values[0])){
                    counter++;
                }
                else {
                    if (!key.isEmpty()) {
                        System.out.println(key + "\t" + counter);
                    }

                    key = values[0];
                    counter = 1;
                }
            }

            if (!key.isEmpty()) {
                System.out.println(key + "\t" + counter);
            }

        } catch (IOException e) {

        }
    }
}
