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

  // constructor
  public ArrayListToJSONString(ArrayList inputArrayList)
  {
    arr = inputArrayList;
  }// ArrayListToJSONString(ArrayList inputArrayList)

  @Override
  // return a JSON string for arr
    public
    String toString()
  {
    Iterator it = arr.iterator();
    StringBuilder sb = new StringBuilder();
    sb.append("[");

    while (it.hasNext())
      {
        Object currentElement = it.next();

        // add double quotation marks to elements that are strings
        if (currentElement instanceof String)
          {
            sb.append("\"" + currentElement + "\"");
          }// if
        else
          {
            sb.append(currentElement);
          }// else

        if (it.hasNext())
          {
            sb.append(",");
          }// if
      }// while

    sb.append("]");
    return sb.toString();
  }// toJSONString()
}// ArrayListToJSONString
