import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String path = System.getProperty("user.dir")+"\\src\\java\\DecisionTree\\"; // Change this pls

    public static void main(String[] args) {
        ArrayList<Entry> all = (ArrayList<Entry>) read(args[0]);

        DecisionTree dt = new DecisionTree(all);
        Node root = dt.ID3pp();
        Node.print(root);

        ArrayList<Entry> test = (ArrayList<Entry>) read(args[1]);
        dt.check_loo(root, test);
    }

    public static List<Entry> read(String filename) {
        ArrayList<Entry> all = new ArrayList<>();

        try {
            Scanner cvs = new Scanner(new File(path+filename));

            String line = cvs.nextLine();
            String[] sep = line.split(",");

            ArrayList<String> titles = new ArrayList<>();
            Collections.addAll(titles, sep);

            while(cvs.hasNextLine()) {
                line = cvs.nextLine();
                ArrayList<String> values = new ArrayList<>();

                sep = line.split(",");

                Collections.addAll(values, sep);

                all.add(new Entry(titles, values));
            }
            cvs.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        return all;
    }
}
