package realestatemanagement.model;

import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "USER")
@NamedQueries( {@NamedQuery(name = "User.findById", query = "SELECT w FROM User w WHERE w.id = :id"), 
    @NamedQuery(name = "User.findByFirstname", query = "SELECT w FROM User w WHERE w.firstname = :firstname"), 
    @NamedQuery(name = "User.findByLastname", query = "SELECT w FROM User w WHERE w.lastname = :lastname"), 
    @NamedQuery(name = "User.findByUsername", query = "SELECT w FROM User w WHERE w.username = :username"), 
    @NamedQuery(name = "User.findByPassword", query = "SELECT w FROM User w WHERE w.password = :password"), 
    @NamedQuery(name = "User.findBySince", query = "SELECT w FROM User w WHERE w.since = :since"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT w FROM User w WHERE w.email = :email")})
public class User  {

    @Id
    @GeneratedValue(strategy=IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Column(name = "FIRSTNAME", nullable = false)
    private String firstname;
    @Column(name = "LASTNAME", nullable = false)
    private String lastname;
    @Column(name = "USERNAME", nullable = false)
    private String username;
    @Column(name = "PASSWORD", nullable = false, length=128)
    private String password;
    @Column(name = "SINCE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date since;
    @Column(name = "EMAIL", nullable = false)
    private String email;
    /* Creates a new instance of Wuser */
    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }

    public User(Integer id, String firstname, String lastname, String username, String password, String email) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.email=email;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public Date getSince() {
        return this.since;
    }

    public void setSince(Date since) {
        this.since = since;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
