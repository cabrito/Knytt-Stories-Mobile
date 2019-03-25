package io.github.scalrx.utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/***************************************************************************************************
 * Knytt Stories Mobile      (https://www.github.com/scalrx/knytt-stories-mobile)
 * FILE NAME GOES HERE.java
 * Created by: scalr at 9:04 PM, 3/24/19
 *
 * FILE DESCRIPTION GOES HERE.
 *
 **************************************************************************************************/
public class GZip {

    final String filepath;

    // Constructor
    public GZip(String filepath) {
        this.filepath = filepath;
    }
    public void decompress() {
        byte[] buffer = new byte[1024];
        try {
            GZIPInputStream input = new GZIPInputStream(new FileInputStream(filepath));

            FileOutputStream output = new FileOutputStream(filepath + ".raw");

            int totalSize;
            while((totalSize = input.read(buffer)) > 0 ) {
                output.write(buffer, 0, totalSize);
            }

            output.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void compress() {
        byte[] buffer = new byte[1024];
        try {
            GZIPOutputStream output = new GZIPOutputStream(new FileOutputStream(filepath + ".gz"));

            FileInputStream input = new FileInputStream(filepath);

            int totalSize;
            while((totalSize = input.read(buffer)) > 0 ) {
                output.write(buffer, 0, totalSize);
            }

            input.close();
            output.finish();
            output.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
