/* -------- IMPORTS ---------- */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;
import java.time.Instant;
import java.time.Duration;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineEvent;
/* -------------------------- */

/**
 * [DuberChatServer.java]
 * This program is the server side of the Duber Chat Assignment.
 * This program shows everything that the server will see, including the chat groups, the total users, and the server log.
 * The server log displays information of everything that has happened (joins, quits, messages, etc.)
 * The clients cannot see the server log, only the server can.
 * The server is able to send and see messages in chat groups, but cannot access private messages between two clients.
 * Click the "help" button to receive information on additional commands that you can use (/add, /approve, /remove, etc.)
 * Notifications will pop up if messages are sent in chat groups that you are not currently viewing.
 * @author Braydon Wang
 * @version 1.0, Dec 13, 2020
 */

class DuberChatServer {
  
  /* -------- JFrame ---------- */
  /** The frame that encompasses the entire server GUI */
  private JFrame serverFrame;
  /** The frame that encompasses the entire help option */
  private JFrame helpFrame;
  /** The frame that encompasses the entire notification pop-up */
  private JFrame notifFrame;
  
  /* -------- JSplitPane ---------- */
  /** The split pane that divides the chat text area and the input field */
  private JSplitPane splitPane; 
  /** The split pane that divides the chat groups and the total users */
  private JSplitPane sideBar;
  /** The split pane that divides the side bar and the chat log half */
  private JSplitPane splitPanel;
  /** The split pane that divides the chat group buttons and the chat group label */
  private JSplitPane groupsSplit;
  /** The split pane that divides the total user buttons and the total users label */
  private JSplitPane userSplit;
  
  /* -------- JPanel ---------- */
  /** The panel that holds the chat log label and the chat text area */
  private JPanel topPanel;      
  /** The panel that holds the chat text field, the send button and the help button */
  private JPanel bottomPanel;   
  /**The panel that holds the buttons of all the users */
  private JPanel userPanel;
  /** The panel that holds the buttons of all the chat groups */
  private JPanel chatGroupPanel;
  /** The panel that holds the input text field */
  private JPanel inputPanel;     
  /** The panel that holds the chat groups label */
  private JPanel groups;
  /** The panel that holds the total users label */
  private JPanel membersPanel;
  /** The panel that holds the help text area */
  private JPanel helpPanel;
  /** The panel that holds the notification */
  private JPanel notifPanel;
  
  /* -------- JScrollPane ---------- */
  /** The scroll pane that makes the chat text area scrollable */
  private JScrollPane scrollPane; 
  /** The scroll pane that makes the user buttons scrollable */
  private JScrollPane users;
  /** The scroll pane that makes the chat group buttons scrollable */
  private JScrollPane chatGroups;
  /** The scroll pane that makes the help text area scrollable */
  private JScrollPane helpScroll;
  
  /* -------- JTextArea ---------- */
  /** The text area for the help option that contains information on commands */
  private JTextArea helpTextArea;
  
  /* -------- JTextField ---------- */
  /** The text field that the server write messages and commands in */
  private JTextField textField; 
  
  /* -------- JButton ---------- */
  /** The send button to send messages */
  private JButton button;
  /** The help button to open up a new help frame */
  private JButton help;
  
  /* -------- JLabel ---------- */
  /** The chat log label */
  private JLabel chatLog;
  /** The chat groups label */
  private JLabel groupsLabel;
  /** The total users online label */
  private JLabel onlineLabel;
  /** The total members in the chat group label */
  private JLabel membersLabel;
  /** The notification label */
  private JLabel notifLabel;
  /** The notification title label */
  private JLabel notificationLabel;
  /** The notification chat group label */
  private JLabel notifGroup;
  
  /* ----------------  Colors ------------------ */
  private Color BEIGE = new Color(245,245,220);
  private Color BLUE = new Color(240,248,255);
  private Color GOLD = new Color(212,175,55);
  private Color BROWN = new Color(245,222,179);
  private Color YELLOW = new Color(255,250,205);
  /* ----------------------------------------- */
  
  /** The server socket that is needed to set up a port and accept client requests */
  ServerSocket serverSock;
  /** The boolean running variable that makes sure the server is continuously waiting for client input */
  static Boolean running = true;
  /** The queue data structure used to hold information of incoming and outgoing messages */
  static java.util.Queue<String> q = new java.util.LinkedList<String>();
  /** An arraylist that holds all of the chat groups that have been created, including the server log, which is always the first chat group */
  static ArrayList<ChatGroup> chats = new ArrayList<ChatGroup>();
  /** The index of the chat that the server is currently looking at */
  static int currentChat = 0;
  /** The instant that the notification first came out */
  static Instant begin = null;
  /** The instant that the program is checking for how long the notification has been displayed for */
  static Instant end = null;
  
  public static void main(String[] args) { 
    
    //calling the main function from the outer class
    new DuberChatServer().go();
    
  }
  
  /**
   * This method is the main running method that creates the server GUI and continuously accepts clients joining the port
   */
  
