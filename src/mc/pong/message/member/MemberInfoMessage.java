package mc.pong.message.member;

import java.net.SocketAddress;
import java.util.List;

/*
 *  When a node accepts a member join message,
 *  it sends back all list of members
 * 
 */
public class MemberInfoMessage implements MemberMessage{
     public List<String> memberList;
     public List<List<String>> memberGraph;
     
     public MemberInfoMessage(List<String> memberList, List<List<String>>  memberGraph){
    	 this.memberList = memberList;
    	 this.memberGraph = memberGraph;
     }
}
