package de.fileinputstream.mytraz.worldmanagement.backup;

/**
 * © Alexander Fiedler 2018 - 2019
 * <p>
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * You may not use parts of this program as long as you were not given permission.
 * You may not distribute this project or parts of it under you own name, you company name or someone other's name.
 * Created: 18.12.2018 at 17:19
 */
public class BackupData {

    String worldID;
    long timestamp;

    public BackupData(String worldID, long timestamp) {
        this.worldID = worldID;
        this.timestamp = timestamp;
    }

    public String getWorldID() {
        return worldID;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
