/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package realestatemanagement.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import java.util.ArrayList;
import java.util.List;

import realestatemanagement.ejb.*;
import realestatemanagement.model.*;

/**
 *
 * @author 12233612
 */
@Named("managerController")
@RequestScoped
public class PropertyManagerController {

    @EJB
    private PropertyManagerEJB propertyManagerEJB;

    @EJB
    private PropertyEJB propertyEJB;

    @EJB
    private AllocationEJB allocationEJB;

    private PropertyManager manager = new PropertyManager();

    private String total;

    private List<PropertyManager> managerList = new ArrayList<PropertyManager>();

    private String numberOfAllocations = String.valueOf(0);

    @PostConstruct
    public void init() {
        managerList = propertyManagerEJB.findManagers();
        total = String.valueOf(managerList.size());

        Long propertyManagerId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("propertyManagerId");
        if (propertyManagerId != null) {
            manager = propertyManagerEJB.findPropertyManagerById(propertyManagerId);
            List<Allocation> allocations = allocationEJB.findAllocationsByPropertyManagerId(manager.getId());
            manager.setAllocations(allocations);
            numberOfAllocations = String.valueOf(allocations.size());
        }
    }

    public String doCreateManager() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (getManager().getFirstName() == null || "".equals(getManager().getFirstName())) {
            context.addMessage("managerForm:", new FacesMessage("First Name is required"));
            return null;
        }
        if (this.getManager().getLastName() == null || "".equals(getManager().getLastName())) {
            context.addMessage("managerForm:", new FacesMessage("Last Name is required"));
            return null;
        }
        if (getManager().getEmail() == null || "".equals(getManager().getEmail())) {
            context.addMessage("managerForm:", new FacesMessage("Email is required"));
            return null;
        }
        if (getManager().getPhone() == null || "".equals(getManager().getPhone())) {
            context.addMessage("managerForm:", new FacesMessage("Phone Number is required"));
            return null;
        }
        if (getManager().getMobile() == null || "".equals(getManager().getMobile())) {
            context.addMessage("managerForm:", new FacesMessage("Mobile is required"));
            return null;
        }
        propertyManagerEJB.createManager(getManager());
        //after creating the manager, update the manager list
        setManagerList((List<PropertyManager>) propertyManagerEJB.findManagers());
        setTotal(String.valueOf(managerList.size())); //update the total manager number
        return "listManager.xhtml";
    }


    public String view(PropertyManager manager) {
        this.setManager(manager);
        numberOfAllocations = String.valueOf(allocationEJB.findAllocationsByPropertyManagerId(manager.getId()).size());
        return "viewManager.xhtml";
    }

    public String doSearchManager() {
        if (getManager().getFirstName() == null || "".equals(getManager().getFirstName())) {
            FacesContext.getCurrentInstance().addMessage("searchForm:", new FacesMessage("First Name is required"));
            return null;
        }
        if (getManager().getLastName() == null || "".equals(getManager().getLastName())) {
            FacesContext.getCurrentInstance().addMessage("searchForm:", new FacesMessage("Last Name is required"));
            return null;
        }
        managerList = propertyManagerEJB.searchManager(getManager().getFirstName(), getManager().getLastName());
        if (managerList.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage("searchForm:", new FacesMessage("Manager not Found"));
            return null;
        }
        return "foundManager.xhtml";

    }

    public String toHomePage() {
        return "home.xhtml";
    }

    public int allocatedPropertyCount() {
        return propertyManagerEJB.totalProperties(getManager());

    }

    public int getManagerCount() {
        return propertyManagerEJB.totalManagers();
    }

    public String doCreateAllocation() {
        return "createAllocation.xhtml";
    }

    public PropertyManager getManager() {
        return manager;
    }

    public void setManager(PropertyManager manager) {
        this.manager = manager;
    }

    public List<PropertyManager> getManagerList() {
        return managerList;
    }

    public void setManagerList(List<PropertyManager> managerList) {
        this.managerList = managerList;
    }

    /**
     * @return the total
     */
    public String getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(String total) {
        this.total = total;
    }

    /**
     * @return the numberOfAllocations
     */
    public String getNumberOfAllocations() {
        return numberOfAllocations;
    }

    /**
     * @param numberOfAllocations the numberOfAllocations to set
     */
    public void setNumberOfAllocations(String numberOfAllocations) {
        this.numberOfAllocations = numberOfAllocations;
    }

    public String redirectToProperty(Long id) {
        return getString(id, propertyEJB);
    }

    static String getString(Long id, PropertyEJB propertyEJB) {
        if (propertyEJB.findRentPropertyById(id) != null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("propertyId", id);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Rent property found."));
            return "rentpropertydetails?faces-redirect=true"; // Redirects to the details page
        } else if (propertyEJB.findSalePropertyById(id) != null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("propertyId", id);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sale property found."));
            return "salepropertydetails?faces-redirect=true"; // Redirects to the details page
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Rent property not found.", ""));
            return null; // Stay on the same page
        }
    }

}
