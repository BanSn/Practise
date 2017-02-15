package driver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import analyser.UserActionAnalyser;
import fileprocessors.FileSorter;
import fileprocessors.FileSplittter;
import util.Constants;

public class LogFileAnalyserDriver {

	
	public static void main(String[] args) {
		
		LogFileAnalyserDriver logfileObj = new LogFileAnalyserDriver();
		String inputFile="";
		String inputDir="";
		
		if(args.length==3){
			inputFile = args[1];
			inputDir = args[2];
			
		}else if(args.length==2){
			inputFile = args[1];
			
		}
		
		if(inputFile==null || inputFile.length()==0){
			inputFile = logfileObj.getFilefromResources(Constants.LOG_FILE_NAME);
		}
		
		if(inputDir==null || inputDir.length()==0){
			inputDir = Constants.TEMP_DIRECTORY;
		}
		
		System.out.println("Using "+ inputFile + " as Input File");
		System.out.println("Using "+ inputDir + " as Input Directory");
	
		FileSplittter fs = new FileSplittter();
		try {
			fs.split(inputFile, inputDir);
		}
		catch (FileNotFoundException e){
			System.out.println("Error Occured while Splitting the File");
		}
		catch (IOException e){
			System.out.println("IO Error Occured while Splitting the File");
		}
		
		FileSorter fs_sort = new FileSorter();
		try {
			fs_sort.sortFiles(fs.getOutputDir());
		} catch (FileNotFoundException e) {
			System.out.println("Error Occured while Sorting the File");
		}
		catch (IOException e){
			System.out.println("IO Error Occured while Sorting the File");
		}
		
		UserActionAnalyser uaa = new UserActionAnalyser();
		try {
			uaa.getAverageSpendTime(fs.getOutputDir());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

		private String getRecourseDirectory(String inputFile) {
			ClassLoader classLoader = getClass().getClassLoader();
			String resDir = new File(classLoader.getResource(inputFile).getFile()).getParent();
			return resDir;
	}

		private  String getFilefromResources(String resourceName) {
		
		ClassLoader classLoader = getClass().getClassLoader();
		return classLoader.getResource(resourceName).getPath();
		
	}

}
