package creatingToken;

/*          References to api's used
 * http://www.json.org/javadoc/org/json/JSONObject.html
 * http://joda-time.sourceforge.net
 * /apidocs/org/joda/time/format/ISODateTimeFormat.html
 * http://docs.oracle.com/javase/7/docs/api/java/lang/String.html
 * http://docs.oracle.com/javase/7/docs/api/java/net/URLConnection.html
 */
import org.joda.time.*;
import java.net.*;
import java.io.*;
import java.util.Date;
import org.json.*;




/* This class is used for the CODE2040 coding challenge for those
 * applying to the fellows program. Below is my code for each problem
 * that are each seperated with comments specifiying what problem that
 * section of code is for. 
 * 
 * Author: Armando Alvarado
 * Date: 01/07/2015
 */
public class armandoToken {


	public static void main(String args[]) throws Exception{
		JSONObject send;
		JSONObject recieve;
		BufferedReader dataRecieved;
		String token;
		
		send = new JSONObject();
		//uses hashmap as a subclass
		send.put("email", "aialvara@ucsd.edu");
		send.put("github", "https://github.com/armando7818/CODE2040.git");
		
		//used to send and recieve data from server
		dataRecieved = sendingRequest(send.toString(),
		"http://challenge.code2040.org/api/register");
		
		//output that is returned aka my token
		String result = dataRecieved.readLine(); 
		recieve = new JSONObject(result);
		dataRecieved.close();
		token = recieve.getString("result");
		
		
		
		
		//                     Problem 1: String reversal
		//My method was faster than using a string builder to reverse a string
		//This is because string builder was mainly designed to be used to 
		//simplify code, or to be more effiecnt when concatenating. 
	
		
		send = new JSONObject(); 
		send.put("token", token); 
		//send request and returns values requested
		dataRecieved = sendingRequest(send.toString(), "http://challenge.code2040.org/api/getstring"); 
		result = dataRecieved.readLine(); 
		//stores string into Json object in order to keep in Json format
		recieve = new JSONObject(result);
		dataRecieved.close();
	
		
	    //sending info back in json form
		send = new JSONObject(); 
		send.put("token", token);
		//reverses the string given to us. 
		send.put("string", reverseString(recieve.getString("result"))); 
		//sending back answer now in json form
		dataRecieved = sendingRequest(send.toString(), "http://challenge.code2040.org/api/validatestring");
		//returned values
		result = dataRecieved.readLine(); 
		recieve = new JSONObject(result);
		dataRecieved.close();
		
		
		
	
		//				problem 2: needle haystack problem
		
		//request dictionary using token
		String needle; 
		JSONArray hayStackArray; 
		JSONObject resultObject; 
		
		//recieve data that needs to be modified from server
		send = new JSONObject(); 
		send.put("token", token); 
		dataRecieved = sendingRequest(send.toString(), "http://challenge.code2040.org/api/haystack"); 
		result = dataRecieved.readLine(); 
		//stores string into Json object in order to keep in Json format
		recieve = new JSONObject(result);
		
		//we get needle and haystack
	    resultObject = recieve.getJSONObject("result"); 
	    hayStackArray = resultObject.getJSONArray("haystack");
	    needle = resultObject.getString("needle");

	    //loops to find needle in array
	    int i =0;
	    for(; i<hayStackArray.length(); i++){
	    	//you should not use the equals method to check for null, so
	    	//this is a must check. 
	    	if(!hayStackArray.isNull(i)){
	    		//returns index of where it is located
	    		if(needle.equals(hayStackArray.getString(i))){
	    			break; 
	    		}
	    		}
	    }
	    
	    //returns index of where the needle was found
	    //and send it as requested back to server in json format
	    send = new JSONObject();
	    send.put("token", token);
	    send.put("needle", i);
	    dataRecieved = sendingRequest(send.toString(), 
	    		"http://challenge.code2040.org/api/validateneedle"); 
		dataRecieved.close();
		
		
		
		//            		Problem 3: PREFIX     
		String prefix; 
		JSONArray arrayPrefixCheck; 
		
		//request information from there server
		send = new JSONObject(); 
		send.put("token", token); 
		dataRecieved = sendingRequest(send.toString(), 
				"http://challenge.code2040.org/api/prefix"); 
		result = dataRecieved.readLine(); 
		
		//stores string into Json object in order to keep in Json format
		recieve = new JSONObject(result);
		resultObject = recieve.getJSONObject("result"); 
		arrayPrefixCheck = resultObject.getJSONArray("array");
		prefix = resultObject.getString("prefix");
		//prepares answer to be returned to server
		send = new JSONObject(); 
		send.put("token", token);
		
		//checks array composed of strings and makes sure 
		//it returns an array that does not contain the prefix
		JSONArray returnArray = new JSONArray(); 
		for( i =0; i< arrayPrefixCheck.length(); i++){
			if(!arrayPrefixCheck.isNull(i)){
				if(!arrayPrefixCheck.getString(i).startsWith(prefix)){
					//appends the string 
					returnArray.put(arrayPrefixCheck.getString(i));
				}
			}
		}

		//send answer to server
		send.put("array", returnArray);
		dataRecieved = sendingRequest(send.toString(), 
				"http://challenge.code2040.org/api/validateprefix");
		//returned value
		result = dataRecieved.readLine(); 
		recieve = new JSONObject(result);
		dataRecieved.close();
		
		
		
		
		
		//				Problem 4: The Dating Game
		//I used joda date and time package because it directly deals
		//with the iso-8601 format. Orginally, I was going to use java's
		//date and time package because they now support this format, but
		//it would have been messier to read and would have took more code.
		//to accomplish the same task. 
		String dateStamp; 
		int interval; 
		DateTime datePlusSeconds; 
		
		//request information
		send = new JSONObject(); 
		send.put("token", token);
		dataRecieved = sendingRequest(send.toString(), 
				"http://challenge.code2040.org/api/time");
		result = dataRecieved.readLine();
		//System.out.println(result); 
		recieve = new JSONObject(result);
		resultObject = recieve.getJSONObject("result"); 
		
		//adds ISO8601 date Plus seconds using joda package
        dateStamp = resultObject.getString("datestamp");
        interval = resultObject.getInt("interval");
        datePlusSeconds = DateTime.parse(dateStamp);
        datePlusSeconds = datePlusSeconds.plusSeconds(interval); 
        
        //sending answer
        send = new JSONObject(); 
        send.put("token", token);
        send.put("datestamp", datePlusSeconds.toString());
        dataRecieved = sendingRequest(send.toString(), 
				"http://challenge.code2040.org/api/validatetime");
		//returned values
		result = dataRecieved.readLine();
		recieve = new JSONObject(result);
		dataRecieved.close();
        
        
		
		
        //      Receiving Status of my work. 
        send = new JSONObject(); 
        send.put("token", token);
        dataRecieved = sendingRequest(send.toString(), 
				"http://challenge.code2040.org/api/status");
        result = dataRecieved.readLine();
		System.out.println(result);
		dataRecieved.close(); 
		
	}
	
	
	
	/*
	 * Reverses a string using a char array because you cannot do it in place
	 * since strings in java are immutable. 
	 */
	public static String reverseString(String s){
		char[] stringArray = s.toCharArray(); 
		char holder; 
		//start from middle and work your way out in string rever
		for(int i = (s.length()/2); i >=0; i-- ){
			//starts swapping from middle and works outward
			holder = stringArray[i]; 
			stringArray[i] = stringArray[s.length()-1-i]; 
			stringArray[s.length()-1-i] = holder; 
		}
		
		return String.valueOf(stringArray); 
	}
		
	
	/**
	 * used to open connection between the server to exchange answers
	 * and data
	 */
	public static BufferedReader sendingRequest(String msg, String link)
			throws Exception {
		
				URLConnection connection = new URL(link).openConnection();
				//allows input / output
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.connect();
				
				OutputStreamWriter outputWrite = new OutputStreamWriter(connection.getOutputStream());
				//writes to output stream
				outputWrite.write(msg);
				//flushes output stream and forces what was in buffer
				//to be written
				outputWrite.flush();
				outputWrite.close();
				
				return new BufferedReader(new InputStreamReader(connection.getInputStream()));
			}
	
	}
	
