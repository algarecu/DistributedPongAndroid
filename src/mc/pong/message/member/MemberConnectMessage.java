package mc.pong.message.member;

import java.util.List;

/*
 *  The joining node receives a list of members,
 *  then it tries to connect to all of them.
 *  If succees, it will send this msg to register.
 */

public class MemberConnectMessage implements MemberMessage{
	public String listeningAddress;
    
    
    public MemberConnectMessage(String listeningAddr){
    	listeningAddress = listeningAddr;
    }
    
}
