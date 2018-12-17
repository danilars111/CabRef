package se.kau.cs.serg.cabrefbenchmark;

import java.awt.*;

public class BenchmarkGUI extends Frame{
	Button runTestBtn;
	Label status = new Label();
	Label indexResult = new Label();
	Label entryPageResult = new Label();
	Label addEntryResult = new Label();
	Label delEntryResult = new Label();
	Label importEntryResult = new Label();
	Label exportEntriesResult = new Label();
	
	public BenchmarkGUI(Benchmark benchmark) {
		benchmark.setGUI(this);
		setTitle("CabRef Benchmark");
		setLayout(new GridLayout(3,6));
		runTestBtn = new Button("Run test");
		add(runTestBtn);
		add(status);
		add(new Label());
		add(new Label());
		add(new Label());
		add(new Label());
		add(new Label());
		add(new Label("Action:"));
		add(new Label("Get index page"));
		add(new Label("Get entry page"));
		add(new Label("Add new entry"));
		add(new Label("Delete entry"));
		add(new Label("Import entry"));
		add(new Label("Export entries"));
		add(new Label("Average time:"));
		add(indexResult);
		add(entryPageResult);
		add(addEntryResult);
		add(delEntryResult);
		add(importEntryResult);
		add(exportEntriesResult);

		runTestBtn.addActionListener(benchmark);
		setSize(250, 100);
		setVisible(true);	
	}
}
