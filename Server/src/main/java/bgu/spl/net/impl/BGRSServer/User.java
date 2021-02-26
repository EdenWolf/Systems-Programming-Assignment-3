package bgu.spl.net.impl.BGRSServer;

import java.util.ArrayList;

public class User {
    private final String username;
    private final String password;
    private final boolean isAdmin;
    private ArrayList<Short> coursesList;
    private boolean isLogin;

    public User(String _username, String _password, boolean _isAdmin) {
        username = _username;
        password = _password;
        isAdmin = _isAdmin;
        coursesList = new ArrayList<>();
        isLogin = false;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() { return password; }

    public boolean compare(User other) {
        return password.equals(other.getPassword()) && username.equals(other.getUsername());
    }

    public ArrayList<Short> getCourses() {
        return coursesList;
    }

    public boolean isAdmin() { return isAdmin; }

    public synchronized void registerToCourse(short courseNumber) {
        coursesList.add(courseNumber);
    }

    public boolean isRegistered(short courseNumber) {
        return coursesList.contains(courseNumber);
    }

    public synchronized void unregister(short courseNumber) {
        coursesList.remove((Short)courseNumber);
    }

    public synchronized void setLogin(boolean login) { isLogin = login; }

    public boolean isLogin() { return isLogin; }

}
