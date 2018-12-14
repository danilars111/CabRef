package se.kau.cs.serg.cabrefbenchmark;

import java.awt.*;

public class BenchmarkGUI extends Frame{
	Button test;
	
	public BenchmarkGUI() {
		setTitle("CabRef Benchmark");
		setLayout(new FlowLayout());
		/*TODO: Do this for every button */
		test = new Button("Test");
		add(test);
		//TODO: also, when functionality is in place: test.addActionListener(theListener);
		setSize(250, 100);
		setVisible(true);
	}
}
