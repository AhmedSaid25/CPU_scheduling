import java.util.Comparator;

public class Comp implements Comparator<process> {
    @Override
    public int compare(process o1, process o2) {
        return o1.arrivalTime - o2.arrivalTime;
    }
}
