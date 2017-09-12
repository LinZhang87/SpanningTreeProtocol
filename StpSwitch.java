/*This class defines a Spanning Tree Switch that servers as a parent class to the Switch class.
 * It abstracts details of sending messages and verifying topologies.
 */

package stp;

import java.util.ArrayList;

public class StpSwitch {
	protected int  switchId;
	protected Topology topolink;
	protected ArrayList<Integer> neighbors;
	
	public StpSwitch(int switchId, Topology topolink, ArrayList<Integer> neighbors) {
		this.switchId = switchId;
		this.topolink = topolink;
		this.neighbors = neighbors;
	}
	
	//Invoked at initialization of topology of switches
	//Verify that all your neighbors has a backlink to you
	public void verifyNeighbors() throws Exception {
		for(Integer neighbor : neighbors) {
			Switch sw = this.topolink.getSwitch(neighbor);
			if(sw == null) {
				throw new Exception("Topology does not have switch of id " + neighbor);
			}
			if(!sw.hasNeighbor(this.switchId)) {
				throw new Exception(neighbor + " does not have link to " + this.switchId);
			}
		}
	}
	
	public boolean hasNeighbor(int id) {
		return this.neighbors.contains(id);
	}
	
	public void sendMessage(Message msg) {
		this.topolink.sendMessage(msg);
	}
}
