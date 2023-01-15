import java.io.*;
import java.util.*;
import java.lang.Integer;
class Dscheduler {
		public static void main(String [] args) {
			File file = new File("Dinput.txt");
			try {
				Scanner inFile = new Scanner(file);	//reads in from Dinput.txt
				String line = inFile.nextLine();
				String[] queue = line.split(", ");		//queue
				Node[] reqs = new Node[queue.length];
				for (int i = 0; i < queue.length; i++)
				{
					reqs[i] = new Node();	//make a new node for each request
					reqs[i].loc = Integer.parseInt(queue[i]);	//add its loc
				}
				line = inFile.nextLine();
				int headpoint = Integer.parseInt(line);		//head pointer
				line = inFile.nextLine();
				String[] minmax = line.split(",");
				int diskmin = Integer.parseInt(minmax[0]);	//diskmin
				int diskmax = Integer.parseInt(minmax[1]);	//diskmax
				FCFS(queue, headpoint);
				SSTF(queue, headpoint, reqs);
				SCAN(queue, headpoint, diskmin, diskmax);
				CSCAN(queue, headpoint, diskmin, diskmax);
				CLOOK(queue, headpoint);
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		static void FCFS(String[] queue, int headpoint){
			//services in order they arrive
			try {
				FileWriter wr = new FileWriter("FCFSoutput.txt");	//open output file
				wr.write(headpoint + ",");	//write starting position to file
				for(int i = 0; i < queue.length - 1; i++)
				{
					wr.write(queue[i] + ",");
				}
				wr.write(queue[queue.length - 1]);	//last request (no comma)
				wr.close();
			}
			catch (Exception e) {
				e.getStackTrace();
			}
		}
		static void SSTF(String[] queue, int headpoint, Node[] reqs){
			//services request w/ shortest seek time from current head position
			try {
				FileWriter wr = new FileWriter("SSTFoutput.txt");	//open output file
				wr.write(headpoint + ",");	//write starting position to file
				Node nextreqs = null;	//to save position of shortest
				for(int i = 0; i < queue.length; i++)
				{
					int shortestseek = Integer.MAX_VALUE;
					for(int j = 0; j < queue.length; j++)	//finding req with shortest seek time
					{
						int headdist = Math.abs(reqs[j].loc - headpoint);
						if((headdist < shortestseek) && (reqs[j].serviced == false))
						{
							shortestseek = headdist;
							nextreqs = reqs[j];
						}
					}
					if(i < queue.length - 1)
					{
						wr.write(String.valueOf(nextreqs.loc) + ",");
					}
					else
					{
						wr.write(String.valueOf(nextreqs.loc));
					}
					nextreqs.serviced = true;
					headpoint = nextreqs.loc; //make used request new headpoint
				}
				wr.close();
			}
			catch (Exception e) {
				e.getStackTrace();
			}
		}
		static void SCAN(String[] queue, int headpoint, int diskmin, int diskmax){
			//disk arm starts at one end of disk and moves to other side while servicing requests on the way
			//when it gets to the other end, it reverses and repeats
			try {
				FileWriter wr = new FileWriter("SCANoutput.txt");	//open output file
				wr.write(headpoint + ",");	//write starting position to file
				int[] orderedqueue = new int[queue.length + 1];
				for(int i = 0; i < queue.length; i++)
				{
					orderedqueue[i] = Integer.valueOf(queue[i]);
				}
				orderedqueue[orderedqueue.length - 1] = headpoint;	//adds headpoint into array
				Arrays.sort(orderedqueue);		//sorts in ascending order
				//14,37,53,65,67,98,122,124,183 is orderedqueue
				//53,37,14,0,65,67,98,122,124,183,199 output for SCAN
				
				int headindex = 0;
				for(int i = 0; i < orderedqueue.length; i++)	//finds index of headpoint
				{
					if(orderedqueue[i] == headpoint)
					{
						headindex = i;
						break;
					}
				}
				int count = 0;
				for(int i = headindex - 1; i >= 0; i--)	//writes values before headpoint
				{
					if(count < orderedqueue.length - 1)
					{
						wr.write(String.valueOf(orderedqueue[i] + ","));
						count += 1;
					}
					else
					{
						wr.write(String.valueOf(orderedqueue[i]));
						count += 1;
					}
				}
				wr.write(diskmin + ",");	//write diskmin to file
				for(int i = headindex + 1; i < orderedqueue.length; i++)	//writes values after headpoint
				{
					if(count < orderedqueue.length - 1)
					{
						wr.write(String.valueOf(orderedqueue[i] + ","));
						count += 1;
					}
					else
					{
						wr.write(String.valueOf(orderedqueue[i]));
						count += 1;
					}
				}
				wr.write(String.valueOf(diskmax));		//write diskmax to file
				wr.close();
				}
			catch (Exception e) {
				System.out.println(e.toString());
			}
		}
					
		static void CSCAN(String[] queue, int headpoint, int diskmin, int diskmax){
			//disk arm starts at head and moves to the 0 while servicing requests on the way
			//when it gets to 0, it jumps to 199 and repeats
			try {
				FileWriter wr = new FileWriter("CSCANoutput.txt");	//open output file
				wr.write(headpoint + ",");	//write starting position to file
				int[] orderedqueue = new int[queue.length + 1];
				for(int i = 0; i < queue.length; i++)
				{
					orderedqueue[i] = Integer.valueOf(queue[i]);
				}
				orderedqueue[orderedqueue.length - 1] = headpoint;	//adds headpoint into array
				Arrays.sort(orderedqueue);		//sorts in ascending order
				
				int headindex = 0;
				for(int i = 0; i < orderedqueue.length; i++)	//finds index of headpoint
				{
					if(orderedqueue[i] == headpoint)
					{
						headindex = i;
						break;
					}
				}
				int count = 1;
				for(int i = headindex - 1; i >= 0; i--)	//writes values before headpoint
				{
					if(count < orderedqueue.length - 1)
					{
						wr.write(String.valueOf(orderedqueue[i] + ","));
						count += 1;
					}
					else
					{
						wr.write(String.valueOf(orderedqueue[i]));
						count += 1;
					}
				}
				wr.write(diskmin + ",");	//write diskmin to file
				wr.write(String.valueOf(diskmax) + ",");	//write diskmax to file
				for(int i = orderedqueue.length - 1; i > headindex; i--)	//writes values after headpoint, starting at 199
				{
					if(count < orderedqueue.length - 1)
					{
						wr.write(String.valueOf(orderedqueue[i] + ","));
						count += 1;
					}
					else
					{
						wr.write(String.valueOf(orderedqueue[i]));
						count += 1;
					}
				}
				wr.close();
				}
			catch (Exception e) {
				System.out.println(e.toString());
			}
		}

		static void CLOOK(String[] queue, int headpoint){
			//disk arm starts at one end of disk and moves as far as the last request in one direction
			//then it goes back to the start immediately and repeats
			try {
				FileWriter wr = new FileWriter("CLOOKoutput.txt");	//open output file
				wr.write(headpoint + ",");	//write starting position to file
				int[] orderedqueue = new int[queue.length + 1];
				for(int i = 0; i < queue.length; i++)
				{
					orderedqueue[i] = Integer.valueOf(queue[i]);
				}
				orderedqueue[orderedqueue.length - 1] = headpoint;	//adds headpoint into array
				Arrays.sort(orderedqueue);		//sorts in ascending order
				
				int headindex = 0;
				for(int i = 0; i < orderedqueue.length; i++)	//finds index of headpoint
				{
					if(orderedqueue[i] == headpoint)
					{
						headindex = i;
						break;
					}
				}
				int count = 1;
				for(int i = headindex - 1; i >= 0; i--)	//writes values before headpoint
				{
					if(count < orderedqueue.length - 1)
					{
						wr.write(String.valueOf(orderedqueue[i] + ","));
						count += 1;
					}
					else
					{
						wr.write(String.valueOf(orderedqueue[i]));
						count += 1;
					}
				}
				for(int i = orderedqueue.length - 1; i > headindex; i--)	//writes values after headpoint, starting at 199
				{
					if(count < orderedqueue.length - 1)
					{
						wr.write(String.valueOf(orderedqueue[i] + ","));
						count += 1;
					}
					else
					{
						wr.write(String.valueOf(orderedqueue[i]));
						count += 1;
					}
				}
				wr.close();
				}
			catch (Exception e) {
				System.out.println(e.toString());
			}
		}

	static class Node implements Comparable<Node>{	//node class to tie request to its seek time
				int loc;	//distance from head pointer
				boolean serviced = false;	//if that request has been serviced yet
				@Override
				public int compareTo(Node o1)
				{
					return loc - o1.loc; //current loc - o1.loc
				}
				@Override
				public String toString()
				{
					return String.valueOf(loc);
				}
	}
}