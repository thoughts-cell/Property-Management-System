package realestatemanagement.Beans;

/**
 *
 * @author harsh patel
 */
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import realestatemanagement.ejb.PropertyEJB;
import realestatemanagement.model.Address;
import realestatemanagement.model.SaleProperty;

@Named
@RequestScoped
public class CreateSalePropertyBean {

    @EJB
    private PropertyEJB propertyManager;

    private SaleProperty property;
    private Address address;

    public CreateSalePropertyBean() {
        property = new SaleProperty();
        address = new Address();
    }

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

    public Long getSalePrice() {
        return property.getSalePrice();
    }

    public void setSalePrice(Long salePrice) {
        property.setSalePrice(salePrice);
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

//    public String getState() {
//        return address.getState();
//    }
//
//    public void setState(String state) {
//        address.setState(state);
//    }

    public Long getPostCode() {
        return address.getPostcode();
    }

    public void setPostCode(Long zipCode) {
        address.setPostcode(zipCode);
    }

    public String createProperty() {
        try {
            property.setAddress(address);
            propertyManager.createSaleProperty(property);
            System.out.println("Property created: " + property.getAddress().getStreetAddress());  // Log after creation
            return "salepropertylist?faces-redirect=true";
        } catch (Exception e) {
            // Handle exception (e.g., add error message to faces context)
            return null;
        }
    }
}