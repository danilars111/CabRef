package se.kau.cs.serg.cabrefbenchmark;

import java.awt.event.*;
import java.util.*;

import com.mashape.unirest.http.*;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.*;

public class Benchmark implements ActionListener {
	String baseURL = "http://localhost:4567";
	String token = "GgetQeWVAx";
	
	
	public Benchmark() {
	}
	

	public void actionPerformed(ActionEvent e) {
		runTest();
	}
	
	private void runTest() {
		Map<String, String> headers = new HashMap<String, String>();
	    headers.put("accept", "application/json");
	    headers.put("Authorization", "Bearer " + token);
	    
		try {
			HttpResponse<String> response = Unirest.get(baseURL + "/cabref").headers(headers).asString();
			System.out.println(response.getBody());
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}

}
