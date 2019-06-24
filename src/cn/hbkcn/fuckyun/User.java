package cn.hbkcn.fuckyun;

import fuckyun.bean.FuckYunObject;

/**
 * 2019/6/8 11:53
 */
public class User extends FuckYunObject {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
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

    @Override
    public String toString() {
        return username + ": " + password;
    }
}
