package mc.pong.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mc.pong.message.member.MemberConnectEndMessage;
import mc.pong.message.member.MemberConnectMessage;
import mc.pong.message.member.MemberInfoMessage;
import mc.pong.message.member.MemberInitEndMessage;
import mc.pong.message.member.MemberJoinMessage;
import mc.pong.message.member.MemberMessage;
import mc.pong.message.state.StateNeighborList;
import mc.pong.network.mgr.MemberManager;

import android.os.AsyncTask;
import android.util.Pair;
import android.view.View;

public class NetworkMember {
	static MemberManager memberManager ;
	static NetworkGameState stateManager= NetworkGameState.getInstance();
	static String myAddress;
	static Set<String> pendingConnectingMember = new HashSet<String>();
	static NetworkMember instance = null;
	
	private NetworkMember(){
		//new MessageHanlder().execute();
	}
	
	public static NetworkMember getInstance(){
		if(instance ==null)
			instance = new NetworkMember();
		return instance;
	}
	

	public void startListening(String port){
		int numPort = 0;
		if(port.equals(""))
			numPort = 9999;
		else
			numPort = Integer.parseInt(port);
		NetworkMessenger.getInstance().startListening(numPort);
		myAddress = NetworkMessenger.getInstance().getListeningAddr();
		memberManager = new MemberManager(NetworkMessenger.getInstance().getListeningAddr());
	}
	
	public boolean connectTo(String address){
		 boolean connectSuccess = connectAddMember(address);
		 if(connectSuccess == true){
			MemberJoinMessage memMessage = 
					new MemberJoinMessage(NetworkMessenger.getInstance().getListeningAddr());
		    NetworkMessenger.getInstance().sendMessage(address,memMessage);
		 }
		 return connectSuccess;
	}
	
//	class MessageHanlder extends AsyncTask<Void, Void, Void> {
//
//		@Override
//		protected Void doInBackground(Void... params) {
//			List<MemberMessage> messageQueue;
//		    while(true){
//		    	if( !(messageQueue =NetworkMessenger.getInstance().getMemMessage()).isEmpty() ){
//			    	for(MemberMessage msg : messageQueue)
//			    		handleMessage(msg);
//			    	NetworkMessenger.getInstance().clearMemMessage();
//			    	try {
//						Thread.sleep(200);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//		    	}
//		    }
//		}
//	}

      synchronized public void handleMessage(MemberMessage msg) {
		if(msg instanceof MemberJoinMessage){
			handleMemberJoin((MemberJoinMessage)msg);
		}
		else if(msg instanceof MemberConnectMessage){
			handleMemberConnect((MemberConnectMessage)msg);
		}
		else if(msg instanceof MemberConnectEndMessage){
			handleMemberConnectEnd((MemberConnectEndMessage)msg);
		}
		else if(msg instanceof MemberInfoMessage){
			handleMemberInfo((MemberInfoMessage)msg);
		}
		else if(msg instanceof MemberInitEndMessage){
			handleMemberInitEnd((MemberInitEndMessage)msg);
		}
	}
	


    synchronized private void handleMemberConnectEnd(MemberConnectEndMessage msg) {
		stateManager.addScoreBoard(msg.info);
		pendingConnectingMember.remove(msg.info.ownerAddress);
		if(pendingConnectingMember.isEmpty()){
			NetworkMessenger.getInstance().broadcastMessage(
					new MemberInitEndMessage(memberManager.getMemberGraph()));
		}
	}

    synchronized private  void handleMemberInfo(MemberInfoMessage msg) {
		List<String> remoteMembers = ((MemberInfoMessage)msg).memberList;
		for(String member : remoteMembers){
			if( connectAddMember(member) == true){
			//Send the new member graph and my address
			pendingConnectingMember.add(member);
			NetworkMessenger.getInstance().sendMessage(member,
					new MemberConnectMessage(NetworkMessenger.getInstance().getListeningAddr()));
			}
		}
		
		memberManager.setMemberGraph(msg.memberGraph);
		stateManager.setNeighbourList(memberManager.getNeighbourList());
		
		if(pendingConnectingMember.isEmpty()){
			NetworkMessenger.getInstance().broadcastMessage(
					new MemberInitEndMessage(memberManager.getMemberGraph()));
		}
	}

    synchronized private  void handleMemberConnect(MemberConnectMessage msg) {
		//Add member
		String memaddr = msg.listeningAddress;
		addNewMember(memaddr, false);
		NetworkMessenger.getInstance().sendMessage(memaddr, 
				new MemberConnectEndMessage(stateManager.getScoreBoardInfo()));
	}

    synchronized private  void handleMemberJoin(MemberJoinMessage msg){
		String memAddr = msg.listeningAddress; 
		
		//Add member; Because the node accepts connection so it is the coordinator
		addNewMember(memAddr, true);
		
		//Send member list
		NetworkMessenger.getInstance().sendMessage(memAddr,
				  new MemberInfoMessage(memberManager.getMembers(), memberManager.getMemberGraph()));
		
		stateManager.sendScoreBoard(memAddr);//, memberManager.getNeighbourList());
	}
	
    synchronized private  void handleMemberInitEnd(MemberInitEndMessage msg) {
		memberManager.setMemberGraph(msg.memberGraph);
		stateManager.setNeighbourList(memberManager.getNeighbourList());
	}

	//Just add to list... Usually this is like passive add and means that 
	//has established connection with that node
	public  void addNewMember(String addr, boolean updateGraph){
		if(!memberManager.contains(addr) && !addr.equals(myAddress)){
			if(updateGraph == false)
			 memberManager.add(addr);
			else
		     memberManager.addAndUpateGraph(addr);
			stateManager.addScoreBoard(addr);
		}
	}
	
	//Active add, means connect and then add
	public  boolean connectAddMember(String addr){
		if(!memberManager.contains(addr) &&  !addr.equals(myAddress)){
			if( NetworkMessenger.getInstance().connectTo(addr) == true){
				memberManager.add(addr);
				return true;
			}
		}
		return false;
	}

	public NetworkGameState getStateManager() {
		return stateManager;
	}

	public void startGame() {
		stateManager.initBall();
	}
}
