package whiteboardServer;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.rmi.Naming;
import remote.IRemoteClient;
import remote.IRemoteDrawing;


import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.swing.table.DefaultTableModel;
/**
 * Haoyu Bai
 * 956490
 *
 *
 * description: creating the Server white board UI and some locigcs
 **/

public class WhiteBoard extends UnicastRemoteObject implements IRemoteDrawing{
	  public String savingaddress;
	  public CavasPanel drawArea;
	  ArrayList<Shape> shapelist = new ArrayList<>();
	  ArrayList<String> Userslist;
	  JTextArea chatroom;
	  JMenuBar menuBar;
	  JMenu menu, submenu;
	  private JTextField drawtextinput;
	  private JTextField ChatInput;
	  DefaultTableModel model;
	  private JTable table;
	  private String USERNAME;
	  JFrame frame;
	  String RMIaddress;
	  	
	  public WhiteBoard(String username,String Rmiaddress) throws RemoteException {
		    
		    // UI constructor.. all the UI are here
		  	savingaddress  = "";
		  	USERNAME = username;
		  	RMIaddress = Rmiaddress;
		    Userslist = new ArrayList<String>();
		    drawArea = new CavasPanel(this);
		    drawArea.setBounds(135, 84, 542, 423);
		    canvasController canvascontroller = drawArea.canvascontroller;
		    
		    // add to content pane
		  	frame = new JFrame("Swing Paint");
		  	frame.getContentPane().setFont(new Font("SimSun", Font.PLAIN, 18));
			
		  	// the menu bar
		  	menuBar = new JMenuBar();
		  	menu = new JMenu("File");
		  	menuBar.add(menu);
		  	
		  	// create a new whiteboard for everyone
		  	JMenuItem menuaddItem = new JMenuItem("new");
		  	menuaddItem.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		try {
		    			cleanallboard();
		    			;
					} catch (RemoteException | NotBoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		    	}
		    });
		  	menu.add(menuaddItem);
		  	
		  	// load the file from existing source
		  	JMenuItem menueloadItem = new JMenuItem("load");
		  	menueloadItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
			  		JFrame popoutf = new JFrame();
			  		String filename =JOptionPane.showInputDialog(popoutf,"Enter the filepath you want to load");
			  		BufferedImage img = null;
			  		 try {
			  			 //convert the image to binary first
						img = ImageIO.read(new File(filename));
						drawArea.setimage((Image)img);
						savingaddress = filename;
						drawArea.g2 = (Graphics2D) img.getGraphics();
						drawArea.g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
						drawArea.repaint();
						
						// drawing the image to cliet
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						ImageIO.write(img, "jpg", baos);
						 byte[] bytes = baos.toByteArray();
						for(String user :Userslist) {
							IRemoteClient client =  (IRemoteClient) Naming.lookup(Rmiaddress + user);
							client.setimage(bytes);
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (NotBoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
		  		
		  	});
		  	menu.add(menueloadItem);
		  	
		  	//saving the image to the previous specified address
		  	JMenuItem menuesaveItem = new JMenuItem("save");
		  	menuesaveItem.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		
		    		//if the imagepath has already exitst
		    		if(savingaddress != "") {
		    			File outfile = new File(savingaddress);
		    			try {
		    				Image imagestore = drawArea.getImage();
				    		JFrame popoutf;
				    		popoutf = new JFrame(); 
							BufferedImage bufferedImage = (BufferedImage) imagestore;
							ImageIO.write(bufferedImage, "jpg", outfile);
							 JOptionPane.showMessageDialog(popoutf,"The image has been successfuly stored");  
						}catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		    		}else {
		    			
		    			//if the image path does not exitst, ask the manager to input one
			    		JFrame popoutf;
			    		popoutf =new JFrame();   
			    		String filename =JOptionPane.showInputDialog(popoutf,"not know address, plz enter your file Name");  
			    		Image imagestore = drawArea.getImage();
						BufferedImage bufferedImage = (BufferedImage) imagestore;
						File outfile = new File(filename);
						try {
							ImageIO.write(bufferedImage, "jpg", outfile);
							savingaddress = filename;
							 JOptionPane.showMessageDialog(popoutf,"The image has been successfuly stored");  
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}		
		    		}

		    	}
		    });
		  	menu.add(menuesaveItem);
		  	
		  	// saving the image to a specified address
		  	JMenuItem menusaveasItem = new JMenuItem("saveas");
		  	menusaveasItem.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		JFrame popoutf;
		    		popoutf =new JFrame();   
		    		String filename =JOptionPane.showInputDialog(popoutf,"Enter your file Name");  
		    		Image imagestore = drawArea.getImage();
					BufferedImage bufferedImage = (BufferedImage) imagestore;
					File outfile = new File(filename);
					try {
						ImageIO.write(bufferedImage, "jpg", outfile);
						savingaddress = filename;
						 JOptionPane.showMessageDialog(popoutf,"The image has been successfuly stored");  
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		    	}
		    });
		  	menu.add(menusaveasItem);
		  	
		  	
		  	// closing the entire whiteboard for all the clients
		  	JMenuItem menucloseItem = new JMenuItem("close");
		  	menucloseItem.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		try {
		    			endthewhiteboard();
					} catch (RemoteException | NotBoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		    	}
		    });
		  	menu.add(menucloseItem);
		  	frame.setJMenuBar(menuBar);
		  	
		  	//the drawing area
		    Container content = frame.getContentPane();
		    frame.getContentPane().setLayout(null);
		    // create draw area
		    content.add(drawArea);
		    
		    //cir btn
		    JButton CircleBtn = new JButton("Circle");
		    CircleBtn.setBounds(10, 133, 115, 39);
		    CircleBtn.setFont(new Font("SimSun", Font.PLAIN, 18));
		    CircleBtn.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		canvascontroller.setVariabels(null, "OVAL", null);
		    	}
		    });
		    frame.getContentPane().add(CircleBtn);
		    
		    //line btn
		    JButton LineBttn = new JButton("Line");
		    LineBttn.setBounds(10, 84, 115, 39);
		    LineBttn.setFont(new Font("SimSun", Font.PLAIN, 18));
		    LineBttn.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		canvascontroller.setVariabels(null, "LINE", null);
		    	}
		    });
		    frame.getContentPane().add(LineBttn);
		    
		    //tri btn
		    JButton TriangleBtn = new JButton("Triangle");
		    TriangleBtn.setBounds(10, 182, 115, 39);
		    TriangleBtn.setFont(new Font("SimSun", Font.PLAIN, 15));
		    TriangleBtn.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		canvascontroller.setVariabels(null, "TRI", null);
		    	}
		    });
		    frame.getContentPane().add(TriangleBtn);
		    
		    //rect btn
		    JButton RecBtn = new JButton("Rectangle");
		    RecBtn.setBounds(10, 231, 115, 39);
		    RecBtn.setFont(new Font("SimSun", Font.PLAIN, 15));
		    RecBtn.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		canvascontroller.setVariabels(null, "RECT", null);
		    	}
		    });
		    frame.getContentPane().add(RecBtn);
		    
		    //text btn
		    JButton TextInBtn = new JButton("Text");
		    TextInBtn.setBounds(10, 362, 115, 39);
		    TextInBtn.setFont(new Font("SimSun", Font.PLAIN, 18));
		    TextInBtn.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		String inputtextfieldvalue = drawtextinput.getText();
		    		canvascontroller.setVariabels(null, "STRING",inputtextfieldvalue);
		    	}
		    });
		    frame.getContentPane().add(TextInBtn);
		    
		    // color btn
		    JButton ColorBtn = new JButton("Color");
		    ColorBtn.setBounds(10, 411, 115, 39);
		    ColorBtn.setFont(new Font("SimSun", Font.PLAIN, 18));
		    ColorBtn.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		Color color = JColorChooser.showDialog(null,"Pick a color", new Color(1));
		    		canvascontroller.setVariabels(color, null, null);
		    	}
		    });
		    frame.getContentPane().add(ColorBtn);
		    
		    // draw text input
		    drawtextinput = new JTextField();
		    drawtextinput.setBounds(10, 280, 115, 57);
		    frame.getContentPane().add(drawtextinput);
		    drawtextinput.setColumns(10);
		    
		    //clean btn + logic
		    JButton CleanBtn = new JButton("Clean");
		    CleanBtn.setBounds(10, 460, 115, 39);
		    CleanBtn.setFont(new Font("SimSun", Font.PLAIN, 18));
		    CleanBtn.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		try {
						cleanallboard();
					} catch (RemoteException | NotBoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

		    	}
		    });
		    frame.getContentPane().add(CleanBtn);
		    
		    
		    //chat room UI here
		    ChatInput = new JTextField();
		    ChatInput.setBounds(687, 429, 262, 39);
		    frame.getContentPane().add(ChatInput);
		    ChatInput.setColumns(10);
		    
		    JScrollPane scrollPane = new JScrollPane();
		    scrollPane.setBounds(687, 85, 262, 334);
		    frame.getContentPane().add(scrollPane);
		    
		    chatroom = new JTextArea();
		    scrollPane.setViewportView(chatroom);
		    frame.setSize(973, 689);
		    
		    // can close frame
		    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    
		    //char room logic here (with input line)
		    JTextField disabled = new JTextField();
		    disabled.setBounds(10, 280, 115, 57);
		    frame.getContentPane().add(disabled);
		    frame.setVisible(true);
		    
		    JButton SendBtn = new JButton("Send");
		    SendBtn.setBounds(769, 468, 115, 39);
		    SendBtn.setFont(new Font("SimSun", Font.PLAIN, 18));
		    SendBtn.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		String inputtextfieldvalue = ChatInput.getText();
		    		try {
						updateAllChartRoom(USERNAME + ": " + inputtextfieldvalue);
					} catch (RemoteException | NotBoundException e1) {
						// TODO Auto-generated catch block
						System.out.println("cannot boadcasting chart to all clients");
						e1.printStackTrace();
					}
		    	}
		    });
		    frame.getContentPane().add(SendBtn);
		    
		    //connected user pannel
		    JScrollPane scrollPane_1 = new JScrollPane();
		    scrollPane_1.setBounds(135, 517, 542, 94);
		    frame.getContentPane().add(scrollPane_1);
		     
		    //connected users table
		    model = new DefaultTableModel();
		    model.addColumn("Connected Users");
		    table = new JTable(model);
		    table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);		 
		    table.getColumnModel().getColumn(0).setPreferredWidth(243);
		    model.setRowCount(0);
		    
		    model.addRow(new String[]{USERNAME});

		    JButton KickBtn = new JButton("Kick out");
		    KickBtn.setFont(new Font("SimSun", Font.PLAIN, 15));
		    KickBtn.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		
		    		if(table.getSelectedRow() != -1) {
		                // remove selected row from the model
		    			int row = table.getSelectedRow();
		    			if(row == 0) {
		    				JOptionPane.showMessageDialog(null, "You cannot kick youself out!");
		    			}else {
			    			String value = table.getModel().getValueAt(row, 0).toString();
			                model.removeRow(table.getSelectedRow());
							for(String user :Userslist) {
								try {
									/* remove this client from the activate client list for all*/
									IRemoteClient allclient = (IRemoteClient) Naming.lookup(RMIaddress + user);
									allclient.updateConnectedUsersByList(gettingallitems());
								}catch(MalformedURLException | RemoteException | NotBoundException e1) {
									
								}
							}
			                
			                try {
			                	//kick out the user( notifying
								terminateAnUser("     OOPS, you are kicked out",value);
								JOptionPane.showMessageDialog(null, "The user [  " + value +"  ] has been kicked out");
							} catch (RemoteException | NotBoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();	
							}
			            }
		    		}
		    	}
		    });
		    scrollPane_1.setRowHeaderView(KickBtn);
		    scrollPane_1.setViewportView(table);
		}
	  
	public void paintlist() {
		for(Shape s: shapelist) {
			s.addtoboard(drawArea.g2);
		}
		drawArea.repaint();
		
	}

	public void endthewhiteboard() throws RemoteException, NotBoundException {
		List<String> usercpylist = new ArrayList<String>(Userslist);
		for(String usercpy : usercpylist) {
			terminateAnUser("THE BOARD HAS BEEN SHUTTED DOWN BY THE MANAGER PLZ EXIT",usercpy);
		}
		System.exit(0); 
	}
	
	public void terminateAnUser(String info, String user) throws RemoteException, NotBoundException {
		IRemoteClient client;
		try {
			client = (IRemoteClient) Naming.lookup(this.RMIaddress + user);
			client.exitmessage(info);
		} catch (MalformedURLException | RemoteException | NotBoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Userslist.remove(user);
		try {
			Naming.unbind(this.RMIaddress + user);
			for(String alluser :Userslist) {
				try {
					IRemoteClient allclient = (IRemoteClient) Naming.lookup(this.RMIaddress + alluser);
					allclient.updateConnectedUsersByList(gettingallitems());
				}catch(MalformedURLException | RemoteException | NotBoundException e1) {
					
				}
			}
		} catch (RemoteException | NotBoundException | MalformedURLException e) {
			System.out.print("cann not ");
		}
		
	}
	public ArrayList<String> gettingallitems() {
		ArrayList<String> allusers = new ArrayList<String>();
		for(int i=0; i<model.getRowCount();i++) {
			String value = table.getModel().getValueAt(i, 0).toString();
			allusers.add(value);
		}
		return allusers;
		
	}
	public void addShapetoList(Shape s) throws RemoteException, NotBoundException {
		shapelist.add(s);
		boardcastingtoallboard(s);

	}
	
	public boolean canUseThisName(String s) throws RemoteException, NotBoundException{
		return !Userslist.contains(s) && !USERNAME.equals(s) ;
	} 
	
	  public void clear() {
		  shapelist = new ArrayList<>();
		  drawArea.g2.setPaint(Color.white);
		   // draw white on entire draw area to clear
		  drawArea.g2.fillRect(0, 0, drawArea.getSize().width, drawArea.getSize().height);
		  drawArea.g2.setPaint(Color.black);
		  drawArea.repaint();
	}
	
	@Override
	public synchronized Boolean RequestToJoin(String a) throws RemoteException, NotBoundException {
		if(Userslist.contains(a)) {
			return false;
		}else {
			int result = JOptionPane.showConfirmDialog(null, "Do you want " + a + " to join this drawboard");
			
			if (result == JOptionPane.YES_OPTION) {
				try {
					IRemoteClient client = (IRemoteClient) Naming.lookup(this.RMIaddress + a);
					model.addRow(new String[]{a});
					Userslist.add(a);
					/* add this client to the activate client list for all*/
					for(String user :Userslist) {
						try {
							IRemoteClient allclient = (IRemoteClient) Naming.lookup(this.RMIaddress + user);
							allclient.updateConnectedUsersByList(gettingallitems());
						}catch(MalformedURLException | RemoteException | NotBoundException e1) {
							
						}
					}
					//convert the picture to the binary then transfer
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					BufferedImage img = (BufferedImage) drawArea.getImage();
					try {
						ImageIO.write(img,"jpg", baos);
						 byte[] bytes = baos.toByteArray();
						 client.setimage(bytes);

					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println("cannot convert the image into bytes flow");
						e.printStackTrace();
					}
				} catch (MalformedURLException | RemoteException | NotBoundException e1) {
					// TODO Auto-generated catch block
					System.out.println("cannot let client join");
					e1.printStackTrace();
				}
				return true;	
			}
		return false;
		}
	}
	
	
	@Override
	public synchronized Boolean leave(String a) throws RemoteException {

		if(Userslist.contains(a)) {
			Userslist.remove(a);
			int row = 0;
			int i;
			//remove from the jtable
			System.out.println(a + " has left");
			for(i=0; i<model.getRowCount();i++) {
				String value = table.getModel().getValueAt(i, 0).toString();
				if(value.equals(a)) {
					row = i;
					break;
				}
			}
			model.removeRow(row);
			for(String user :Userslist) {
				try {
					/* remove this client to the activate client list for all*/
					IRemoteClient allclient = (IRemoteClient) Naming.lookup(this.RMIaddress + user);
					allclient.updateConnectedUsersByList(gettingallitems());
				}catch(MalformedURLException | RemoteException | NotBoundException e1) {
					
				}
			}
			try {
				//disable clients' name
				Naming.unbind(this.RMIaddress + a);
			} catch (RemoteException | NotBoundException | MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}
	
	@Override
	public void show() throws RemoteException {
		// TODO Auto-generated method stub
		
	}	
	@Override
	public void addtoboard(WhiteboardClient.Shape s) {
		addtoboard(s);
		
	}
	@Override
	public  synchronized  void boardcastingtoallboard(WhiteboardClient.Shape s) throws RemoteException, NotBoundException {
		// TODO Auto-generated method stub
		s.addtoboard(drawArea.g2);
		drawArea.repaint();
		for(String user :Userslist) {
			try {
				IRemoteClient client = (IRemoteClient) Naming.lookup(this.RMIaddress + user);
				client.addtoboard(s);
			} catch (MalformedURLException | RemoteException | NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("cannot boadcasting drawing to all boards");
			}
		}
	}
	
	@Override
	public synchronized void  boardcastingtoallboard(Shape s) throws RemoteException, NotBoundException {
		// TODO Auto-generated method stub
		s.addtoboard(drawArea.g2);
		drawArea.repaint();
		for(String user :Userslist) {
			IRemoteClient client;
			try {
				client = (IRemoteClient) Naming.lookup(this.RMIaddress + user);
				client.addtoboard(s);
			} catch (MalformedURLException | RemoteException | NotBoundException e) {
				System.out.println("cannot find clients'rmi address");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void cleanallboard() throws RemoteException, NotBoundException {
		clear();
		for(String user :Userslist) {
			IRemoteClient client;
			try {
				client = (IRemoteClient) Naming.lookup(this.RMIaddress + user);
				client.clear();
			} catch (MalformedURLException | RemoteException | NotBoundException e) {
				// TODO Auto-generated catch block
				System.out.println("cannot clean the clients' board");
				e.printStackTrace();
			}
		}
	}
	
	@Override
	//using an string to update all the chat room
	public void updateAllChartRoom(String string) throws RemoteException, NotBoundException {
		chatroom.append(string + "\n");
		for(String user :Userslist) {
			IRemoteClient client;
			try {
				client = (IRemoteClient) Naming.lookup(this.RMIaddress + user);
				client.addstringtochatRoom(string);
			} catch (MalformedURLException | RemoteException | NotBoundException e) {
				// TODO Auto-generated catch block
				System.out.println("cannot boadcasting chart to all clients");
				e.printStackTrace();
			}
		}
	}
}

