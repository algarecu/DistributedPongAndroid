package mc.pong.message.state;

import mc.pong.utils.PingPongInfo;

public class StateBallMessage implements StateMessage{
    public PingPongInfo ballInfo;
    
    public StateBallMessage(PingPongInfo ballInfo){
    	this.ballInfo = ballInfo;
    }
}
