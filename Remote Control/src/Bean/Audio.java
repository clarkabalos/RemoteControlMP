package Bean;

public class Audio {
    private String title;
    private String audiopath;
    
    public Audio(String _title, String _audiopath) {
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
