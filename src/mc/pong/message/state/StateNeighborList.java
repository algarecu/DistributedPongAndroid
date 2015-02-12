package mc.pong.message.state;

import java.util.List;

public class StateNeighborList implements StateMessage{
    public List<List<Integer>> memberGraph;
    public int myIndex;
   
    public StateNeighborList(int index, List<List<Integer>> memberGraph){
    	myIndex = index;
    	this.memberGraph = memberGraph;
    }
    
}
