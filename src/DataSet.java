import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class DataSet {

	private ArrayList<Float> data;
	private float lowerBound;
	private float upperBound;
	private DecimalFormat df;

	// init all data
	public DataSet() {
		data = new ArrayList<Float>();
		lowerBound = 0;
		upperBound = 100;
		df = new DecimalFormat("########.###");
	}

	// append the following data from the file to the data set
	public void loadFile(File file, Log log) {
		String filename = file.getName();
		log.log("Loading file " + filename);
		String ext = filename.substring(filename.length() - 4);
		
		//size before new data
		int prevSize = data.size();
		
		//if a txt file, add as txt
		if(ext.equals(".txt")) {
			readTXTFile(file, log);
		}
		//if a csv file, add as csv
		else if(ext.equals(".csv")) {
			readCSVFile(file, log);
		}
		//otherwise, error
		else {
			log.logError("Invalid file type");
		}
		
		int numLoaded = data.size() - prevSize;
		log.log("Loaded " + numLoaded + " new entries into data set");
	}
	
	private void readCSVFile(File file, Log log) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			//read input line by line
			String line = "";
			int linenum = 0;
			while((line = br.readLine()) != null) {
				linenum++;
				//for each comma seperated value
				String[] elements = line.split(",");
				for(String elm: elements) {
					//convert to float,
					Float converted = stringToFloat(elm, log);
					if(converted != null) {
						//if out of range, error
						if(converted < lowerBound || converted > upperBound) {
							log.logError(String.format(
								"Input <%s> at line <%s> in file <%s> "
								+ "is out of range [%s,%s]", 
								converted,
								linenum,
								file.getName(),
								df.format(lowerBound), 
								df.format(upperBound)));
						}
						else {
							insert(converted);
						}
					}
					else {
						log.logError(String.format(
								"Cannot convert <%s> at line <%s> in file <%s>:"
								+ " not a valid number",
								elm, linenum, file.getName()));
					}
				}
			}
			
			br.close();
		}
		catch(IOException e) {
			log.logError("Could not read input file " + file.getName());
		}
	}

	//read in a file as a txt file
	private void readTXTFile(File file, Log log) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			//read input line by line
			String input = "";
			int linenum = 0;
			while((input = br.readLine()) != null) {
				linenum++;
				//convert to float, if null error was handled, just ignore
				Float converted = stringToFloat(input, log);
				if(converted != null) {
					//if out of range, error
					if(converted < lowerBound || converted > upperBound) {
						log.logError(String.format(
								"Input <%s> at line <%s> in file <%s> "
								+ "is out of range [%s,%s]", 
								converted,
								linenum,
								file.getName(),
								df.format(lowerBound), 
								df.format(upperBound)));
					}
					else {
						insert(converted);
					}
				}
				else {
					log.logError(String.format(
							"Cannot convert <%s> at line <%s> in file <%s>:"
							+ " not a valid number",
							input, linenum, file.getName()));
				}
			}
			
			br.close();
		}
		catch(IOException e) {
			log.logError("Could not read input file " + file.getName());
		}
	}

	// insert the new float in sorted order into the array
	private void insert(float f) {
		// need to find index
		int i = 0;
		boolean inserted = false;
		while (i < data.size() && !inserted) {
			// if its bigger than current index, insert before the index
			if (f > data.get(i)) {
				data.add(i, f);
				inserted = true;
			}
			i++;
		}
		// if still not inserted, append to end
		if (!inserted) {
			data.add(f);
		}
	}

	//convert a string to a float within a certain range
	private Float stringToFloat(String input, Log log) {
		
		Float converted = null;
		
		try {
			//convert to float
			converted = Float.parseFloat(input);
		}
		catch(NumberFormatException e) {
			converted = null;
		}
		
		return converted;
	}

	// add new data
	public void addData(String input, Log log) {
		log.log("Inserting <" + input +">");
		//convert to float, if null error was handled, just ignore
		Float converted = stringToFloat(input, log);
		if(converted != null) {
			//if out of range, error
			if(converted < lowerBound || converted > upperBound) {
				log.logError(String.format(
						"Input <%s> from keyboard is out of range [%s,%s]", 
						converted,
						df.format(lowerBound), df.format(upperBound)));
			}
			else {
				insert(converted);
			}
		}
		else {
			log.logError("Cannot convert <" + input + 
					"> from keyboard: not a valid number");
		}
	}

	//comapre two floats within a tolerance
	private boolean floatEquals(float f1, float f2) {
		float tolerance = 0.001f;
		return Math.abs(f1 - f2) <= tolerance;
	}
	//find the first index of a number in a descended order list
	private int firstIndex(Float f) {
		int i = 0;
		int index = -1;
		boolean found = false;
		while(i < data.size() && !found) {
			//check if equals, within tolerance
			if(floatEquals(data.get(i), f)) {
				index = i;
				found = true;
			}
			i++;
		}
		return index;
	}
	
	// remove the first occurrence of data
	public void removeData(String input, Log log) {
		log.log("Removing <" + input +">");
		//convert to float, if null error was handled, just ignore
		Float converted = stringToFloat(input, log);
		if(converted != null) {
			//if out of range, error
			if(converted < lowerBound || converted > upperBound) {
				log.logError(String.format(
						"Input <%s> from keyboard is out of range [%s,%s]", 
						converted,
						df.format(lowerBound), 
						df.format(upperBound)));
			}
			else {
				int index = firstIndex(converted);
				if(index >= 0) {
					data.remove(index);
				}
				else {
					//does not exists, error
					log.logError(String.format(
							"Cannot remove <%s>: does not exist", 
							df.format(converted)));
				}
			}
		}
		else {
			log.logError("Cannot convert <" + input + 
					"> from keyboard: not a valid number");
		}
	}
	
	//find the mean by summing then dividing by the size
	private float mean() {
		float sum = 0;
		for(Float f: data) {
			sum += f;
		}
		return sum / data.size();
	}
	//find the median, if odd just the middle, 
	//if even the avg of the two middle values
	//assume list has data
	private float median() {
		float median = 0;
		int middle = data.size() / 2;
		//odd case, just the middle
		if((data.size() & 1) == 1) {
			median = data.get(data.size() / 2);
		}
		//even case, avg of middle
		else {
			median = (data.get(middle-1) + data.get(middle)) / 2;
		}
		return median;
	}
	//find the mode of the data
	//use a hash map to keep track of previous counts
	//this improves the running time from O(n^2) to O(n) 
	//no mode occurs if everthing has the same frequency
	//can have more than one mode
	//assume data has more at least 1 element
	private ArrayList<Float> mode() {
		//store frequencys in a hashmap
		HashMap<Float, Integer> freq = new HashMap<Float, Integer>();
		int maxCount = 0;
		for(Float f: data) {
			//get the new float from the hm
			//add one because we just found one
			int count = freq.getOrDefault(f, 0) + 1;
			//update max count
			if(count > maxCount) {
				maxCount = count;
			}
			//now put back in hash map
			freq.put(f, count);
		}
		ArrayList<Float> mode = new ArrayList<Float>();
		//need to find all values that have a frequency of maxCount
		//this loop will also check if the frequency is uniform
		int prevFreq = freq.get(data.get(0));
		boolean uniform = true;
		for(Entry<Float, Integer> entry: freq.entrySet()) {
			if(entry.getValue() == maxCount) {
				mode.add(entry.getKey());
			}
			//if two there is one freq not the same then it is not uniform 
			if(prevFreq != entry.getValue()) {
				uniform = false;
			}
			prevFreq = entry.getValue();
		}
		
		//if data is uniform, no mode
		if(uniform) {
			mode.clear();
		}
		
		return mode;
	}

	// analyze the data set
	public String analyze(Log log) {
		
		log.log("Analyzing data set");
		
		String output = "";
		
		//if no data, nothing to do
		if(data.isEmpty()) {
			output = "No Data";
			log.logError("No data to analyze");
		}
		else {
			int size = data.size();
			float high = data.get(0);
			float low = data.get(size - 1);
			float mean = mean();
			float median = median();
			ArrayList<Float> mode = mode();
			
			output += String.format("Number of entries: %s\n", size);
			output += String.format("High: %s\n", df.format(high));
			output += String.format("Low: %s\n", df.format(low));
			output += String.format("Mean: %s\n", df.format(mean));
			output += String.format("Median: %s\n", df.format(median));
			output += "Mode: ";
			//if no mode, say so
			if(mode.isEmpty()) {
				output += "N/A";
			}
			else {
				output += mode
						.stream()
						.map(df::format)
						.collect(Collectors.joining(", "));
			}
			output += "\n";
		}
		log.log(output);
		return output;
	}
	
	//calculate the percentages for the data based on the currently set bounds
	private ArrayList<Float> calculatePercentage() {
		ArrayList<Float> percent = new ArrayList<Float>();
		float diff = upperBound - lowerBound;
		for(int i = 0; i < data.size(); i++) {
			percent.add(((data.get(i) - lowerBound) * 100) / diff);
		}
		return percent;
	}
	//return a subset of the percentage array in a certain range
	private ArrayList<Float> percentageInRange(ArrayList<Float> percents, 
												Float lower, 
												Float upper) {
		ArrayList<Float> inRange = new ArrayList<Float>();
		for(int i = 0; i < percents.size(); i++) {
			//if in range, add to inRange
			if((percents.get(i) < upper) && (percents.get(i) >= lower)) {
				inRange.add(percents.get(i));
			}
		}
		return inRange;
	}

	// graph the dataset, in ascii
	public String graph(Log log) {
		log.log("Graphing data set");
		
		String output = ""; 
		if(data.isEmpty()) {
			output = "No Data";
			log.logError("No data to display");
		}
		else {
			//calculate the percentages
			ArrayList<Float> percents = calculatePercentage();
			ArrayList<Float> inRange = null;
			int[] upper = {100,89,79,69,59,49,39,29,19,9};
			int[] lower = {90,80,70,60,50,40,30,20,10,0};
			int maxSize = 0;
			//print graph
			for(int i = 0; i < 10; i++) {
				//print range
				output += String.format("%3d%% - %2d%%|", upper[i], lower[i]);
				//use the rightPad function to pad an empty string 
				//with the number of percents
				inRange = percentageInRange(percents, 
											(float)lower[i], 
											(float)upper[i]+1);
				output += rightPad("", inRange.size(), "***");
				output += "\n";
				//compute max bar graph
				if(inRange.size() > maxSize) {
					maxSize = inRange.size();
				}
			}
			//now print x axis
			output += "-----------+";
			String label = "           0";
			for(int i = 1; i <= maxSize+4; i++) {
				output += "--+";
				label += String.format("%3d", i);
			}
			output += "-----";
			output += "\n";
			output += label;
			output += "\n";
			
		}
		
		log.log(output);
		return output;
	}

	// return the distribution data of the data set
	public String distribution(Log log) {
		
		log.log("Showing data set distribution");
		
		String output = ""; 
		if(data.isEmpty()) {
			output = "No Data";
			log.logError("No data to display");
		}
		else {
			//calculate the percentages
			ArrayList<Float> percents = calculatePercentage();
			ArrayList<Float> inRange = null;
			int[] upper = {100,89,79,69,59,49,39,29,19,9};
			int[] lower = {90,80,70,60,50,40,30,20,10,0};
			//print distributions
			for(int i = 0; i < 10; i++) {
				//print range
				output += String.format("[%3d%% - %2d%%]:", upper[i], lower[i]);
				
				
				inRange = percentageInRange(percents, 
											(float)lower[i], 
											(float)upper[i]+1);
				output += " ";
				//calc percent of numbers in that range
				float percent = ((float)inRange.size() / (float)percents.size()) * 100.0f;
				//do rounding
				percent *= 100;
				percent = Math.round(percent);
				percent /= 100;
				
				output += String.format("%.2f", percent);
				output += "%";
				output += "\n";
			}
		}
		
		log.log(output);
		return output;
	}

	//pad a str on the left so that it takes up `space`
	private String rightPad(String in, int space, String pad) {
		String out = in;
		for(int i = 0; i < space - in.length(); i++) {
			out += pad;
		}
		return out;
	}
	
	// return the data in 4 columns
	public String data(Log log) {
		
		log.log("Displaying data set");
		
		String output = "";
		
		//if no data, nothing to do
		if(data.isEmpty()) {
			output = "No Data";
			log.logError("No data to display");
		}
		else {
			
			int elmsPerList = data.size() / 4;
			//sublists for each column
			List<Float> one = new ArrayList<Float>();
			List<Float> two = new ArrayList<Float>();
			List<Float> three = new ArrayList<Float>();
			List<Float> four = new ArrayList<Float>();
			//if remainder is 1, put extra in one
			//if remainder is 2, put extra in one and two
			//if remainder is 3, put extra in one, two, and three
			int remainder = data.size() % 4;
			if(remainder == 1) {
				one = data.subList(0, elmsPerList+1);
				//only need to check index for sublist 2
				if(2*elmsPerList+1 <= data.size()) {
					two = data.subList(elmsPerList+1, 2*elmsPerList+1);
					three = data.subList(2*elmsPerList+1, 3*elmsPerList+1);
					four = data.subList(3*elmsPerList+1, data.size());
				}
			}
			else if(remainder == 2) {
				one = data.subList(0, elmsPerList+1);
				two = data.subList(elmsPerList+1, 2*elmsPerList+2);
				//only need to check index for sublist 3
				if(3*elmsPerList+2 <= data.size()) {
					three = data.subList(2*elmsPerList+2, 3*elmsPerList+2);
					four = data.subList(3*elmsPerList+2, data.size());
				}
			}
			else if(remainder == 3) {
				one = data.subList(0, elmsPerList+1);
				two = data.subList(elmsPerList+1, 2*elmsPerList+2);
				three = data.subList(2*elmsPerList+2, 3*elmsPerList+3);
				//no need for index check, data.size() is always in data
				four = data.subList(3*elmsPerList+3, data.size());
			}
			else {
				one = data.subList(0, elmsPerList);
				two = data.subList(elmsPerList, 2*elmsPerList);
				three = data.subList(2*elmsPerList, 3*elmsPerList);
				four = data.subList(3*elmsPerList, 4*elmsPerList);
			}
			
			//for each row, print the column
			for(int i = 0; i < one.size(); i++) {
				//only use each index if valid
				String out1 = df.format(one.get(i));
				String out2 = "";
				String out3 = "";
				String out4 = "";
				if(i < two.size()) {
					out2 = df.format(two.get(i));
				}
				if(i < three.size()) {
					out3 = df.format(three.get(i));
				}
				if(i < four.size()) {
					out4 = df.format(four.get(i));
				}
				output += rightPad(out1, 15, " ") + " ";
				output += rightPad(out2, 15, " ") + " ";
				output += rightPad(out3, 15, " ") + " ";
				output += rightPad(out4, 15, " ");
				output += "\n";
			}
		}
		
		log.log(output);
		
		return output;
	}
	
	//removes all data not in bounds
	private void throwOutData(Log log) {
		//loop through backwards so we can safely delete
		for(int i = data.size() - 1; i >= 0; i--) {
			float num = data.get(i);
			//if out of bounds, throw out with error
			if(num < lowerBound || num > upperBound) {
				data.remove(i);
				log.logError("LOSS OF DATA: Due to new bounds, <" 
							+ df.format(num) 
							+ "> has been thrown out of data set");
			}
		}
	}

	// set the lower bound
	public void setLowerBound(String input, Log log) {
		log.log("Setting lower bound to <" + input + ">");
		//convert to float, if null error was handled, just ignore
		Float converted = stringToFloat(input, log);
		if(converted != null) {
			//new lower bound must be less than upper bound
			if(converted < upperBound) {
				lowerBound = converted;
				throwOutData(log);
			}
			else {
				log.logError("Lower bound must be less than upper bound");
			}
		}
		else {
			log.logError("Cannot convert <" + input + 
					"> from keyboard: not a valid number");
		}
	}

	// set the upper bound
	public void setUpperBound(String input, Log log) {
		log.log("Setting upper bound to <" + input + ">");
		//convert to float, if null error was handled, just ignore
		Float converted = stringToFloat(input, log);
		if(converted != null) {
			//new upper bound must be greater than lower bound
			if(converted > lowerBound) {
				upperBound = converted;
				throwOutData(log);
			}
			else {
				log.logError("Upper bound must be greater than lower bound");
			}
		}
		else {
			log.logError("Cannot convert <" + input + 
					"> from keyboard: not a valid number");
		}
	}

	// get the lower bound
	public float getLowerBound() {
		return lowerBound;
	}

	// get the upper bound
	public float getUpperBound() {
		return upperBound;
	}

	// clear the data set
	public void clear() {
		data.clear();
	}
}
