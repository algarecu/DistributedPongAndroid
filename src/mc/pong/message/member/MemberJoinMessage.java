package mc.pong.message.member;

public class MemberJoinMessage  implements MemberMessage{
	public String listeningAddress;
	
	 public MemberJoinMessage(String listeningAddr){
		 listeningAddress = listeningAddr;
	 }
}
