package stp;

public class RunSpanningTree {
	public static void main(String[] args) {
		//test case 1 sample_input.txt: passed
		/*Topology topo1 = new Topology("sample_input.txt");
		topo1.runSpanningTree();
		topo1.logSpanningTree("outputToCheck.txt");*/
		
		//test case 2 NoLoopTopo_input.txt: passed
		/*Topology topo2 = new Topology("NoLoopTopo_input.txt");
		topo2.runSpanningTree();
		topo2.logSpanningTree("outputToCheck.txt");*/
		
		//test case 3 SimpleLoopTopo_input.txt: passed
		/*Topology topo3 = new Topology("SimpleLoopTopo_input.txt");
		topo3.runSpanningTree();
		topo3.logSpanningTree("outputToCheck.txt");*/
		
		//test case 4 TailTopo_input.txt: failed
		Topology topo4 = new Topology("TailTopo_input.txt");
		topo4.runSpanningTree();
		topo4.logSpanningTree("outputToCheck.txt");
		
		//test case 5 ComplexLoopTopo_input.txt: 
		/*Topology topo5 = new Topology("ComplexLoopTopo_input.txt");
		topo5.runSpanningTree();
		topo5.logSpanningTree("outputToCheck.txt");*/
	}
}
