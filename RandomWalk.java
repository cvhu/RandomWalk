// CS394R Assignment 2
// Example 6.2, Sutton & Barto

import java.util.*;
import java.io.*;

class RWProcess{
	int n;
	LinkedList<Character> states;
	char curr;
	int r;
	int ret;
	public RWProcess(int n){
		int base = (int) 'A';
		states = new LinkedList<Character>();
		states.add('L');
		for(int i = 0; i < n; i++){
			char node = (char) (base+i);			
			states.add(node);
		}
		states.add('R');
	}
	
	public void start(){
		curr = 'C';
		r = 0;
		ret = 0;
	}
	
	public void walk(){
		char old = curr;
		Random rand = new Random();
		if(rand.nextBoolean()){
			transition(curr, 1);
			reward(old, 1, curr);
		}else{
			transition(curr, -1);
			reward(old, -1, curr);
		}
	}
	
	public void transition(char state, int action){
		curr = states.get(states.indexOf(state)+action);
	}
	
	public void reward(char state, int action, char next){
		if(state==states.get(states.size()-2)&&next=='R'){
			r = 1;
		}else{
			r = 0;
		}
		ret += r;
	}
	
	public char getState(){
		return curr;
	}
	
	public int getReward(){
		return r;
	}
	
	public int getReturn(){
		return ret;
	}
	
	public boolean terminate(){
		return (curr=='L' || curr=='R');
	}
	
	public String toString(){
		return states.toString();
	}
}

public class RandomWalk{
	public static void runEpisode(){
		RWProcess rwp = new RWProcess(5);
		rwp.start();
		System.out.printf("%c", rwp.getState());
		while(!rwp.terminate()){
			rwp.walk();
			System.out.printf("-%d-%c", rwp.getReward(), rwp.getState());
		}
		System.out.printf(": %d\n", rwp.getReturn());
	}
		
	public static void main(String[] args){
		for(int i = 0; i < 10; i++){
			runEpisode();
		}
	}
}