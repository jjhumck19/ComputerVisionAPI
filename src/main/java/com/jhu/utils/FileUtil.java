package com.jhu.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
/**
 * FileUtil to provide quick methods to reading write data from files.
 * @author James Hu
 *
 */
public final class FileUtil {
	
	//Get properties based of properties file name
	//The file name is relative to the root class directory.
	public static Properties getConfigProperties(String fileName)  {
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			input = ClassLoader.getSystemResourceAsStream(fileName);
			if (input != null ) {
				prop.load(input);
			} 
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return prop;
	}
	
	public static void  writeToFile(List<?> records, String fileName, String path,
	            String ext) throws IOException {
	        if (fileName == null)
	            throw new IllegalStateException("Filename is not specified!");
	        if (records == null || records.size() < 1) {
	        	System.out.println("Warning: No  data to write to file:" + fileName);
	            return;
	        }

	        StringBuffer outFileName = new StringBuffer(100);
	        if (path != null) {
	            outFileName.append(path).append("/");
	        }

	        outFileName.append(fileName);
	        if (ext != null) {
	            outFileName.append(".").append(ext);
	        }

	        File outputfile = new File(outFileName.toString());
	        File dir = getParentDir(outputfile);
	        if (dir != null && !dir.exists()) {
	            boolean success = dir.mkdirs();
	            if (!success) {
	                String msg = "Fail to make directory: "
	                        + outputfile.getParent();
	                System.out.println("WARNING, writeToFile error: " +msg);
	                throw new IOException(msg);
	            }
	        }

	        FileWriter writer = new FileWriter(outputfile, true);
	        try {
	            for (Object data : records) {
	                if (data != null) {
	                    writer.write(data.toString());
	                    writer.write("\n");
	                }
	            }
	            writer.flush();
	        } finally {
	            if (writer != null)
	                writer.close();
	        }
	    }
	
	/**
     * Get parent directory of the given file.
     * 
     * @param file the given file
     * @return the parent directory of the given file.
     * @throws IOException fail to handle files.
     */
    public static File getParentDir(File file) throws IOException {
        String result = null;
        if (file != null) {
            String absPath = file.getAbsolutePath();
            int lastIndex = absPath.lastIndexOf(file.getName());
            result = absPath.substring(0, lastIndex);
            return new File(result);
        } else {
            return null;
        }
    }
    
    public static File getSubDir(File dir, String subDir) throws Exception {
        String result = subDir;
        if (result == null || result.length() < 1) {
        	throw new Exception("The input subDir is null or empty");
        }

		if (dir != null) {
			String absPath = dir.getAbsolutePath();
			result = absPath + "\\" + subDir;
		} 
   
        File newDir =  new File(result);
        if (!newDir.exists()) {
        	boolean success = newDir.mkdirs();
        	if (!success) {
        		throw new Exception("Fail to create new directory  " + newDir);
        	}
        }
        
        return newDir;
        
    }

	
}
