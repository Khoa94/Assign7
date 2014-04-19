package JSON;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

public class JSONParser
{

  /*
   * Preconditions: charIndex[0]=1 Postconditions: Will return a valid Hashtable
   */
  public static Hashtable readJSONObject(String str, int[] charIndex,
                                         Stack currentStack, Hashtable hb,
                                         ArrayList currentAL)
  {
    while (true)
      {
        if (str.charAt(charIndex[0]) == '}')
          {
            currentStack.pop();
            if (charIndex[0] < (str.length() - 1))
              {
                charIndex[0]++;
              }// if not at the last brace, increment
            return hb;
          }// if at right brace, pop off left brace from currentStack

        StringBuilder sb = new StringBuilder();

        if (charIndex[0] < (str.length() - 1))
          {
            // skip over the first quotation mark
            charIndex[0] = charIndex[0] + 1;
          }// if position is less than string length, skip over quotation mark

        if (charIndex[0] < (str.length() - 1))
          {
            // read the key
            while (str.charAt(charIndex[0]) != '\"')
              {
                sb.append(str.charAt(charIndex[0]));
                charIndex[0]++;
              }// while char is not "\""
          }// if position less than string

        // store sb to key
        String key = sb.toString();

        // reset sb
        sb.setLength(0);

        if (charIndex[0] < (str.length() - 1))
          {
            // go to the character next ':'
            charIndex[0] = charIndex[0] + 2;
          }// if

        if (str.charAt(charIndex[0]) == '\"')
          {
            // skip over the quotation mark
            charIndex[0]++;

            while (str.charAt(charIndex[0]) != '\"')
              {
                sb.append(str.charAt(charIndex[0]));
                charIndex[0]++;
              }// while not end of string, keep appending
            charIndex[0] = charIndex[0] + 2;
            String value = sb.toString();
            sb.setLength(0);

            hb.put(key, value);
            key = null;
            value = null;
          }// if char is '\"'

        else if (str.charAt(charIndex[0]) <= '9'
                 && str.charAt(charIndex[0]) >= '0')
          {

            boolean containsE = false;
            int indexOfE = 0;
            int counter = 0;
            double value = 0;

            while ((str.charAt(charIndex[0]) <= '9' && str.charAt(charIndex[0]) >= '0')
                   || (str.charAt(charIndex[0]) == 'e'))
              {
                if (str.charAt(charIndex[0]) == 'e')
                  {
                    containsE = true;
                    indexOfE = counter;
                  }// if e is in number
                sb.append(str.charAt(charIndex[0]));
                charIndex[0]++;
                counter++;
              }// while char is still number, keep appending

            if (containsE == true)
              {
                double beforeE =
                    Double.parseDouble(sb.toString().substring(0, indexOfE));
                double afterE =
                    Double.parseDouble(sb.toString().substring(indexOfE + 1,
                                                               sb.toString()
                                                                 .length()));
                value = beforeE * (Math.pow(10, afterE));
                // value=afterE;
              }// if
            else
              {
                value = Double.parseDouble(sb.toString());
              }// else
            sb.setLength(0);
            hb.put(key, value);
            key = null;
            value = 0;
          }// if char represents a number

        else if (str.charAt(charIndex[0]) == '{')
          {

            charIndex[0]++;
            Hashtable newHB = new Hashtable();
            currentStack.push('{');
            Hashtable value =
                readJSONObject(str, charIndex, currentStack, newHB, currentAL);
            hb.put(key, value);
          }// if left curly brace, add new Hashtable

        else if (str.charAt(charIndex[0]) == '[')
          {
            currentStack.push('[');
            charIndex[0]++;
            ArrayList newAL = new ArrayList();
            ArrayList value =
                readJSONArray(str, charIndex, currentStack, hb, newAL);
            hb.put(key, value);
          }// if left square brace, add new ArrayList

        else if (str.charAt(charIndex[0]) == 't'
                 || str.charAt(charIndex[0]) == 'f'
                 || str.charAt(charIndex[0]) == 'n')
          {
            while (str.charAt(charIndex[0]) != '}'
                   && str.charAt(charIndex[0]) != ',')
              {
                sb.append(str.charAt(charIndex[0]));
                charIndex[0]++;
              }// while char is not end brace or comma (seperator)
            Boolean value = Boolean.valueOf(sb.toString());

            if (value == true)
              {
                value = Boolean.TRUE;
              }// if
            else if (value == false)
              {
                value = Boolean.FALSE;
              }// else if

            else
              {
                value = null;
              }// else

            hb.put(key, value);
            key = null;
            value = null;
          }// if char is JSon Constants

        if (str.charAt(charIndex[0]) == ',')
          {
            charIndex[0]++;
          }// if char is a comma, skip over

        if (currentStack.isEmpty())
          {
            return hb;
          }// if empty, return hashtable
      }// while true
  }// Hashtable readJSONObject(String str, int[] charIndex, Stack
   // currentStack, Hashtable hb, ArrayList currentAL)

