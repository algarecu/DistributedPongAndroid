package mc.pong.message.member;

import mc.pong.utils.ScoreBoardInfo;

public class MemberConnectEndMessage implements MemberMessage{
	public ScoreBoardInfo info;
	
	public MemberConnectEndMessage(ScoreBoardInfo info){
		this.info = info;
	}
}
