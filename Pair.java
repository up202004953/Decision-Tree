public class Pair implements Comparable<Pair> {
    private final String key;
    private final Double value;

    private final int factor;

    Pair(String key, Double value, int factor) {
        this.key = key;
        this.value = value;
        this.factor = factor;
    }

    public String getKey() {return key;}
    public Double getValue() {return value;}

    public int getFactor() {return factor;}

    @Override
    public int compareTo(Pair p) {
        if (value.equals(p.getValue())) {
            if (factor > p.getFactor()) return 1;
            return -1;
        }
        if (value > p.getValue()) return 1;
        else return -1;
    }

    @Override
    public String toString() {
        return key + " : " + value;
    }
}
