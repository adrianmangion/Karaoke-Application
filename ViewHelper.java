import javafx.scene.Group; 
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.scene.text.Font;
import java.io.File;
import javafx.scene.media.*;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.geometry.*;
import javafx.event.*;
import java.util.*;

public class ViewHelper{

	private Song selectedSong;
	private Song selectedPlaylistSong;
	private TableView<Song> globalPlaylist;
	public MediaPlayer mainMP;
	public MediaView mainMV; 

    public ViewHelper(){

    }

    /* Arranges current song section layout */
    public GridPane setCurrentSongSection(Song selectedSong){

		// Initialise layout
		GridPane layout = new GridPane();

		// Set spacing and size
		layout.setAlignment(Pos.CENTER);
		layout.setPadding(new Insets(10,10,10,10));
		layout.setHgap(10);
		layout.setVgap(10);
		layout.setMinWidth(200);
		layout.setMaxWidth(200);

		// Set columns to take 25% each
		ColumnConstraints col1 = new ColumnConstraints();
		ColumnConstraints col2 = new ColumnConstraints();
		ColumnConstraints col3 = new ColumnConstraints();
		ColumnConstraints col4 = new ColumnConstraints();
		col1.setPercentWidth(25);
		col2.setPercentWidth(25);
		col3.setPercentWidth(25);
		col4.setPercentWidth(25);
		layout.getColumnConstraints().addAll(col1, col2, col3, col4);

		// Initialise labels
		Label lblAppTitle        = new Label("Currently playing");
		Label lblArtist = new Label("Artist: No song selected");
		Label lblSong = new Label("Title: No song selected");;

		if(selectedSong.getTitle() != null){
			lblArtist          = new Label("Artist: " + selectedSong.getArtist());
			lblSong            = new Label("Title: " + selectedSong.getTitle());
		}

		

		// Put all controls in lists
		List<Label> labels = Arrays.asList(lblAppTitle,lblArtist,lblSong);

		// Style controls
		for(Label label : labels){
			label.setFont(new Font("Nunito", 14));
		}

		lblAppTitle.setFont(new Font("Nunito", 18));

		// Add labels to grid
		layout.add(lblAppTitle, 0, 0, 4, 1); // columnIndex, rowIndex, columnSpan, rowSpan;
		layout.add(lblArtist,   0, 1, 4, 1);
		layout.add(lblSong,     0, 2, 4, 1);
			
		return layout;	
    }

    public GridPane setLibrarySection(GridPane layout, SongLibrary songLibrary, TableView<Song> table, BorderPane root){

		// Set spacing and size
		layout.setAlignment(Pos.CENTER);
		layout.setPadding(new Insets(10,10,10,10));
		layout.setHgap(10);
		layout.setVgap(10);
		layout.setPrefSize(200, 400);

		// Set columns to take 20% each
		ColumnConstraints col1 = new ColumnConstraints();
		ColumnConstraints col2 = new ColumnConstraints();
		ColumnConstraints col3 = new ColumnConstraints();
		ColumnConstraints col4 = new ColumnConstraints();
		ColumnConstraints col5 = new ColumnConstraints();
		col1.setPercentWidth(20);
		col2.setPercentWidth(20);
		col3.setPercentWidth(20);
		col4.setPercentWidth(20);
		col5.setPercentWidth(20);
    	layout.getColumnConstraints().addAll(col1, col2, col3, col4, col5);

		// Initialise controls
		Label libraryLbl = new Label("Library");
		libraryLbl.setFont(new Font("Nunito", 14));
	
		TextField searchTxt = new TextField();	
		Button searchBtn = new Button("Search");
		Button addSongBtn   = new Button("Add song");
		Button addToPlaylistBtn = new Button("Add to playlist");

		// Button events
		addSongBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				// Create Form
				AddSongFrm addSongFrm = new AddSongFrm(songLibrary);
				
				// Open Form
				addSongFrm.open();
							
				if(!addSongFrm.checkStatus()){
					
					// Get updated library
					SongLibrary songLibrary = addSongFrm.getNewLibrary();
				
					// Populate table
					TableView<Song> newTable = populateTable(songLibrary);

					// Set table event handlers
					newTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
						if (newSelection != null) {
							setSelectedSong(newSelection);
						}
					});

