/*------------ IMPORTS -------------*/
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Component;
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
/*----------------------------------*/

/* [ChatClient.java]
 * An implementation of a complex chat client
 * This program displays the client side of social networking chat
 * The client is able to view other users and available chat groups
 * Using private messaging and groupchats, the client can reach out to other users
 * @author Dylan Wang
 * @version 1.0, Dec 13, 2020
 */

class ChatClient{
  
  /** Stores the current duber chat client */
  static ChatClient chat;
  
  /*------------ JFrame -------------*/
  /** The frame that holds the chat screen */
  private JFrame window;
  /** The frame that holds the login screen */
  private JFrame setting;
  /** The frame that holds the menu screen */
  private JFrame menu;
  /** The frame that holds the ask to join screen */
  private JFrame askFrame;
  /** The frame that holds the user is banned screen */
  private JFrame banFrame;
  /*---------------------------------*/
  
  /*------------ JSplitPane -------------*/
  /** The split pane that divides chat information and chat content on the chat screen */
  private JSplitPane windowS;
  /** The split pane that divides the message area and the textfield on the chat screen */
  private JSplitPane windowS2;
  /** The split pane that divides user information and the rest of the menu */
  private JSplitPane lrgSplitPane;
  /** The split pane that divides chatgroups and users on the menu screen */
  private JSplitPane bigSplitPane;
  /** The split pane that divides the chat group title and the list of chat groups on the menu screen */
  private JSplitPane medSplitPane1;
  /** The split pane that divides the user title and the list of users on the menu screen */
  private JSplitPane medSplitPane2;
  /** The split pane that divides online users and offline users on the menu screen */
  private JSplitPane avgSplitPane;
  /** The split pane that divides the online title and the list of online users on the menu screen */
  private JSplitPane smlSplitPane1;
  /** The split pane that divides the offline title and the list of offline users on the menu screen */
  private JSplitPane smlSplitPane2;
  /*-------------------------------------*/
  
  /*------------ JSplitPane -------------*/
  /** The scroll pane that allows the chat message area to be scrollable */
  private JScrollPane msgAreaS;
  /** The scroll pane that allows the chat group list to be scrollable */
  private JScrollPane chatgroupsS;
  /** The scroll pane that allows the online user list to be scrollable */
  private JScrollPane onlineS;
  /** The scroll pane that allows the offline user list to be scrollable */
  private JScrollPane offlineS;
  /*-------------------------------------*/
  
  /*------------ JPanel -------------*/
  /** The panel that holds chat information on the chat screen */
  private JPanel windowP;
  /** The panel that holds the message area on the chat screen */
  private JPanel windowP2;
  /** The panel that holds text field and send button on the chat screen */
  private JPanel windowP3;
  /** The panel that holds the login labels and textfields on the login screen */
  private JPanel settingP;
  /** The panel that holds the user information on the menu screen*/
  private JPanel userInfo;
  /** The panel that holds the chat group title on the menu screen */
  private JPanel chatgroupsP1;
  /** The panel that holds the chat group list on the menu screen */
  private JPanel chatgroupsP2;
  /** The panel that holds the user title on the menu screen */
  private JPanel usersP1;
  /** The panel that holds the user list on the menu screen */
  private JPanel usersP2;
  /** The panel that holds the online title on the menu screen */
  private JPanel onlineP1;
  /** The panel that holds the online user list on the menu screen */
  private JPanel onlineP2;
  /** The panel that holds the offline title on the menu screen */
  private JPanel offlineP1;
  /** The panel that holds the offline user list on the menu screen */
  private JPanel offlineP2;
  /** The panel that holds ask label and option buttons on the ask to join screen */
  private JPanel askPanel;
  /** The panel that holds ban label and acknowledgement button on the user is banned screen */
  private JPanel banPanel;
  /*---------------------------------*/
  
  /*------------ JButton -------------*/
  /** The button that allows users to send messages */
  private JButton sendB;
  /** The button that allows users to close the current chat */
  private JButton closeB;
  /** The button that allows users to enter login information */
  private JButton okB;
  /** The button that allows users to request to join a chat group */
  private JButton yesButton;
  /** The button that allows users to close the ask to join screen */
  private JButton noButton;
  /** The button that allows users to close the user is banned screen */
  private JButton sadButton;
  /** The button that allows users to exit the program */
  private JButton quitButton;
  /*----------------------------------*/
  
  /*------------ JLabel -------------*/
  /** The chat name label */
  private JLabel chatName;
  /** The number of participants label */
  private JLabel chatSize;
  /** The name of the current user label */
  private JLabel currUser;
  /** The status of the current user label */
  private JLabel currStatus;
  /** The ip address label */
  private JLabel ipAddressL;
  /** The port number label */
  private JLabel portL;
  /** The username label */
  private JLabel userNameL;
  /** The users own information */
  private JLabel userInfoL;
  /** The chatgroup title label */
  private JLabel chatgroupsL;
  /** The user title label */
  private JLabel usersL;
  /** The online title label */
  private JLabel onlineL;
  /** The offline title label */
  private JLabel offlineL;
  /** The ask to join chatgroup label */
  private JLabel askLabel;
  /** The user is banned label */
  private JLabel banLabel;
  /*---------------------------------*/
  
  /*------------ JTextArea -------------*/
  /** The list of all the group chat message areas */
  private ArrayList<JTextArea> msgAreaG = new ArrayList<JTextArea>();
  /** The list of all the private message areas */
  private ArrayList<JTextArea> msgAreaU = new ArrayList<JTextArea>(); 
  /*------------------------------------*/
  
  /*------------ JTextField -------------*/
  /** The text field where the user types messages to send to other users */
  private JTextField typeT;
  /** The text field where the user types in the ip address */
  private JTextField ipAddressT;
  /** The text field where the user types in the port number */
  private JTextField portT;
  /** The text field where the user types in their username */
  private JTextField userNameT;
  /*-------------------------------------*/
  
  /*------------ Server I/O -------------*/
  /** The socket that is required to connect with the server for input and output */
  private Socket mySocket; 
  /** The reader for the network stream */
  private BufferedReader input;
  /** The printwriter for the network stream */
  private PrintWriter output;  
  /** Controls the input stream from the server */
  private boolean running = false; 
  /*-------------------------------------*/
  
  /*------------- Colors -------------*/
  private Color GREY = new Color(201,201,201);
  private Color GREEN = new Color(149,249,133);
  private Color RED = new Color(255,163,147);
  private Color BLUE = new Color(42, 157, 244);
  private Color LIGHT_BLUE = new Color(151, 203, 255);
  private Color BRIGHT_GREEN = new Color(102, 255, 0);
  /*----------------------------------*/
  
  /*------------ Login Information ------------*/
  /** Holds the ip address to connect with the server */
  private String ipAddress;
  /** Holds the port number to connect with the server */
  private int portNo;
  /** Holds the username of the user */
  private String personalName;
  /*-------------------------------------------*/
  
