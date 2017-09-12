/* This class defines a Message sent from one node to another using Spanning Tree Protocol */

package stp;

public class Message {
	protected int root;
	protected int distanceToRoot;
	protected int originId;
	protected int destinationId;
	protected boolean pathThrough;
	
	public Message(int root, int distanceToRoot, int originId, int destinationId, boolean pathThrough) {
		this.root = root;
		this.distanceToRoot = distanceToRoot;
		this.originId = originId;
		this.destinationId = destinationId;
		//boolean value indicating the path to the claimed root from the origin passes 
		//through the destination
		this.pathThrough = pathThrough;
	}
}
