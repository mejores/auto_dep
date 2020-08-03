package search;

/**
 * @ClassName Result
 * @Description
 * @Author Fernando Juan(joven)
 * @Date 7/17/2020 5:51 PM
 * @Version 1.0
 **/
public class Result {
    private String path;
    private boolean found;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }
}
