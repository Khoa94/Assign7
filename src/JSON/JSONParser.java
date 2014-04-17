package JSON;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Stack;

public class JSONParser {

	public static Hashtable readJSONObject(String str, int[] charIndex,
			Stack currentStack, Hashtable hb, ArrayList currentAL) {
		while (true) {
			if (str.charAt(charIndex[0]) == '}') {
				currentStack.pop();
				if (charIndex[0] < (str.length() - 1)) {
					charIndex[0]++;
				}
				return hb;
			}

			StringBuilder sb = new StringBuilder();

			if (charIndex[0] < (str.length() - 1)) {
				// skip over the first quotation mark
				charIndex[0] = charIndex[0] + 1;
			}

			if (charIndex[0] < (str.length() - 1)) {
				// read the key
				while (str.charAt(charIndex[0]) != '\"') {
					sb.append(str.charAt(charIndex[0]));
					charIndex[0]++;
				}
			}

			// store sb to key
			String key = sb.toString();

			// reset sb
			sb.setLength(0);

			if (charIndex[0] < (str.length() - 1)) {
				// go to the character next ':'
				charIndex[0] = charIndex[0] + 2;
			}

			if (str.charAt(charIndex[0]) == '\"') {
				// skip over the quotation mark
				charIndex[0]++;

				while (str.charAt(charIndex[0]) != '\"') {
					sb.append(str.charAt(charIndex[0]));
					charIndex[0]++;
				}
				charIndex[0] = charIndex[0] + 2;
				String value = sb.toString();
				sb.setLength(0);

				hb.put(key, value);
				key = null;
				value = null;
			}

			else if (str.charAt(charIndex[0]) <= '9'
					&& str.charAt(charIndex[0]) >= '0') {
				while (str.charAt(charIndex[0]) <= '9'
						&& str.charAt(charIndex[0]) >= '0') {
					sb.append(str.charAt(charIndex[0]));
					charIndex[0]++;
				}
				int value = Integer.parseInt(sb.toString());
				sb.setLength(0);
				hb.put(key, value);
				key = null;
				value = 0;
			}

			else if (str.charAt(charIndex[0]) == '{') {
				
				charIndex[0]++;
				Hashtable newHB = new Hashtable();
				currentStack.push('{');
				Hashtable value = readJSONObject(str, charIndex, currentStack,
						newHB, currentAL);
				hb.put(key, value);
			}

			else if (str.charAt(charIndex[0]) == '[') {
				currentStack.push('[');
				charIndex[0]++;
				ArrayList value = readJSONArray(str, charIndex, currentStack,
						hb, currentAL);
				hb.put(key, value);
			}

			else if (str.charAt(charIndex[0]) == 't'|| str.charAt(charIndex[0]) == 'f') {
				while (str.charAt(charIndex[0]) != '}'
						&& str.charAt(charIndex[0]) != ',') {
					sb.append(str.charAt(charIndex[0]));
					charIndex[0]++;
				}
				Boolean value = Boolean.valueOf(sb.toString());
				hb.put(key, value);
				key = null;
				value = null;
			}

			if (str.charAt(charIndex[0]) == ',') {
				charIndex[0]++;
				// readJSONObject(str, charIndex, currentStack, hb, currentAL);
			}

			if (currentStack.isEmpty()) {
				return hb;
			}
		}// while true
	}

	
	public static ArrayList readJSONArray(String str, int[] charIndex,
			Stack currentStack, Hashtable hb, ArrayList currentAL) {
		while (true) {
			StringBuilder sb = new StringBuilder();

			if (str.charAt(charIndex[0]) == ']') {
				currentStack.pop();
				if (charIndex[0] < str.length() - 1) {
					charIndex[0]++;
				}
			}

			if (str.charAt(charIndex[0]) == '\"') {
				// skip over the quotation mark
				charIndex[0]++;

				while (str.charAt(charIndex[0]) != '\"') {
					sb.append(str.charAt(charIndex[0]));
					charIndex[0]++;
				}
				charIndex[0] = charIndex[0] + 2;
				String value = sb.toString();
				sb.setLength(0);

				currentAL.add(value);
				value = null;
			}

			else if (str.charAt(charIndex[0]) <= '9'
					&& str.charAt(charIndex[0]) >= '0') {
				while (str.charAt(charIndex[0]) <= '9'
						&& str.charAt(charIndex[0]) >= '0') {
					sb.append(str.charAt(charIndex[0]));
					charIndex[0]++;
				}
				int value = Integer.parseInt(sb.toString());
				sb.setLength(0);
				currentAL.add(value);
				value = 0;
			}

			else if (str.charAt(charIndex[0]) == '{') {
				charIndex[0]++;
				Hashtable newHB = new Hashtable();
				currentStack.push('{');
				Hashtable value = readJSONObject(str, charIndex, currentStack,
						hb, currentAL);
				currentAL.add(value);
			}

			else if (str.charAt(charIndex[0]) == '[') {
				readJSONArray(str, charIndex, currentStack, hb, currentAL);
				currentStack.push('[');
			}

			else if (str.charAt(charIndex[0]) == 't'
					|| str.charAt(charIndex[0]) == 'f') {
				while (str.charAt(charIndex[0]) != ']'
						&& str.charAt(charIndex[0]) != ',') {
					sb.append(str.charAt(charIndex[0]));
					charIndex[0]++;
				}
				// charIndex[0]++;
				Boolean value = Boolean.valueOf(sb.toString());
				currentAL.add(value);
				value = null;
			}
			if (str.charAt(charIndex[0]) == ',') {
				charIndex[0] = charIndex[0] + 1;
			}

			if (currentStack.isEmpty()) {
				return currentAL;
			}
		}// while true
	}

