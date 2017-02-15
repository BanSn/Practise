package fileprocessors;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import util.Constants;

public class FileSplittter {
	
	private  long lineSize = 4;
	private String outputDir;
	private String inputFile;
	
	public String getInputFile() {
		return inputFile;
	}
	
	public void setChunkSize(long lineSize) {
		this.lineSize = lineSize;
	}
	
	
	public String getOutputDir() {
		return outputDir;
	}
	/**
	  * split the file specified by filename into pieces, each of size
	  * chunkSize except for the last one, which may be smaller
	 * @throws IOException 
	  */
	public void split(String filename, String outputDir) throws IOException{
		
		BufferedWriter out=null;
		FileInputStream fis=null;
		BufferedReader in = null;
	
		
		this.inputFile = filename;
		this.outputDir = outputDir;
		
		//If Output Directory is not present create it
		//If Output Directory is present, delete all files 
		File outputidirDes = new File(outputDir);
        if (!outputidirDes.exists()) {
            if (outputidirDes.mkdir()) {
                System.out.println(outputDir + " Directory is created!");
            }
        }else{
        	for(File f: outputidirDes.listFiles()) 
        		  f.delete(); 
        }
		
        try {
        	fis = new FileInputStream(filename);
        	in = new BufferedReader(new InputStreamReader(fis));	 
        	String line = null;
        	int lineSize = 1;
        	int fileSuffix = 1;
        	out = new BufferedWriter(new FileWriter(outputDir + Constants.FILE_SEPERATOR  + Constants.LOG_FILE_NAME + Constants.DOT_APPENDER + fileSuffix));
        	
        	while ((line = in.readLine()) != null) {
        		out.write(line);
        		out.write("\n");
        		lineSize++;
			
        		if(lineSize==this.lineSize){
        			fileSuffix++;
        			lineSize = 1;
        			out.close();
        			out = new BufferedWriter(new FileWriter(outputDir + Constants.FILE_SEPERATOR  + Constants.LOG_FILE_NAME + Constants.DOT_APPENDER + fileSuffix));
        		}
        	}
        	
        }catch (FileNotFoundException e){
			System.out.println("Input File not found");
			throw e;
		}catch (IOException e){
			System.out.println("Error Occured while Reading/Writing to split files");
			throw e;
		}finally{
			try {
				out.close();
				in.close();
			} catch (IOException e) {
					System.out.println("Error Occured while closing Split Files");
					throw e;
		}
	}
	}
}
