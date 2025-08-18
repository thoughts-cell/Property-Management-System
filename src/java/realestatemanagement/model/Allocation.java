package realestatemanagement.model;

import jakarta.persistence.*;

import java.util.Date;

/**
 *
 * @author akshay benny
 * 
 */
@Entity

@NamedQuery(name = "findAllAllocations", query = "SELECT a FROM Allocation a")
@NamedQuery(name = "findAllocationById", query = "SELECT a FROM Allocation a WHERE  a.id = :id")
@NamedQuery(name = "getTotalAllocations", query = "SELECT COUNT(a) FROM Allocation a")
public class Allocation   {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn( name = "property_manager_id" ,nullable = false)
    private PropertyManager propertyManager;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "property_id",nullable = false)
    private Property property;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * @return the propertyManager
     */
    public PropertyManager getPropertyManager() {
        return propertyManager;
    }

    /**
     * @param propertyManager the propertyManager to set
     */
    public void setPropertyManager(PropertyManager propertyManager) {
        this.propertyManager = propertyManager;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    /**
     * @param creationTime the creationTime to set
     */
    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    /**
     * @return the property
     */
    public Property getProperty() {
        return property;
    }

    /**
     * @param property the property to set
     */
    public void setProperty(Property property) {
        this.property = property;
    }
}
