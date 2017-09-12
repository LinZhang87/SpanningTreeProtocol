package stp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;


public class Switch extends StpSwitch {
	protected HashMap<Integer, Boolean> stLinks;
	protected int claimedRoot;
	protected int distanceToClaimedRoot;
	protected int neighborOnItsPathToRoot;
	public Switch(int switchId, Topology topolink, ArrayList<Integer> neighbors) {
		super(switchId, topolink, neighbors);
		//Todo: define a data structure to keep track of which links are part of or 
		//not part of the spanning tree.
		
		this.claimedRoot = this.switchId;
		this.distanceToClaimedRoot = 0;
		this.neighborOnItsPathToRoot = this.switchId;
		
		//For each switch, assign a boolean value to each of its neighbors
		//true: this neighbor is on the shortest path of this switch to root
		//false: this neighbor is not on the shortest path of this switch to root
		this.stLinks = new HashMap<Integer, Boolean>();
		for(Integer i : this.neighbors) {
			this.stLinks.put(i, false);
		}
	}

	//This method creates and sends an initial message to all its immediate neighboring switches
	public void sendInitialMessages() {
		for(Integer neighbor : this.neighbors) {
			this.sendMessage(new Message(this.claimedRoot, 0, this.switchId, neighbor,false));			
		}
	}
	
	public void processMessage(Message msg) {
		//debug 
		if(this.switchId == 8 && msg.originId == 4) {
			System.out.println("debugging");				
		}
		//switch's claimed root is bigger than msg's root, needs to update
		if(msg.root < this.claimedRoot) {
			//update its view of root
			this.claimedRoot = msg.root;
			this.distanceToClaimedRoot = msg.distanceToRoot + 1;
			
			this.stLinks.remove(this.neighborOnItsPathToRoot);
			this.neighborOnItsPathToRoot = msg.originId;
			
			//set the new neighbor on its shortest path to the claimed root
			this.stLinks.put(msg.originId, true);
			
			//broadcast view of root update to its neighbors
			for(Integer neighbor : this.neighbors) {
				boolean onPathToRoot = this.stLinks.containsKey(neighbor) ? this.stLinks.get(neighbor) : false;
				Message newMsg = new Message(this.claimedRoot, this.distanceToClaimedRoot, 
										  this.switchId, neighbor, onPathToRoot);
				this.sendMessage(newMsg);
			}
		}
		else if(msg.root == this.claimedRoot) {
			//the current switch is on the shortest path of switch msg.destinationId to root
			//create a link from the current switch to switch msg.destinationId if it does not exist yet
			if(msg.pathThrough) {
				this.stLinks.put(msg.originId, false);
			}
			//the current switch is not on the shortest path of switch msg.destinationId to root
			//switch msg.destinationId's shortest path to its claimed root does not pass through the current switch
			else {
				//this.distanceToClaimedRoot > msg.distanceToRoot + 1
				if(this.distanceToClaimedRoot > msg.distanceToRoot + 1) {
					this.distanceToClaimedRoot = msg.distanceToRoot + 1;
					
					//update neighbor that is on its shortest path to claimed root
					this.stLinks.remove(neighborOnItsPathToRoot);
					this.stLinks.put(msg.originId, true);
					int prevNeighborOnitsPathToRoot = this.neighborOnItsPathToRoot;
					this.neighborOnItsPathToRoot = msg.originId;		
										
					//if neighborOnItsPathToRoot changes, this switch needs to notify its old and new neighbors on path					
					Message msgToOld = new Message(this.claimedRoot, this.distanceToClaimedRoot, 
												  this.switchId, prevNeighborOnitsPathToRoot, false);
					this.sendMessage(msgToOld);
					
					Message msgToNew = new Message(this.claimedRoot, this.distanceToClaimedRoot, 
							  this.switchId, this.neighborOnItsPathToRoot, true);
					this.sendMessage(msgToNew);					
				}			
				//this.distanceToClaimedRoot = msg.distanceToRoot + 1, 
				//there is a tie, choose the neighbor of smaller id
				else if(this.distanceToClaimedRoot == msg.distanceToRoot + 1) {
					//current shortest path to root passes a switch of bigger id
					if(this.neighborOnItsPathToRoot > msg.originId) {
						this.stLinks.remove(neighborOnItsPathToRoot);
						this.stLinks.put(msg.originId, true);		
						int prevNeighborOnitsPathToRoot = this.neighborOnItsPathToRoot;
						this.neighborOnItsPathToRoot = msg.originId;
						
						//if neighborOnItsPathToRoot changes, this switch needs to notify its old and new neighbors on path					
						Message msgToOld = new Message(this.claimedRoot, this.distanceToClaimedRoot, 
													  this.switchId, prevNeighborOnitsPathToRoot, false);
						this.sendMessage(msgToOld);
						
						Message msgToNew = new Message(this.claimedRoot, this.distanceToClaimedRoot, 
								  this.switchId, this.neighborOnItsPathToRoot, true);
						this.sendMessage(msgToNew);
					}
					//current shortest path to root passes a switch of smaller id
					else {
						this.stLinks.remove(msg.originId);
					}
				}
				//this.distanceToClaimedRoot < msg.distanceToRoot + 1
				else {
					this.stLinks.remove(msg.originId);
				}				
			}
		}
	}
	
	public String generateLogString() {
		ArrayList<Integer> sorted = new ArrayList<Integer>();
		Iterator<Integer> iter = this.stLinks.keySet().iterator();
		while(iter.hasNext()) {
			sorted.add(iter.next());
		}
		Collections.sort(sorted);
		StringBuilder sb = new StringBuilder();
		int idx = 0;
		while(idx < sorted.size() - 1) {			
			sb.append(this.switchId + " - " + sorted.get(idx) + ", ");
			idx++;
		}
		sb.append(this.switchId + " - " + sorted.get(idx));
		return sb.toString();
	}
}
