package publicGUI.communicationJPanel.remote;
/**
 * 
 * @author jame
 * 服务器端客户端的代表类
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class RemoteClient extends Thread {

	private String target;//与当前对象连接的服务器唯一标识
	private Socket socket;//当前代表对象的socket
	private String curName;//当前代表的客户端的唯一标识
	public RemoteClient(Socket current){
		this.socket = current;
	}
	public void run(){
		try{
			InputStream in = socket.getInputStream();
			//拿到双方的唯一标识
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			//客户端连接后发送的第一条消息 SELF#TARGET
			String msgs[] = reader.readLine().split("#");
			this.curName  = msgs[0];
			this.target = msgs[1];
			RemoteServer.clients.put(this.curName,this);
			System.out.println(RemoteServer.clients+":"+this.curName);
			while(true){
				byte[] buf = new byte[1024];
				int length;
				while((length = in.read(buf))!=-1){
					RemoteServer.clients.get(this.target).getSocket().getOutputStream().write(buf, 0, length);
				}
				RemoteServer.clients.get(this.target).getSocket().getOutputStream().flush();
			}
		}catch(IOException ex){
			RemoteServer.clients.remove(this.curName);
			ex.printStackTrace();
		}
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
}
