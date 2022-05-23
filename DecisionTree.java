import java.util.*;

public class DecisionTree {
    private static final int discrLen = 4;
    String id;
    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> answer = new ArrayList<>();
    String res;
    ArrayList<Entry> all;

    DecisionTree(ArrayList<Entry> all) {
        this.all = all;

        titles.addAll(all.get(0).getMap().keySet());

        id = titles.get(0);
        res = titles.get(titles.size()-1);
        for (Entry e : all) answer.add(e.getMap().get(res));
    }

    public void check_loo(Node root, List<Entry> test) {
        for(Entry e : test) {
            System.out.println(e.getMap().get(id)+ ": " + find(root, e));
        }
    }

    private String find(Node cur, Entry e) {
        if (cur.getNext().isEmpty()) return cur.getValue();

        String value = e.getMap().get(cur.getValue());

        for (Node n : cur.getNext()) {
            if (isPart(n.getValue(), value)) return find(n.getNext().get(0), e);
        }

        return "Error";
    }

    private Boolean isPart(String a, String b) {
        if (isNumber(a)) return Double.parseDouble(a) == Double.parseDouble(b);
        else {
            if (a.charAt(0) == ']' || a.charAt(0) == '[') {
                String[] interval = a.split(",");
                Double num = Double.parseDouble(b);

                Double min;
                interval[0] = interval[0].substring(1);
                if (isNumber(interval[0])) min = Double.parseDouble(interval[0]);
                else min = -1.0;

                Double max;
                interval[1] = interval[1].substring(1, interval[1].length() - 1);
                if (isNumber(interval[1])) max = Double.parseDouble(interval[1]);
                else max = -1.0;

                if (max == -1.0) {
                    return num >= min;
                } else if (min == -1.0) {
                    return num < max;
                } else {
                    return num >= min && num < max;
                }
            }
            else return a.equals(b);
        }
    }
    public Node ID3pp() {
        return ID3pp(all, titles);
    }

    public Node ID3pp(List<Entry> examples, List<String> attributes) {
        Node tree;

        if (allEqual(examples)) return new Node(examples.get(0).getMap().get(res), examples.size());
        if (attributes.size() == 2) return new Node(mostCommon(examples), examples.size());

        List<Pair> entropyList = getEntropy(examples, attributes);
        String A = entropyList.get(0).getKey();
        tree = new Node(A, examples.size());

        Set<String> set = getAnswer(examples, A).keySet();
        attributes.remove(A);

        for (String s : set) {
            List<Entry> newExamples = new ArrayList<>();

            for (Entry e : examples) {
                if (isPart(s, e.getMap().get(A))) newExamples.add(e);
            }

            Node next = new Node(s, newExamples.size());
            next.setNext(ID3pp(newExamples, attributes));
            tree.addNext(next);
        }

        return tree;
    }

    private boolean allEqual(List<Entry> l) {
        String first = l.get(0).getMap().get(res);
        for (int i = 1; i < l.size(); i++) {
            if (!first.equals(l.get(i).getMap().get(res))) return false;
        }
        return true;
    }

    public String mostCommon(List<Entry> list) {
        Map<String, Integer> map = new HashMap<>();

        for (Entry e : list) {
            String s = e.getMap().get(res);
            Integer val = map.get(s);
            map.put(s, val == null ? 1 : val + 1);
        }

        String max = null;

        for(String s : map.keySet()) {
            if (max == null) max = s;
            else {
                if (map.get(s) > map.get(max)) max = s;
            }
        }

        return max;
    }

    private List<Pair> getEntropy(List<Entry> all, List<String> titles) {
        ArrayList<Pair> entropy = new ArrayList<>();
        for (String title : titles) {
            if (title.equals(id) || title.equals(res)) continue;

            Map<String, Answer> map = getAnswer(all, title);
            Double value = calculate(map.values());
            entropy.add(new Pair(title, value, map.size()));
        }
        Collections.sort(entropy);
        return entropy;
    }

    private Map<String, Answer> getAnswer(List<Entry> all, String title) {
        Map<String, Answer> map = new HashMap<>();

        if (isNumber(all.get(0).getMap().get(title))) {
            Map<Double, Answer> curMap = new HashMap<>();

            for (Entry e : all) {
                double symb = Double.parseDouble(e.getMap().get(title));

                Answer a;
                if (curMap.get(symb) == null) {
                    a = new Answer(answer);
                }
                else a = curMap.get(symb);

                a.put(answer.get(this.all.indexOf(e)));
                curMap.put(symb, a);
            }

            if (curMap.size() > 5) {
                double max = Collections.max(curMap.keySet()), min = Collections.min(curMap.keySet());
                double size = (max-min) / discrLen;

                for (Entry e : all) {
                    double k = Double.parseDouble(e.getMap().get(title));
                    int symb = Math.min(div(k - min,size), (int) (max/size));

                    String name;
                    if (symb == 0) name = "]-inf, " + (min + size) + "[";
                    else if (symb == discrLen - 1) name = "[" + (min + symb*size) + ", +inf[";
                    else name = "[" + (min + symb*size) + ", " + (min + (symb+1)*size) + "[";

                    Answer a;
                    if (map.get(name) == null) {
                        a = new Answer(answer);
                    }
                    else a = map.get(name);

                    a.put(answer.get(this.all.indexOf(e)));
                    map.put(name, a);
                }
            }
            else {
                for (Double d : curMap.keySet()) {
                    map.put(Double.toString(d), curMap.get(d));
                }
            }
        }
        else {
            for (Entry e : all) {
                String symb = e.getMap().get(title);

                Answer a;
                if (map.get(symb) == null) {
                    a = new Answer(answer);
                }
                else a = map.get(symb);

                a.put(answer.get(this.all.indexOf(e)));
                map.put(symb, a);
            }
        }

        return map;
    }

    private double calculate(Collection<Answer> classes) {
        int t = 0;
        for (Answer ans : classes) {
            for (int v : ans.getAnswer().values()) {
                t += v;
            }
        }

        double sum = 0;

        for (Answer ans : classes) {
            double n = ans.getValueSize();

            double parcel = 0;
            for (Integer p : ans.getAnswer().values()) {
                if (p != 0) parcel += - (p/n)*Math.log(p/n)/Math.log(2);
            }

            sum += (n/t) * parcel;
        }

        return sum;
    }

    private boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
        }
        catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private int div(double a, double b) {
        int sum = 0;
        while(a > b) {
            sum++;
            a -= b;
        }

        return sum;
    }
}