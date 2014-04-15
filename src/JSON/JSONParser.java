package JSON;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

public class JSONParser {
	
	public static Hashtable readJSONObject (String str, int charIndex, Stack currentStack, Hashtable hb)
	{
		while (true)
		{
			StringBuilder sb= new StringBuilder();
			
			//skip over the first quotation mark
			charIndex=charIndex + 2;
			
			//read the key
			while (str.charAt(charIndex) != '\"')
			{
				sb.append(str.charAt(charIndex));
				charIndex++;
			}
			
			//store sb to key
			String key = sb.toString();
			
			//reset sb
			sb.setLength(0);
			
			//go to the character next to the colon
			charIndex=charIndex+2;
			
			if (str.charAt(charIndex)=='\"')
			{
				//skip over the quotation mark
				charIndex++;
				
				while (str.charAt(charIndex)!='\"')
				{
					sb.append(str.charAt(charIndex));
					charIndex++;
				}
				
				String value = sb.toString();
				sb.setLength(0);
				
				hb.put(key, value);
				key = null;
				value = null;
			}
			
			else if (str.charAt(charIndex)<='9' && str.charAt(charIndex)>='0')
			{
				while (str.charAt(charIndex)<='9' && str.charAt(charIndex)>='0')
				{
					sb.append(str.charAt(charIndex));
					charIndex++;
				}
				
				int value=Integer.parseInt(sb.toString());
				sb.setLength(0);
				hb.put(key, value);
				key=null;
				value=0;
			}
			
			else if (str.charAt(charIndex) == '{')
			{
				readJSONObject(str, charIndex, currentStack, hb);
			}
			
			else if (str.charAt(charIndex)== '[')
			{
				readJSONArray(str, charIndex, currentStack);
			}
			
			else if (str.charAt(charIndex)=='t' || str.charAt(charIndex)=='f')
			{
				while (str.charAt(charIndex)!='}' && str.charAt(charIndex)!=',')
				{
					sb.append(str.charAt(charIndex));
					charIndex++;
				}
				Boolean value= Boolean.valueOf(sb.toString());
				hb.put(key, value);
				key=null;
				value=null;
			}
			if (str.charAt(charIndex+1)==',')
			{
				charIndex=charIndex+2;
				readJSONObject(str, charIndex, currentStack, hb);
			}
			
			if (str.charAt(charIndex+1)=='}')
			{
				currentStack.pop();
				charIndex++;
			}
			
			if (currentStack.isEmpty())
			{
				return hb;
			}
		}//while true
	}
	
	
	public static ArrayList readJSONArray (String str, int charIndex, Stack currentStack)
	{
		return null;
	}
	
	public static Object JSONToObject (String str)
	{
		Stack<Character> currentStack = new Stack<Character>();
		
		if (str.charAt(0)=='{')
		{
			currentStack.push('{');
			Hashtable hb = new Hashtable();
			return (readJSONObject(str, 0, currentStack, hb));
		}
		
		else if (str.charAt(0)=='[')
		{
			return (readJSONArray(str, 0, currentStack));
		}
		
		else
		{
			return null;
		}
		
	}
	
	
	
	
	
	
	public static void main(String[] args) {
		String str1 = "{\"firstName\":\"John\",\"lastName\":\"Doe\" }";
		String result = JSONToObject(str1).toString();
		System.out.println(result);
		
		/*
		BufferedReader br = null;

		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader("JSON1.rtf"));

			
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
			} 

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}*/

	}//main
}
