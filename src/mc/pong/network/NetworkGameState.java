package mc.pong.network;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mc.pong.game.GameState;
import mc.pong.message.state.StateBallMessage;
import mc.pong.message.state.StateBallNotifyMessage;
import mc.pong.message.state.StateMessage;
import mc.pong.message.state.StateScoreMessage;
import mc.pong.network.mgr.NeighborManager;
import mc.pong.utils.PingPong;
import mc.pong.utils.PingPongInfo;
import mc.pong.utils.ScoreBoardInfo;
import android.os.AsyncTask;

public class NetworkGameState {
	NeighborManager neighborMgr = new NeighborManager();
	Map<Integer, Boolean> ballNotification = new HashMap<Integer, Boolean>();

	int lastTimeScore = 5;
	int padDirection = 3;
	List<PingPongInfo> pendingBalls = Collections.synchronizedList(new ArrayList<PingPongInfo>());
	List<Integer> pendingNotify = 	Collections.synchronizedList(new ArrayList<Integer>());
	List<ScoreBoardInfo> pendingScores = Collections.synchronizedList(new ArrayList<ScoreBoardInfo>());
	static NetworkGameState instance = null;
	GameState gameState;
	
	private NetworkGameState(){
		//new MessageHanlder().execute();
	}
	
	static public NetworkGameState getInstance(){
		if(instance ==null)
			instance = new NetworkGameState();
		return instance;
	}
	


	public void setNeighbourState(int dir, boolean isExisting) {
		neighborMgr.set(dir, isExisting);
	}

	public boolean notifyBall(PingPong ball, int dir) {
		if (neighborMgr.exists(dir).equals(false)) // It is wall!
			return true;
		else {
			// Do notification
			if (ball.isHitDestWall() == true) {
				StateBallMessage message = new StateBallMessage(
						ball.getBallInfo(dir));
				ballNotification.put(ball.id, false);
				NetworkMessenger.getInstance().sendMessage(
						neighborMgr.getAddr(dir), message);
				return false;
			} else { // Have not sent notification for the ball
				if (ballNotification.containsKey(ball.id) == false
						|| ballNotification.get(ball.id) == false) {
					StateBallNotifyMessage message = new StateBallNotifyMessage(
							dir);
					// Have sent notification for the ball, so mark
					ballNotification.put(ball.id, true);
					NetworkMessenger.getInstance().sendMessage(
							neighborMgr.getAddr(dir), message);
				}
				return true;
			}
		}
	}

	public void notifyScore(ScoreBoardInfo myInfo) {
		myInfo.ownerAddress = NetworkMessenger.getInstance().getListeningAddr();
		lastTimeScore = myInfo.score;
		NetworkMessenger.getInstance().broadcastMessage(
				new StateScoreMessage(myInfo));
	}

	public List<PingPongInfo> getPendingBalls() {
		return pendingBalls;
	}

	public Date getLastBallGenerTime() {
		return pendingBalls.get(pendingBalls.size() - 1).genDate;
	}

	public List<Integer> getPendingNotify() {
		return pendingNotify;
	}

	public List<ScoreBoardInfo> getPendingScore() {
		return pendingScores;
	}
	
	public int getPadDirection(){
		return padDirection;
	}
	
	public void clearPendingNotify(){
		pendingNotify.clear();
	}

	public void clearPendingScore() {
		pendingScores.clear();
	}

	public void clearPendingBalls() {
		pendingBalls.clear();
	}

//	public class MessageHanlder extends AsyncTask<Void, Void, Void> {
//
//		@Override
//		protected Void doInBackground(Void... params) {
//			List<StateMessage> messageQueue;
//			while (true) {
//				if (!(messageQueue = NetworkMessenger.getInstance()
//						.getStateMessage()).isEmpty()) {
//					for (StateMessage msg : messageQueue)
//						handleMessage(msg);
//					NetworkMessenger.getInstance().clearStateMessage();
//				}
//			}
//		}
//
//	}

	synchronized public void handleMessage(StateMessage msg) {
		// TODO Auto-generated method stub
		if (msg instanceof StateBallMessage) {
			gameState.checkPendingBalls(((StateBallMessage) msg).ballInfo);
		} else if (msg instanceof StateBallNotifyMessage) {
			gameState.checkPendingNotify(((StateBallNotifyMessage) msg).dir);
		} else if (msg instanceof StateScoreMessage) {
			gameState.checkPendingScore(((StateScoreMessage) msg).info);
		}
	}

	//When game start, init a first ball.
	public void initBall() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		gameState.checkPendingBalls((new PingPongInfo(0, 150, 150, 3, 3, new Date())));
	}

	public void addScoreBoard(String addr) {
		gameState.checkPendingScore(new ScoreBoardInfo(addr, 5));
	}
	
	public void addScoreBoard(ScoreBoardInfo info) {
		gameState.checkPendingScore(info);
	}

	public void sendScoreBoard(String addr) {
		NetworkMessenger.getInstance().sendMessage(
				addr,
				new StateScoreMessage(NetworkMessenger.getInstance()
						.getListeningAddr(), lastTimeScore));
	}
	
	public ScoreBoardInfo getScoreBoardInfo() {
		return new ScoreBoardInfo(NetworkMessenger.getInstance()
						.getListeningAddr(), lastTimeScore);
	}

//	public void joinNewPlayer(String memAddr, String[] neighbourList) {
//		sendScoreBoard(memAddr);
//		padDirection = neighborMgr.setAll(neighbourList);
//	}
	
	public void setNeighbourList(String[] neighbourList){
		gameState.checkDirection(neighborMgr.setAll(neighbourList));
	}

	public void setGameState(GameState gameState) {
		// TODO Auto-generated method stub
		this.gameState = gameState;
	}



}
