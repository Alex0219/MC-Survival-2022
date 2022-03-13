package de.fileinputstream.mytraz.worldmanagement;

/**
 * © Alexander Fiedler 2018 - 2019
 * <p>
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * You may not use parts of this program as long as you were not given permission.
 * You may not distribute this project or parts of it under you own name, you company name or someone other's name.
 * Created: 18.12.2018 at 20:19
 */
public class BackupPerformData {

    String worldID;
    String backupPath;

    public BackupPerformData(String worldID, String backupPath) {
        this.worldID = worldID;
        this.backupPath = backupPath;
    }

    public String getWorldID() {
        return worldID;
    }

    public String getBackupPath() {
        return backupPath;
    }
}