  /*------------ List Information -------------*/
  /** The list of the current chat groups */
  private ArrayList<ChatGroup> groups = new ArrayList<ChatGroup>();
  /** The list of the current users, both online and offline */
  private ArrayList<User> users = new ArrayList<User>();
  /*-------------------------------------------*/
  
  private MessageHandler readServer;
  
  public static void main(String[] args) { 
    //initialize the chat client
    chat = new ChatClient();
    //calls the method which creates the login screen
    chat.begin();
  }
  
  
  /**
   * This method creates the GUI for users to enter connection information and their username 
   */
  
  public void begin(){
    //creates the frame for the login screen
    setting = new JFrame("Setting");
    //creates the panel going onto the screen
    settingP = new JPanel();
    //sets the size of the frame
    setting.setSize(300,175);
    //positions the frame to the center of the screen
    setting.setLocationRelativeTo(null);
    //the program will exit if the login frame is closed
    setting.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //add the panel to the frame
    setting.add(settingP);
    //set the panel layout to default
    settingP.setLayout(null);
    
    //create the ip address label and add it to the panel
    ipAddressL = new JLabel("IP Address: ");
    ipAddressL.setBounds(10,20,80,25);
    settingP.add(ipAddressL);
    
    //create the ip address text field and add it to the panel
    ipAddressT = new JTextField(20);
    ipAddressT.setBounds(100,20,165,25);
    settingP.add(ipAddressT);
    
    //create the port number label and add it to the panel
    portL = new JLabel("Port No: ");
    portL.setBounds(10,50,80,25);
    settingP.add(portL);
    
    //create the port number text field and add it to the panel
    portT = new JTextField(20);
    portT.setBounds(100,50,165,25);
    settingP.add(portT);
    
    //create the username label and add it to the panel
    userNameL = new JLabel("Username: ");
    userNameL.setBounds(10,80,80,25);
    settingP.add(userNameL);
    
    //create the username text field and add it to the panel
    userNameT = new JTextField(20);
    userNameT.setBounds(100,80,165,25);
    settingP.add(userNameT);
    
    //create the button to enter the information and add it to the panel
    okB = new JButton("Ok");
    okB.setBounds(10,110,80,25);
    //add an action listener to detect if the button is clicked
    okB.addActionListener(new OKButtonListener());
    settingP.add(okB);
    
    //set the frame to be seen
    setting.setVisible(true);
    
  }
  
  /**
   * This method creates the GUI for the menu screen which allows users to view different chats and users
   */
  
  public void startMenu() {
    //creates the frame for the menu screen
    menu = new JFrame("DUBER");
    //add a window listener to detect if the window is closing
    menu.addWindowListener(listener);
    //initialize split panes to divide key areas
    lrgSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    bigSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    bigSplitPane.setDividerLocation(275);
    medSplitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    medSplitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    avgSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    avgSplitPane.setDividerLocation(215);
    smlSplitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    smlSplitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    
    //the program will exit if the menu frame is closed
    menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    //create panel with the user's personal name and add it to the top panel
    userInfo = new JPanel();
    userInfo.setBackground(LIGHT_BLUE);
    userInfoL = new JLabel(personalName);
    userInfo.add(userInfoL);
    
    //create panel with the user's personal name and add it to the top panel
    quitButton = new JButton("QUIT");
    quitButton.addActionListener(new QuitButtonListener());
    userInfo.add(quitButton);
    
    //create panel with the chat group title and add it to the medium split pane
    chatgroupsP1 = new JPanel();
    chatgroupsP1.setBackground(GREY);
    chatgroupsL = new JLabel("CHAT GROUPS");
    chatgroupsP1.add(chatgroupsL);
    medSplitPane1.setTopComponent(chatgroupsP1);
    
    //create panel with the list of chat groups and add it to the medium split pane
    chatgroupsP2 = new JPanel();
    chatgroupsS = new JScrollPane();
    medSplitPane1.setBottomComponent(chatgroupsS);
    chatgroupsS.setViewportView(chatgroupsP2);
    chatgroupsP2.setLayout(new GridLayout());
    
    //create panel with the user title and add it to the medium split pane
    usersP1 = new JPanel();
    usersP1.setBackground(GREY);
    usersL = new JLabel("USERS");
    usersP1.add(usersL);
    medSplitPane2.setTopComponent(usersP1);
    medSplitPane2.setBottomComponent(avgSplitPane);
    
    //create panel with the online title and add it to the small split pane
    onlineP1 = new JPanel();
    onlineP1.setBackground(GREEN);
    onlineL = new JLabel("ONLINE");
    onlineP1.add(onlineL);
    smlSplitPane1.setTopComponent(onlineP1);
    
    //create panel with the list of online users and add it to the small split pane
    onlineP2 = new JPanel();
    onlineS = new JScrollPane();
    smlSplitPane1.setBottomComponent(onlineS);
    onlineS.setViewportView(onlineP2);
    onlineP2.setLayout(new GridLayout());
    
    //create panel with the offline title and add it to the small split pane 
    offlineP1 = new JPanel();
    offlineP1.setBackground(RED);
    offlineL = new JLabel("OFFLINE");
    offlineP1.add(offlineL);
    smlSplitPane2.setTopComponent(offlineP1);
    
    //create panel with list of offline users and add it to the small split pane
    offlineP2 = new JPanel();
    offlineS = new JScrollPane();
    smlSplitPane2.setBottomComponent(offlineS);
    offlineS.setViewportView(offlineP2);
    offlineP2.setLayout(new GridLayout());
    
    //add the contents of the average split pane
    avgSplitPane.setTopComponent(smlSplitPane1);
    avgSplitPane.setBottomComponent(smlSplitPane2);
    
    //add the contents of the big split pane
    bigSplitPane.setTopComponent(medSplitPane1);
    bigSplitPane.setBottomComponent(medSplitPane2);
    
    //add the contents of the large split pane
    lrgSplitPane.setTopComponent(userInfo);
    lrgSplitPane.setBottomComponent(bigSplitPane);
    
    //set preffered size
    menu.setPreferredSize(new Dimension(400,900));
    //set layout
    menu.getContentPane().setLayout(new GridLayout());
    //add the large split pane
    menu.getContentPane().add(lrgSplitPane);
    
    //set the menu to be seen
    menu.pack();
    menu.setVisible(true);
    
    //atempt a connection with the server
    chat.connect(ipAddress, portNo);
    //the chat client can now read messages from the server
    running = true;
    
    //communicate to the server through a thread
    Thread t = new Thread(new MessageHandler());
    //start the thread
    t.start();
    
  }
  
  /**
   * This method creates the chat GUI which allows users to private message and chat in chat groups
   * @param idx The index of the chat group or user within the list
   * @param type The type of chat the current chat falls under (private message or chatgroup)
   * @param isBlocked A boolean variable telling whether the user is blocked or not
   */
  
