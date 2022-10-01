package remote;
/**
 * Haoyu Bai
 * 956490
 *
 *
 * description: Remote Client Interface
 **/
import java.awt.Image;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import whiteboardServer.Shape;

public interface IRemoteClient extends Remote{
	public void setisjoined()throws RemoteException;
	public void addtoboard(WhiteboardClient.Shape s)throws RemoteException;
	public void clear() throws RemoteException;
	void addtoboard(Shape s) throws RemoteException;
	public void addstringtochatRoom(String string)throws RemoteException;
	public void exitmessage(String info)throws RemoteException;
	void setimage(byte[] bytes)throws RemoteException, NotBoundException;
	public void updateConnectedUsersByList(ArrayList<String> text)throws RemoteException, NotBoundException;
}