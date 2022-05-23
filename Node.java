import java.util.*;

public class Node {
    private final String label;
    private final int counter;
    private List<Node> next = new ArrayList<>();

    Node(String label, int counter) {
        this.label = label;
        this.counter = counter;
    }

    public String getValue() {return label;}
    public int getCounter() {return counter;}
    public List<Node> getNext() {return next;}

    public void setNext(Node n) {
        next = new ArrayList<>();
        next.add(n);
    }

    public void addNext(Node n) {next.add(n);}

    public static void print(Node root) {
        System.out.println("Decision Tree: ");
        print(root, 0, true);
    }

    private static void print(Node cur, int height, Boolean title) {
        if (title) {
            if (!cur.getNext().isEmpty()) {
                if (height != 0) {
                    System.out.println();
                    for (int i = 0; i < height; i++) System.out.print("  ");
                }
                System.out.println("<"+cur.getValue()+"> ");
            }
            else System.out.println("\""+cur.getValue()+"\"  ("+cur.getCounter()+")");
        }
        else {
            for (int i = 0; i < height; i++) System.out.print("  ");
            System.out.print(cur.getValue()+ ": ");
        }

        title ^= true;

        if (!cur.getNext().isEmpty())  {
            for (Node n : cur.getNext()) {
                print(n, height+1, title);
            }
        }
    }
}