					// Clear layout
					layout.getChildren().clear();

					// Rebuild layout
					layout.add(libraryLbl,    2, 0, 1, 1); // columnIndex, rowIndex, columnSpan, rowSpan;
					layout.add(searchTxt,     1, 1, 2, 1);
					layout.add(searchBtn,     3, 1, 1, 1);

					// Set horizontal alignment
					GridPane.setHalignment(addSongBtn, HPos.LEFT);
					layout.add(addSongBtn,    4, 1, 1, 1);

					// Set horizontal alignment
					GridPane.setHalignment(addToPlaylistBtn, HPos.CENTER);
					layout.add(addToPlaylistBtn, 4, 1, 1, 1);

					// Add table to layout
					layout.add(newTable, 0, 2, 5, 1);
				}
		}
	    });

		addToPlaylistBtn.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				songLibrary.addToPlaylist(selectedSong);
				globalPlaylist = populatePlaylist(songLibrary);
				TableView<Song> playlist = populatePlaylist(songLibrary);
				SplitPane sPane = setLeftSplitPane(playlist, root);
				root.setLeft(sPane);
			}
		});

		searchBtn.setOnAction(new EventHandler<ActionEvent>(){
		
			@Override
			public void handle(ActionEvent event) {

				// Store search query
				String search = searchTxt.getText();

				// Initialise resultSet
				ArrayList<Song> resultSet = new ArrayList<Song>();

				// Retrieve songs in current library
				ArrayList<Song> songList = songLibrary.getList();

				// Loop through songs in list
				long startTime = System.nanoTime(); // Cost: 1 
				for(Song song : songList){ // Cost:  N
					if(song.getTitle().contains(search)){ // Cost: N
						resultSet.add(song); // Cost: N
					}
				}
				long endTime = System.nanoTime(); // Cost: 1
				// Total cost: 3 x N
				// Order of growth: Linear

				double durationInSeconds = (double)(endTime - startTime)/1000000000;
				System.out.println(durationInSeconds);

				// Populate table
				TableView<Song> newTable = populateTable(resultSet);

				// Set table event handlers
				newTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
					if (newSelection != null) {
						setSelectedSong(newSelection);
					}
				});

				// Clear layout
				layout.getChildren().clear();

				// Rebuild layout
				layout.add(libraryLbl,    2, 0, 1, 1); // columnIndex, rowIndex, columnSpan, rowSpan;
				layout.add(searchTxt,     1, 1, 2, 1);
				layout.add(searchBtn,     3, 1, 1, 1);

				// Set horizontal alignment
				GridPane.setHalignment(addSongBtn, HPos.LEFT);
				layout.add(addSongBtn,    4, 1, 1, 1);

				// Set horizontal alignment
				GridPane.setHalignment(addToPlaylistBtn, HPos.CENTER);
				layout.add(addToPlaylistBtn, 4, 1, 1, 1);

				// Add table to layout
				layout.add(newTable, 0, 2, 5, 1);
			}
		});
	
		// Add labels to grid
		layout.add(libraryLbl,  2, 0, 1, 1); // columnIndex, rowIndex, columnSpan, rowSpan;
		layout.add(searchTxt,   1, 1, 2, 1);
		layout.add(searchBtn,   3, 1, 1, 1);

		// Set horizontal alignment
		GridPane.setHalignment(addSongBtn, HPos.LEFT);
		layout.add(addSongBtn,  4, 1, 1, 1);

		// Set horizontal alignment
		GridPane.setHalignment(addToPlaylistBtn, HPos.CENTER);
		layout.add(addToPlaylistBtn, 4, 1, 1, 1);

		table = populateTable(songLibrary);

		// Set table event handlers
		table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				setSelectedSong(newSelection);
			}
		});

		layout.add(table, 0, 2, 5, 1);

		// Set horizontal alignment
		GridPane.setHalignment(addSongBtn, HPos.LEFT);

		return layout;
    }

	public SplitPane setLeftSplitPane(TableView<Song> playlistTableView, BorderPane layout){

		// Split Panes
		SplitPane splitPane  = new SplitPane();
		splitPane.setOrientation(Orientation.VERTICAL);
		splitPane.setDividerPosition(1, 0.2);

		// Create Song object
		Song song = new Song();

		if(selectedPlaylistSong != null){
			song = selectedPlaylistSong;
		}

		// Grab first song in list
		try{
			song = playlistTableView.getItems().get(0);
		}
		catch(Exception ex){

		}

		// Current Song Section
		GridPane playControls = setCurrentSongSection(song);

		// Playlist sections
		GridPane playlist = setPlaylistSection(playlistTableView, layout);

		// Add panes to split panes
		splitPane.getItems().addAll(playControls, playlist);

		return splitPane;
	}

	public Group setVideoSection(String videoPath) {
		//Converts media to string URL
		Media media = new Media(new File("src/resources/"+videoPath).toURI().toString());

		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setAutoPlay(true);
		mainMP = mediaPlayer;
		MediaView mediaView = new MediaView(mediaPlayer);
		mainMV = mediaView;
		Group root = new Group();  
		root.getChildren().add(mainMV);

		return root;
    }

    public GridPane setPlaylistSection(TableView<Song> playlistTable, BorderPane mainLayout){

		//Initiate GridPane
		GridPane layout = new GridPane();

		// Set up table columns
		playlistTable = setColumns(playlistTable);

		// Set spacing and size
		GridPane.setHalignment(layout, HPos.CENTER);
		layout.setPadding(new Insets(10,10,10,10));
		layout.setHgap(10);
		layout.setVgap(10);
		layout.setPrefSize(450, 200);

		// Set columns to take 25% each
		ColumnConstraints col1 = new ColumnConstraints();
		ColumnConstraints col2 = new ColumnConstraints();
		ColumnConstraints col3 = new ColumnConstraints();
		ColumnConstraints col4 = new ColumnConstraints();
		col1.setPercentWidth(25);
		col2.setPercentWidth(25);
		col3.setPercentWidth(25);
		col4.setPercentWidth(25);
		layout.getColumnConstraints().addAll(col1, col2, col3, col4);

		Label currentPlaylistLbl = new Label("Current Playlist");
		currentPlaylistLbl.setFont(new Font("Nunito", 14));

		Button play = new Button("Play");
		Button pause = new Button("Pause");
		Button skip = new Button("Skip");
		Button remove = new Button("Remove");

		// Play Button Click 
		play.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {

				// Set media view section
				Group mediaSection = setVideoSection(selectedPlaylistSong.getVideoFileName());

				// Set currentSong section
				GridPane currentSongSection = setCurrentSongSection(selectedPlaylistSong);

				// Grab left_splitPane
				SplitPane splitPane = (SplitPane)mainLayout.getLeft();

				// Clear it
				splitPane.getItems().clear();

				// Reset it
				splitPane.getItems().addAll(currentSongSection, layout);

				// Add it to main layout
				mainLayout.setLeft(splitPane);

				// Grab right_splitPane
				SplitPane rightSplitPane = (SplitPane)mainLayout.getCenter();

				// Update media view
				rightSplitPane.getItems().set(0, mediaSection);
			}
		});

		// Pause Button Click 
		pause.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				// gets the status of the main Mediaplayer
				Status status = mainMP.getStatus();
				if(status == Status.PAUSED){
					mainMP.play();
					pause.setText("Pause");
				}
				else if(status == Status.PLAYING){
					mainMP.pause();
					pause.setText("Play");
				} 
			}
		});

		// Skip Button Click 
		skip.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				mainMP.stop();			
				//playlistTable.getItems().remove(0);			
				//playnext(playlistTable.getItems().get(0).getVideoFileName());
				mainMP.play();
			}
		});

		// Remove Button Click 
		remove.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				Object sender = event.getSource();
				Button button = (Button)sender;
				GridPane parent = (GridPane)button.getParent();
				TableView<Song> currentTable = (TableView<Song>)parent.getChildren().get(1);
				removeFromPlaylist(currentTable);
			}
		});

		// Set table event handlers
		playlistTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				setPlaylistSelectedSong(newSelection);
			}
		});

		layout.add(currentPlaylistLbl, 1, 0, 2, 1);
		layout.add(playlistTable, 0, 1, 4, 1);
		layout.add(play, 		  0, 2, 1, 1);
		layout.add(pause,		  1, 2, 1, 1);
		layout.add(skip,		  2, 2, 1, 1);
		layout.add(remove,		  3, 2, 1, 1);

		return layout;
	}

	public void playnext(String videoPath){

		//Converts media to string URL
		Media playnext = new Media(new File("src/resources/"+videoPath).toURI().toString());

		MediaPlayer mediaPlayer = new MediaPlayer(playnext);
		mainMP = mediaPlayer;

		mainMV.setMediaPlayer(mainMP);
	}
	
	public void removeFromPlaylist(TableView<Song> playlist){
		playlist.getItems().removeIf(Song -> (Song.getTitle() == selectedPlaylistSong.getTitle()));
	}

    public TableView<Song> setColumns(TableView<Song> table){
	
		if(table.getColumns().size() == 0){
			// Initialise Table Columns
			TableColumn<Song, String> titleColumn = new TableColumn<>("Title");
			titleColumn.setMinWidth(0.5);
			titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

			TableColumn<Song, String> artistColumn = new TableColumn<>("Artist");
			artistColumn.setMinWidth(0.4);
			artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));

			TableColumn<Song, Integer> playingTime = new TableColumn<>("Playing time");
			playingTime.setMinWidth(0.1);
			playingTime.setCellValueFactory(new PropertyValueFactory<Song, Integer>("playingTime"));

			table.getColumns().addAll(titleColumn, artistColumn, playingTime);
			table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		}
	
        return table;
    }

	public TableView<Song> populatePlaylist(SongLibrary songLibrary){
		TableView<Song> table = new TableView<Song>();
		table = setColumns(table);

		if(songLibrary != null){
			ArrayList<Song> songList = songLibrary.getPlayList();

			for(Song song : songList){
				table.getItems().add(song);
			}
		}

		return table;
	}

	public TableView<Song> populateTable(SongLibrary songLibrary){
		TableView<Song> librarySongsTable = new TableView<Song>();
		librarySongsTable = setColumns(librarySongsTable);

		if(songLibrary != null){
			ArrayList<Song> songList = songLibrary.getLibraryList();

			for(Song song : songList){
				librarySongsTable.getItems().add(song);
			}
		}

		return librarySongsTable;
	}

	public TableView<Song> populateTable(ArrayList<Song> songList){
		TableView<Song> librarySongsTable = new TableView<Song>();
		librarySongsTable = setColumns(librarySongsTable);

		if(songList != null){
			for(Song song : songList){
				librarySongsTable.getItems().add(song);
			}
		}

		return librarySongsTable;
	}

	//#region Private Methods
	private void setSelectedSong(Song selectedItem) {
		selectedSong = selectedItem;
	}

	private void setPlaylistSelectedSong(Song selectedItem){
		selectedPlaylistSong = selectedItem;
	}
	//#endregion

}
