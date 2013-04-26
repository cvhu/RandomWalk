// CS394R Assignment 2
// Example 6.2, Sutton & Barto

import java.util.*;
import java.io.*;

class RWProcess{
	int n;
	LinkedList<Character> states;
	char curr;
	double r;
	double ret;
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
		r = 0.0;
		ret = 0.0;
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
		if(next=='R'){
			r = 1.0;
		}else{
			r = 0.0;
		}
		ret += r;
	}
	
	public char getState(){
		return curr;
	}

	public LinkedList<Character> getStates(){
		return states;
	}
	
	public double getReward(){
		return r;
	}
	
	public double getReturn(){
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
	
	public static double CAMC(double alpha, int n){
		// System.out.println("constant-alpha Monte Carlo start...");
		RWProcess rwp = new RWProcess(5);		
		HashMap<Character, Double> v = new HashMap<Character, Double>();
		for(Character state : rwp.getStates()){
			v.put(state, 0.0);
		}		

		for(int i = 0; i < n; i++){
			rwp.start();
			// System.out.printf("%d ", i);
			LinkedList<Character> states = new LinkedList<Character>();
			double ret = 0.0;
			while(!rwp.terminate()){
				rwp.walk();
				states.add(rwp.getState());
				ret += rwp.getReward();			
			}		
			for(Character state : states){
				double v_t = v.get(state);
				v.put(state, v_t*(1-alpha) + alpha*ret);
			}		
		}
		return evaluateOnFive(v);
		
		// for(Character state : v.keySet()){
		// 	System.out.printf("%c: %f\n", state, v.get(state));
		// }
	}
	
	public static double TD(double gamma, double alpha, int n){
		// System.out.println("TD start...");
		RWProcess rwp = new RWProcess(5);		
		HashMap<Character, Double> v = new HashMap<Character, Double>();
		for(Character state : rwp.getStates()){
			v.put(state, 0.5);
		}		
		v.put('L', 0.0);
		v.put('R', 0.0);		

		for(int i = 0; i < n; i++){
			rwp.start();
			// System.out.printf("%d ", i);			
			while(!rwp.terminate()){				
				char curr_state = rwp.getState();
				double v_curr = v.get(curr_state);
				rwp.walk();
				char next_state = rwp.getState();
				double v_next = v.get(next_state);
				double r_next = rwp.getReward();
				v.put(curr_state, (1.0-alpha)*v_curr + alpha*(r_next + gamma*v_next));
				// System.out.printf("%c-%f-",curr_state, r_next);
			}								
			
		}		
		return evaluateOnFive(v);
		
		// for(Character state : v.keySet()){
		// 	System.out.printf("%c: %f\n", state, v.get(state));
		// }
	}
		
	public static double evaluateOnFive(HashMap<Character, Double> v){
		double rms = 0.0;
		rms += Math.pow(1.0/6.0 - v.get('A'),2);
		rms += Math.pow(2.0/6.0 - v.get('B'),2);
		rms += Math.pow(3.0/6.0 - v.get('C'),2);
		rms += Math.pow(4.0/6.0 - v.get('D'),2);
		rms += Math.pow(5.0/6.0 - v.get('E'),2);	
		rms = Math.sqrt(rms/5.0);
		return rms;
	}

	public static void main(String[] args){
		int N = 200;
		int iter = 500;
		for(int n = 1; n <= N; n++){
			double s = 0.0;
			for(int i = 0; i < iter; i++){
				s += CAMC(0.5, n);
			}
			s = s/iter;
			System.out.printf("%f\n", s);
		}
		
		// CAMC(0.03);		
	}
}