package com.shoukaiseki.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CopyFile {
	/**
	 * 
	 * @param in
	 * @param out
	 * @return true 拷贝成功
	 * @throws IOException 
	 */
	public static boolean copyFile(File srcFile,File destFile) throws IOException{
		FileInputStream input = new FileInputStream(srcFile);
        try {
            FileOutputStream output = new FileOutputStream(destFile);
            try {
            	byte[] buffer = new byte[input.available()];
                int n = 0;
                while (-1 != (n = input.read(buffer))) {
                    output.write(buffer, 0, n);
                }
                System.out.println("文件拷贝成功!");
            } finally {
            	try {
                    if (output != null) {
                        output.close();
                    }
                } catch (IOException ioe) {
                    // ignore
                	return false;
                }
            }
        } finally {
        	try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ioe) {
            	return false;
                // ignore
            }
        }
        return true;
	}
}


