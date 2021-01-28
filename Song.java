/******************** Song class *************************
 *     title - artist - playingTime - videoFileName      *
 *     play()   -   pause()   -   stop()  -   skip()     *
 *********************************************************/

public class Song{

    // Properties
    private String Title;
    private String artist;
    private String videoFileName;
    private int playingTime;

    // Getters and Setters
	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		this.Title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
    }
    
    public String getVideoFileName(){
        return videoFileName;
    }

    public void setVideoFileName(String fileName){
        this.videoFileName = fileName;
    }

    public int getPlayingTime() {
	return playingTime;
    }

    public void setPlayingTime(int playingTime) {
	this.playingTime = playingTime;
    }
    
    // Constructor
    /**
     * @param playingTime in seconds
     */
    Song(String title, String artist, int playingTime, String videoFileName){
        this.Title = title;
        this.artist = artist;
        this.videoFileName = videoFileName;
        this.playingTime = playingTime;
    }

    Song(){

    }

}
