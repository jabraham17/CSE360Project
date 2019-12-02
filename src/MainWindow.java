import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainWindow implements Initializable {
	
	@FXML
	private BorderPane mainWindowPane;
	
	//File
	@FXML
	private MenuItem loadNewFileButton;
	@FXML
	private MenuItem loadAppendFileButton;
	@FXML
	private MenuItem createReportButton;
	
	
	//get a file from a file chooser for input
	private File getInputFile() {
		//setup file input dialog
    	FileChooser fc = new FileChooser();
    	ExtensionFilter filter = new ExtensionFilter("Text/CSV File", "*.txt", "*.csv");
    	fc.getExtensionFilters().add(filter);
    	
    	//get file, if any
    	File file = fc.showOpenDialog(mainWindowPane.getScene().getWindow());
    	return file;
	}
	//get a file from a file chooser for output
	private File getOutputFile() {
		//setup file input dialog
    	FileChooser fc = new FileChooser();
    	ExtensionFilter filter = new ExtensionFilter("Text File", "*.txt");
    	fc.getExtensionFilters().add(filter);
    	fc.setInitialFileName("report.txt");
    	
    	//get file, if any
    	File file = fc.showSaveDialog(mainWindowPane.getScene().getWindow());
    	return file;
	}
	
    @FXML
    private void loadNewFileAction(ActionEvent event) {
    	File file = getInputFile();
        if(file != null) {
        	dataSet.clear();
        	log.clear();
        	dataSet.loadFile(file, log);
        }
        //cancel pressed
    }
    @FXML
    private void loadAppendFileAction(ActionEvent event) {
    	File file = getInputFile();
        if(file != null) {
        	dataSet.loadFile(file, log);
        }
        //cancel pressed
    }
    @FXML
    private void createReportAction(ActionEvent event) {
    	File file = getOutputFile();
        if(file != null) {
        	log.createReport(file);
        }
        //cancel pressed
    }
    
    //Data
    @FXML
	private MenuItem addButton;
    @FXML
    private MenuItem deleteButton;
    @FXML
    private MenuItem lowerBoundButton;
    @FXML
    private MenuItem upperBoundButton;
    
    //create a dialog box and return the user input from it
    private String inputDialog(String title, String header) {
    	//setup text input dialog
        TextInputDialog input = new TextInputDialog();
        input.setHeaderText(header);
        input.setTitle(title);
        input.setGraphic(null);
        
        //get user input, if any
        Optional<String> userInput = input.showAndWait();
        return userInput.isPresent() ? userInput.get() : null;
    }
    
    @FXML
    private void addAction(ActionEvent event) {
    	//get user input
        String input = inputDialog("Input", "Input number to add to data set");
        
        if(input != null) {
        	dataSet.addData(input, log);
        }
        //cancel pressed
    }
    @FXML
    private void deleteAction(ActionEvent event) {
    	//get user input
        String input = inputDialog("Delete", "Input number to delete from data set");
        
        if(input != null) {
        	dataSet.removeData(input, log);
        }
        //cancel pressed
    }
    @FXML
    private void lowerBoundAction(ActionEvent event) {
    	//get user input
        String input = inputDialog("Lower Bound", "Input new lower bound");
        
        if(input != null) {
        	dataSet.setLowerBound(input, log);
        	lowerBoundButton.setText(String.format("Lower Bound: %.0f", dataSet.getLowerBound()));
        }
        //cancel pressed
    }
    @FXML
    private void upperBoundAction(ActionEvent event) {
    	//get user input
        String input = inputDialog("Upper Bound", "Input new upper bound");
        
        if(input != null) {
        	dataSet.setUpperBound(input, log);
        	upperBoundButton.setText(String.format("Upper Bound: %.0f", dataSet.getUpperBound()));
        }
        //cancel pressed
    }
    
    //Display
    @FXML
	private MenuItem analyzeButton;
    @FXML
    private MenuItem displayDataButton;
    @FXML
    private MenuItem displayGraphButton;
    @FXML
    private MenuItem displayDistributionButton;
    @FXML
    private MenuItem displayErrorLogButton;
    
    @FXML
    private void analyzeAction(ActionEvent event) {
    	String output = dataSet.analyze(log);
    	textArea.setText(output);
    }
    @FXML
    private void displayDataAction(ActionEvent event) {
        String output = dataSet.data(log);
        textArea.setText(output);
    }
    @FXML
    private void displayGraphAction(ActionEvent event) {
    	 String output = dataSet.graph(log);
         textArea.setText(output);
    }
    @FXML
    private void displayDistributionAction(ActionEvent event) {
    	 String output = dataSet.distribution(log);
         textArea.setText(output);
    }
    @FXML
    private void displayErrorLogAction(ActionEvent event) {
    	String output = log.errorLog();
    	textArea.setText(output);
    }
    
    //textarea
    @FXML
    private TextArea textArea;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//initial setup
		
	}
	
	
	//access to the dataset
	private DataSet dataSet;
	public DataSet getDataSet() {
		return dataSet;
	}
	public void setDataSet(DataSet dataSet) {
		this.dataSet = dataSet;
	}
	
	//access to the log file
	private Log log;
	public Log getLog() {
		return log;
	}
	public void setLog(Log log) {
		this.log = log;
	}
}
