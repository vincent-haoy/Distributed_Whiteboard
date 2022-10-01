package WhiteboardClient;
import java.rmi.Naming;
import remote.IRemoteClient;
import remote.IRemoteDrawing;


/**
 * Haoyu Bai
 * 956490
 *
 *
 * description: Join white board logics, using RMI
 **/
public class JoinWhiteBoard {
	
	public static void main(String[] args) {

		if(args.length != 3) {
			System.out.println("check the paramter plz");
			System.exit(0);
		}

		//reading paramteres
		String hostName = args[0];
		String port = args[1];
		String username = args[2];
		
		/*
		String hostName = "localhost";
		String port = "1099";
		String username = "manager";
		*/
		String RMIaddress = "rmi://"+hostName + ":" + port + "/";
		try {
			//Connect to the rmiregistry that is running on localhost
			
			IRemoteDrawing remotewhiteboard = (IRemoteDrawing) Naming.lookup(RMIaddress + "whiteboard");
			
			/* enforcing the client has a unique id */
			String Stringfinalname = new String(username);
			int postpend = 0;
			while(!remotewhiteboard.canUseThisName(Stringfinalname)) {
				Stringfinalname = username + Integer.toString(postpend);
				postpend += 1;
				
			}
			
			/* when the unique id is get, try use this id to join whitebaord service*/
			IRemoteClient whiteboard = (IRemoteClient) new WhiteboardClient(Stringfinalname,RMIaddress);
			Naming.rebind((RMIaddress + Stringfinalname), whiteboard);
			if(remotewhiteboard.RequestToJoin(Stringfinalname)) {
				whiteboard.setisjoined();
			}else {
				Naming.unbind(RMIaddress + Stringfinalname);
				System.exit(0);
			}
		}catch(Exception e) {
			System.out.println("cannot find the White board registery");
			e.printStackTrace();
		}
		
	}
}
	