package realestatemanagement.Beans;


import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import realestatemanagement.ejb.PropertyEJB;
import realestatemanagement.model.RentProperty;

/**
 *
 * @author harsh
 */
@Named
@ViewScoped
public class RentPropertyListBean implements Serializable{

    @EJB
    private PropertyEJB propertyManager;

    private List<RentProperty> rentProperties;

    @PostConstruct
    public void init() {
        rentProperties = propertyManager.findAllRentProperties();
    }

    public List<RentProperty> getRentProperties() {
        return rentProperties;
    }

    public String viewPropertyDetails(Long id) {
       // Redirect to the property details page, passing the property ID
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("propertyId", id);
        return "rentpropertydetails?faces-redirect=true"; // Change the navigation as needed
    }
    
    // get count of total rest property or return 0
    public int getRentPropertyCount() {
        return (rentProperties != null) ? rentProperties.size() : 0;
    }
}
