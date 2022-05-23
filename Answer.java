import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Answer {
    private final Map<String, Integer> answer = new HashMap<>();

    Answer(ArrayList<String> answer) {
        for (String a : answer) {
            this.answer.put(a, 0);
        }
    }

    public Map<String, Integer> getAnswer() {return answer;}
    public void put(String key) {answer.put(key, answer.get(key) + 1);}
    public int getValueSize() {
        int size = 0;
        for (Integer i : answer.values()) size += i;

        return size;
    }

    @Override
    public String toString() {
        return answer.toString();
    }
}
