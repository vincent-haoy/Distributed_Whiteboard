package remote;
import java.awt.Image;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import WhiteboardClient.Shape;

/**
 * Haoyu Bai
 * 956490
 *
 *
 * description: Remote Server Interface
 **/
public interface IRemoteDrawing extends Remote {

	public void show() throws RemoteException;
	public Boolean RequestToJoin(String a) throws RemoteException, NotBoundException;
	public Boolean leave(String a) throws RemoteException;
	public void addtoboard(Shape s) throws RemoteException;;
	public void boardcastingtoallboard(Shape s) throws RemoteException, NotBoundException;
	void boardcastingtoallboard(whiteboardServer.Shape s) throws RemoteException, NotBoundException;
	void cleanallboard() throws RemoteException, NotBoundException;
	void updateAllChartRoom(String string) throws RemoteException, NotBoundException;
	public boolean canUseThisName(String username) throws RemoteException, NotBoundException;

}
