package mc.pong.message.state;

import mc.pong.utils.ScoreBoardInfo;

public class StateScoreMessage implements StateMessage{
    public ScoreBoardInfo info;
    
    public StateScoreMessage(ScoreBoardInfo info){
    	this.info = info;
    }
    
    public StateScoreMessage(String addr, int score){
    	this.info = new ScoreBoardInfo(addr, score);
    }
}
