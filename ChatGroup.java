/*------------ IMPORTS -------------*/
import java.util.ArrayList;
/*----------------------------------*/

/* [ChatGroup.java]
 * A class that creates chat group objects
 * @author Dylan Wang
 * @version 1.0, Dec 13, 2020
 */

class ChatGroup {
  /** The name of the chat group */
  String name;
  /** The list of members in the chat group */
  ArrayList<User> members;
  /** The list of banned users in the chat group */
  ArrayList<User> banned;
  /** The list of blocked users in the chat group */
  ArrayList<User> blocked;

  /**
   * Creates an object from the ChatGroup class
   * @param name The name of the chat group
   */

  ChatGroup(String name) {
    this.name = name;
    this.members = new ArrayList<User>();
    this.banned = new ArrayList<User>();
    this.blocked = new ArrayList<User>();
  }
}