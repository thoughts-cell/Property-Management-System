package realestatemanagement.model;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;

import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import jakarta.persistence.*;

/**
 *
 * @author akshay benny
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@MappedSuperclass
public class Property {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Changed to IDENTITY for consistency with your reference
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "PROPERTY_TYPE", nullable = false)
    private String propertyType;

    @Column(name = "NO_OF_BEDROOMS", nullable = false)
    private int noOfBedrooms;

    @Column(name = "NO_OF_BATHROOMS", nullable = false)
    private int noOfBathrooms;

    @Column(name = "PROPERTY_DESCRIPTION", nullable = false)
    private String propertyDescription;
    
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "ADDRESS_ID")
    private Address address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the propertyType
     */
    public String getPropertyType() {
        return propertyType;
    }

    /**
     * @param propertyType the propertyType to set
     */
    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    /**
     * @return the noOfBedrooms
     */
    public int getNoOfBedrooms() {
        return noOfBedrooms;
    }

    /**
     * @param noOfBedrooms the noOfBedrooms to set
     */
    public void setNoOfBedrooms(int noOfBedrooms) {
        this.noOfBedrooms = noOfBedrooms;
    }

    /**
     * @return the noOfBathrooms
     */
    public int getNoOfBathrooms() {
        return noOfBathrooms;
    }

    /**
     * @param noOfBathrooms the noOfBathrooms to set
     */
    public void setNoOfBathrooms(int noOfBathrooms) {
        this.noOfBathrooms = noOfBathrooms;
    }

    /**
     * @return the propertyDescription
     */
    public String getPropertyDescription() {
        return propertyDescription;
    }

    /**
     * @param propertyDescription the propertyDescription to set
     */
    public void setPropertyDescription(String propertyDescription) {
        this.propertyDescription = propertyDescription;
    }

    /**
     * @return the address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(Address address) {
        this.address = address;
    }

}
