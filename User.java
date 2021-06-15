/* [User.java]
 * A class that creates user objects
 * @author Dylan Wang
 * @version 1.0, Dec 13, 2020
 */

class User {
  /** The username of the user */
  String username;
  /** The current status of the user (online/offline) */
  boolean online;

  /**
   * Creates an object from the User class
   * @param username The username of the user
   * @param online The current status of the user (online/offline)
   */

  User(String username, boolean online) {
    this.username = username;
    this.online = online;
  }
}