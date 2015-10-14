package tools;

import java.util.Comparator;
import java.util.Map;

//source: http://stackoverflow.com/questions/109383/how-to-sort-a-mapkey-value-on-the-values-in-java
public class MapValueComparator implements Comparator<String>{

	private Map<String, Double> base;

    public MapValueComparator(Map<String, Double> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with
    // equals.
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }

}
