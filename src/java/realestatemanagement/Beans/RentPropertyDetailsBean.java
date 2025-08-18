/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package realestatemanagement.Beans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.inject.Inject;
import realestatemanagement.ejb.PropertyEJB;
import realestatemanagement.model.RentProperty;

/**
 *
 * @author harsh
 */

@Named
@RequestScoped
public class RentPropertyDetailsBean {
    
    @Inject
    private PropertyEJB propertyManager;
    
    private RentProperty rentProperty;

    // Called after the bean is created
    @PostConstruct
    public void init() {
        // Load the rent property based on the ID passed from search or list page
        Long propertyId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("propertyId");
        System.out.println(propertyId + "hello this is id");
        if (propertyId != null) {
            rentProperty = propertyManager.findRentPropertyById(propertyId);
        }
    }



    public RentProperty getRentProperty() {
        return rentProperty;
    }

    public void setRentProperty(RentProperty rentProperty) {
        this.rentProperty = rentProperty;
    }
}