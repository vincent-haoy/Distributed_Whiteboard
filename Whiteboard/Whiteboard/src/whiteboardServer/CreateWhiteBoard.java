package whiteboardServer;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import remote.IRemoteDrawing;

/**
 * Haoyu Bai
 * 956490
 *
 *
 * description: creating the white board UI and some locigcs
 **/


public class CreateWhiteBoard {

	public static void main(String[] args)  {
		//IP PORT USERNAME

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
		try {
			// registery the whiteboard server
			LocateRegistry.createRegistry(Integer.parseInt(port));
			IRemoteDrawing whiteboard = new WhiteBoard(username,"rmi://"+hostName + ":" + port + "/");
			Naming.rebind("rmi://"+hostName + ":" + port + "/"+"whiteboard", whiteboard);
            System.out.println("Whiteboard server ready");
		} catch (Exception e) {
			System.out.println("Cannot rebind the server");
		}
		
	}
}
