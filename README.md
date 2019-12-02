# Grade Analytics

The code for the CSE 360 Fall 2019 group project.

Written by Jacob Abraham, Sameer Channar, Matteo Chirco, and LT Tran


## Running in Eclipse

This repoository is can be imported as a Project into Eclipse with little difficulty. 
Note that this runs with Java 8 and JavaFX, so both are required.
Additionally, the `src` folder must be a source folder and the `resources` folder must be a resource folder.
This is so the code can find the `fxml` file to load the GUI.

## Building a executable jar

An executable jar can be built with the ANT commandline tool, by running `ant -f CSE360Project.xml`.
The jar can then be run with `java -jar GradeAnalysis.jar`.
Note: this assumes all required dependencies such as ANT, Java 8, and JavaFX
