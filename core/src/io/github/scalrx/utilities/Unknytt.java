package io.github.scalrx.utilities;

/***************************************************************************************************
 * Knytt Stories Mobile      (https://www.github.com/scalrx/knytt-stories-mobile)
 * Unknytt.java
 * Created by: scalr at 12:21 PM, 4/8/19
 *
 * A tool written to extract .knytt.bin files.
 * TODO: Integrate into the main menu. Right now, this tool functions as a standalone program.
 * TODO: To run, execute the following commands at the terminal:
 *                      javac Unknytt.java
 *                      java Unknytt PATH-TO-FILE.knytt.bin
 * Based upon unknytt.py from the Nifflas forums:
 * http://madewokherd.nfshost.com/unknytt.tar.gz
 * https://pastebin.com/kb0MwTuJ
 *
 **************************************************************************************************/
import java.util.*;
import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Unknytt {
    public static void main(String[] args) throws IOException {
        DataInputStream knyttBinFile = new DataInputStream(new FileInputStream(args[0]));
        unknytt(knyttBinFile);
    }

    //
    //	METHODS: INSTALL LEVEL
    //	Copy these methods into the "Install Level" screen when it is available
    //
    private static void unknytt(DataInputStream knyttBinFile) throws IOException {
        // Read header for world name
        String worldName = readHeaderName(knyttBinFile);
        readHeaderSize(knyttBinFile); // Unused data

        // Make directory here of the World name
        String worldDir = "./" + worldName + "/";
        new File(worldDir).mkdirs();
        // Extract files in while loop passing in this dir
        while(extractFile(knyttBinFile, worldDir)) {
            continue;
        }

        // SUCCESS!
        System.out.println("Extracted successfully!");
        // TODO: Credit???
    }
    private static String readHeaderName(DataInputStream data) throws IOException {
        // Read in 2 bytes
        byte[] buffer = new byte[2];
        data.read(buffer,0,2);
        String magic = new String(buffer);
        // If they don't equal "NF", quit
        if(!magic.equals("NF")) {
            return null;
        }
        // Otherwise, read in next character
        String filename = new String();
        char nextchar = (char)data.read();
        // Build the filename from each successive char while it's not \0
        while(nextchar != '\0') {
            filename += nextchar;
            nextchar = (char)data.read();
        }
        // Return name as a String TODO: return proper value
        return filename;
    }
    private static int readHeaderSize(DataInputStream data) throws IOException {
        // Read in 4 bytes as an integer, and reverse the bytes for the proper endianness.
        return Integer.reverseBytes(data.readInt());
    }
    private static boolean extractFile(DataInputStream data, String destDir) throws IOException {
        // Try to read in name and size
        try {
            String filename = readHeaderName(data).replace("\\","/");   // replace necessary due to Windows filepath format
            int bytestowrite = readHeaderSize(data);

            // Construct output filepath
            String outputFilepath = destDir + filename;
            // Make output file
            File file = new File(outputFilepath);
            file.getParentFile().mkdirs();
            FileOutputStream outputFile = new FileOutputStream(file, false);

            // bytestowrite = size? TODO: Do we need size?

            // while bytestowrite > 0:
            while(bytestowrite > 0) {
                // buffer = read(min(bytestowrite,4096))
                byte[] buffer = new byte[Math.min(bytestowrite, 4096)];
                data.read(buffer, 0, buffer.length);
                // write buffer data into output file
                outputFile.write(buffer);
                bytestowrite -= buffer.length;
            }
            outputFile.close();

            // File extracted successfully!
            return true;
        } catch(NullPointerException e) {
            return false;
        }
    }

    private static void decompressMapBinFile(String mapBinFile) throws IOException{
        // In KS, the Map.bin file is GZIP-compressed. We decompress it for easier access.
        // TODO: The real KS doesn't produce this file. FIX this for integrity purposes!

        // Set up necessary information for file streams.
        FileInputStream inputStream = new FileInputStream(mapBinFile);
        GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
        FileOutputStream outputStream = new FileOutputStream("Map");
        byte[] buff = new byte[1024];
        int len;

        // Continuously read in a buffer from the GZIP file and write the buffer to the Map file.
        while((len = gzipInputStream.read(buff)) != -1){
            outputStream.write(buff, 0, len);
        }
        // Close the streams now that we're done with them
        outputStream.close();
        gzipInputStream.close();

    }
}
