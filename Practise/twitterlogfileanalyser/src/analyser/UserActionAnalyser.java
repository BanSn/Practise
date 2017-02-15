package analyser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import util.Constants;


public class UserActionAnalyser {
	LinkedList<String> queue = new 	LinkedList<String>();
	
	public void getAverageSpendTime(String outputDir) throws IOException {
		
		List<RandomAccessFile> filePointers = new ArrayList<RandomAccessFile>();
		
		File outputidirDes = new File(outputDir);
		for(File f: outputidirDes.listFiles()){
			if(f.getName().contains(Constants._SORTED)){
				RandomAccessFile rif=null;
				try {
					rif = new RandomAccessFile(f, "rw");
				} catch (FileNotFoundException e) {
					System.out.println("File not found while setting File Pointers");
					throw e;
				}
				filePointers.add(rif);
			}
		}
		
		String prevId = null, currId = null , action = null, line = null;
		long fp = 0;
		int totaltime = 0, totalActions=0, index = 0;
		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter(outputDir + Constants.FILE_SEPERATOR + Constants.USER_SPEND_FILE));
			while(!filePointers.isEmpty()){
			
				RandomAccessFile currentReader = filePointers.get(index);
				fp = currentReader.getFilePointer();
				line = currentReader.readLine();
			
				//Reached End of file, all data from the file is processed
				if(line==null){
					filePointers.remove(currentReader);
					continue;
				}
			
				currId = line.split(Constants.FIELD_SEPERATOR)[0];
			
				//Reached a point where Current User Id Data in current file is over.
				//Move to next file
				if(!currId.equalsIgnoreCase(prevId) && prevId!=null){
				
					//All Data related to Previous Id has been processed, print the Previous Id Data
					//Start processing New UserId beginning with new File
					if(index == filePointers.size()-1){
						System.out.println(prevId.toString());
						System.out.println(((float)totaltime/(float)totalActions));
					
						out.write(prevId.toString() + ":" + ((float)totaltime/(float)totalActions));
						out.write(Constants.NEW_LINE);
					
						index = 0;
						totaltime = 0;
						totalActions = 0;
						queue = new LinkedList<String>();
						prevId = null;	
					}else{
						index = index + 1;
					}
					currentReader.seek(fp);
					continue;
				}
			
				prevId = currId;
				action =  line.split(Constants.FIELD_SEPERATOR)[2];
				if(action.equalsIgnoreCase(Constants.OPEN)){
					queue.add(line);
				}
				else if(action.equalsIgnoreCase(Constants.CLOSE)){
					String openLine = queue.poll();
					if(openLine==null){
						continue;
				}
					String ts_open = openLine.split(Constants.FIELD_SEPERATOR)[1];
					String ts_close = line.split(Constants.FIELD_SEPERATOR)[1];
					totaltime = totaltime + (Integer.parseInt(ts_close)-Integer.parseInt(ts_open));
					totalActions = totalActions + 1;
				}
			
			}
			out.write(prevId.toString() + ":" + ((float)totaltime/(float)totalActions));
			out.write(Constants.NEW_LINE);
			out.close();
			System.out.println(prevId.toString());
			System.out.println(((float)totaltime/(float)totalActions));
		
		} catch (IOException e) {
			System.out.println("IO Error occured while calculating User Time Spent");
			throw e;
		}
		
		
	}

}
