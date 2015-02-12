package mc.pong.message.state;

public class StateBallNotifyMessage implements StateMessage{
	public int dir;
	
	//Transform the dir here, this makes the receiver much easier..
	public StateBallNotifyMessage(int dir){
		if( 0 == dir || 2== dir)
			this.dir = 2- dir;
		else
			this.dir = dir;
	}
	
}
