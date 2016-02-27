package Bean;

public class Media {
    private String title;
    private String audiopath;
    
    public Media(String _title, String _audiopath) {
        title = _title;
        audiopath = _audiopath;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getPath() {
        return audiopath;
    }
}
