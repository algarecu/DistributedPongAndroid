//package mc.pong.network.tasks;
//
//import java.io.IOException;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.List;
//
//import mc.pong.message.member.MemberMessage;
//import mc.pong.message.state.StateMessage;
//
//import android.os.AsyncTask;
//
//public class IncomingCommTask extends AsyncTask<Void, Socket, Void> {
//
//	ServerSocket listenSocket;
//	int listeningPort;
//	
//	List<StateMessage> stateMessageQueue;
//	List<MemberMessage> memMessageQueue;
//	
//	public IncomingCommTask(int listeningPort, List<StateMessage> stateMsg, List<MemberMessage> memMsg){
//		this.listeningPort = listeningPort;
//		stateMessageQueue = stateMsg;
//		memMessageQueue = memMsg;
//	}
//	
//	@Override
//	protected Void doInBackground(Void... params) {
//		try {
//			listenSocket = new ServerSocket(listeningPort);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		while (!Thread.currentThread().isInterrupted()) {
//			try {
//				
//				Socket sock = listenSocket.accept();
//				//if (peers != null)
//				//	sock.close();
//				//else {
//				publishProgress( sock);
//				//}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return null;
//	}
//	
//	@Override
//	protected void onProgressUpdate(Socket... values) {
//		peers.put(values[0].getInetAddress().toString(), values[0]);
//		comm = new ReceiveCommTask(syncStateMessageQueue, syncMemMessageQueue);
//		comm.execute( values[0]);
//	}
//}
