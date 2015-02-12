package mc.pong.network.mgr;

import java.util.ArrayList;
import java.util.List;

public class MemberManager {
	//Myself is not in the member list!
	List<String> members = new ArrayList<String>();
	List<List<String>> memberGraph = new ArrayList<List<String>>();
	String myAddress;
	
	public MemberManager(String address){
		myAddress = address;
		memberGraph.add(new ArrayList<String>());
		memberGraph.get(0).add(myAddress);
	}
	
	public List<List<String>> getMemberGraph(){
		return memberGraph;
	}
	
	public List<String> getMembers(){
		return members;
	}
	
	public boolean isEmpty() {
		return 	members.isEmpty();
	}

	public boolean contains(String addr) {
		return members.contains(addr);
	}

	//TODO Just some simple partition are added.. Need further refinement
	public void add(String addr) {
		members.add(addr);
	}
	
	public void addAndUpateGraph(String addr){
		members.add(addr);
		if(members.size() == 1){
			memberGraph.get(0).add(addr);
		}
		else if(members.size() == 2){
			memberGraph.get(0).add(addr);
		}
		else if(members.size() == 3){
			String previousLast = memberGraph.get(0).remove(2);
			memberGraph.add(new ArrayList<String>());
			memberGraph.get(1).add(previousLast);
			memberGraph.get(1).add(addr);
		}
	}

	public void setMemberGraph(List<List<String>> memberGraph) {
		this.memberGraph = memberGraph;
	}

	public String[] getNeighbourList() {
		String[] result = new String[4];
		
		int[] coord = getMemberCoord(myAddress);
		result[0] = getMember(coord[0],coord[1]-1);
		result[1] = getMember(coord[0]-1,coord[1]);
		result[2] = getMember(coord[0],coord[1]+1);
		result[3] = getMember(coord[0]+1,coord[1]);
		return result;
	}
	
	private int[] getMemberCoord(String addr){
		int[] coord = {0,0};
		for(List<String> row : memberGraph){
			coord[1] = 0;
			for(String elem: row){
				if(elem.equals(addr)){
					return coord;
				}
				++coord[1];
			}
			++coord[0];
		}
		coord[0] = -1;
		coord[1] = -1;
		return coord;
	}
	
	// Member:  ""  empty string means that will be wall
	//         null means empty, but it is not wall --- This may happen
	//         a normal string means a neighbour
	private String getMember(int x, int y){
		if(memberGraph.size() <= x || x<0)
			return "";
		else if( memberGraph.get(x).size() <=y || y <0)
			return "";
		else
			return memberGraph.get(x).get(y);
	}

	public int getSize() {
		return members.size();
	}
}


