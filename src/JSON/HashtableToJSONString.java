package JSON;

import java.util.Hashtable;
import java.util.Iterator;

//Use the "toString()" method of this class to print out Java Hashtable in JSON format
public class HashtableToJSONString extends Hashtable {

	// field
	Hashtable hb;

	// constructor
	HashtableToJSONString(Hashtable inputHB) {
		hb = inputHB;
	}//HashtableToJSONString(Hashtable inputHB)

	@Override
	// return a JSON string for hb
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('{');

		for (Iterator it = hb.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			sb.append("\"" + key + "\":");

			Object value = (Object) hb.get(key);
			if (value instanceof String) {
				sb.append("\"" + value + "\"");
			}//if 
			else
				sb.append(value);
			
			if (it.hasNext())
			{
				sb.append(",");
			}//if
		}//for
		sb.append("}");
		return sb.toString();
	}//toString
}//HashtableToJSONString
