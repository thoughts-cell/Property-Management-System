package realestatemanagement.Beans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import realestatemanagement.ejb.PropertyEJB;
import realestatemanagement.model.RentProperty;

/**
 * Managed Bean for Rent Property Details operations
 * Handles display and management of individual rent property details
 * 
 *
 * @version 2.0 - Enhanced with comprehensive error handling and validation
 */
@Named
@RequestScoped
public class RentPropertyDetailsBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(RentPropertyDetailsBean.class.getName());
    
    @Inject
    private PropertyEJB propertyManager;
    
    private RentProperty rentProperty;
    private Long propertyId;
    private boolean propertyFound = false;
    private String errorMessage;
    
    // Navigation outcomes
    private static final String RENT_PROPERTY_LIST = "rentpropertylist?faces-redirect=true";
    private static final String RENT_PROPERTY_SEARCH = "searchrentproperty?faces-redirect=true";
    private static final String HOME_PAGE = "home?faces-redirect=true";

    /**
     * Initialize the bean after construction
     * Loads the rent property based on the ID from various sources
     */
    @PostConstruct
    public void init() {
        try {
            // Try to get property ID from multiple sources
            propertyId = getPropertyIdFromContext();
            
            if (propertyId != null) {
                loadRentProperty(propertyId);
            } else {
                handleNoPropertyId();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing RentPropertyDetailsBean", e);
            handleError("An unexpected error occurred while loading property details.");
        }
    }

    /**
     * Load rent property by ID with comprehensive error handling
     * @param id Property ID to load
     */
    public void loadRentProperty(Long id) {
        if (id == null || id <= 0) {
            handleError("Invalid property ID provided.");
            return;
        }
        
        try {
            LOGGER.log(Level.INFO, "Loading rent property with ID: {0}", id);
            
            rentProperty = propertyManager.findRentPropertyById(id);
            
            if (rentProperty != null) {
                propertyFound = true;
                propertyId = id;
                LOGGER.log(Level.INFO, "Successfully loaded rent property: {0}", rentProperty.getId());
                
                // Add success message
                addMessage(FacesMessage.SEVERITY_INFO, "Property Details", 
                          "Property details loaded successfully.");
            } else {
                handlePropertyNotFound(id);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading rent property with ID: " + id, e);
            handleError("Failed to load property details. Please try again.");
        }
    }

    /**
     * Refresh the current property data
     * @return Navigation outcome
     */
    public String refreshProperty() {
        if (propertyId != null) {
            loadRentProperty(propertyId);
            return null; // Stay on current page
        } else {
            addMessage(FacesMessage.SEVERITY_WARN, "Refresh Failed", 
                      "No property ID available for refresh.");
            return null;
        }
    }

    /**
     * Navigate back to rent property list
     * @return Navigation outcome
     */
    public String backToList() {
        LOGGER.log(Level.INFO, "Navigating back to rent property list");
        return RENT_PROPERTY_LIST;
    }

    /**
     * Navigate to search page
     * @return Navigation outcome
     */
    public String searchProperties() {
        LOGGER.log(Level.INFO, "Navigating to rent property search");
        return RENT_PROPERTY_SEARCH;
    }

    /**
     * Navigate to home page
     * @return Navigation outcome
     */
    public String goHome() {
        LOGGER.log(Level.INFO, "Navigating to home page");
        return HOME_PAGE;
    }

    /**
     * Get similar properties (same city and similar price range)
     * @return List of similar rent properties
     */
    public List<RentProperty> getSimilarProperties() {
        if (rentProperty == null || rentProperty.getAddress() == null) {
            return List.of(); // Return empty list if no property or address
        }
        
        try {
            String city = rentProperty.getAddress().getCity();
            Long currentRent = rentProperty.getWeeklyRent();
            
            // Find properties in same city with rent within 20% range
            Long minRent = Math.round(currentRent * 0.8);
            Long maxRent = Math.round(currentRent * 1.2);
            
            List<RentProperty> similarProperties = propertyManager.findRentPropertiesByCity(city);
            
            // Filter by rent range and exclude current property
            return similarProperties.stream()
                    .filter(p -> !p.getId().equals(rentProperty.getId()))
                    .filter(p -> p.getWeeklyRent() >= minRent && p.getWeeklyRent() <= maxRent)
                    .limit(5) // Limit to 5 similar properties
                    .toList();
                    
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error finding similar properties", e);
            return List.of();
        }
    }

    /**
     * Calculate monthly rent from weekly rent
     * @return Monthly rent amount
     */
    public Long getMonthlyRent() {
        if (rentProperty != null && rentProperty.getWeeklyRent() != null) {
            return Math.round(rentProperty.getWeeklyRent() * 4.33); // Average weeks per month
        }
        return 0L;
    }

    /**
     * Calculate yearly rent from weekly rent
     * @return Yearly rent amount
     */
    public Long getYearlyRent() {
        if (rentProperty != null && rentProperty.getWeeklyRent() != null) {
            return rentProperty.getWeeklyRent() * 52; // 52 weeks per year
        }
        return 0L;
    }

    /**
     * Get formatted address string
     * @return Formatted address or empty string if no address
     */
    public String getFormattedAddress() {
        if (rentProperty != null && rentProperty.getAddress() != null) {
            var address = rentProperty.getAddress();
            return String.format("%s, %s %s, %s", 
                    address.getStreetAddress(),
                    address.getCity(),
                    address.getPostcode(),
                    address.getCountry());
        }
        return "";
    }

    /**
     * Get property type with bedroom/bathroom info
     * @return Formatted property type string
     */
    public String getPropertyTypeDetails() {
        if (rentProperty != null) {
            return String.format("%s - %d bed, %d bath", 
                    rentProperty.getPropertyType(),
                    rentProperty.getNoOfBedrooms(),
                    rentProperty.getNoOfBathrooms());
        }
        return "";
    }

    /**
     * Check if property has essential amenities info
     * @return true if property has complete information
     */
    public boolean isPropertyInfoComplete() {
        return rentProperty != null 
                && rentProperty.getPropertyType() != null 
                && !rentProperty.getPropertyType().trim().isEmpty()
                && rentProperty.getPropertyDescription() != null 
                && !rentProperty.getPropertyDescription().trim().isEmpty()
                && rentProperty.getAddress() != null
                && rentProperty.getWeeklyRent() != null 
                && rentProperty.getWeeklyRent() > 0;
    }

    /**
     * Get furnished status as user-friendly string
     * @return Furnished status string
     */
    public String getFurnishedStatusText() {
        if (rentProperty != null && rentProperty.getIsFurnished() != null) {
            return rentProperty.getIsFurnished() ? "Furnished" : "Unfurnished";
        }
        return "Not specified";
    }

    /**
     * Check if this is a good value property (below average rent for the area)
     * @return true if property is good value
     */
    public boolean isGoodValue() {
        if (rentProperty == null || rentProperty.getWeeklyRent() == null) {
            return false;
        }
        
        try {
            // Get average rent for properties in the same city
            double averageRent = propertyManager.getAverageWeeklyRent();
            return rentProperty.getWeeklyRent() < averageRent * 0.9; // 10% below average
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error calculating property value", e);
            return false;
        }
    }

    // ==================== PRIVATE HELPER METHODS ====================

    /**
     * Get property ID from various context sources
     * @return Property ID or null if not found
     */
    private Long getPropertyIdFromContext() {
        FacesContext context = FacesContext.getCurrentInstance();
        
        // Try flash scope first (from redirects)
        Object flashId = context.getExternalContext().getFlash().get("propertyId");
        if (flashId != null) {
            return convertToLong(flashId);
        }
        
        // Try request parameter
        String paramId = context.getExternalContext().getRequestParameterMap().get("propertyId");
        if (paramId != null && !paramId.trim().isEmpty()) {
            return convertToLong(paramId);
        }
        
        // Try request attribute
        Object attrId = context.getExternalContext().getRequestMap().get("propertyId");
        if (attrId != null) {
            return convertToLong(attrId);
        }
        
        return null;
    }

    /**
     * Convert object to Long safely
     * @param value Object to convert
     * @return Long value or null if conversion fails
     */
    private Long convertToLong(Object value) {
        if (value == null) {
            return null;
        }
        
        try {
            if (value instanceof Long) {
                return (Long) value;
            } else if (value instanceof Number) {
                return ((Number) value).longValue();
            } else {
                return Long.parseLong(value.toString().trim());
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Failed to convert value to Long: " + value, e);
            return null;
        }
    }

    /**
     * Handle case when no property ID is provided
     */
    private void handleNoPropertyId() {
        propertyFound = false;
        errorMessage = "No property ID provided. Please select a property from the list.";
        LOGGER.log(Level.WARNING, "RentPropertyDetailsBean initialized without property ID");
        
        addMessage(FacesMessage.SEVERITY_WARN, "No Property Selected", 
                  "Please select a property to view its details.");
    }

    /**
     * Handle case when property is not found
     * @param id Property ID that was not found
     */
    private void handlePropertyNotFound(Long id) {
        propertyFound = false;
        errorMessage = "Property with ID " + id + " was not found.";
        LOGGER.log(Level.WARNING, "Rent property not found with ID: {0}", id);
        
        addMessage(FacesMessage.SEVERITY_ERROR, "Property Not Found", 
                  "The requested property could not be found. It may have been removed or the ID is incorrect.");
    }

    /**
     * Handle general errors
     * @param message Error message to display
     */
    private void handleError(String message) {
        propertyFound = false;
        errorMessage = message;
        addMessage(FacesMessage.SEVERITY_ERROR, "Error", message);
    }

    /**
     * Add faces message to context
     * @param severity Message severity
     * @param summary Message summary
     * @param detail Message detail
     */
    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null) {
            FacesMessage message = new FacesMessage(severity, summary, detail);
            context.addMessage(null, message);
        }
    }

    // ==================== GETTERS AND SETTERS ====================

    public RentProperty getRentProperty() {
        return rentProperty;
    }

    public void setRentProperty(RentProperty rentProperty) {
        this.rentProperty = rentProperty;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public boolean isPropertyFound() {
        return propertyFound;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public PropertyEJB getPropertyManager() {
        return propertyManager;
    }

    public void setPropertyManager(PropertyEJB propertyManager) {
        this.propertyManager = propertyManager;
    }
}