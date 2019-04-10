package io.github.scalrx.utilities;

/***************************************************************************************************
 * Knytt Stories Mobile      (https://www.github.com/scalrx/knytt-stories-mobile)
 * Unknytt.java
 * Created by: scalr at 12:21 PM, 4/8/19
 *
 * A tool written to extract .knytt.bin files.
 * Based upon unknytt.py from the Nifflas forums:
 * http://madewokherd.nfshost.com/unknytt.tar.gz
 * https://pastebin.com/kb0MwTuJ
 *
 **************************************************************************************************/

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public final class Unknytt {

    // Method for extracting .knytt.bin archive
    public static void unknytt(DataInputStream knyttBinFile, String ksmDir) throws IOException {

        // Read header for world information (size is unused for the directory name)
        String worldName = readHeaderName(knyttBinFile);
        readHeaderSize(knyttBinFile);

        // Make directory of the World name
        String worldDir = ksmDir + worldName + "/";
        new File(worldDir).mkdirs();

        // Extract files in while loop passing in this dir
        while(extractFile(knyttBinFile, worldDir)) {
            continue;
        }
    }

    // Collects information about the header of the incoming data
    private static String readHeaderName(DataInputStream data) throws IOException {

        // Each new object in the .knytt.bin archive is preceded by a magic NF. Make sure it's there!
        byte[] buffer = new byte[2];
        data.read(buffer,0,2);
        String magic = new String(buffer);

        if(!magic.equals("NF"))
            return null;

        // If so, continue reading in information about the name of the object
        StringBuilder sb = new StringBuilder();
        char nextchar = (char)data.read();

        // Build the filename from each successive char while it's not \0
        while(nextchar != '\0') {
            sb.append(nextchar);
            nextchar = (char)data.read();
        }

        // Return object name as a String
        return sb.toString();
    }

    // Reads in the number of bytes that need to be handled by the extractor
    private static int readHeaderSize(DataInputStream data) throws IOException {
        // Bytes need to be reversed due to an endian quirk in the .knytt.bin format.
        return Integer.reverseBytes(data.readInt());
    }

    // The heart of the code, which extracts a file to the appropriate location
    private static boolean extractFile(DataInputStream data, String destDir) throws IOException {

        // Try to read in name and size
        try {
            String filename = readHeaderName(data).replace("\\","/");   // Replace necessary due to Windows filepath format
            int bytesToWrite = readHeaderSize(data);

            // Construct output filepath
            String outputFilepath = destDir + filename;

            // Make output file
            File file = new File(outputFilepath);
            file.getParentFile().mkdirs();  // Make the directories (if they don't already exist)
            FileOutputStream outputFile = new FileOutputStream(file, false);

            // Write the buffer
            while(bytesToWrite > 0) {
                byte[] buffer = new byte[Math.min(bytesToWrite, 4096)];
                data.read(buffer, 0, buffer.length);
                // write buffer data into output file
                outputFile.write(buffer);
                bytesToWrite -= buffer.length;
            }
            outputFile.close();

            // File extracted successfully!
            return true;
        } catch(NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }
}
