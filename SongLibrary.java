/********************* SongLibrary class *****************
 *                                                       *
 *        search(title) - add(song) - queue(song)        *
 *        retrieve(song) - delete(song)                  *
 *********************************************************/

import javafx.scene.control.Alert;
import java.util.ArrayList;
import java.io.*;

class SongLibrary{

    private String path;
    private ArrayList<Song> playList = new ArrayList<Song>();
    private ArrayList<String> titleList = new ArrayList<String>();
    private ArrayList<Song> libraryList = new ArrayList<Song>();

    SongLibrary(String path){
        this.path = path;
    }

    public ArrayList<Song> getPlayList(){
        return playList;
    }

    public void setLibraryList(ArrayList<Song> list){
	    this.libraryList = list;
    }

    public String getPath(){
        return path;
    }

    public ArrayList<Song> getList(){
        return libraryList;
    }

    public ArrayList<String> getTitleList(){
        return titleList;
    }

    // Methods
    public ArrayList<Song> getLibraryList(){
	try{
	    File file = new File(this.path);
	    BufferedReader br = new BufferedReader(new FileReader(file));

	    String st = br.readLine();
	    while (st != null){
		if(st.length() > 0){
            // songDetails[0] = Title
            // songDetails[1] = Artist
            // songDetails[2] = PlayTime
            // songDetails[3] = Video Path
		    String[] songDetails = st.split("\t", 4); 
		    Song song = new Song(songDetails[0], songDetails[1], Integer.parseInt(songDetails[2]), songDetails[3]);
            libraryList.add(song);
            titleList.add(songDetails[0]);
		    st = br.readLine();
		}
        }
        br.close();
	    return libraryList;
	    
	}catch(Exception ex){
	    new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
	    return null;
	}
    }
    
    public void add(Song song){
	    libraryList.add(song);
    }

    public void addToPlaylist(Song song){
        this.playList.add(song);
    }
    
}
