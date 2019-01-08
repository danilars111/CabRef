package se.kau.cs.serg.cabrefbenchmark;

 
public class BenchmarkStartup 
{
    public static void main(String[] args)
    {
    	Benchmark benchmark = new Benchmark();
        BenchmarkGUI gui = new BenchmarkGUI(benchmark);
    }
}
