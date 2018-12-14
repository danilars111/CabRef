package se.kau.cs.serg.cabrefbenchmark;

import java.awt.*;

public class BenchmarkGUI extends Frame{
	Button runTestBtn;
	
	public BenchmarkGUI(Benchmark benchmark) {
		setTitle("CabRef Benchmark");
		setLayout(new FlowLayout());
		runTestBtn = new Button("Run test");
		add(runTestBtn);
		runTestBtn.addActionListener(benchmark);
		setSize(250, 100);
		setVisible(true);
	}
}
