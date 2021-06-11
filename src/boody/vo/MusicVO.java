package boody.vo;

public class MusicVO {
	   //제목, 가수, 재생시간, 파일경로
	    private String title;
	    private String singer;
	    private String path;

	    public MusicVO(String title, String singer, String path) {
	        super();
	        this.title = title;
	        this.singer = singer;
	        this.path = path;
	    }

	    public String getTitle() {
	        return title;
	    }

	    public String getSinger() {
	        return singer;
	    }

	    public String getPath() {
	        return path;
	    }

	}