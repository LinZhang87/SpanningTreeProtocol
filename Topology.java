/*Defines a Topology, which is a collection of switches and links between them */

package stp;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class Topology {
	private HashMap<Integer, Switch> switches;
	private Queue<Message> messages;
	
	public Topology(String topoInputFile) {
		this.switches = new HashMap<Integer, Switch>();
		this.messages = new LinkedList<Message>();
		parseInputTopoFile(topoInputFile);
		try {
			//verify the topology read from file is correct.
			for(Integer switchId : this.switches.keySet()) {
				this.switches.get(switchId).verifyNeighbors();
			}			
		}
		catch (Exception e) {
			System.err.println(topoInputFile + " does not provide a correct topology");
		}
	}
	
	private void parseInputTopoFile(String topoInputFile) {
		FileInputStream fstream;
		DataInputStream in;
		BufferedReader br;
		boolean isValidFile = true;
		try {
			fstream = new FileInputStream(topoInputFile);
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			String line; String[] splits;
			int switchId;
			while((line = br.readLine()) != null) {
				splits = line.split("\\s+", 0);
				if(splits.length != 2) {
					System.err.println("Error: Invalid input file");
					isValidFile = false;
					break;					
				}
				switchId = Integer.parseInt(splits[0]);
				splits = splits[1].split(",", 0);
				ArrayList<Integer> neighbors = new ArrayList<Integer>();
				for(String s : splits) {
					neighbors.add(Integer.parseInt(s));
				}
				Switch newSwitch = new Switch(switchId, this, neighbors);
				this.switches.put(switchId, newSwitch);
			}
			if(!isValidFile) {
				this.switches.clear();
			}
			fstream.close();				
			in.close();
			br.close();	
		} catch (Exception e) {
			System.err.println("error parsing topology input file: " + topoInputFile);
		}
	}
	public Switch getSwitch(int switchId) {
		return this.switches.get(switchId);
	}
	
	public void sendMessage(Message msg) {
		Switch origin = this.switches.get(msg.originId);
		if(origin == null) {
			System.err.println("topology does not have switch of id " + msg.originId);
			return;
		}
		if(origin.neighbors.contains(msg.destinationId)) {
			this.messages.add(msg);
		}
		else {
			System.err.println("Messages can only be sent to immediate neighbors");
		}
	}
	
	/*
	 * This function drives the simulation of a Spanning Tree. It first sends the initial
	 * messages from each switch by invoking send_inital_message. Afterward, each message
	 * is delivered to the destination switch, where processMessage is invoked.
	 */
	public void runSpanningTree() {
		for(Integer switchId : this.switches.keySet()) {
			this.switches.get(switchId).sendInitialMessages();
		}
		while(!this.messages.isEmpty()) {
			Message msg = this.messages.poll();
			this.switches.get(msg.destinationId).processMessage(msg);
		}
	}
	
	/*
	 * This function drives the logging of the text file representing the spanning tree.
	 * It is invoked at the end of the simulation, and iterates through the switches in 
	 * increasing order of Id and invokes the generateLogString function. That string 
	 * is written to the provided file outputFile.
	 */
	public void logSpanningTree(String outputFile) {
		try {
			PrintStream out = new PrintStream(new FileOutputStream(outputFile));
			Iterator<Integer> switchIds = this.switches.keySet().iterator();
			ArrayList<Integer> sortedIds = new ArrayList<Integer>();
			while(switchIds.hasNext()) {
				sortedIds.add(switchIds.next());
			}
			Collections.sort(sortedIds);
			for(Integer id : sortedIds) {
				String entry = this.switches.get(id).generateLogString() + "\n";
				out.print(entry);
				out.flush();
			}
			out.close();
		}
		catch (Exception e) {
			System.err.println("Log spanning tree error: " + e.getMessage());
		}
	}
}