  public void go(int idx, String type, boolean isBlocked) {
    //creates the frame for the chat screen
    window = new JFrame("Chat Client");
    //set the size of the chat screen
    window.setSize(400,400);
    //position the chat to the center of the screen
    window.setLocationRelativeTo(null);
    
    //initialize split panes and their panels
    windowS = new JSplitPane();
    windowP = new JPanel();
    windowS2 = new JSplitPane();
    windowP2 = new JPanel();
    windowP3 = new JPanel();
    //set the layout of the chat
    window.getContentPane().setLayout(new GridLayout());
    //add the large split pane to the frame
    window.getContentPane().add(windowS);
    //the window is disposed when closed
    window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
    //define the split pane
    windowS.setOrientation(JSplitPane.VERTICAL_SPLIT);
    windowS.setDividerLocation(40);
    windowS.setTopComponent(windowP);
    windowS.setBottomComponent(windowS2);
    
    //define the split pane
    windowS2.setOrientation(JSplitPane.VERTICAL_SPLIT);
    windowS2.setDividerLocation(285);
    windowS2.setTopComponent(windowP2);
    windowS2.setBottomComponent(windowP3);
    
    //set the layout to follow a horizontal box layout
    windowP.setLayout(new BoxLayout(windowP, BoxLayout.X_AXIS));
    windowP2.setLayout(new BoxLayout(windowP2, BoxLayout.X_AXIS));
    windowP3.setLayout(new BoxLayout(windowP3, BoxLayout.X_AXIS));
    
    //create a close button and add it to the top panel
    closeB = new JButton("CLOSE");
    closeB.addActionListener(new CloseButtonListener());
    windowP.add(closeB);
    
    if (type.equals("GROUP")) { //the chat is a group chat
      //the name of the chat
      String groupName = groups.get(idx).name;
      //the number of members in the chat
      int groupSize = groups.get(idx).members.size();
      //create labels to display the group information and add it to the panel
      chatName = new JLabel("    " + groupName + "  ");
      chatSize = new JLabel(groupSize + " participants");
      chatSize.setForeground(BLUE);
      windowP.add(chatName);
      windowP.add(chatSize);
      
      //make the message area scrollable
      msgAreaS = new JScrollPane();
      //add the scroll pane to the panel
      windowP2.add(msgAreaS);
      //make the chat area uneditable
      msgAreaG.get(idx).setEditable(false);
      msgAreaS.setViewportView(msgAreaG.get(idx));
    }
    else { //the chat is a private chat
      //the user the client is talking to
      String otherUser = users.get(idx).username;
      //the online status of the user
      boolean status  = users.get(idx).online;
      //create labels to display the private chat information and add it to the panel
      currUser = new JLabel("    " + otherUser + "  ");
      currStatus = new JLabel("Online: " + status);
      if (status){ //if the user is online
        //set the text color to green 
        currStatus.setForeground(BRIGHT_GREEN);
      }
      else{ //if the user is offline
        //set the text color to red
        currStatus.setForeground(RED);
      }
      windowP.add(currUser);
      windowP.add(currStatus);
      
      //make the message area scrollable
      msgAreaS = new JScrollPane();
      //add the scroll pane to the panel
      windowP2.add(msgAreaS);
      //make the chat area uneditable
      msgAreaU.get(idx).setEditable(false);
      msgAreaS.setViewportView(msgAreaU.get(idx));
      
    }
    
    if (isBlocked == false){ //if the user is not blocked
      //create a typable text field and add it to the panel
      typeT = new JTextField(20);
      windowP3.add(typeT);
      //create a send button and add it to the panel
      sendB = new JButton("SEND");
      //add action listener to detect if the button is clicked
      sendB.addActionListener(new SendButtonListener(type,idx,personalName));
      windowP3.add(sendB);
    }
    
    //set chat to be seen
    window.setVisible(true);
    
  }
  
  /**
   * This method creates the GUI which asks the user whether or not they would like to ask to join a certain chat group
   * @param chatGroupIdx The index of the chat group within the list
   */
  
  public void askToJoin(int chatGroupIdx){
    
    //creates the frame for the ask to join screen
    askFrame = new JFrame();
    //set the size of the ask to join frame
    askFrame.setSize(350,85);
    //the window is disposed when closed
    askFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    //create panel to add to the frame
    askPanel = new JPanel();
    
    //create the ask label
    askLabel = new JLabel("Would you like to ask to join this chatgroup?");
    askLabel.setBounds(10, 10, 140, 30);
    //create the yes button
    yesButton = new JButton("YES");
    //add action listener to detect if the button is clicked
    yesButton.addActionListener(new YesButtonListener(chatGroupIdx));
    yesButton.setBounds(20, 70, 25, 15);
    //create the no button
    noButton = new JButton("NO");
    //add action listener to detect if the button is clicked
    noButton.addActionListener(new NoButtonListener());
    noButton.setBounds(55, 70, 25, 15);
    
    //add all the components to the panel
    askPanel.add(askLabel);
    askPanel.add(yesButton);
    askPanel.add(noButton);
    askFrame.add(askPanel);
    
    //set the ask to join screen to be seen
    askFrame.setVisible(true);
  }
  
  /**
   * This method creates the GUI which tells the user that they are banned from the current chat group
   */
  
  public void userIsBanned(){
    //creates the frame for the user is banned screen
    banFrame = new JFrame();
    //set the size of the user is banned screen
    banFrame.setSize(300,85);
    //the window is disposed when closed
    banFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    //create a panel to go on top of the frame
    banPanel = new JPanel();
    
    //create the banned label
    banLabel = new JLabel("YOU ARE BANNED FROM THIS CHATGROUP!");
    banLabel.setBounds(10, 10, 180, 50);
    //create the acknowledgement button
    sadButton = new JButton("OK");
    //add action listener to detect if the button is clicked
    sadButton.addActionListener(new SadButtonListener());
    sadButton.setBounds(65, 70, 25, 15);
    
    //add all the components to the panel
    banPanel.add(banLabel);
    banPanel.add(sadButton);
    banFrame.add(banPanel);
    
    //set the user is banned screen to be seen
    banFrame.setVisible(true);
  }
  
  /**
   * This method attempts to establishes a connection with the chat server using the provided login information
   * Creates the socket and input/ouput streams
   * @param ip The provided ip address
   * @param port The provided port number
   * @return The socket connection
   */

  public Socket connect(String ip, int port) { 
    System.out.println("Attempting to make a connection..");
    
    //attempt to make a connection with the server 
    try {
      //attempt socket connection (local address). This will wait until a connection is made
      mySocket = new Socket(ip,port);
      //Stream for network input
      InputStreamReader stream1 = new InputStreamReader(mySocket.getInputStream());
      input = new BufferedReader(stream1);
      //assign printwriter to network stream
      output = new PrintWriter(mySocket.getOutputStream());
      
    } catch (IOException e) {  //connection error occured
      System.out.println("Connection to Server Failed");
      e.printStackTrace();
    }
    
    System.out.println("Connection made.");
    
    //output to the user the username
    output.println(personalName);
    output.flush();
    
    // returns the socket connection
    return mySocket;
  }
  
  /**
   * This class is an inner class of the duber chat client which deals with input from the server via threading
   * @author Dylan Wang
   * @version 1.0, Dec 13, 2020
   */
  
