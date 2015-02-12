package mc.pong.message.member;

import java.util.List;

public class MemberInitEndMessage implements MemberMessage{
    public List<List<String>> memberGraph;
    
    public MemberInitEndMessage(List<List<String>> memberGraph){
    	this.memberGraph = memberGraph;
    }
}
