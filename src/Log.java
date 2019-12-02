import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

//keep track of all actions and output of actions
public class Log {

	//contains all actions and errors
	private ArrayList<String> actions;
	//contains only errors
	private ArrayList<String> errors;
	
	//init all data
	public Log() {
		actions = new ArrayList<String>();
		errors = new ArrayList<String>();
	}
	
	//log a new error event
	public void logError(String errorMessage) {
		errorMessage = "ERROR: " + errorMessage.trim();
		errors.add(errorMessage);
		actions.add(errorMessage);
	}
	
	//append new message to the log
	public void log(String message) {
		message = message.trim();
		actions.add(message);
	}
	
	//output the log to a file as a report
	public void createReport(File outputFile) {
		log("Writing report to file " + outputFile.getName());
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
			//write each log action/error to output file
			for(String s: actions) {
				bw.write(s);
				bw.newLine();
			}
			bw.close();
		}
		catch(IOException e) {
			logError("Could not write output file " + outputFile.getName());
		}
	}
	//return a listing of only the error
	public String errorLog() {
		return String.join("\n", errors);
	}
	
	
	//clear the log
	public void clear() {
		actions.clear();
		errors.clear();
	}
}