	public static Hashtable JSONToObject(String str) {
		Stack<Character> currentStack = new Stack<Character>();
		
	    if (str.charAt (0) == '{')
      {
        currentStack.push ('{');
        Hashtable hb = new Hashtable ();
        ArrayList currentAL = new ArrayList ();
        int [] charIndex = {1};
        return (readJSONObject (str, charIndex, currentStack, hb, currentAL));
      }
//		if (str.charAt(0) == '[') {
//			currentStack.push('[');
//			Hashtable hb = new Hashtable();
//			ArrayList currentAL = new ArrayList();
//			int[] charIndex = { 1 };
//			return (readJSONArray(str, charIndex, currentStack, hb, currentAL));
//		}

		else {
			return null;
		}

	}
	
	/*
	 * pre-condition: obj must be either a HashTable or an ArrayList
	 * post-condition: return a string in JSON format
	 */
	public static String JavaObjectsToJSONString (Object obj)
	{
		String str = obj.toString();
		
		//delete all the blank spaces
		str=str.replaceAll("\\s+","");
		
		//replace '=' with ':'
		str=str.replaceAll("=", ":");
		return str;
	}
	
	
	public static void main(String[] args) {
		String str1 = "{\"firstName\":\"John\",\"lastName\":\"Doe\" }";
		String str2 = "{\"id\":32}";
		String str3 = "{\"Department\":\"CSC\",\"Number\":207,\"Prof\":{\"LName\":\"Rebelsky\",\"FName\":\"Sam\"}}";
		String str4 = "{\"glossary\":{\"title\":\" example glossary\",\"GlossDiv\":{\"title\":\"S\",\"GlossList\":{\"GlossEntry\":{\"ID\":\"SGML\",\"SortAs\":\"SGML\",\"GlossTerm\":\"StandardGeneralizedMarkupLanguage\",\"Acronym\":\"SGML\",\"Abbrev\":\"ISO\",\"GlossDef\":{\"para\":\"AmetamarkuplanguageusedtocreatemarkuplanguagessuchasDocBook.\",\"GlossSeeAlso\":\"GML\"},\"GlossSee\":\"markup\"}}}}}";
		String str5 = "[212,\"a\",{\"id\":32},true]";
		String str0 = "{\"employees\":[\"firstName\":\"John\",\"lastName\":\"Doe\"},{\"firstName\":\"Anna\",\"lastName\":\"Smith\"},{\"firstName\":\"Peter\",\"lastName\":\"Jones\"}]}";
		String str21 = "{\"id\":32,\"ugly\":{\"a\":{\"hello\":21}}}";
		String str22 = "{\"id\":32,\"ugly\":[\"a\",{\"hello\":21}]}";
		String str23 = "{\"ab\":[1,2]}";

		Hashtable hb1 = new Hashtable();
		hb1.put("a", "1");
		hb1.put("b", "2");
		hb1.put("c", "3");
		
		ArrayList ar1 = new ArrayList();
		ar1.add("a");
		ar1.add("b");
		ar1.add(hb1);
		ar1.add("c");
		
		System.out.println(JavaObjectsToJSONString(ar1));
		

	}// main
}