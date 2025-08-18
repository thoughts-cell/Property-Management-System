package realestatemanagement.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import realestatemanagement.ejb.AllocationEJB;
import realestatemanagement.model.Allocation;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import realestatemanagement.ejb.PropertyEJB;
import realestatemanagement.ejb.PropertyManagerEJB;
import realestatemanagement.model.PropertyManager;
import realestatemanagement.model.RentProperty;
import realestatemanagement.model.SaleProperty;

import static realestatemanagement.jsf.PropertyManagerController.getString;

/**
 * @author 12233613
 */
@Named("allocationController")
@ViewScoped
public class AllocationController implements  java.io.Serializable {

    private String total;
    @EJB
    private AllocationEJB allocationEJB;
    @EJB
    private PropertyManagerEJB propertyManagerEJB;
    @EJB
    private PropertyEJB propertyEJB;
    private Allocation allocation = new Allocation();
    private String propertyManagerId;
    private String rentPropertyId;
    private String salePropertyId;
    private List<Allocation> allocationList = new ArrayList<Allocation>();
    private List<PropertyManager> managerList = new ArrayList<PropertyManager>();
    private List<RentProperty> rentProperties = new ArrayList<RentProperty>();
    private List<SaleProperty> saleProperties = new ArrayList<SaleProperty>();

    @PostConstruct
    public void init() {
        setManagerList((List<PropertyManager>) propertyManagerEJB.findManagers());
        setAllocationList(allocationEJB.findAllocations());
        setTotal(String.valueOf(getAllocationList().size()));
        setRentProperties(propertyEJB.findAllRentProperties());
        setSaleProperties(propertyEJB.findAllSaleProperties());
    }

    public String doCreateAllocation(Allocation allocation) {

        FacesContext context = FacesContext.getCurrentInstance();
        allocationEJB.createAllocation(this.getAllocation());
        setAllocationList(allocationEJB.findAllocations());
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Allocation has been created", "Allocation has been created successfully"));
        setTotal(String.valueOf(getAllocationCount()));
        return "allocationList.xhtml";
    }

    public String doSearchAllocation() {
        Allocation foundAllocation = allocationEJB.findAllocationsById(getAllocation().getId());

        if (foundAllocation == null){
            FacesContext.getCurrentInstance().addMessage("searchForm:", new FacesMessage("Allocation Not Found!"));
            return null;
        }
        else {
            allocationList = new ArrayList<Allocation>();
            allocationList.add(foundAllocation);
            total = "2";
            return "foundAllocation.xhtml";
        }
    }

    public String doDeleteAllocation(Allocation allocation) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            allocationEJB.deleteAllocation(allocation);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Allocation has been deleted", "Allocation has been deleted successfully"));
        } catch (Exception e) {
            context.addMessage("error", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Allocation hasn't been deleted", e.getMessage()));
        }
        return "allocationList.xhtml";

    }

    public String redirectToPropertyManager(Long propertyManagerId) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("propertyManagerId", propertyManagerId);
        return "viewManager?faces-redirect=true";
    }

    public String redirectToProperty(Long id) {
        return getString(id, propertyEJB);
    }

    public int getAllocationCount() {
        return allocationEJB.countAllocations();
    }

    public List<Allocation> getAllocationList() {
        return allocationList;
    }

    public void setAllocationList(List<Allocation> allocationList) {
        this.allocationList = allocationList;
    }

    public Allocation getAllocation() {
        return allocation;
    }

    public void setAllocation(Allocation allocation) {
        this.allocation = allocation;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    /**
     * @return the managerList
     */
    public List<PropertyManager> getManagerList() {
        return managerList;
    }

    /**
     * @param managerList the managerList to set
     */
    public void setManagerList(List<PropertyManager> managerList) {
        this.managerList = managerList;
    }

    /**
     * @return the rentProperties
     */
    public List<RentProperty> getRentProperties() {
        return rentProperties;
    }

    /**
     * @param rentProperties the rentProperties to set
     */
    public void setRentProperties(List<RentProperty> rentProperties) {
        this.rentProperties = rentProperties;
    }

    /**
     * @return the saleProperties
     */
    public List<SaleProperty> getSaleProperties() {
        return saleProperties;
    }

    /**
     * @param saleProperties the saleProperties to set
     */
    public void setSaleProperties(List<SaleProperty> saleProperties) {
        this.saleProperties = saleProperties;
    }

    public String doNewRentPropertyAllocation() {
        allocation = new Allocation();
        allocation.setPropertyManager(propertyManagerEJB.findPropertyManagerById(Long.valueOf(propertyManagerId)));
        allocation.setProperty(propertyEJB.findRentPropertyById(Long.valueOf(rentPropertyId)));
        allocation.setCreationTime(new Date());
        allocationEJB.createAllocation(allocation);
        return "allocationList.xhtml";
    }

    public String doNewSalePropertyAllocation() {
        allocation = new Allocation();
        allocation.setPropertyManager(propertyManagerEJB.findPropertyManagerById(Long.valueOf(propertyManagerId)));
        allocation.setProperty(propertyEJB.findSalePropertyById(Long.valueOf(salePropertyId)));
        allocation.setCreationTime(new Date());
        allocationEJB.createAllocation(allocation);
        return "allocationList.xhtml";
    }

    /**
     * @return the propertyManagerId
     */
    public String getPropertyManagerId() {
        return propertyManagerId;
    }

    /**
     * @param propertyManagerId the propertyManagerId to set
     */
    public void setPropertyManagerId(String propertyManagerId) {
        this.propertyManagerId = propertyManagerId;
    }

    /**
     * @return the rentPropertyId
     */
    public String getRentPropertyId() {
        return rentPropertyId;
    }

    /**
     * @param rentPropertyId the rentPropertyId to set
     */
    public void setRentPropertyId(String rentPropertyId) {
        this.rentPropertyId = rentPropertyId;
    }

    /**
     * @return the salePropertyId
     */
    public String getSalePropertyId() {
        return salePropertyId;
    }

    /**
     * @param salePropertyId the salePropertyId to set
     */
    public void setSalePropertyId(String salePropertyId) {
        this.salePropertyId = salePropertyId;
    }

}