  private class MessageHandler implements Runnable{
    /**
     * This method continuously reads messages from the server when the thread starts
     * Waits for server input then decides what to do with the input
     */
    
    public void run() { 
      
      //keeps looping to get input from the server until the user exits the program
      while(running) { 
        try {
          if (input.ready()) { //check for an incoming messge
            
            String msg;          
            //read the message
            msg = input.readLine();
            
            if (msg.indexOf("[STATUS]") == 0){ //if the input is related to user status
              //get the number of online users and offline users
              int pOnline = 0;
              int pOffline = 0;
              int numOfGroups = groups.size();
              //loop through all the users in the system
              for (int i = 0; i < users.size(); i++) {
                if (users.get(i).online){ //if the user is online
                  pOnline++;
                }
                else{ //if the user is offline
                  pOffline++;
                }
              }
              
              if (msg.indexOf("online/") != -1) { //when a user just comes online and is already registered in the system
                //the specified username
                String userName = msg.substring(msg.indexOf("/") + 1);
                //the user is the specified user
                if (personalName.equals(userName)) {
                  //the number of chat groups
                  int numOfChatGroups = Integer.parseInt(input.readLine());
                  //get input for all the chat groups
                  for (int i = 0; i < numOfChatGroups; i++){ 
                    //the chat group name
                    String nameOfChatGroup = input.readLine();
                    //adds the new chat group to the list
                    groups.add(new ChatGroup(nameOfChatGroup));
                    
                    //change menu layout to fit an additional chat group
                    chatgroupsP2.setLayout(new GridLayout(numOfGroups + 1, 1));
                    //create new chat group button and add it to the panel
                    JButton groupsB = new JButton (nameOfChatGroup + " " + 0 + " participants"); 
                    //add action listener to detect if the button is clicked
                    groupsB.addActionListener(new ChatButtonListener(nameOfChatGroup));
                    chatgroupsP2.add(groupsB);
                    
                    //set the menu frame to be seen
                    menu.setVisible(true);
                    
                    //add new message area to the list
                    msgAreaG.add(new JTextArea());
                  }
                  
                  //the number of groups the user in currently in
                  int numOfChatGroupsIn = Integer.parseInt(input.readLine());
                  
                  //loop through all the chat groups the user is in
                  for (int i = 0; i < numOfChatGroupsIn; i++){
                    //the name of the chat group
                    String chatGroupName = input.readLine();
                    for (int k = 0; k < groups.size(); k++){
                      if (groups.get(k).name.equals(chatGroupName)){ //if the chatgroup name matches the one specified
                        //add the user to the group
                        groups.get(k).members.add(new User(userName, true));
                      }
                    }
                  }
                  
                  //number of users already registered in the system (excluding themselves)
                  int numOfUsers = Integer.parseInt(input.readLine());
                  
                  //loop through all those users and get input
                  for (int i = 0; i < numOfUsers; i++) { 
                    //the name of the user
                    String usersName = input.readLine();
                    //the online status of the user
                    String usersStatus = input.readLine();
                    boolean online;
                    if (usersStatus.equals("Online")){ //if the user is online
                      online = true;
                    }
                    else { //if the user is offline 
                      online = false;
                    }
                    if (online){ //if the user is online
                      //add the user to the system
                      users.add(new User(usersName, true));
                      //change the menu layout to fit an additional user
                      onlineP2.setLayout(new GridLayout(pOnline + 1, 1));
                      //create a new user button and add it to the panel
                      JButton onlineB = new JButton (usersName + " Online: " + true); 
                      //add action listener to detect if the button is clicked
                      onlineB.addActionListener(new PrivateButtonListener(usersName));
                      onlineP2.add(onlineB);
                      
                      //set the menu to be seen
                      menu.setVisible(true);
                    }
                    else { //if the user is offline 
                      //add the user to the system
                      users.add(new User(usersName, false));
                      //change the menu layout to fit another user
                      offlineP2.setLayout(new GridLayout(pOffline + 1, 1));
                      //create a new user button and add it to the panel
                      JButton offlineB = new JButton (usersName + " Online: " + false); 
                      //add action listener to detect if the button is clicked
                      offlineB.addActionListener(new PrivateButtonListener(usersName));
                      offlineP2.add(offlineB);
                      
                      //set the menu to be seen
                      menu.setVisible(true);
                    }
                    //add a new chat area
                    msgAreaU.add(new JTextArea());
                    
                    //the number of groups the current user is in
                    int numberOfGroupsIn = Integer.parseInt(input.readLine());
                    //loop through all those groups
                    for (int j = 0; j < numberOfGroupsIn; j++){
                      //the name of the chat group
                      String nameOfChatGroupIn = input.readLine();
                      //find the idx of the group with the specified name
                      for (int k = 0; k < groups.size(); k++){
                        if (groups.get(k).name.equals(nameOfChatGroupIn)){
                          //add the user to that group
                          groups.get(k).members.add(new User(usersName, online));
                        }
                      }
                    }
                  }
                  
                  //remove all components on the panel and redraw the entire panel
                  chatgroupsP2.removeAll();
                  //change the menu layout to fit all groups
                  chatgroupsP2.setLayout(new GridLayout(groups.size(), 1));
                  //iterate through all existing groups and create a button to add to the panel
                  for (int i = 0; i < groups.size(); i++){
                    JButton groupUpdate = new JButton(groups.get(i).name + " " + groups.get(i).members.size() + " participants");
                    groupUpdate.addActionListener(new ChatButtonListener(groups.get(i).name));
                    chatgroupsP2.add(groupUpdate);
                  }
                  
                  //the panel was dynamically modified so we need to revalidate and repaint it
                  chatgroupsP2.revalidate();
                  chatgroupsP2.repaint();
                  //set the menu to be seen
                  menu.setVisible(true);
                  
                }
                else { //the user is not the specified user
                  
                  //take input from the server and ignore it
                  //only the username is used
                  int numOfChatGroups = Integer.parseInt(input.readLine());
                  for (int i = 0; i < numOfChatGroups; i++){ 
                    String nameOfChatGroup = input.readLine();
                  }
                  
                  int numOfChatGroupsIn = Integer.parseInt(input.readLine());
                  for (int i = 0; i < numOfChatGroupsIn; i++){
                    String chatGroupName = input.readLine();
                  }
                  
                  int numOfUsers = Integer.parseInt(input.readLine());
                  for (int i = 0; i < numOfUsers; i++) { 
                    String usersName = input.readLine();
                    String usersStatus = input.readLine();
                    
                    int numberOfGroupsIn = Integer.parseInt(input.readLine());
                    for (int j = 0; j < numberOfGroupsIn; j++){
                      String nameOfChatGroupIn = input.readLine();
                    }
                  }
                  
                  //loop through all the users
                  for (int i = 0; i < users.size(); i++) { 
                    if (users.get(i).username.equals(userName)){ //if the name of the users matches the one specified
                      //change their status to be online
                      users.get(i).online = true;
                      //change the online user layout to fit in an additional user 
                      onlineP2.setLayout(new GridLayout(pOnline + 1, 1));
                      //create the new user button and add it to the panel
                      JButton onlineB = new JButton (users.get(i).username + " Online: " + users.get(i).online); 
                      //add an action listener to detect if the button is clicked
                      onlineB.addActionListener(new PrivateButtonListener(users.get(i).username));
                      onlineP2.add(onlineB);
                      
                      //remove the user from the list of offline users in the menu
                      offlineP2.setLayout(new GridLayout(pOffline - 1, 1));
                      //store all the components on the panel in an array
                      Component[] componentList = offlineP2.getComponents();
                      
                      //loop through all the components on the panel
                      for(Component c : componentList){
                        String content = c.toString();
                        //determine if the button text matches the expected button text
                        int idxOfText = content.indexOf("text=");
                        int idxOfCom = content.indexOf(",", idxOfText + 1);
                        String textContent = content.substring(idxOfText + 5, idxOfCom);
                        String expect = users.get(i).username + " Online: " + false;
                        
                        //if the button text matches the expected button text
                        if (textContent.equals(expect)){
                          //remove the component from the panel
                          offlineP2.remove(c);
                        }
                      }
                      //since we dynamically removed a component we must revalidate and repaint the panel
                      offlineP2.revalidate();
                      offlineP2.repaint();
                      //set the menu to be seen
                      menu.setVisible(true);
                    }
                  }
                  
                }
                
              }
              else if (msg.indexOf("offline/") != -1){ //if a user goes offline
                //the username of the user
                String userName = msg.substring(msg.indexOf("/") + 1);
                
                if (!personalName.equals(userName)){ //if the client is not the specified user
                  //loop through all the users
                  for (int i = 0; i < users.size(); i++) { 
                    if (users.get(i).username.equals(userName)){ //if the user's name matches the one specified
                      //set the user's statuts to be offline
                      users.get(i).online = false;
                      
                      //change the offline user layout to fit in an additional user 
                      offlineP2.setLayout(new GridLayout(pOffline + 1, 1));
                      //create the new user button and add it to the panel
                      JButton offlineB = new JButton (users.get(i).username + " Online: " + false); 
                      //add an action listener to detect if the button is clicked
                      offlineB.addActionListener(new PrivateButtonListener(users.get(i).username));
                      offlineP2.add(offlineB);
                      
                      //remove the user from the list of offline users in the menu
                      onlineP2.setLayout(new GridLayout(pOnline - 1, 1));
                      //store all the components on the panel in an array
                      Component[] componentList = onlineP2.getComponents();
                      
                      //loop through all the components on the panel
                      for(Component c : componentList){
                        String content = c.toString();
                        //determine if the button text matches the expected button text
                        int idxOfText = content.indexOf("text=");
                        int idxOfCom = content.indexOf(",", idxOfText + 1);
                        String textContent = content.substring(idxOfText + 5, idxOfCom);
                        String expect = users.get(i).username + " Online: " + true;
                        
                        //if the button text matches the expected button text
                        if (textContent.equals(expect)){
                          //remove the component from the panel
                          onlineP2.remove(c);
                        }
                      }
                      //since we dynamically removed a component we must revalidate and repaint the panel
                      onlineP2.revalidate();
                      onlineP2.repaint();
                      //set the menu to be seen
                      menu.setVisible(true);
                      
                    }
                  }
                }
                
              }
              else if (msg.indexOf("join/") != -1) { //a completely new user logs into the system
                //the name of the user
                String userName = msg.substring(msg.indexOf("/") + 1);
                
                if (personalName.equals(userName)){ //the client is the specified user
                  //the number of chat groups
                  int numOfChatGroups = Integer.parseInt(input.readLine());
                  //get input for all the chat groups
                  for (int i = 0; i < numOfChatGroups; i++){ 
                    //the name of the chat group
                    String nameOfChatGroup = input.readLine();
                    //adds the new chat group to the list
                    groups.add(new ChatGroup(nameOfChatGroup));
                    
                    //change the group layout to fit in an additional chat group
                    chatgroupsP2.setLayout(new GridLayout(numOfGroups + 1, 1));
                    //create a new chat group button and add it to the panel
                    JButton groupsB = new JButton (nameOfChatGroup + " " + 0 + " participants"); 
                    //add an action listener to detect if the button has been clicked
                    groupsB.addActionListener(new ChatButtonListener(nameOfChatGroup));
                    chatgroupsP2.add(groupsB);
                    
                    //set the menu to be seen
                    menu.setVisible(true);
                    
                    //add a new group chat area
                    msgAreaG.add(new JTextArea());
                  }
                  
                  //number of users already registered in the system (exculding themselves)
                  int numOfUsers = Integer.parseInt(input.readLine());
                  //loop through all those users and get input
                  for (int i = 0; i < numOfUsers; i++) { 
                    //the name of the user
                    String usersName = input.readLine();
                    //the online status of the user
                    String usersStatus = input.readLine();
                    boolean online;
                    if (usersStatus.equals("Online")) { //if the user if online
                      online = true;
                    }
                    else { //if the user is offline
                      online = false;
                    }
                    if (online) { //if the user if online
                      //add the user to the system
                      users.add(new User(usersName, true));
                      //change the menu layout to fit another user
                      onlineP2.setLayout(new GridLayout(pOnline + 1, 1));
                      //create a new user button and add it to the panel
                      JButton onlineB = new JButton (usersName + " Online: " + true); 
                      //add action listener to detect if the button is clicked
                      onlineB.addActionListener(new PrivateButtonListener(usersName));
                      onlineP2.add(onlineB);
                      
                      //set the menu to be seen
                      menu.setVisible(true);
                    }
                    else { //if the user if offline
                      //add the user to the system
                      users.add(new User(usersName, false));
                      //change the menu layout to fit another user
                      offlineP2.setLayout(new GridLayout(pOffline + 1, 1));
                      //create a new user button and add it to the panel
                      JButton offlineB = new JButton (usersName + " Online: " + false); 
                      //add action listener to detect if the button is clicked
                      offlineB.addActionListener(new PrivateButtonListener(usersName));
                      offlineP2.add(offlineB);
                      
                      //set the menu to be seen
                      menu.setVisible(true);
                    }
                    
                    //add a new user chat area
                    msgAreaU.add(new JTextArea());
                    
                    //the number of chat groups the user in currently in
                    int numberOfGroupsIn = Integer.parseInt(input.readLine());
                    //loop through all those groups
                    for (int j = 0; j < numberOfGroupsIn; j++){
                      //the name of the group
                      String nameOfChatGroupIn = input.readLine();
                      //find the index of the group with that name
                      for (int k = 0; k < groups.size(); k++){
                        if (groups.get(k).name.equals(nameOfChatGroupIn)){
                          //add the user to that group
                          groups.get(k).members.add(new User(usersName, online));
                        }
                      }
                    }
                  }
                  
                  //remove all components on the panel and redraw the entire panel
                  chatgroupsP2.removeAll();
                  //change the menu layout to fit all groups
                  chatgroupsP2.setLayout(new GridLayout(groups.size(), 1));
                  //iterate through all existing groups and create a button to add to the panel
                  for (int i = 0; i < groups.size(); i++){
                    JButton groupUpdate = new JButton(groups.get(i).name + " " + groups.get(i).members.size() + " participants");
                    groupUpdate.addActionListener(new ChatButtonListener(groups.get(i).name));
                    chatgroupsP2.add(groupUpdate);
                  }
                  
                  //the panel was dynamically modified so we need to revalidate and repaint it
                  chatgroupsP2.revalidate();
                  chatgroupsP2.repaint();
                  //set the menu to be seen
                  menu.setVisible(true);
                  
                }
                else { //the client is not the specified user
                  //take input from the server and ignore it
                  //only use the username
                  int numOfChatGroups = Integer.parseInt(input.readLine());
                  for (int i = 0; i < numOfChatGroups; i++){ 
                    String nameOfChatGroup = input.readLine();
                  }
                  
                  int numOfUsers = Integer.parseInt(input.readLine());
                  for (int i = 0; i < numOfUsers; i++) { 
                    String usersName = input.readLine();
                    String usersStatus = input.readLine();
                    
                    int numberOfGroupsIn = Integer.parseInt(input.readLine());
                    for (int j = 0; j < numberOfGroupsIn; j++){
                      String nameOfChatGroupIn = input.readLine();
                    }
                  }
                  
                  //add the new user to the system
                  users.add(new User(userName, true));
                  //change the online user layout to fit in an additional user
                  onlineP2.setLayout(new GridLayout(pOnline + 1, 1));
                  //create a new user button and add it to the panel
                  JButton onlineB = new JButton (userName + " Online: " + true); 
                  //add actiona listener to detect if the button is clicked
                  onlineB.addActionListener(new PrivateButtonListener(userName));
                  onlineP2.add(onlineB);
                  
                  //set the menu to be seen
                  menu.setVisible(true);
                  
                  //add a new user chat area
                  msgAreaU.add(new JTextArea());
                }
                
              }
              else if (msg.indexOf("closing") != -1) { //if the server exits
                //the clients will also all exit
                //stop getting input from the server
                running = false;
                System.exit(0);
              }
              else if (msg.indexOf("timeout/") != -1) { //if the user is timed out
                //the name of the user
                String nameOfUser = msg.substring(msg.indexOf("/") + 1);
                if (nameOfUser.equals(personalName)){ //if the client is the timed out user
                  //tell the server the user went offline
                  output.println("[STATUS]offline/" + personalName);
                  output.flush();
                  //stop getting input from the server
                  running = false;
                  //the client will exit
                  System.exit(0);
                }
              }
              else if (msg.indexOf("group/") != -1) { //if a new chat group is created
                //the name of the chat group
                String groupName = msg.substring(msg.indexOf("group/") + 6);
                //add the new chat group to the list
                groups.add(new ChatGroup(groupName));
                
                //change the menu layout to fit an additional chat group
                chatgroupsP2.setLayout(new GridLayout(numOfGroups + 1, 1));
                //create a new group button and add it to the panel
                JButton groupsB = new JButton (groupName + " " + 0 + " participants");
                //add action listener to detect if the button clicked
                groupsB.addActionListener(new ChatButtonListener(groupName));
                chatgroupsP2.add(groupsB);
                
                //set the menu to be seen
                menu.setVisible(true);
                
                //add a new group message area
                msgAreaG.add(new JTextArea());
              }
            }
            else if (msg.indexOf("[COMMAND]") == 0){ //if the input is related to a server command
              //the name of the user
              String nameOfUser = msg.substring(msg.indexOf("/") + 1, msg.indexOf(":"));
              //the index of the group in the system list
              int groupIdx = Integer.parseInt(msg.substring(msg.indexOf(":") + 1));
              
              if (msg.indexOf("approve/") != -1){ //if the server approved a user to join a chat group
                if (nameOfUser.equals(personalName)) { //if the client matches the specified user
                  //add the user to the group
                  groups.get(groupIdx).members.add(new User(personalName, true));
                }
                
                //loop through all existing the users
                for (User u: users){
                  if (u.username.equals(nameOfUser)){ //if the user is the specified user
                    //add that user to the group
                    groups.get(groupIdx).members.add(u);
                  }
                }
                
                //remove all components on the panel and redraw the entire panel
                chatgroupsP2.removeAll();
                //change the menu layout to fit all groups
                chatgroupsP2.setLayout(new GridLayout(groups.size(), 1));
                //iterate through all existing groups and create a button to add to the panel
                for (int i = 0; i < groups.size(); i++){
                  JButton groupUpdate = new JButton(groups.get(i).name + " " + groups.get(i).members.size() + " participants");
                  groupUpdate.addActionListener(new ChatButtonListener(groups.get(i).name));
                  chatgroupsP2.add(groupUpdate);
                }
                
                //the panel was dynamically modified so we need to revalidate and repaint it
                chatgroupsP2.revalidate();
                chatgroupsP2.repaint();
                //set the menu to be seen
                menu.setVisible(true);
              }
              else if (msg.indexOf("remove/") != -1) { //if a user is removed from the group
                if (personalName.equals(nameOfUser)) { //the client is the specified user
                  //loop through all the members in the specified chat group
                  for (User u: groups.get(groupIdx).members){
                    if (u.username.equals(personalName)){ //if the client is a member
                      //remove the client from the group
                      groups.get(groupIdx).members.remove(u);
                      //remove the client from the blocked list
                      groups.get(groupIdx).blocked.remove(u);
                      //break to avoid concurrent modification exception
                      break;
                    }
                  }
                }
                
                //loop through all existing users
                for (User u: users) {
                  if (u.username.equals(nameOfUser)) { //if the user is the specified user
                    //remove the user from the group
                    groups.get(groupIdx).members.remove(u);
                  }
                }
                
                //remove all components on the panel and redraw the entire panel
                chatgroupsP2.removeAll();
                //change the menu layout to fit all groups
                chatgroupsP2.setLayout(new GridLayout(groups.size(), 1));
                //iterate through all existing groups and create a button to add to the panel
                for (int i = 0; i < groups.size(); i++){
                  JButton groupUpdate = new JButton(groups.get(i).name + " " + groups.get(i).members.size() + " participants");
                  groupUpdate.addActionListener(new ChatButtonListener(groups.get(i).name));
                  chatgroupsP2.add(groupUpdate);
                }
                
                //the panel was dynamically modified so we need to revalidate and repaint it
                chatgroupsP2.revalidate();
                chatgroupsP2.repaint();
                //set the menu to be seen
                menu.setVisible(true);
                
                //if the client is the one being removed and the chat has been initialized
                if (nameOfUser.equals(personalName) && window != null){
                  //close the chat
                  window.dispose();
                }
              }
              else if (msg.indexOf("ban/") != -1) { //if a user is banned from a chat group
                if (personalName.equals(nameOfUser)) { //the client is the specified user
                  //loop through all the members in the specified chat group
                  for (User u: groups.get(groupIdx).members){
                    if (u.username.equals(personalName)){ //if the client is a member
                      //add user to a banned list
                      groups.get(groupIdx).banned.add(u);
                      //remove the client from the group
                      groups.get(groupIdx).members.remove(u);
                      //remove the client from the blocked list
                      groups.get(groupIdx).blocked.remove(u);
                      //break to avoid concurrent modification exception
                      break;
                    }
                  }
                }
                //loop through all existing users
                for (User u: users){
                  if (u.username.equals(nameOfUser)){ //if the user is the specified user
                    //remove the user from the group
                    groups.get(groupIdx).members.remove(u);
                    //add user to a banned list
                    groups.get(groupIdx).banned.add(u);
                  }
                }
                
                //remove all components on the panel and redraw the entire panel
                chatgroupsP2.removeAll();
                //change the menu layout to fit all groups
                chatgroupsP2.setLayout(new GridLayout(groups.size(), 1));
                //iterate through all existing groups and create a button to add to the panel
                for (int i = 0; i < groups.size(); i++){
                  JButton groupUpdate = new JButton(groups.get(i).name + " " + groups.get(i).members.size() + " participants");
                  groupUpdate.addActionListener(new ChatButtonListener(groups.get(i).name));
                  chatgroupsP2.add(groupUpdate);
                }
                
                //the panel was dynamically modified so we need to revalidate and repaint it
                chatgroupsP2.revalidate();
                chatgroupsP2.repaint();
                //set the menu to be seen
                menu.setVisible(true);
                
                //if the client is the one being removed and the chat has been initialized
                if (nameOfUser.equals(personalName) && window != null){
                  //close the chat
                  window.dispose();
                }
                
              }
              else if (msg.indexOf("block/") != -1) { //if a user is blocked from a chat group
                if (personalName.equals(nameOfUser)) { //the client is the specified user
                  //loop through all the members in the specified chat group
                  for (User u: groups.get(groupIdx).members){
                    if (u.username.equals(personalName)){ //if the client is a member
                      //add the client to a blocked list
                      groups.get(groupIdx).blocked.add(u);
                    }
                  }
                }
                
                //loop through all existing users
                for (User u: users){
                  if (u.username.equals(nameOfUser)){ //if the user is the specified user
                    //add user to a blocked list
                    groups.get(groupIdx).blocked.add(u);
                  }
                }
                
                //if the client is the one being removed and the chat has been initialized
                if (nameOfUser.equals(personalName) && window != null){
                  //close the chat
                  window.dispose();
                }
              }
              
            }
            else if (msg.indexOf("[SERVER]") == 0){ //the server sends a message in a chat group
              //the chat group index
              int chatIdx = Integer.parseInt(msg.substring(8, msg.indexOf("/")));
              //the text that was sent
              String text = msg.substring(msg.indexOf("/") + 1);
              
              //add this message to the chat area
              msgAreaG.get(chatIdx).append("SERVER: " + text + "\n");
            }
            else if (msg.indexOf("[GROUP]") == 0){ //a group member sends a message in a chat group
              //the name of the user
              String userName = msg.substring(7, msg.indexOf("/"));
              //the chat group index
              int chatIdx = Integer.parseInt(msg.substring(msg.indexOf("/") + 1, msg.indexOf(":")));
              //the text that was sent
              String text = msg.substring(msg.indexOf(":") + 1);
              
              //add this message to the chat area
              msgAreaG.get(chatIdx).append(userName + ": " + text + "\n");
            }
            else if (msg.indexOf("[PRIVATE]") == 0){ //a user sends a private message to another user
              //the name of the person who sent the message
              String nameSent = msg.substring(9, msg.indexOf("/"));
              //the name of the person who receiving the message
              String nameRec = msg.substring(msg.indexOf("/") + 1, msg.indexOf(":"));
              //the text that was sent
              String text = msg.substring(msg.indexOf(":") + 1);
              
              if (nameSent.equals(personalName)){ //client sent the message
                //loop through all the users and find the user receiving the message
                for (int i = 0; i < users.size(); i++) {
                  if (users.get(i).username.equals(nameRec)){
                    //add the message to the private chat area
                    msgAreaU.get(i).append(nameSent + ": " + text + "\n");
                  }
                }
              }
              else if (nameRec.equals(personalName)){ //client is receiving the message
                //loop through all the users and find the user who sent the message
                for (int i = 0; i < users.size(); i++) {
                  if (users.get(i).username.equals(nameSent)){
                    //add the message to the private chat area
                    msgAreaU.get(i).append(nameSent + ": " + text + "\n");
                  }
                }
              }
            }
          }
        } catch (IOException e) { //was not able to recieve a message from the server
          System.out.println("Failed to receive msg from the server");
          e.printStackTrace();
        }
      }
      
      try { 
        //after leaving the main loop we need to close all the sockets
        input.close();
        output.close();
        mySocket.close();
      }catch (Exception e) { //was not able to close the sockets
        System.out.println("Failed to close socket");
      }
    }
  }
  
