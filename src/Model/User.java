package Model;

public class User {
    public String username;
    public User(String username){
        this.username = username;
    }

    public User(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