  /*
   * Preconditions: charIndex[0]=1 Postconditions: Will return a valid ArrayList
   */
  public static ArrayList readJSONArray(String str, int[] charIndex,
                                        Stack currentStack, Hashtable hb,
                                        ArrayList currentAL)
  {
    while (true)
      {
        StringBuilder sb = new StringBuilder();

        if (str.charAt(charIndex[0]) == ']')
          {
            currentStack.pop();
            if (charIndex[0] < str.length() - 1)
              {
                charIndex[0]++;
              }// if not at the last brace, increment
            return currentAL;
          }// if char is right square brace, pop off left brace from current
           // Stack

        if (str.charAt(charIndex[0]) == '\"')
          {
            // skip over the quotation mark
            charIndex[0]++;

            while (str.charAt(charIndex[0]) != '\"')
              {
                sb.append(str.charAt(charIndex[0]));
                charIndex[0]++;
              }// while char is not '\"'
            charIndex[0] = charIndex[0] + 2;
            String value = sb.toString();
            sb.setLength(0);
            currentAL.add(value);
            value = null;
          }// if char is '\"'

        else if (str.charAt(charIndex[0]) <= '9'
                 && str.charAt(charIndex[0]) >= '0')
          {

            boolean containsE = false;
            int indexOfE = 0;
            int counter = 0;
            double value = 0;

            while ((str.charAt(charIndex[0]) <= '9' && str.charAt(charIndex[0]) >= '0')
                   || (str.charAt(charIndex[0]) == 'e'))
              {
                if (str.charAt(charIndex[0]) == 'e')
                  {
                    containsE = true;
                    indexOfE = counter;
                  }// if e is in number
                sb.append(str.charAt(charIndex[0]));
                charIndex[0]++;
                counter++;
              }// while char is still number, keep appending

            if (containsE == true)
              {
                double beforeE =
                    Double.parseDouble(sb.toString().substring(0, indexOfE));
                double afterE =
                    Double.parseDouble(sb.toString().substring(indexOfE + 1,
                                                               sb.toString()
                                                                 .length()));
                value = beforeE * (Math.pow(10, afterE));
                // value=afterE;
              }// if
            else
              {
                value = Double.parseDouble(sb.toString());
              }// else
            sb.setLength(0);
            currentAL.add(value);
            value = 0;
          }// if char represents a number

        else if (str.charAt(charIndex[0]) == '{')
          {
            charIndex[0]++;
            Hashtable newHB = new Hashtable();
            currentStack.push('{');
            Hashtable value =
                readJSONObject(str, charIndex, currentStack, newHB, currentAL);
            currentAL.add(value);
          }// if left curly brace, add new hashtable

        else if (str.charAt(charIndex[0]) == '[')
          {
            ArrayList newAL = new ArrayList();
            currentStack.push('[');
            readJSONArray(str, charIndex, currentStack, hb, currentAL);
          }// if left square brace, add new ArrayList

        else if (str.charAt(charIndex[0]) == 't'
                 || str.charAt(charIndex[0]) == 'f'
                 || str.charAt(charIndex[0]) == 'n')
          {
            while (str.charAt(charIndex[0]) != ']'
                   && str.charAt(charIndex[0]) != ',')
              {
                sb.append(str.charAt(charIndex[0]));
                charIndex[0]++;
              }// while char is not end brace of comma (seperator)
               // charIndex[0]++;
            Boolean value = Boolean.valueOf(sb.toString());
            if (value == true)
              {
                value = Boolean.TRUE;
              }// if
            else if (value == false)
              {
                value = Boolean.FALSE;
              }// else if

            else
              {
                value = null;
              }// else
            currentAL.add(value);
            value = null;
          }// if char represents JSON constant
        if (str.charAt(charIndex[0]) == ',')
          {
            charIndex[0] = charIndex[0] + 1;
          }// if comma, skip over

        if (currentStack.isEmpty())
          {
            return currentAL;
          }// if currentStack is empty, return currentAL
      }// while true
  }// ArrayList readJSONArray(String str, int[] charIndex, Stack currentStack,
   // Hashtable hb, ArrayList currentAL)

