package net.lcadsl.qintalker.factory.model.api.account;

import net.lcadsl.qintalker.factory.model.db.User;

public class AccountRspModel {

    private User user;

    private String account;

    private String token;

    private boolean isBand;




    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isBand() {
        return isBand;
    }

    public void setBand(boolean band) {
        isBand = band;
    }

    @Override
    public String toString() {
        return "AccountRspModel{" +
                "user=" + user +
                ", Account='" + account + '\'' +
                ", token='" + token + '\'' +
                ", isBand=" + isBand +
                '}';
    }
}
