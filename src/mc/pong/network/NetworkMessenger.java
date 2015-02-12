package mc.pong.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mc.pong.message.member.MemberConnectMessage;
import mc.pong.message.member.MemberJoinMessage;
import mc.pong.message.member.MemberMessage;
import mc.pong.message.state.StateMessage;
import mc.pong.message.state.StateScoreMessage;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


public class NetworkMessenger {
	private ServerSocket listenSocket = null;
	private Map<String, Socket> peers = new HashMap<String,Socket>();
	private int listeningPort;
	private ReceiveCommTask comm = null;
	static private NetworkMessenger instance = null;
	static private boolean hasStarted = false;
	
    List<StateMessage> stateMessageQueue = Collections.synchronizedList(new ArrayList<StateMessage>());
    List<MemberMessage> memMessageQueue = Collections.synchronizedList(new ArrayList<MemberMessage>());


	
	public static NetworkMessenger getInstance(){
		if(instance == null)
			instance = new NetworkMessenger();
		return instance;
	}
	
	private NetworkMessenger(){
		//this.port = port;
	}
	
	public String getListeningAddr(){
		return "10.0.2.2"+":"+listeningPort;
	}
	
	
	public void startListening(int port){
		if(hasStarted == false){
		this.listeningPort = port;
		new IncommingCommTask().execute();
		hasStarted = true;
		}
	}
	
	public boolean connectTo(String address){
		String[] addrs = address.split(":");
		Socket sock = new Socket();
		//ip.
		try {
			sock = new Socket();
			SocketAddress addr = new InetSocketAddress(addrs[0], Integer.parseInt(addrs[1]));
			sock.connect(addr, 4000);
		} catch (Exception e) {
			return false;
		}
		
		peers.put(address ,sock);
		
		new ReceiveCommTask().execute( sock);
		return true;
	}

	
	public boolean sendMessage(String addr,Object message){
		try {
			ObjectOutputStream os = new ObjectOutputStream(peers.get(addr).getOutputStream());
			os.writeObject(message);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean broadcastMessage(Object message) {
		for(Socket peer : peers.values()){
			try {
				ObjectOutputStream os = new ObjectOutputStream(peer.getOutputStream());
				os.writeObject(message);
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
	    }
		return true;
	}
	
	public void updateIncomingPeerAddr(String listeningAddr, Socket s){
		peers.put(listeningAddr, s);
	}

	public List<MemberMessage> getMemMessage() {
		return memMessageQueue;
	}
	
	public List<StateMessage> getStateMessage() {
		return stateMessageQueue;
	}

	public void clearMemMessage() {
		memMessageQueue.clear();
	}
	
	public void clearStateMessage() {
		stateMessageQueue.clear();
	}
	
	
	
	
	public class IncommingCommTask extends AsyncTask<Void, Socket, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				listenSocket = new ServerSocket(listeningPort);
			} catch (IOException e) {
				e.printStackTrace();
			}
			while (!Thread.currentThread().isInterrupted()) {
				try {
					
					Socket sock = listenSocket.accept();
					publishProgress(sock);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Socket... values) {
			String addr = values[0].getRemoteSocketAddress().toString().split("/")[1];
			new ReceiveCommTask().execute(values[0]);
		}
	}
	
	
	public class ReceiveCommTask extends AsyncTask<Socket, Object, Void> {
		Socket s;
		
		@Override
		protected Void doInBackground(Socket... params) {
			Object message;
			
			s = params[0];
			
			try {
				ObjectInputStream objStream;
				while( true)
				{   
					objStream = new ObjectInputStream(s.getInputStream());
					if((message = objStream.readObject())!=null)
					   publishProgress(message);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Object... message) {
			// TODO Auto-generated method stub
			if(message[0] instanceof StateMessage){
				//stateMessageQueue.add((StateMessage)message[0]);
				NetworkGameState.getInstance().handleMessage((StateMessage)message[0]);
			}
			else{
				//memMessageQueue.add((MemberMessage)message[0]);
				if(message[0] instanceof MemberJoinMessage){
					MemberJoinMessage memJoin = (MemberJoinMessage)message[0];
					updateIncomingPeerAddr(memJoin.listeningAddress, s);
				}
				else if(message[0] instanceof MemberConnectMessage){
					MemberConnectMessage memJoin = (MemberConnectMessage)message[0];
					updateIncomingPeerAddr(memJoin.listeningAddress, s);
				}
				NetworkMember.getInstance().handleMessage((MemberMessage)message[0]);
			}
		}

		@Override
		protected void onPreExecute() {
			
		}

		@Override
		protected void onPostExecute(Void result) {
			try {
				s.close();
			}
			catch (Exception e) {
				Log.d("erro fecho", e.getMessage());
			}
			s = null;
		}
	}
}