  /*------------ Inner Classes for Action Listeners -------------*/
  
  /**
   * This class is an inner class of the duber chat client which implements action listener 
   * Checks to see if the ok button has been clicked by the user
   * Upon button click it will store the login information and start the menu
   * @author Dylan Wang
   * @version 1.0, Dec 13, 2020
   */
  
  class OKButtonListener implements ActionListener { 
    
    /**
     * This method checks to see if the ok button has been clicked by the user 
     * If the button is clicked, the login information is stored in the system and the menu is called
     * @param event The action event that views the status of the ok button
     */
    
    public void actionPerformed(ActionEvent event)  {
      ipAddress = ipAddressT.getText();
      portNo = Integer.parseInt(portT.getText());
      personalName = userNameT.getText();
      setting.dispose();
      chat.startMenu();
    }
  }
  
  /**
   * This class is an inner class of the duber chat client which implements action listener 
   * Checks to see if the quit button has been clicked by the user
   * Upon button click it will exit the program and tell the server that the user went offline
   * @author Dylan Wang
   * @version 1.0, Dec 13, 2020
   */
  
  class QuitButtonListener implements ActionListener { 
    
    /**
     * This method checks to see if the quit button has been clicked by the user 
     * If the button is clicked, the server gets a message that the user went offline and the program exits
     * @param event The action event that views the status of the quit button
     */
    
