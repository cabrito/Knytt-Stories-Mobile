/*
 * Unknytt.java
 * Tool written to extract .knytt.bin files, based upon unknytt.py from the Nifflas forums:
 * http://madewokherd.nfshost.com/unknytt.tar.gz
 * https://pastebin.com/kb0MwTuJ
 *
 * Created by: scalr on 4/8/2019.
 *
 * Knytt Stories Mobile
 * https://github.com/scalrx
 * Copyright (c) 2019 by scalr.
 *
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR  A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package io.github.scalrx.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.DataInputStream;
import java.io.EOFException;
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
        FileHandle createDirectory = Gdx.files.external(worldDir);
        createDirectory.mkdirs();

        // Extract files in while loop passing in this dir
        while(extractFile(knyttBinFile, worldDir)) {
            continue;
        }

        GZip.decompress(worldDir + "Map.bin");
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
		// Check to make sure we haven't reached the end of the file
		if(data.available() == 0)
			return false;

        // Try to read in name and size
        try {
            String filename = readHeaderName(data).replace("\\","/");   // Replace necessary due to Windows filepath format
			int bytesToWrite = readHeaderSize(data);

            // Construct output filepath
            String outputFilepath = destDir + filename;

            // Make output file
			FileHandle outputFile = Gdx.files.external(outputFilepath);
			outputFile.file().getParentFile().mkdirs();

            // Write the buffer
            while(bytesToWrite > 0) {
                byte[] buffer = new byte[Math.min(bytesToWrite, 4096)];
                data.read(buffer, 0, buffer.length);
                // write buffer data into output file
                outputFile.writeBytes(buffer, true);
                bytesToWrite -= buffer.length;
            }

            // File extracted successfully!
            System.out.println("Extracted " + filename);
            return true;
        } catch(NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }
}
