package se.kau.cs.serg.cabrefbenchmark;

import java.awt.event.*;
import java.util.*;

import com.mashape.unirest.http.*;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.*;

public class Benchmark implements ActionListener {
	String apiURL = "http://localhost:4567/api";
	
	
	public Benchmark() {
	}
	

	public void actionPerformed(ActionEvent e) {
		runTest();
	}
	
	private void runTest() {
		Map<String, String> headers = new HashMap<String, String>();
	    headers.put("accept", "application/json");
	    headers.put("Authorization", "Bearer 1234");
	    
		try {
			HttpResponse<String> response = Unirest.get(apiURL + "/entry/test").headers(headers).asString();
			System.out.println(response.getBody());
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}

}