    public void actionPerformed(ActionEvent event)  {
      output.println("[STATUS]offline/" + personalName);
      output.flush();
      running = false;
      System.exit(0);
    }
  }
  
  /**
   * This class is an inner class of the duber chat client which implements action listener 
   * Checks to see if a group chat button has been clicked by the user
   * Upon button click it displays the ask to join screen, the chat, or the user is banned screen
   * @author Dylan Wang
   * @version 1.0, Dec 13, 2020
   */
  
  class ChatButtonListener implements ActionListener { 
    /** The name of the group chat */
    String name;
    
    /**
     * Creates an object from the ChatButtonListener class
     * @param name The name of the group chat
     */
    
    ChatButtonListener(String name){
      this.name = name;
    }
    
    /**
     * This method checks to see if a group chat button has been clicked by the user 
     * If a group chat button is clicked, it displays the ask to join screen, the chat or the user is banned screen
     * @param event The action event that views the status of the group chat button
     */
    
    public void actionPerformed(ActionEvent event)  {
      
      for (int i = 0; i < groups.size(); i++){
        if (this.name.equals(groups.get(i).name)){
          
          boolean inGroup = false;
          for (User u: groups.get(i).members){
            if (u.username.equals(personalName)){
              inGroup = true;
            }
          }
          
          boolean blockedFromGroup = false;
          for (User u: groups.get(i).blocked) {
            if (u.username.equals(personalName)) {
              blockedFromGroup = true;
            }
          }
          
          if (inGroup) {
            if (window != null) {
              window.dispose();
            }
            chat.go(i, "GROUP", blockedFromGroup);
            return;
          }
          
          
          boolean bannedFromGroup = false;
          for (User u: groups.get(i).banned) {
            if (u.username.equals(personalName)) {
              bannedFromGroup = true;
            }
          }
          if (bannedFromGroup == false) {
            chat.askToJoin(i);
          }
          else {
            chat.userIsBanned();
          }
        }
      }
    }
  }
  
