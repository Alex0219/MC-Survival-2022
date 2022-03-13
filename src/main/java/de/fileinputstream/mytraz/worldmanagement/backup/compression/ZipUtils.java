package de.fileinputstream.mytraz.worldmanagement.backup.compression;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * © Alexander Fiedler 2018 - 2019
 * <p>
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * You may not use parts of this program as long as you were not given permission.
 * You may not distribute this project or parts of it under you own name, you company name or someone other's name.
 * Created: 18.12.2018 at 16:37
 */
public class ZipUtils {

    public boolean zipFolder(String sourceDir, String zipFile) {
        try {
            FileOutputStream fout = new FileOutputStream(zipFile);


            ZipOutputStream zout = new ZipOutputStream(fout);


            File fileSource = new File(sourceDir);

            addDirToZipArchive(zout, fileSource, null);

            zout.flush();
            fout.flush();
            zout.close();
            fout.close();
            return true;


        } catch (IOException ioe) {
            System.out.println("IOException :" + ioe);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void addDirToZipArchive(ZipOutputStream zos, File fileToZip, String parrentDirectoryName) throws Exception {
        if (fileToZip == null || !fileToZip.exists()) {
            return;
        }

        String zipEntryName = fileToZip.getName();
        if (parrentDirectoryName != null && !parrentDirectoryName.isEmpty()) {
            zipEntryName = parrentDirectoryName + "/" + fileToZip.getName();
        }

        if (fileToZip.isDirectory()) {
            System.out.println("+" + zipEntryName);
            for (File file : fileToZip.listFiles()) {
                addDirToZipArchive(zos, file, zipEntryName);
            }
        } else {
            System.out.println("   " + zipEntryName);
            byte[] buffer = new byte[1024];
            FileInputStream fis = new FileInputStream(fileToZip);
            zos.putNextEntry(new ZipEntry(zipEntryName));
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
            fis.close();
        }
    }

}

