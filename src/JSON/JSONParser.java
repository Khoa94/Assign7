package JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

/*
 For converting from a JSON string to a Java objects:

 The JSONParser will take any correctly syntaxed JSON String, and convert it into its relative Java Hashtable or ArrayList. 
 There are two functions within this class, the JSONObjectToHashtable that returns a Hashtable, and the JSONObjectToArrayList that returns an ArrayList.
 The user must dictate which function to use, by looking at the first character of the JSON string that they want to convert. 
 If the first character is '{', then the user must call the readJSONObject function, 
 otherwise, if the first character is '[', the user must call the readJSONArray function.

 For converting from a Java object to a JSON string:
 Our program only support converting Hashtable and ArrayList to JSONString. To convert a Java Hashtable
 or ArrayList to JSON string, build an object of the class HashtableToJSONString
 or ArrayListToJSONString and call the "toString()" method of that object.
 An ArrayList will be converted to a JSON array. A Hashtable will be converted to a JSON object.

 */

public class JSONParser
{

  public static String URLReader(String inputURL)
    throws IOException
  {
    StringBuilder sb = new StringBuilder();
    URL oracle = new URL(inputURL);
    BufferedReader in =
        new BufferedReader(new InputStreamReader(oracle.openStream()));
    String inputLine;
    while ((inputLine = in.readLine()) != null)
      sb.append(inputLine);

    in.close();
    return sb.toString();
  }

  /*
   * Preconditions: charIndex[0]=1 Postconditions: Will return a valid
   * Hashtable
   */
  public static Hashtable readJSONObject(String str, int[] charIndex,
                                         Stack<Character> currentStack,
                                         Hashtable hb, ArrayList currentAL)
    throws Exception
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
              }// while not end of stristr.replaceAll("\\s+","");ng, keep appending
            charIndex[0] = charIndex[0] + 1;
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
            int value = 0;

            while ((str.charAt(charIndex[0]) <= '9' && str.charAt(charIndex[0]) >= '0')
                   || (str.charAt(charIndex[0]) == 'e') || (str.charAt(charIndex[0]) == '.'))
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
                value = (int) (beforeE * (Math.pow(10, afterE)));
                // value=afterE;
              }// if
            else
              {
                value = (int) Double.parseDouble(sb.toString());
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
   * Preconditions: charIndex[0]=1 Postconditions: Will return a valid
   * ArrayList
   */
  public static ArrayList readJSONArray(String str, int[] charIndex,
                                        Stack<Character> currentStack,
                                        Hashtable hb, ArrayList currentAL)
    throws Exception
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
          }// if char is right square brace, pop off left brace from current Stack

        if (str.charAt(charIndex[0]) == '\"')
          {
            // skip over the quotation mark
            charIndex[0]++;

            while (str.charAt(charIndex[0]) != '\"')
              {
                sb.append(str.charAt(charIndex[0]));
                charIndex[0]++;
              }// while char is not '\"'
            charIndex[0] = charIndex[0] + 1;
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
            int value = 0;

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
                value = (int) (beforeE * (Math.pow(10, afterE)));
                // value=afterE;
              }// if
            else
              {
                value = (int) Double.parseDouble(sb.toString());
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
    throws Exception
  {
    Stack<Character> currentStack = new Stack<Character>();

    //get rid of white spaces
    str.replaceAll("\\s+", "");

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
    throws Exception
  {
    Stack<Character> currentStack = new Stack<Character>();

    //get rid of white spaces
    str.replaceAll("\\s+", "");

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
    throws Exception
  {
    String str =
        URLReader("http://api.kivaws.org/v1/loans/search.json?status=fundraising");
    str.replaceAll("\\s+","");
//    System.out.println(str);
  //  Hashtable hb = JSONObjectToHashtable("{\"balance\":1000.21,\"num\":100,\"nickname\":null,\"is_vip\":true,\"name\":\"foo\"}");
    Hashtable hb = JSONObjectToHashtable(str);
    ArrayList al = JSONObjectToArrayList("[\"hello\",\"ugly\",\"face\",\"sometimes\",{\"bye\":12}]");
    //Object str2 = hb.get("paging");
   System.out.println(al);

  }// main
}// JSONParser Class

