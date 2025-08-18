package realestatemanagement.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQuery(name = "findAllPropertyManagers", query = "SELECT pm FROM PropertyManager pm")
//search property manager by first name and last name
@NamedQuery(name = "findPropertyManagerByName", query = "SELECT pm FROM PropertyManager pm WHERE pm.firstName = :firstName AND pm.lastName = :lastName")
//search property manager by first name
@NamedQuery(name = "findPropertyManagerByFirstName", query = "SELECT pm FROM PropertyManager pm WHERE pm.firstName = :firstName")
//search property manager by last name
@NamedQuery(name = "findPropertyManagerByLastName", query = "SELECT pm FROM PropertyManager pm WHERE pm.lastName = :lastName")
@NamedQuery(name = "getTotalManagers", query = "SELECT COUNT(pm) FROM PropertyManager pm")
@NamedQuery(name = "getTotalProperties", query = "SELECT COUNT(a) FROM Allocation a WHERE a.propertyManager = :manager")
public class PropertyManager {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "propertyManager")
    @JoinColumn(name= "id")
    private List<Allocation> allocations = new ArrayList<>();
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column( nullable = false)
    private String phone;
    
    @Column(nullable = false)
    private String mobile;
    
    private String email;

    public PropertyManager(String firstName, String LastName, String phone, String mobile, String email) {
        this.firstName = firstName;
        this.lastName = LastName;
        this.phone = phone;
        this.mobile = mobile;
        this.email = email;
    }

    public PropertyManager() {

    }


    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }



    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the allocations
     */
    public List<Allocation> getAllocations() {
        return allocations;
    }

    /**
     * @param allocations the allocations to set
     */
    public void setAllocations(List<Allocation> allocations) {
        this.allocations = allocations;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
   
}