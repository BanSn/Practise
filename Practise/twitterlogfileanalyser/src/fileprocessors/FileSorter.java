package fileprocessors;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import util.Constants;

public class FileSorter {
	
	//key: UserId value:List of Log Lines
	private Map<String,List<String>> sortMap;
	
	public void sortFiles(String outputDir) throws IOException{
		
		File outputDir_f = new File(outputDir);
		for(File f: outputDir_f.listFiles()) {
			System.out.println("Sorting File " + f.getName());
			sortSingleFile(f,outputDir);
		}
	}
	
	private void sortSingleFile(File f, String outputDir) throws IOException{
		
		sortMap =  new TreeMap<String,List<String>>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(f));
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				String userId = sCurrentLine.split(",")[0];
				if(userId!=null){
					if(sortMap.get(userId)!=null){
						sortMap.get(userId).add(sCurrentLine);
					}else{
						List<String> temp = new ArrayList<String>();
						temp.add(sCurrentLine);
						sortMap.put(userId,temp);
					}
				}
			}
			
			BufferedWriter out = new BufferedWriter(new FileWriter(outputDir + Constants.FILE_SEPERATOR + f.getName() +  Constants._SORTED));
			for(String key:sortMap.keySet()){
				for(String temp:sortMap.get(key)){
					out.write(temp);
					out.write(Constants.NEW_LINE);
					System.out.println(sortMap.get(key));
				}		
			}
			out.close();
		} catch (FileNotFoundException e) {
			System.out.println("Input Files not found while Sorting Files");
			throw e;
		} catch (IOException e) {
			System.out.println("Read/Write Error occured while Sorting Files ");
			throw e;
		}
		
	}
	
	
	
	
	
	

}
