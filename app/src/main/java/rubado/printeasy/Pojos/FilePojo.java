package rubado.printeasy.Pojos;

/**
 * Created by MariaSol on 06/09/2016.
 */
public class FilePojo {
    private String _id;
    private String name;
    private int username;
    private boolean isPrinted;
    private String printed_at;

    public FilePojo (String fileName, int username, boolean alreadyPrinted){
        this.setname(fileName);
        this.setAlreadyPrinted(alreadyPrinted);
        this.setUsername(username);
    }


    public String getFileName() {
        return name;
    }

    public void setname(String fileName) {
        this.name = fileName;
    }

    public boolean isAlreadyPrinted() {
        return isPrinted;
    }

    public void setAlreadyPrinted(boolean alreadyPrinted) {
        this.isPrinted = alreadyPrinted;
    }

    public int getUsername() {
        return username;
    }

    public void setUsername(int username) {
        this.username = username;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getPrinted_at() {
        return printed_at;
    }

    public void setPrinted_at(String printed_at) {
        this.printed_at = printed_at;
    }
}
