package mc.pong.utils;

import java.io.Serializable;

public class ScoreBoardInfo  implements Serializable{
	public String ownerAddress;
	public Integer score;
	
	public ScoreBoardInfo(String ownerAddress, Integer score) {
		this.ownerAddress = ownerAddress;
		this.score = score;
	}


}
