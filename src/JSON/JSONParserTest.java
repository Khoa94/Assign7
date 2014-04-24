package JSON;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Hashtable;

import org.junit.Test;

// All tests passed

public class JSONParserTest
{

  @Test
  public void testJavaObjectToJSONString()
  {
    // Convert Hashtable to string in JSON format
    Hashtable hb1 = new Hashtable();
    hb1.put("a", "1");
    hb1.put("b", 2345);
    hb1.put("c", "89");
    HashtableToJSONString hb1JSON = new HashtableToJSONString(hb1);
    assertEquals("{\"b\":2345,\"a\":\"1\",\"c\":\"89\"}", hb1JSON.toString());

    // Convert ArrayList to string in JSON format
    ArrayList ar1 = new ArrayList();
    ar1.add("ab");
    ar1.add(12);
    ar1.add(hb1JSON);
    ar1.add("c");
    ArrayListToJSONString ar1JSON = new ArrayListToJSONString(ar1);
    assertEquals("[\"ab\",12,{\"b\":2345,\"a\":\"1\",\"c\":\"89\"},\"c\"]",
                 ar1JSON.toString());

    // A more complicated case
    Hashtable hb2 = new Hashtable();
    hb2.put("d", 123);
    hb2.put("e", ar1JSON);
    hb2.put("f", hb1JSON);
    hb2.put("ghi", "xyz");
    HashtableToJSONString hb2JSON = new HashtableToJSONString(hb2);
    assertEquals("{\"f\":{\"b\":2345,\"a\":\"1\",\"c\":\"89\"},\"e\":[\"ab\",12,{\"b\":2345,\"a\":\"1\",\"c\":\"89\"},\"c\"],\"ghi\":\"xyz\",\"d\":123}",
                 hb2JSON.toString());
  }// testJavaObjectToJSONString()

  @Test
  public void testJSONStringToJavaObject() throws Exception
  {
    // test 1
    String str1 = "{\"firstName\":\"John\",\"lastName\":\"Doe\" }";
    Hashtable hb1 = JSONParser.JSONObjectToHashtable(str1);
    Hashtable hb1Expected = new Hashtable();
    hb1Expected.put("firstName", "John");
    hb1Expected.put("lastName", "Doe");
    assertTrue(hb1Expected.equals(hb1));

    // test 2
    String str2 = "{\"id\":32,\"ugly\":[\"a\",{\"hello\":21}]}";
    Hashtable hb2 = JSONParser.JSONObjectToHashtable(str2);
    Hashtable hb2Expected = new Hashtable();
    hb2Expected.put("id", 32);

    ArrayList ar = new ArrayList();
    ar.add("a");
    Hashtable temp = new Hashtable();
    temp.put("hello", 21);
    ar.add(temp);

    hb2Expected.put("ugly", ar);
    assertTrue(hb2Expected.equals(hb2));

    // test 3
    ArrayList ALTester1 = new ArrayList();
    ALTester1.add(1);
    ALTester1.add(2);
    assertEquals(JSONParser.JSONObjectToArrayList("[1,2]"), ALTester1);

    // test 4
    ArrayList ALTester2 = new ArrayList();
    ALTester2.add(true);
    ALTester2.add("Why");
    Hashtable temp1 = new Hashtable();
    ArrayList ALtemp1 = new ArrayList();
    ALtemp1.add(777);
    temp1.put("Yes", ALtemp1);
    ALTester2.add(temp1);
    assertEquals(JSONParser.JSONObjectToArrayList("[true,\"Why\",{\"Yes\"=[777]}]"),
                 ALTester2);

  }// testJSONStringToJavaObject()
}// JSONParserTest