  /**
   * This class is an inner class of the duber chat client which implements action listener 
   * Checks to see if a private message button has been clicked by the user
   * Upon button click it will display the private chat
   * @author Dylan Wang
   * @version 1.0, Dec 13, 2020
   */
  
  class PrivateButtonListener implements ActionListener { 
    /** The name of the user */
    String name;
    
    /**
     * Creates an object from the PrivateButtonListener class
     * @param name The name of the user
     */
    
    PrivateButtonListener(String name) {
      this.name = name;
    }
    
    /**
     * This method checks to see if a private message button has been clicked by the user 
     * If a private message button is clicked, it displays the private message chat
     * @param event The action event that views the status of the private message button
     */
    
    public void actionPerformed(ActionEvent event) {
      for (int i = 0; i < users.size(); i++) {
        if (this.name.equals(users.get(i).username)) {
          if (window != null) {
            window.dispose();
          }
          chat.go(i, "PRIVATE", false);
        }
      }
    }
  }
  
  /**
   * This class is an inner class of the duber chat client which implements action listener 
   * Checks to see if the send message button has been clicked by the user
   * Upon button click it will send the message to the server
   * @author Dylan Wang
   * @version 1.0, Dec 13, 2020
   */
  
  class SendButtonListener implements ActionListener { 
    /** The type of chat the current chat is */
    String type;
    /** The idx of the chat in the list */
    int idx;
    /** The name of the person who sent the message */
    String person;
    
