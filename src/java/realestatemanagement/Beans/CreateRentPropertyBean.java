package realestatemanagement.Beans;


import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import realestatemanagement.ejb.PropertyEJB;
import realestatemanagement.model.Address;
import realestatemanagement.model.RentProperty;

/**
 *
 * @author harsh
 */
@Named
@RequestScoped
public class CreateRentPropertyBean {
    
    @EJB
    private PropertyEJB propertyManager;

    private RentProperty property;
    private Address address;
    
    @PostConstruct
    public void init() {
        property = new RentProperty();
        address = new Address();
    }
    
    // Getters and setters for all fields

    public String getPropertyType() {
        return property.getPropertyType();
    }

    public void setPropertyType(String propertyType) {
        property.setPropertyType(propertyType);
    }

    public int getNoOfBedrooms() {
        return property.getNoOfBedrooms();
    }

    public void setNoOfBedrooms(int noOfBedrooms) {
        property.setNoOfBedrooms(noOfBedrooms);
    }

    public int getNoOfBathrooms() {
        return property.getNoOfBathrooms();
    }

    public void setNoOfBathrooms(int noOfBathrooms) {
        property.setNoOfBathrooms(noOfBathrooms);
    }

    public String getPropertyDescription() {
        return property.getPropertyDescription();
    }

    public void setPropertyDescription(String propertyDescription) {
        property.setPropertyDescription(propertyDescription);
    }

    public Long getWeeklyRent() {
        return property.getWeeklyRent();
    }

    public void setWeeklyRent(Long weeklyRent) {
        property.setWeeklyRent(weeklyRent);
    }

    public String getStreetName() {
        return address.getStreetName();
    }

    public void setStreetName(String street) {
        address.setStreetName(street);
    }
    
    public int getStreetNumber() {
        return address.getStreetNumber();
    }

    public void setStreetNumber(int streetNumber) {
        address.setStreetNumber(streetNumber);
    }

    public String getCity() {
        return address.getCity();
    }

    public void setCity(String city) {
        address.setCity(city);
    }
    
    public String getCountry() {
        return address.getCountry();
    }

    public void setCountry(String country) {
        address.setCountry(country);
    }

    public Long getPostCode() {
        return address.getPostcode();
    }

    public void setPostCode(Long postCode) {
        address.setPostcode(postCode);
    }
    
    public Boolean getIsFurnished() {
        return property.getIsFurnished();
    }

    public void setIsFurnished(Boolean isFurnished) {
        property.setIsFurnished(isFurnished);
    }

    public String createProperty() {
        property.setAddress(address);
        propertyManager.createRentProperty(property);
        return "rentpropertylist?faces-redirect=true";

    }

    public RentProperty getRentProperty() {
        return property;
    }

    public void setRentProperty(RentProperty rentProperty) {
        this.property = rentProperty;
    }
}