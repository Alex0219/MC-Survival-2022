package de.fileinputstream.mytraz.worldmanagement.chatlog.entry;

/**
 * © Alexander Fiedler 2018 - 2019
 * <p>
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * You may not use parts of this program as long as you were not given permission.
 * You may not distribute this project or parts of it under you own name, you company name or someone other's name.
 * Created: 15.12.2018 at 22:41
 */
public class ChatEntry {

    String id;
    String uuid;
    String timestamp;
    String message;
    String username;

    public ChatEntry(String id,String uuid, String timestamp, String message, String username) {
        this.id = id;
        this.uuid = uuid;
        this.timestamp = timestamp;
        this.message = message;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }
}
