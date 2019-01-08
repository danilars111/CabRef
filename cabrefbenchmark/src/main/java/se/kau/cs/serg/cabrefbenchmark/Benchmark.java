package se.kau.cs.serg.cabrefbenchmark;

import java.awt.event.*;
import java.util.*;

import com.mashape.unirest.http.*;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.*;

public class Benchmark implements ActionListener {
	String baseURL = "http://localhost:4567";
	String token = "GgetQeWVAx";
	String indexResult;
	BenchmarkGUI gui;
	
	
	public Benchmark() {
	}
	
	public void setGUI(BenchmarkGUI gui) {
		this.gui = gui;
	}
	

	public void actionPerformed(ActionEvent e) {
		runTest();
		gui.status.setText("Tests ended!");
	}
	
	private void runTest() {
		long time;
		Map<String, String> headers = new HashMap<String, String>();
	    headers.put("accept", "application/json");
	    headers.put("Authorization", "Bearer " + token);
	   
		time = System.currentTimeMillis();
		sendBulkRequests("/cabref", headers, "get");
		time = System.currentTimeMillis() - time;
		gui.indexResult.setText(Long.toString(time/10) + " ms");
		
		time = System.currentTimeMillis();
		sendBulkRequests("/cabref/0", headers, "get");
		time = System.currentTimeMillis() - time;
		gui.entryPageResult.setText(Long.toString(time/10) + "ms");
		
		time = System.currentTimeMillis();
		sendBulkRequests("/cabref/addNew?key=12341234", headers, "post");
		time = System.currentTimeMillis() - time;
		gui.addEntryResult.setText(Long.toString(time/10) + "ms");
		
		time = System.currentTimeMillis();
		sendBulkRequests("/cabref/doDelete/12341234", headers, "get");
		time = System.currentTimeMillis() - time;
		gui.delEntryResult.setText(Long.toString(time/10) + "ms");
		
		time = System.currentTimeMillis();
		sendBulkRequests("/cabref/importFromDiVa?id=diva2:372585", headers, "post");
		time = System.currentTimeMillis() - time;
		gui.importEntryResult.setText(Long.toString(time/10) + "ms");
		
		time = System.currentTimeMillis();
		sendBulkRequests("/cabref/export?exportFormat=Bibtex", headers, "post");
		time = System.currentTimeMillis() - time;
		gui.exportEntriesResult.setText(Long.toString(time/10) + "ms");
	}
	
	private void sendBulkRequests(String urlString, Map<String, String> headers, String method) {
		for(int i = 0; i < 10; i++) {
			try {
				if(method.equals("get")) {
					Unirest.get(baseURL + urlString).headers(headers).asString();
				} else if(method.equals("post")) {
					Unirest.post(baseURL + urlString).headers(headers).asString();
				}
				
			} catch (UnirestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

}
