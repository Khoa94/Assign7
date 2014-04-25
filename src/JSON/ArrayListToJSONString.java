package JSON;

import java.util.ArrayList;
import java.util.Iterator;

//Use the "toString()" method of this class to print out Java ArrayList in JSON format
public class ArrayListToJSONString
    extends
      ArrayList
{
  // field
  ArrayList arr;

  /*
   * Constructor Preconditions: inputArrayList is an ArrayList PostCondition:
   * arr = inputArrayList
   */
  public ArrayListToJSONString (ArrayList inputArrayList)
  {
    arr = inputArrayList;
  }// ArrayListToJSONString(ArrayList inputArrayList)

  @Override
  /*
   * return a JSON string for arr Precondition: arr is initialized
   * Postcondition: return a JSON string for arr
   * 
   * @see java.util.AbstractCollection#toString()
   */
  public String
    toString ()
  {
    Iterator it = arr.iterator ();
    StringBuilder sb = new StringBuilder ();
    sb.append ("[");

    while (it.hasNext ())
      {
        Object currentElement = it.next ();

        // add double quotation marks to elements that are strings
        if (currentElement instanceof String)
          {
            sb.append ("\"" + currentElement + "\"");
          }// if
        else
          {
            sb.append (currentElement);
          }// else

        if (it.hasNext ())
          {
            sb.append (",");
          }// if
      }// while

    sb.append ("]");
    return sb.toString ();
  }// toJSONString()
}// ArrayListToJSONString
