# Duber Chat Software

![Project Image](https://braydonwang.github.io/duberchat.png)

> This is my Duber Chat Software project created for my ICS4UE course

---

## Description

A chat software similar to Discord that consists of a server and clients. The server can create chat groups, see group messages, manage who comes in and out, and has access to the server log. The client is able to join chat groups and privately message online users. Clients require an IP address and a port number to connect. Graphics created using Java's JFrame class and multiplayer done using Java's ServerSocket and Socket class. Server made by me, client made by Dylan Wang.

### In-depth Overview

1. The server log displays everything that is 
happening (messages, commands, etc.) in 
real time. It is not a chat group itself, it is 
only for the server.

![ServerLog](https://github.com/braydonwang/Duber-Chat-Software/blob/main/ChatLog.png)

2. The server can see messages in chat 
groups, but cannot see in private messages.

![PrivateMessages](https://github.com/braydonwang/Duber-Chat-Software/blob/main/PrivateMessage.png)

3. The side bar shows all the chat groups 
that are currently on the server, as well as 
all the users that have joined.

![ChatGroups](https://github.com/braydonwang/Duber-Chat-Software/blob/main/ChatGroups.png)
![TotalUsers](https://github.com/braydonwang/Duber-Chat-Software/blob/main/TotalUsers.png)

4. Red outlined users are currently offline, 
while green outlined users are online.

5. You can click on the users to see their 
stats, which will appear in the server log.

![UserStats](https://github.com/braydonwang/Duber-Chat-Software/blob/main/UserStats.png)

6. Users will be timedout/disconnected if 
they have not sent a message in 2 minutes 
time.

7. Notifications will appear at the top right of the screen

![Notifications](https://github.com/braydonwang/Duber-Chat-Software/blob/main/Notification.png)

8. List of commands to use by typing in the 
text field:

  -  /add chatName   --> creates a chat 
group with the specified name

  -  /approve userName   --> accepts the 
users request to join a chat group (must 
type this command in the specific chat 
group)

  -  /remove userName   --> remove the 
user from a chat group (must type this 
command in the specific chat group)

  -  /ban userName   --> bans the user 
from a chat group and is unable to request 
to join the chat group (must type this 
command in the specific chat group)

  -  /block userName   --> blocks the user 
from a chat group, can see messages, but 
cannot send messages (must type this 
command in the specific chat group)

#### Server POV
---

![ServerPOV](https://github.com/braydonwang/Duber-Chat-Software/blob/main/ServerChatPOV.png)

#### User POV
---

![UserPOV](https://github.com/braydonwang/Duber-Chat-Software/blob/main/UserPOV.png)

#### Language

- Java

#### Contributors

- [Dylan Wang](https://github.com/dylanwang0)

---