  public void go() {
    
    //intializing the server frame with the title
    serverFrame = new JFrame("Duber Chat Server");
    
    //initializing each split pane
    splitPane = new JSplitPane();
    groupsSplit = new JSplitPane();
    splitPanel = new JSplitPane();
    sideBar = new JSplitPane();
    userSplit = new JSplitPane();
    
    //the server frame exits when the close button is clicked
    serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    //intiializing the groups and members panel with their background colours
    groups = new JPanel();
    groups.setBackground(BEIGE);
    membersPanel = new JPanel();
    membersPanel.setBackground(BEIGE);
    
    //intializing each panel 
    topPanel = new JPanel();       
    bottomPanel = new JPanel();      
    userPanel = new JPanel();
    chatGroupPanel = new JPanel();
    
    //initializing each label with their text
    chatLog = new JLabel("Chat Log:                                                                                      ");
    groupsLabel = new JLabel("Chat groups: ");
    onlineLabel = new JLabel("Total users: ");
    membersLabel = new JLabel("Chat group members: ");
    
    //initializing each scroll pane
    scrollPane = new JScrollPane(); 
    users = new JScrollPane();
    chatGroups = new JScrollPane();
    
    //initializing the text field input area of the GUI
    inputPanel = new JPanel();
    textField = new JTextField();    
    button = new JButton("Send");   
    help = new JButton("Help");
    
    //adding an action listener to both buttons to perform an action when clicked
    help.addActionListener(new HelpButtonListener());
    button.addActionListener(new SendButtonListener());
    
    //setting the size of the frame, adding a window listener to know when the frame is closing, and setting the layout of the frame
    serverFrame.setPreferredSize(new Dimension(1000, 600));    
    serverFrame.addWindowListener(listener);
    serverFrame.getContentPane().setLayout(new GridLayout()); 
    serverFrame.getContentPane().add(splitPanel);
    
    //setting the orientation of each split pane, whether split horizontally or vertically
    splitPanel.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
    splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
    sideBar.setOrientation(JSplitPane.VERTICAL_SPLIT);
    groupsSplit.setOrientation(JSplitPane.VERTICAL_SPLIT);
    userSplit.setOrientation(JSplitPane.VERTICAL_SPLIT);
    
    //setting the divider location of each split pane
    splitPane.setDividerLocation(535);                  
    sideBar.setDividerLocation(300);
    splitPanel.setDividerLocation(350);
    
    //for each split pane, set the top and bottom, or left and right component with their respective panels
    splitPane.setTopComponent(topPanel);                
    splitPane.setBottomComponent(bottomPanel);      
    splitPanel.setLeftComponent(sideBar);
    splitPanel.setRightComponent(splitPane);
    sideBar.setTopComponent(groupsSplit);
    groupsSplit.setTopComponent(groups);
    groupsSplit.setBottomComponent(chatGroups);
    sideBar.setBottomComponent(userSplit);
    userSplit.setTopComponent(membersPanel);
    userSplit.setBottomComponent(users);
    
    //setting the layout of the top and bottom panel to be aligned vertically (on top of one another)
    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS)); 
    bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
    topPanel.setBackground(BLUE);
    bottomPanel.setBackground(BLUE);
    
    //setting the layout of the chat group to be one row, one column, for now
    chatGroupPanel.setLayout(new GridLayout(1,1));
    chats.add(new ChatGroup("Server Log",0));
    
    //for both groups and members panel, add their corresponding label
    groups.add(groupsLabel);
    membersPanel.add(onlineLabel);
    
    //for the top panel, add the chat log label and the scroll pane for the text area
    topPanel.add(chatLog);
    topPanel.add(scrollPane);      
    
    //set the viewport of the scroll pane to be the text area
    scrollPane.setViewportView(chats.get(0).textArea);   
    
    //for the bottom panel, add the input panel (text field) and set the view port of the users and chat groups to be the panels
    bottomPanel.add(inputPanel);            
    users.setViewportView(userPanel);
    chatGroups.setViewportView(chatGroupPanel);
    
    //set the maximum size of the text field to prevent it from being too large
    inputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));     
    inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
    
    //in the input panel, add the text field, the send button and the help button, all aligned horizontally
    inputPanel.add(textField);        
    inputPanel.add(button);           
    inputPanel.add(help);
    
    //unpack the server, set the location to be the center of the screen and make the frame visible
    serverFrame.pack();
    serverFrame.setLocationRelativeTo(null);
    serverFrame.setVisible(true);
    
    //start up of the server, waiting for clients to join
    chats.get(0).textArea.append("[INFO]: Waiting for a client connection..\n");
    
    //information of the clients who have joined
    Socket client = null;
    BufferedReader input;
    String userName = "";
    
    try {
      
      //setting the port of the server socket to be 5000
      serverSock = new ServerSocket(5000); 
      
      while (running) { 
        
        //accept the client and intialize input from them
        client = serverSock.accept();  
        InputStreamReader stream = new InputStreamReader(client.getInputStream());
        input = new BufferedReader(stream);
        
        //read in the username of the client and play a "join" sound effect
        userName = input.readLine();
        buttonSound("join.au");
        chats.get(0).textArea.append("[INFO]: " + userName + " has joined!\n");
        
        //the index of this username in the list of total users
        int index = contains(userName);
        
        //if the username is in the list of total users, set the client to online instead of making a completely new client
        if (index != -1) {

          //replace the current thread with the new thread
          chats.get(0).members.set(index,new ConnectionHandler(client,userName));
          
          //replace all objects of the offline client with the new object
          for (int i = 1; i < chats.size(); i++) {
            for (int j = 0; j < chats.get(i).members.size(); j++) {
              if (chats.get(i).members.get(j).getUserName().equals(userName)) {
                chats.get(i).members.set(j,chats.get(0).members.get(index));
              }
            }
          }
          
          userPanel = new JPanel();
          users.setViewportView(userPanel);
          userPanel.setLayout(new GridLayout(chats.get(currentChat).members.size(),1));
      
          //add every member of the chat group's button to the user panel
          for (int i = 0; i < chats.get(currentChat).members.size(); i++) {
            userPanel.add(chats.get(currentChat).members.get(i).button);
          }
      
          serverFrame.setVisible(true);
          
          String str = "[STATUS]online/" + userName;
          
          //for every client connected to the server
          for (ConnectionHandler c: chats.get(0).members) {
            
            //send the prefix status of the message, in this case, a client has went online
            c.sendMessage(str);
            //send the number of chat groups available in the server
            c.sendMessage(""+(chats.size()-1));
            
            //send the name of every chat group
            for (int i = 1; i < chats.size(); i++) {
              c.sendMessage(chats.get(i).name);
            }
            
            //count the number of chat groups that this user has joined
            int count = 0;
            for (int i = 1; i < chats.size(); i++) {
              for (ConnectionHandler a: chats.get(i).members) {
                if (a.getUserName().equals(userName)) {
                  count++;
                }
              }
            }
            c.sendMessage(""+count);
            
            //send the name of each chat group that the current user is in
            for (int i = 1; i < chats.size(); i++) {
              for (ConnectionHandler a: chats.get(i).members) {
                if (a.getUserName().equals(userName)) {
                  c.sendMessage(chats.get(i).name);
                }
              }
            }
            
            //send the total number of users in the server
            c.sendMessage(""+(chats.get(0).members.size()-1));
            //for every user in the server, send their personal information
            for (int i = 0; i < chats.get(0).members.size(); i++) {
              
              //the name of the user
              String name = chats.get(0).members.get(i).getUserName();
              
              //do not include themself in the list
              if (name.equals(userName)) {
                continue;
              }
              
              c.sendMessage(name);
              
              //the status of the user (online/offline)
              if (chats.get(0).members.get(i).online) {
                c.sendMessage("Online");
              } else {
                c.sendMessage("Offline");
              }
              
              //the number of chat groups that the user is in
              count = 0;
              for (int j = 1; j < chats.size(); j++) {
                for (ConnectionHandler a: chats.get(j).members) {
                  if (a.getUserName().equals(name)) {
                    count++;
                  }
                }
              }
              c.sendMessage(""+count);
              
              //the name of every chat group that the user is in
              for (int j = 1; j < chats.size(); j++) {
                for (ConnectionHandler a: chats.get(j).members) {
                  if (a.getUserName().equals(name)) {
                    c.sendMessage(chats.get(j).name);
                  }
                }
              }
            }
          }
          
          serverFrame.setVisible(true);
          
        //if the client that has joined is completely new to the server, create a new client
        } else {
          
          //create a new connection handler
          chats.get(0).members.add(new ConnectionHandler(client,userName));
          String str = "[STATUS]join/" + userName;
          
          //for every user in the server
          for (ConnectionHandler c: chats.get(0).members) {
            
            //send the prefix status of the message (new user has joined) and the total number of chat groups available
            c.sendMessage(str);
            c.sendMessage(""+(chats.size()-1));
            
            //the name of every chat group
            for (int i = 1; i < chats.size(); i++) {
              c.sendMessage(chats.get(i).name);
            }
            
            //the total number of users in the server
            c.sendMessage(""+(chats.get(0).members.size()-1));
            
            //send the personal information of every user in the server
            for (int i = 0; i < chats.get(0).members.size()-1; i++) {
              
              //the name of the user
              String name = chats.get(0).members.get(i).getUserName();
              c.sendMessage(name);
              
              //the user's status (online/offline)
              if (chats.get(0).members.get(i).online) {
                c.sendMessage("Online");
              } else {
                c.sendMessage("Offline");
              }
              
              //the number of chat groups their in
              int count = 0;
              for (int j = 1; j < chats.size(); j++) {
                for (ConnectionHandler a: chats.get(j).members) {
                  if (a.getUserName().equals(name)) {
                    count++;
                  }
                }
              }
              c.sendMessage(""+count);
              
              //the name of every chat group their in
              for (int j = 1; j < chats.size(); j++) {
                for (ConnectionHandler a: chats.get(j).members) {
                  if (a.getUserName().equals(name)) {
                    c.sendMessage(chats.get(j).name);
                  }
                }
              }
            }
          }
          
          //if the server is currently on the server log chat, display the user that has just joined
          if (currentChat == 0) {
            userPanel.setLayout(new GridLayout(chats.get(0).members.size(),1));
            userPanel.add(chats.get(0).members.get(chats.get(0).members.size()-1).button);
          }
        }
        //set the new changes made to the GUI to be visible
        serverFrame.setVisible(true);
      }
      
    //unable to accept a client connection
    } catch (Exception e) { 
      chats.get(0).textArea.append("[ERROR]: Error accepting connection\n");
      try {
        //close the client socket
        client.close();
      } catch (Exception e1) { 
        //could not close the client socket
        chats.get(0).textArea.append("[ERROR]: Failed to close socket\n");
      }
      //unsuccessfully terminate program
      System.exit(-1);
    }
  }
  
  /**
   * This method is used to go through all incoming messages from clients and dealing with them based on their status.
   * Depending on the purpose of the message, the server would use this information and send it to the appropriate clients.
   */
  
  public void message() {
    
    //for every incoming and outgoing message in the queue
    while (!q.isEmpty()) {
      
      //the current message
      String str = q.poll();
      
      //if the current message is a private message between two users
      if (str.indexOf("[PRIVATE]") == 0) {
        
        //the user who has sent the message and the one who receives it
        String userSent = str.substring(str.indexOf("]")+1,str.indexOf("/"));
        String userReceive = str.substring(str.indexOf("/")+1,str.indexOf(":"));
        
        //display what is happening to the server log
        chats.get(0).textArea.append("[MESSAGE]: " + userSent + " has sent a private message to " + userReceive + "\n");
        
        //send the message to all users in the server
        for (ConnectionHandler c: chats.get(0).members) {
          c.sendMessage(str);
        }
        
      //if the current message is a group message in a chat group
      } else if (str.indexOf("[GROUP]") == 0) {
        
        //the user who has sent the message, the index of the chat group and the message itself
        String userSent = str.substring(str.indexOf("]")+1,str.indexOf("/"));
        int index = Integer.parseInt(str.substring(str.indexOf("/")+1,str.indexOf(":")))+1;
        String msg = str.substring(str.indexOf(":") + 1);
        
        //display what is happenening in the server log and in the specified chat group
        chats.get(0).textArea.append("[MESSAGE]: " + userSent + " has sent a message to the chat group \"" + chats.get(index).name + "\"\n");
        chats.get(index).textArea.append(userSent + ": " + msg + "\n");
        
        //send the message to all users
        for (ConnectionHandler c: chats.get(0).members) {
          c.sendMessage(str);
        }
        
        //if no notification is currently displayed and the server is on a different chat group, send a notification
        if (currentChat != index && begin == null) {
          begin = Instant.now();
          buttonSound("notification.wav");
          notification(userSent, chats.get(index).name);
        }
        
      //if the message is a status information (joins, quits, etc.)
      } else if (str.indexOf("[STATUS]") == 0) {
        
        //a user has left the server/went offline
        if (str.indexOf("offline") == 8) {
          
          //the name of the user who has left
          String name = str.substring(str.indexOf("/")+1);
          chats.get(0).textArea.append("[INFO]: " + name + " has left the server!\n");
          
          //changing that specific user's status to be offline
          for (ConnectionHandler c: chats.get(0).members) {
            if (c.getUserName().equals(name)) {
              c.changeOffline();
              break;
            }
          }
          
          //send the message to every user
          for (ConnectionHandler c: chats.get(0).members) {
            c.sendMessage(str);
          }
          
        //a user has timedout from idling for too long
        } else if (str.indexOf("timeout") == 8) {
          
          //the name of the user who has timedout
          String name = str.substring(str.indexOf("/")+1);
          chats.get(0).textArea.append("[INFO]: " + name + " was timed-out from the server!\n");
          
          //change the user's status to be offline
          for (ConnectionHandler c: chats.get(0).members) {
            if (c.getUserName().equals(name)) {
              c.changeOffline();
              break;
            }
          }
          
          //send the message to every user
          for (ConnectionHandler c: chats.get(0).members) {
            c.sendMessage(str);
          }
          
        //the server has shut down so every client needs to be disconnected from the server
        } else if (str.indexOf("closing") == 8) {
          for (ConnectionHandler c: chats.get(0).members) {
            c.sendMessage(str);
          }
        }
        
      //the message falls under one of the commands that the server can user
      } else if (str.indexOf("[COMMAND]") == 0) {
        
        //a user is requesting to join a chat group
        if (str.indexOf("request") == 9) {
          
          //the name of the user and the index of the chat group
          String name = str.substring(str.indexOf("/") + 1,str.indexOf(":"));
          int index = Integer.parseInt(str.substring(str.indexOf(":")+1))+1;
          
          //display the situation in the server log and the chat group
          chats.get(0).textArea.append("[REQUEST]: " + name + " has requested to join the chat group \"" + chats.get(index).name + "\"\n");
          chats.get(index).textArea.append(name + " has requested to join the chat group!\n");
          
          //if the user is not in the pending list, add the user to it
          for (ConnectionHandler c: chats.get(0).members) {
            if (c.getUserName().equals(name) && !chats.get(index).pending.contains(c)) {
              chats.get(index).pending.add(c);
              break;
            }
          }
          
        //if the message is for one of the other four commands, send the message to all users
        } else {
          for (ConnectionHandler c: chats.get(0).members) {
            c.sendMessage(str);
          }
        }
        
      //the message was sent by the server
      } else {
        
        //the index of the chat group and the message sent
        int idx = Integer.parseInt(str.substring(8,str.indexOf("/")));
        String msg = str.substring(str.indexOf("/")+1);
        
        //display the message in the specific chat grou
        chats.get(currentChat).textArea.append("Server: " + msg + "\n");
        
        //if the chat group is not the server log, send the message to all users
        if (idx != -1) {
          for (ConnectionHandler c: chats.get(0).members) {
            c.sendMessage(str);
          }
        }
      }
    }
  }
  
  /**
   * This method checks if the specified user is already in the server, if so, it returns the index, otherwise, it returns -1
   * @param userName  the username of the specified user
   * @return the index of the specified user in the list, otherwise -1
   */
  
  public int contains(String userName) {
    //the server log chat
    ChatGroup temp = chats.get(0);
    
    //check in the list of total users if the username is in the list
    for (int i = 0; i < temp.members.size(); i++) {
      if (temp.members.get(i).getUserName().equals(userName)) {
        return i;
      }
    }
    
    //otherwise, return -1
    return -1;
  }
  
  /**
   * This method plays an audio file if a button is clicked from the file "button.wav".
   */
  
  public void buttonSound(String fileName) {
    
    try {
      //intializing the audio file, the audio input stream, the data line and the clip of the audio system
      File audioFile = new File(fileName);
      AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
      DataLine.Info info = new DataLine.Info(Clip.class, audioStream.getFormat());
      Clip clip = (Clip) AudioSystem.getLine(info);
      
      //add the sound listener and open the audio stream to start the clip
      clip.addLineListener(new SoundListener());
      clip.open(audioStream);
      clip.start();
      
    //could not find the indicated audio file
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  /**
   * This class is an inner class of the main duber chat server.
   * It implements from the line listener class to determine when to stop playing audio files.
   * @author Braydon Wang
   * @version 1.0, Dec 13, 2020
   */
  
  class SoundListener implements LineListener {
    
    /**
     * This method stops playing the audio file when it reaches the end.
     * @param event the line event used to check the status of the audio file
     */
    
    public void update(LineEvent event) {
      //stop the event if the audio reaches the end
      if (event.getType() == LineEvent.Type.STOP) {
        event.getLine().close();
      }
    }
  }
  
  /**
   * This method displays a notification at the top right of the screen if a message is sent in a chat group.
   * @param userName the name of the user who sent the message
   * @param chatgroup the name of the chat group where the message was sent
   */
  
  public void notification(String userName, String chatgroup) {
    
    //initializing the notification frame with the size and its default close operation (closes the frame, but does not exit the program)
    notifFrame = new JFrame();
    notifFrame.setSize(350,85);
    notifFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
    //initializing the notification panel with the background colour and layout
    notifPanel = new JPanel();
    notifPanel.setBackground(YELLOW);
    notifPanel.setLayout(new GridLayout(3,1));
    
    //creating the three notification labels
    notificationLabel = new JLabel("NOTIFICATION:");
    notifLabel = new JLabel(userName + " has sent a message in chat group: ");
    notifGroup = new JLabel(chatgroup);
    
    //displaying the label at the center of the frame
    notificationLabel.setFont(new Font("Serif", Font.BOLD, 18));
    notificationLabel.setHorizontalAlignment(JLabel.CENTER);
    notifLabel.setHorizontalAlignment(JLabel.CENTER);
    notifGroup.setHorizontalAlignment(JLabel.CENTER);
    
    //intializing the graphics environment and the graphics device to find dimensions of screen
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
    
    //adding the labels to the panel
    notifPanel.add(notificationLabel);
    notifPanel.add(notifLabel);
    notifPanel.add(notifGroup);
    notifFrame.add(notifPanel);
    
    //setting the location of the notification to the top right of the screen depending on the dimensions of the screen
    notifFrame.setLocation((int)(defaultScreen.getDefaultConfiguration().getBounds().getMaxX() - notifFrame.getWidth()),0);
    notifFrame.setVisible(true);
  }
  
  /**
   * This class is an inner class of the main duber chat server.
   * It contains all of the major information of each client, including a thead that repeatedly runs.
   * Every client has a username, a socket, a button, and a timer that checks the last time the user has sent a message.
   * @author Braydon Wang
   * @version 1.0, Dec 13, 2020
   */
  
  class ConnectionHandler implements Runnable, ActionListener {
    
    /** The printwriter variable used to output to the client */
    private PrintWriter output;
    /** The buffered reader used to read input from the client */
    private BufferedReader input;
    /** The socket of the client used to initialize input and output */
    private Socket client;
    /** The boolean variable used to continuously loop for input from the client */
    private boolean running;
    /** The client thread that repeatedly runs */
    private Thread t;
    /** The username of the client */
    private String userName;
    /** The client's button that shows up on the side bar */
    private JButton button;
    /** The status of the client (online/offline) */
    private boolean online;
    /** The instant that the client first messaged */
    private Instant start;
    /** The instant to check if the client has timedout */
    private Instant finish;
    /** The duration of time between the start and finish */
    private long duration;
    
    /**
     * Creates an object from the ConnectionHandler class.
     * @param s the socket of the client
     * @param userName the username of the client
     */
    
    ConnectionHandler(Socket s, String userName) {
      
      //creating the thread
      t = new Thread(this);
      
      //initializing the button displayed at the side bar
      button = new JButton(userName);
      button.addActionListener(this);
      button.setBackground(Color.GREEN);
      button.setOpaque(true);
      
      this.client = s;
      this.userName = userName;
      this.online = true;
      
      //initializing the input and output for the client
      try {
        this.output = new PrintWriter(client.getOutputStream());
        InputStreamReader stream = new InputStreamReader(client.getInputStream());
        this.input = new BufferedReader(stream);
      } catch (IOException e) {
        e.printStackTrace();
      }
      
      //start the timer and start running the thread
      running = true;
      start = Instant.now();
      t.start();
    }
    
    /**
     * This method returns the username of the client
     * @return the username of the client
     */
    
    public String getUserName() {
      return this.userName;
    }
    
    /**
     * This method changes the client to a status of online
     */
    
    public void changeOnline() {
      //change online to be true
      this.online = true;
      //set the background of the button to be green instead
      button.setBackground(Color.GREEN);
      button.setOpaque(true);
      serverFrame.setVisible(true);
    }
    
    /**
     * This method changes the client to a status of offline
     */
    
    public void changeOffline() {
      //change online to be true
      this.online = false;
      //set the background of the button to be red instead
      button.setBackground(Color.RED);
      button.setOpaque(true);
      serverFrame.setVisible(true);
    }
    
    /**
     * This method displays the basic profile of the client if their button has been clicked
     * @param e the action event used to determine if the button has been pressed
     */
    
    public void actionPerformed(ActionEvent e) {
      //play the button clicked sound
      buttonSound("button.wav");
      
      //display the personal information of the user selected
      chats.get(0).textArea.append("------------------------------\n");
      //their username
      chats.get(0).textArea.append("Username: " + this.userName + "\n");
      
      //their status
      if (this.online) {
        chats.get(0).textArea.append("Status: Online\n");
      } else {
        chats.get(0).textArea.append("Status: Offline\n");
      }
      
      //the chat groups that they have joined
      chats.get(0).textArea.append("Chat groups joined: ");
      
      //the names of all the chat groups that they have joined
      boolean first = true;
      for (int i = 1; i < chats.size(); i++) {
        for (ConnectionHandler c: chats.get(i).members) {
          if (c.getUserName().equals(this.userName)) {
            if (first) {
              chats.get(0).textArea.append(chats.get(i).name);
              first = false;
              
            } else {
              chats.get(0).textArea.append(", " + chats.get(i).name);
            }
          }
        }
      }
      
      //when they sent their last message
      chats.get(0).textArea.append("\nLast message sent: " + (duration/1000) + " seconds ago\n");
      chats.get(0).textArea.append("------------------------------\n");
    }
    
    /**
     * This method is the thead's run method that continuously runs to wait for input from the client.
     * It also checks to see if the client should be timedout or not, depeding on the time their last message was sent.
     */
    
    public void run() {
      String msg = "";
      
      while (running) {
        
        try {
          
          //finding the total elapsed time since their last message sent
          finish = Instant.now();
          duration = Duration.between(start, finish).toMillis();
          
          //if they have not sent a message for two minutes, disconnect the client from the server
          if (this.online && duration >= 120000) {
            q.add("[STATUS]timeout/" + this.userName);
            message();
          }
          
          //if a notification is currently being displayed, update its duration
          if (begin != null) {
            end = Instant.now();
            
            //if the notification has been displayed for more than 2.5 seconds, close it
            if (Duration.between(begin, end).toMillis() >= 2500) {
              notifFrame.dispatchEvent(new WindowEvent(notifFrame, WindowEvent.WINDOW_CLOSING));
              begin = null;
              end = null;
            }
          }
          
          //read input from the client and add it to the queue to be up for processing
          if (input.ready()) {
            msg = input.readLine();
            start = Instant.now();
            q.add(msg);
            message();
          }
         
        //catch for input that could not be collected from the client
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      
      //close all input, output and client socket
      try {
        input.close();
        output.close();
        client.close();
      //could not close the socket
      } catch (Exception e) { 
        chats.get(0).textArea.append("[ERROR]: Failed to close socket\n");
      }
    }
    
    /**
     * This method sends a mesage to the client with the specified message
     * @param msg the message that should be sent to the client
     */
    
    public void sendMessage(String msg) {
      output.println(msg);
      output.flush();
    }
  }
  
  /**
   * This class is an inner class of the main duber chat server.
   * It contains all of the major information of each chat group, including the name, the members, the text area and its corresponding button.
   * Every chat group also has a list of people who have requested to join, a list of people who are banned and those who are blocked.
   * @author Braydon Wang
   * @version 1.0, Dec 13, 2020
   */
  
  class ChatGroup implements ActionListener {
    
    /** A list of members of the chat group */
    private ArrayList<ConnectionHandler> members = new ArrayList<ConnectionHandler>();
    /** A list of people who have requested to join the chat group */
    private ArrayList<ConnectionHandler> pending = new ArrayList<ConnectionHandler>();
    /** A list of people who are banned from the group */
    private ArrayList<ConnectionHandler> banned = new ArrayList<ConnectionHandler>();
    /** A list of people who are blocked from the group */
    private ArrayList<ConnectionHandler> blocked = new ArrayList<ConnectionHandler>();
    /** The name of the chat group */
    private String name;
    /** The chat group's button displayed on the side bar */
    private JButton button;
    /** The chat group's corresponding text area */
    private JTextArea textArea;
    /** The index of the chat group in the list of chat groups available */
    private int index;
    
    /**
     * Creates an object from the ChatGroup class.
     * @param name the name of the chat group
     * @param index the index of the chat group in the chat list
     */
    
    ChatGroup(String name, int index) {
      //initializing the chat groups name and index
      this.name = name;
      this.index = index;
      
      //creating their own text area and make it uneditable
      textArea = new JTextArea();
      textArea.setEditable(false);
      
      //create the chat group's own button displayed at the side bar
      button = new JButton(name);
      button.addActionListener(this);
      button.setBackground(GOLD);
      button.setOpaque(true);
      
      chatGroupPanel.add(button);
    }
    
    /**
     * This method checks to see if the chat group's button has been clicked.
     * If so, it replaces the text area and displays all of the members of the chat group.
     * @param e the action event that sees the status of the button
     */
    
    public void actionPerformed(ActionEvent e) {
      //play the button clicked sound
      buttonSound("button.wav");
      //set the current chat that the server is on to be the index of the chat group
      currentChat = this.index;
      
      //create a new members panel
      membersPanel = new JPanel();
      membersPanel.setBackground(BEIGE);
      
      userSplit.setTopComponent(membersPanel);
      
      //depending on the chat, set the label to either be total online users or members of the chat group
      if (currentChat == 0) {
        membersPanel.add(onlineLabel);
      } else {
        membersPanel.add(membersLabel);
      }
      
      //set the chat group's text area to be the text area visible
      scrollPane.setViewportView(textArea);
      
      //create a new user panel on the side bar
      userPanel = new JPanel();
      users.setViewportView(userPanel);
      userPanel.setLayout(new GridLayout(members.size(),1));
      
      //add every member of the chat group's button to the user panel
      for (int i = 0; i < members.size(); i++) {
        userPanel.add(members.get(i).button);
      }
      
      serverFrame.setVisible(true);
    }
  }
  
  /**
   * This class is an inner class of the main duber chat server.
   * It implements from the action listener class to check if the send button has been clicked.
   * If so, depending on what is in the text field, the class would carry out the appropriate commands.
   * @author Braydon Wang
   * @version 1.0, Dec 13, 2020
   */
  
  class SendButtonListener implements ActionListener { 
    
    /**
     * This method checks to see if the send button has been clicked.
     * If so, it either sends a message to the chat group or carries out the corresponding server command.
     * @param event the action event that sees the status of the button
     */
    
    public void actionPerformed(ActionEvent event)  {
      
      //the current message in the text field
      String str = textField.getText();
      
      //the server wants to create a new chat group
      if (str.indexOf("/add ") == 0) {
        
        //create a new chat group with the specified name and display it in the server log
        chats.add(new ChatGroup(str.substring(5),chats.size()));
        chats.get(0).textArea.append("[SERVER]: Created a new chat group named " + str.substring(5) + "\n");
        
        //send the chat group's name to all of the users in the server
        for (ConnectionHandler c: chats.get(0).members) {
          c.sendMessage("[STATUS]group/" + str.substring(5));
        }
        
        //update the chat group panel layout to have one more row
        chatGroupPanel.setLayout(new GridLayout(chats.size(),1));
        serverFrame.setVisible(true);
        
      //the server wants to approve a request to join a chat group
      } else if (str.indexOf("/approve ") == 0) {
        
        //check if the user is part of the pending list
        for (ConnectionHandler c: chats.get(currentChat).pending) {
          
          //if the username matches and the user is not banned from the chat group
          if (c.getUserName().equals(str.substring(9)) && !chats.get(currentChat).banned.contains(c)) {
            
            //display the situation in the server log and the chat group
            chats.get(currentChat).textArea.append(c.getUserName() + " has joined the chat group!\n");
            chats.get(0).textArea.append("[APPROVE]: " + c.getUserName() + " has been approved to join the chat group \"" + chats.get(currentChat).name + "\"\n");
            //add the newly approved user to the chat group
            chats.get(currentChat).members.add(c);
            //remove the user from the pending list
            chats.get(currentChat).pending.remove(c);
            
            //create a new user panel with one additional row
            userPanel = new JPanel();
            users.setViewportView(userPanel);
            userPanel.setLayout(new GridLayout(chats.get(currentChat).members.size(),1));
            
            //add all of the buttons from the chat group to the user panel
            for (ConnectionHandler a: chats.get(currentChat).members) {
              userPanel.add(a.button);
            }
            
            //send the message with the appropriate prefix statement
            q.add("[COMMAND]approve/" + c.getUserName() + ":" + (currentChat-1));
            message();
            break;
          }
        }
        
      //the server wants to remove a user from a chat group
      } else if (str.indexOf("/remove ") == 0) {
        
        //the name of the user
        String name = str.substring(8);
        
        //check if the specified user is a member of the chat group
        for (ConnectionHandler c: chats.get(currentChat).members) {
          if (c.getUserName().equals(name)) {
            
            //remove them from the chat group and display it in the server log and the chat group
            chats.get(currentChat).textArea.append(name + " has been removed from the chat group!\n");
            chats.get(0).textArea.append("[REMOVE]: " + name + " has been removed from the chat group \"" + chats.get(currentChat).name + "\"\n");
            chats.get(currentChat).members.remove(c);
            
            //remove the user if they are also in the blocked list
            if (chats.get(currentChat).blocked.contains(c)) {
              chats.get(currentChat).blocked.remove(c);
            }
            
            //create a new user panel
            userPanel = new JPanel();
            users.setViewportView(userPanel);
            userPanel.setLayout(new GridLayout(chats.get(currentChat).members.size(),1));
            
            //add the buttons of the members of the chat group to the user panel
            for (ConnectionHandler a: chats.get(currentChat).members) {
              userPanel.add(a.button);
            }

            serverFrame.setVisible(true);
            
            //send the message with the appropriate prefix statements
            q.add("[COMMAND]remove/" + c.getUserName() + ":" + (currentChat-1));
            message();
            break;
          }
        }
        
      //the server wants to ban a user from a chat group (removed and unable to request to join)
      } else if (str.indexOf("/ban ") == 0) {
        
        //the name of the user
        String name = str.substring(5);
        
        //check if the user is a member of the chat group
        for (ConnectionHandler c: chats.get(currentChat).members) {
          if (c.getUserName().equals(name)) {
            
            //remove the user from the chat group, add them to the banned list and display it to the server log and chat group
            chats.get(currentChat).textArea.append(name + " has been banned from the chat group!\n");
            chats.get(0).textArea.append("[BAN]: " + name + " has been banned from the chat group \"" + chats.get(currentChat).name + "\"\n");
            chats.get(currentChat).members.remove(c);
            chats.get(currentChat).banned.add(c);
            
            //remove the user if they are also in the blocked list
            if (chats.get(currentChat).blocked.contains(c)) {
              chats.get(currentChat).blocked.remove(c);
            }
            
            //create a new user panel
            userPanel = new JPanel();
            users.setViewportView(userPanel);
            userPanel.setLayout(new GridLayout(chats.get(currentChat).members.size(),1));
            
            //add the buttons of the members of the chat group to the user panel
            for (ConnectionHandler a: chats.get(currentChat).members) {
              userPanel.add(a.button);
            }
            
            serverFrame.setVisible(true);
            
            //send the message with the appropriate prefix statements
            q.add("[COMMAND]ban/" + c.getUserName() + ":" + (currentChat-1));
            message();
            break;
          }
        }
        
      //the server wants to block a user from a chat group (still in the chat group, but cannot send messages)
      } else if (str.indexOf("/block ") == 0) {
        
        //the name of the user
        String name = str.substring(7);
        
        //check if the user is a member of the chat group and that they are not in the banned list
        for (ConnectionHandler c: chats.get(currentChat).members) {
          if (c.getUserName().equals(name) && !chats.get(currentChat).blocked.contains(name)) {
            
            //add the user to the blocked list and display it to the server log and the chat group
            chats.get(currentChat).textArea.append(name + " has been blocked from the chat group!\n");
            chats.get(0).textArea.append("[BLOCK]: " + name + " has been blocked from the chat group \"" + chats.get(currentChat).name + "\"\n");
            chats.get(currentChat).blocked.add(c);
            
            //send the message with the appropriate prefix statements
            q.add("[COMMAND]block/" + c.getUserName() + ":" + (currentChat-1));
            message();
          }
        }
        
      //the server has sent a message to the chat group
      } else {
        if (str.length() > 0) {
          //send the message with the prefix statement "[SERVER]" to all of the clients
          String msg = "[SERVER]" + (currentChat-1) + "/" + str;
          q.add(msg);
          message();
        }
      }
      
      //delete everything from the text field
      textField.setText("");
    }     
  }
  
  /**
   * This class is an inner class of the main duber chat server.
   * It implements from the action listener class to check if the help button has been clicked.
   * If so, it displays the help frame that contains useful information of what the server can do.
   * @author Braydon Wang
   * @version 1.0, Dec 13, 2020
   */
  
  class HelpButtonListener implements ActionListener { 
    
    /**
     * This method checks to see if the help button has been clicked.
     * If so, it displays the GUI of the help frame.
     * @param e the action event that sees the status of the help button
     */
    
    public void actionPerformed(ActionEvent event)  {
      
      //initialize a help frame with the title "Help"
      helpFrame = new JFrame("Help");
      helpPanel = new JPanel();
      
      //set a layout for the panel and initialize the scroll pane and the text area
      helpPanel.setLayout(new BoxLayout(helpPanel,BoxLayout.Y_AXIS));
      helpPanel.setBackground(BROWN);
      helpScroll = new JScrollPane();
      helpTextArea = new JTextArea();
      
      //disallow editing of the text area and make sure the frame is disposed when closed
      helpTextArea.setEditable(false);
      helpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      
      //set the size and layout of the frame 
      helpFrame.setPreferredSize(new Dimension(300, 300));
      helpFrame.getContentPane().setLayout(new GridLayout());
      helpFrame.getContentPane().add(helpPanel);
      
      //add the scroll pane and the text area to the panel
      helpPanel.add(helpScroll);
      helpScroll.setViewportView(helpTextArea);
      
      //display the information on the text area (general information, what the server can do, commands to use, etc.)
      helpTextArea.append("1. The server log displays everything that is \nhappening (messages, commands, etc.) in \nreal time. It is not a chat group itself, it is \nonly for the server.\n\n");
      helpTextArea.append("2. The server can see messages in chat \ngroups, but cannot see in private messages.\n\n");
      helpTextArea.append("3. The side bar shows all the chat groups \nthat are currently on the server, as well as \nall the users that have joined.\n\n");
      helpTextArea.append("4. Red outlined users are currently offline, \nwhile green outlined users are online.\n\n");
      helpTextArea.append("5. You can click on the users to see their \nstats, which will appear in the server log.\n\n");
      helpTextArea.append("6. Users will be timedout/disconnected if \nthey have not sent a message in 2 minutes \ntime.\n\n");
      helpTextArea.append("7. List of commands to use by typing in the \ntext field:\n\n");
      helpTextArea.append("  i.  /add chatName   --> creates a chat \ngroup with the specified name\n\n");
      helpTextArea.append("  ii.  /approve userName   --> accepts the \nusers request to join a chat group (must \ntype this command in the specific chat \ngroup)\n\n");
      helpTextArea.append("  iii.  /remove userName   --> remove the \nuser from a chat group (must type this \ncommand in the specific chat group)\n\n");
      helpTextArea.append("  iv.  /ban userName   --> bans the user \nfrom a chat group and is unable to request \nto join the chat group (must type this \ncommand in the specific chat group)\n\n");
      helpTextArea.append("  v.  /block userName   --> blocks the user \nfrom a chat group, can see messages, but \ncannot send messages (must type this \ncommand in the specific chat group)\n");
      
      //start the text area at the very top of the scroll pane
      helpTextArea.setCaretPosition(0);
      
      //unpack the frame, set the frame at the center of the screen and make the frame visible
      helpFrame.pack();
      helpFrame.setLocationRelativeTo(null);
      helpFrame.setVisible(true);
    }
  }
  
  /**
   * This class is an inner class of the main duber chat server.
   * It checks to see if the server frame has been closed.
   * @author Braydon Wang
   * @version 1.0, Dec 13, 2020
   */
  
  WindowListener listener = new WindowAdapter() {
    
    /**
     * This method checks to see if the window is currently closing.
     * If so, it tells all the clients to shut down their program as well.
     * @param e the window event that sees the status of the server frame
     */
    
    public void windowClosing(WindowEvent evt) {
      //send a closing status message to all clients
      String str = "[STATUS]closing";
      q.add(str);
      message();
    }
    
  };
}