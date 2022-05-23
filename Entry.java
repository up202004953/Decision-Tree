import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Entry {
    private final LinkedHashMap<String, String> map = new LinkedHashMap<>();

    Entry(ArrayList<String> title, ArrayList<String> values) {
        for (int i = 0; i < title.size(); i++) map.put(title.get(i), values.get(i));
    }

    public Map<String, String> getMap() {return map;}

    @Override
    public String toString() {
        for (String k : map.keySet()) System.out.println(k + ": " + map.get(k));
        return "";
    }
}
