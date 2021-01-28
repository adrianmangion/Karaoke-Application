import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.Group; 
import javafx.event.ActionEvent;
import java.io.File;
import javafx.stage.FileChooser;
import javafx.geometry.*;
import javafx.event.*;

public class View extends Application{

    String path = "C:/Users/Adrian/OneDrive - Middlesex University/Middlesex University/Karaoke Application/src/resources/sample.txt";
    SongLibrary currentLibrary;

    public static void main(String[] args){
        launch();
    }

    private void setCurrentLibrary(SongLibrary currentLibrary){
		this.currentLibrary = currentLibrary;
    }

    @Override
    public void start(Stage primaryStage){

		// Variables
		Stage window = primaryStage;
		ViewHelper helper = new ViewHelper();

		// Set window title
		window.setTitle("Karaoke Application");
		
		// Table views
		TableView<Song> librarySongsTable    = new TableView<Song>();
		TableView<Song> currentPlaylistTable = new TableView<Song>();
		// Main Layout
		BorderPane borderPane = new BorderPane();

		// Library Section
		GridPane tempLayout = new GridPane();
		
		// Set currentLibrary
		SongLibrary currentLibrary = new SongLibrary(this.path);

		// Update songLibrary <List>
		currentLibrary.getLibraryList();
		setCurrentLibrary(currentLibrary);

		GridPane library = helper.setLibrarySection(tempLayout, currentLibrary, librarySongsTable, borderPane);


		// Menu Bar
		Menu file = new Menu("File");
		MenuBar menuBar = new MenuBar();
		MenuItem importLibrary = new MenuItem("Import Library");
		file.getItems().add(importLibrary);
		menuBar.getMenus().add(file);

		importLibrary.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				library.getChildren().remove(librarySongsTable);
				FileChooser chooser = new FileChooser();
				File selectedFile = chooser.showOpenDialog(window);
				path = selectedFile.getAbsolutePath();
				SongLibrary songLibrary = new SongLibrary(path);
				setCurrentLibrary(songLibrary);
				TableView<Song> librarySongsTable = new TableView<Song>();
							
				// Populate table
				librarySongsTable = helper.populateTable(songLibrary);
				library.add(librarySongsTable, 0, 2, 5, 1);
			}
	    });

		// Video Player Section
        Group  videoPlayerSection  = new Group();
		String videoPath = "test.mp4";
		videoPlayerSection = helper.setVideoSection(videoPath);	
		
		SplitPane left_splitPane = helper.setLeftSplitPane(currentPlaylistTable, borderPane);
		SplitPane right_splitPane = new SplitPane();
		right_splitPane.setOrientation(Orientation.VERTICAL);
		right_splitPane.setDividerPosition(1, 0.2);

		// Adjust table width
		librarySongsTable.prefWidthProperty().bind(right_splitPane.widthProperty()); 
	
		// Add panes to split panes
		right_splitPane.getItems().addAll(videoPlayerSection, library);

		// Add items to border pane layout
		borderPane.setTop(menuBar);
		borderPane.setLeft(left_splitPane);
		borderPane.setCenter(right_splitPane);

		Scene scene = new Scene(borderPane, 1920, 1000);
		window.setScene(scene);
		window.show();	

    }
}