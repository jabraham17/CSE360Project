import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class Main extends Application
{
    public void start(Stage primaryStage) throws IOException
    {
    	//load main pane from fxml
    	FXMLLoader loader = new FXMLLoader();
    	loader.setLocation(MainWindow.class.getResource("MainWindow.fxml"));
    	Pane mainPane = loader.load();
    	
    	//now we create a new dataset, errorlog, and log
    	//these will be shared across all panes
    	DataSet dataSet = new DataSet();
    	Log log = new Log();
    	
    	//now add them to the controller
    	MainWindow mainController = (MainWindow)loader.getController();
    	mainController.setDataSet(dataSet);
    	mainController.setLog(log);
        
        //create scene with loaded pane
        Scene scene = new Scene(mainPane, 600, 400);
        primaryStage.setTitle("Grade Analytics"); 
        primaryStage.setScene(scene);
        primaryStage.show();
             
    }
    public static void main(String[] args)
    {
        Application.launch(args);
    }
}