    /**
     * Creates an object from the SendButtonListener class
     * @param type The type of chat the current chat is
     * @param idx The idx of the chat in the list
     * @param person The name of the person who sent the message
     */
    
    SendButtonListener(String type, int idx, String person) {
      this.type = type;
      this.idx = idx;
      this.person = person;
    }
    
    /**
     * This method checks to see if the send message button has been clicked by the user 
     * If the send message button is clicked, it outputs the message to the server, flushes the output, and clears the text field
     * @param event The action event that views the status of the send message button
     */
    
    public void actionPerformed(ActionEvent event) {
      //Send a message to the client
      if (type.equals("PRIVATE")) {
        String personRec = users.get(idx).username;
        output.println("[PRIVATE]" + person + "/" + personRec + ":" + typeT.getText());
        output.flush();
      }
      else {
        output.println("[GROUP]" + person + "/" + this.idx + ":" + typeT.getText());
        output.flush();
      }      
      typeT.setText(""); 
    }     
  }
  
  /**
   * This class is an inner class of the duber chat client which implements action listener 
   * Checks to see if close button has been clicked by the user
   * Upon button click it will close the current chat window
   * @author Dylan Wang
   * @version 1.0, Dec 13, 2020
   */

  class CloseButtonListener implements ActionListener { 
    
    /**
     * This method checks to see if the close button has been clicked by the user 
     * If the close button is clicked, the current chat window is disposed
     * @param event The action event that views the status of the close button
     */
    
    public void actionPerformed(ActionEvent event) {
      window.dispose();
    }     
  } 
  
  /**
   * This class is an inner class of the duber chat client which implements action listener 
   * Checks to see if yes button has been clicked by the user
   * Upon button click it will send a message to the server requesting to join the chat group
   * @author Dylan Wang
   * @version 1.0, Dec 13, 2020
   */
  
  class YesButtonListener implements ActionListener {
    /** The idx of the chat group in the list */
    int chatGroupIdx;
    
    /**
     * Creates an object from the YesButtonListener class
     * @param chatGroupIdx The idx of the chat group in the list
     */
    
    YesButtonListener(int chatGroupIdx) {
      this.chatGroupIdx = chatGroupIdx;
    }
    
    /**
     * This method checks to see if the yes button has been clicked by the user 
     * If the yes button is clicked, it outputs a message to the server requesting to join the chat group
     * @param event The action event that views the status of the yes button
     */
    
    public void actionPerformed(ActionEvent event) {
      output.println("[COMMAND]request/" + personalName + ":" + this.chatGroupIdx);
      output.flush();
      askFrame.dispose();
    }
  }
  
  /**
   * This class is an inner class of the duber chat client which implements action listener 
   * Checks to see if no button has been clicked by the user
   * Upon button click it will close the ask to join window
   * @author Dylan Wang
   * @version 1.0, Dec 13, 2020
   */
  
  class NoButtonListener implements ActionListener {
    
    /**
     * This method checks to see if the no button has been clicked by the user 
     * If the no button is clicked, the ask to join window closes
     * @param event The action event that views the status of the no button
     */
    
    public void actionPerformed(ActionEvent event) {
      askFrame.dispose();
    }
  }
  
  /**
   * This class is an inner class of the duber chat client which implements action listener 
   * Checks to see if acknowledgement button has been clicked by the user
   * Upon button click it will close the user is banned window
   * @author Dylan Wang
   * @version 1.0, Dec 13, 2020
   */
  
  class SadButtonListener implements ActionListener {
    
    /**
     * This method checks to see if the acknowledgement button has been clicked by the user 
     * If the acknowledgement button is clicked, the user is banned window closes
     * @param event The action event that views the status of the acknowledgement button
     */
    
    public void actionPerformed(ActionEvent event) {
      banFrame.dispose();
    }
  }
  
  /**
   * This class is an inner class of the duber chat client which implements action listener 
   * Checks to see if the menu frame has been closed
   * Upon close, it will send a message to the server to notify that the user is going offline
   * @author Dylan Wang
   * @version 1.0, Dec 13, 2020
   */
  
  WindowListener listener = new WindowAdapter() {
    
    /**
     * This method checks to see if the window is currently closing
     * If the window is closing, it sends a message to the server to notify that the user is going offline
     * @param evt The window event that views the status of the menu frame
     */
    
    public void windowClosing(WindowEvent evt) {
      output.println("[STATUS]offline/" + personalName);
      output.flush();
      running = false;
    }
  };
}