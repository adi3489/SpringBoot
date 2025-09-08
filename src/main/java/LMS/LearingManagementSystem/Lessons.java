package LMS.LearingManagementSystem;

public class Lessons {
    private String title;
    private String contentDescription;
    private String videoUrl;

    public Lessons(String title, String contentDescription, String videoUrl) {
        this.title = title;
        this.contentDescription = contentDescription;
        this.videoUrl = videoUrl;
    }

    public String getTitle() { return title; }
    public String getContentDescription() { return contentDescription; }
    public String getVideoUrl() { return videoUrl; }

    public void setTitle(String title) { this.title = title; }
    public void setContentDescription(String contentDescription) { this.contentDescription = contentDescription; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
}
