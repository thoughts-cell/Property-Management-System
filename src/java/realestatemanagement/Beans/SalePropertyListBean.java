package realestatemanagement.Beans;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import realestatemanagement.ejb.PropertyEJB;
import realestatemanagement.model.SaleProperty;

@Named
@ViewScoped
public class SalePropertyListBean implements Serializable{

    @EJB
    private PropertyEJB propertyManager;

    private List<SaleProperty> saleProperties;

    @PostConstruct
    public void init() {
        // Load all sale properties from the database
        saleProperties = propertyManager.findAllSaleProperties();
    }

    // Getter for saleProperties
    public List<SaleProperty> getSaleProperties() {
        return saleProperties;
    }
    
    // get count of total sale property or return 0
    public int getSalePropertyCount() {
        return (saleProperties != null) ? saleProperties.size() : 0;
    }
    
    public String viewPropertyDetails(Long id) {
        // Redirect to the property details page, passing the property ID
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("propertyId", id);
        return "salepropertydetails?faces-redirect=true"; // Change the navigation as needed
    }
}
