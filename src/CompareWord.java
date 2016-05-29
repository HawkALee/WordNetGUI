import java.util.Comparator;


public class CompareWord implements Comparator{
	public final int compare(Object pFirst, Object pSecond) {
			word l  =  (word) pFirst;
			word r = (word) pSecond;
			int diff = l.Getname().compareTo(r.Getname());
			return diff;
	}
}