  /*
   * Preconditions: str is a string in JSON format (there's no spaces in str).
   * Postconditions: Will return a valid Hashtable
   */
  public static Hashtable JSONObjectToHashtable(String str)
  {
    Stack<Character> currentStack = new Stack<Character>();

    if (str.charAt(0) == '{')
      {
        currentStack.push('{');
        Hashtable hb = new Hashtable();
        ArrayList currentAL = new ArrayList();
        int[] charIndex = { 1 };
        return (readJSONObject(str, charIndex, currentStack, hb, currentAL));
      }// if first brace is curly left brace
    else
      return null;
  }// JSONObjectToHashtable(String str)

  /*
   * Preconditions: str is a string in JSON format (there's no spaces in str).
   * Postconditions: Will return a valid ArrayList
   */
  public static ArrayList JSONObjectToArrayList(String str)
  {
    Stack<Character> currentStack = new Stack<Character>();

    if (str.charAt(0) == '[')
      {
        currentStack.push('[');
        Hashtable hb = new Hashtable();
        ArrayList currentAL = new ArrayList();
        int[] charIndex = { 1 };
        return (readJSONArray(str, charIndex, currentStack, hb, currentAL));
      }// if first brace is square left brace
    else
      return null;
  }// ArrayList JSONObjectToArrayList(String str)

  // all the methods work as expected.
  public static void main(String[] args)
  {
    String str1 = "{\"firstName\":\"John\",\"lastName\":\"Doe\" }";
    String str2 = "{\"id\":32e5}";
    String str3 =
        "{\"Department\":\"CSC\",\"Number\":32e2,\"Prof\":{\"LName\":\"Rebelsky\",\"FName\":\"Sam\"}}";
    String str4 =
        "{\"glossary\":{\"title\":\" example glossary\",\"GlossDiv\":{\"title\":\"S\",\"GlossList\":{\"GlossEntry\":{\"ID\":\"SGML\",\"SortAs\":\"SGML\",\"GlossTerm\":\"StandardGeneralizedMarkupLanguage\",\"Acronym\":\"SGML\",\"Abbrev\":\"ISO\",\"GlossDef\":{\"para\":\"AmetamarkuplanguageusedtocreatemarkuplanguagessuchasDocBook.\",\"GlossSeeAlso\":\"GML\"},\"GlossSee\":\"markup\"}}}}}";
    String str5 = "[212,\"a\",{\"id\":32},true]";
    String str21 = "{\"id\":32,\"ugly\":{\"a\":{\"hello\":21}}}";
    String str22 = "{\"id\":32,\"ugly\":[\"a\",{\"hello\":21}]}";
    String str23 = "{\"ab\":[1,2]}";
    String str61 = "{\"ab\":{\"cd\":12}}";

    Hashtable result = JSONObjectToHashtable(str2);
    System.out.println(result);

    // converting Hashtable and ArrayList to string in JSON format
    Hashtable hb1 = new Hashtable();
    hb1.put("a", "1");
    hb1.put("b", 2345);
    hb1.put("c", "89");

    HashtableToJSONString hb2 = new HashtableToJSONString(hb1);
    System.out.println(hb2.toString());

    ArrayList ar1 = new ArrayList();
    ar1.add("ab");
    ar1.add(12);
    ar1.add(hb2);
    ar1.add("c");
    ArrayListToJSONString ar2 = new ArrayListToJSONString(ar1);
    System.out.println(ar2.toString());
    // System.out.println(JavaObjectsToJSONString(ar2));
  }// main
}// JSONParser Class
