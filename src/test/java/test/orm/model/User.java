package test.orm.model;

public class User extends ORMEntity {

    private String emailAddress;

    private String password;

    private String username;

    private RfcEmailAddress rfcEmailAddress;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public RfcEmailAddress getRfcEmailAddress() {
        return rfcEmailAddress;
    }

    public void setRfcEmailAddress(RfcEmailAddress rfcEmailAddress) {
        this.rfcEmailAddress = rfcEmailAddress;
    }
}
