package rubado.printeasy.Pojos;

/**
 * Created by Sol Rubado on 10/04/2017.
 */

public class LoginPojo {
    private String username;
    private String password;

    public LoginPojo(String username, String password){
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


