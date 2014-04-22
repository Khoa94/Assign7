package JSON;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

public class JSONParserTest {

	@Test
	public void testJSONObjectToHashtable() {

	}

	@Test
	public void testJSONObjectToArrayList() {
		ArrayList ALTester1= new ArrayList();
		ALTester1.add(1);
		ALTester1.add(2);
		//ArrayList hello = new JSONParser.JSONObjectToArrayList ("[1,2]");
	//	ArrayList hello = JSONParser.JSONObjectToArrayList ("[1,2]");
		assertEquals(JSONParser.JSONObjectToArrayList ("[1,2]"), ALTester1);
		
//		ArrayList ALTester2= new ArrayList();
//		ALTester2.add(212);
//		ALTester2.add("a");
//	Hashtable ALTestHash= new Hashtable();
//	ALTestHash.put("id", 32);
//		ALTester2.add(ALTestHash);
////		ALTester2.add(true);
//		ArrayList hello =JSONParser.JSONObjectToArrayList ("[212,\"a\",{\"id\":32}]");
//			//	+ ",true]");
//		System.out.print(hello.equals(ALTester2));
		
		ArrayList ALTester3= new ArrayList();
		ALTester3.add("Why");
		ALTester3.add("are");
		ALTester3.add("there");
		ALTester3.add("so");
		ALTester3.add("many");
		ALTester3.add("commas");
		assertEquals(JSONParser.JSONObjectToArrayList ("[\"Why\",\"are\",\"there\",\"so\",\"many\",\"commas\"]"), ALTester3);	
		
	}

}
