package realestatemanagement.jsf;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.validator.ValidatorException;
import java.util.ArrayList;
import java.util.List;
import realestatemanagement.Beans.AuthenticationBean;
import realestatemanagement.ejb.UserEJB;
import realestatemanagement.model.User;

/**
 *
 * @author aksha
 */
@Named (value="userController") 
@RequestScoped
public class UserController {
    
    private String firstName;
    
    private String lastName;
    
    private String userName;
    
    private String password;
    
    private String passwordVerify;
    
    private String verificationCode;
    
    private String userVerificationCode;
    
    private String recoveryCode;
    
    private String email;

    // Attributes             
    @EJB
    private UserEJB userEJB;
    private User user;
    private List<User> userList = new ArrayList<User>();
    private AuthenticationBean authenticationBean = new AuthenticationBean();
    
    // Public Methods           
    public String doCreateUser() {
        
        validate();
        //user = new User(firstName, lastName, userName, password, verificationCode, null, email);
        userEJB.createUser(user);
        return "index.xhtml";
    }
    
    public String verification() {
        return "verification.xhtml";
    }
    
    public String login() {
        return "login.xhtml";
    }
    
    public String sendVerificationCode() {
       // verificationCode = authenticationBean.sendRandomCode(email);
        return "registration.xhtml";
    }
    
    public void validate() {
        if (passwordVerify != null && !passwordVerify.equals(password)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password do not match!", null));
        }
        if (verificationCode != null && !verificationCode.equals(userVerificationCode)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Verification Code do not match!", null));
        }
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the userList
     */
    public List<User> getUserList() {
        return userList;
    }

    /**
     * @param userList the userList to set
     */
    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the verificationCode
     */
    public String getVerificationCode() {
        return verificationCode;
    }

    /**
     * @param verificationCode the verificationCode to set
     */
    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    /**
     * @return the recoveryCode
     */
    public String getRecoveryCode() {
        return recoveryCode;
    }

    /**
     * @param recoveryCode the recoveryCode to set
     */
    public void setRecoveryCode(String recoveryCode) {
        this.recoveryCode = recoveryCode;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the userVerificationCode
     */
    public String getUserVerificationCode() {
        return userVerificationCode;
    }

    /**
     * @param userVerificationCode the userVerificationCode to set
     */
    public void setUserVerificationCode(String userVerificationCode) {
        this.userVerificationCode = userVerificationCode;
    }

    /**
     * @return the passwordVerify
     */
    public String getPasswordVerify() {
        return passwordVerify;
    }

    /**
     * @param passwordVerify the passwordVerify to set
     */
    public void setPasswordVerify(String passwordVerify) {
        this.passwordVerify = passwordVerify;
    }

    
}