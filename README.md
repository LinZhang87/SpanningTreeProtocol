# SpanningTreeProtocol
Simplified spanning tree protocol simulation

Spanning tree can be used to prevent forwarding loops on a layer 2 network. 
In this project, you will develop a simplified distributed version of the spanning tree protocol 
that can be run on an arbitrary layer 2 topology.

The following files are included.

Topology.java - Represents a network topology of layer 2 switches. This class reads in a specified topology and arranges it into a 
                data structure that the switch code can access.
                
StpSwitch.java  - A superclass of class Switch. It abstracts certain implementation details to simplify the tasks.

Message.java - This class represents a simple message format that switches will use to communicate among them.

RunSpanningTree.java  - The main file that loads a topology file, uses that data to create a Topology object containing switches, 
                        and starts the simulation.
                        
XXXTopo.txt - topology files that will be passed as input to RunSpanningTree.java file.

sample_output.txt - Example of a valid output file for the example input file sample_input.txt

ValidateAnswer.java - It compares a reference output file against the generated output file to ensure correctness.

