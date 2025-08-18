package realestatemanagement.Beans;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import realestatemanagement.ejb.PropertyEJB;
import realestatemanagement.model.SaleProperty;

@Named
@ViewScoped
public class PropertyDetailsBean implements Serializable{

    @EJB
    private PropertyEJB propertyManager;

    private SaleProperty property;

    @PostConstruct
    public void init() {
        Long propertyId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("propertyId");
        if (propertyId != null) {
            property = propertyManager.findSalePropertyById(propertyId);
        }
    }

    public SaleProperty getProperty() {
        return property;
    }
}
