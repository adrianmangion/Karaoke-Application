import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javafx.stage.FileChooser;
import javafx.geometry.*;

public class AddSongFrm{

    private Boolean status;
    private SongLibrary currentLibrary;
    private SongLibrary newLibrary;
    private TableView<Song> newTable;
    private Stage stage = new Stage();
    private ViewHelper helper = new ViewHelper();

    public AddSongFrm(SongLibrary currentLibrary){
	this.currentLibrary = currentLibrary;
    }

    public SongLibrary getNewLibrary(){
	return this.newLibrary;
    }

    // Creates forms and displays it to user.
    public void open(){

	this.status = true;
    this.stage.setTitle("Add song");

	GridPane layout = new GridPane();
    layout.setPadding(new Insets(10,10,10,10));
    layout.setHgap(10);
    layout.setVgap(10);

	Label titleLbl       = new Label("Title");
	Label artistLbl      = new Label("Artist");
	Label playTimeLbl    = new Label("Playing Time");
    Label videoPathLbl   = new Label("Video Path");
    Label errorMsg       = new Label("");
    errorMsg.setTextFill(Color.FIREBRICK);

	TextField titleTxt     = new TextField();
	TextField artistTxt    = new TextField();
	TextField playTimeTxt  = new TextField();
	TextField videoPathTxt = new TextField();

	Button addBtn    = new Button("Confirm");
	Button cancelBtn = new Button("Cancel");
	Button dirBtn    = new Button("...");

	FileChooser chooser = new FileChooser();

	dirBtn.setOnAction(e -> {
	    	File selectedFile = chooser.showOpenDialog(stage);
		    videoPathTxt.setText(selectedFile.getAbsolutePath());
	    });

	addBtn.setOnAction(e -> {
		
		String  title     = titleTxt.getText();
		String  artist    = artistTxt.getText();
	    String  playTime  = playTimeTxt.getText();
		String  videoPath = videoPathTxt.getText();
		boolean valid     = true;

		// Put inputs in array
		String[] inputs = new String[]{ title, artist, playTime, videoPath };
		
		// Check each entry 
		for(String input : inputs){
		    if(input.trim().isEmpty()){
                valid = false;
                errorMsg.setText("Please fill in all fields and try again.");
		    }
		}

		// If no textfields are empty, create new song.
		if(valid){
            try{
                Song song = new Song(title, artist, Integer.parseInt(playTime), videoPath);
                this.newLibrary = this.currentLibrary;
                this.newLibrary.add(song);
                // Add song to notepad with bufferedWriter
                try(FileWriter fw = new FileWriter(newLibrary.getPath(), true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw))
                {
                    out.println(title+"\t"+artist+"\t"+Integer.parseInt(playTime)+"\t"+videoPath);
                } catch (IOException ex) {
                    
                }
                errorMsg.setTextFill(Color.GREEN);
                errorMsg.setText("Successfully added song");
                this.status = false;
                stage.close();
            }catch(NumberFormatException ex){
                errorMsg.setText("Playing time has to be a number.");
            }
		}

	});

        layout.add(titleLbl,     1, 0);
        layout.add(artistLbl,    1, 1);
        layout.add(playTimeLbl,  1, 2);
        layout.add(videoPathLbl, 1, 3);

        layout.add(titleTxt,     2, 0);
        layout.add(artistTxt,    2, 1);
        layout.add(playTimeTxt,  2, 2);
        layout.add(videoPathTxt, 2, 3);
        
        layout.add(errorMsg, 2, 4);

        layout.add(dirBtn,    3, 3);
        layout.add(cancelBtn, 2, 5);
        layout.add(addBtn,    3, 5);

        GridPane.setHalignment(cancelBtn, HPos.RIGHT);
        GridPane.setHalignment(addBtn, HPos.LEFT);

        Scene scene = new Scene(layout, 350, 225);
        this.stage.setScene(scene);
        this.stage.showAndWait();
    }

    public Boolean checkStatus(){
	    return this.status;
    }

}
