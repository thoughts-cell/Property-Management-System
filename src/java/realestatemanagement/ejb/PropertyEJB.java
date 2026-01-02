package realestatemanagement.ejb;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import realestatemanagement.model.Property;
import realestatemanagement.model.RentProperty;
import realestatemanagement.model.SaleProperty;

/**
 * Enterprise Java Bean for Property management operations
 * Provides CRUD operations and business logic for Property entities (Sale and Rent)
 * 
 * @author Zhengxu
 * @version 2.0 - Enhanced with comprehensive error handling and validation
 */
@Stateless
@LocalBean
public class PropertyEJB {

    private static final Logger LOGGER = Logger.getLogger(PropertyEJB.class.getName());
    
    @PersistenceContext(unitName = "RealEstateManagementPU")
    private EntityManager em;

    // ==================== SALE PROPERTY OPERATIONS ====================

    /**
     * Create a new sale property
     * @param property SaleProperty entity to create
     * @return Created sale property with generated ID
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public SaleProperty createSaleProperty(SaleProperty property) {
        if (property == null) {
            throw new IllegalArgumentException("Sale property cannot be null");
        }
        
        validateSaleProperty(property);
        
        try {
            em.persist(property);
            em.flush(); // Force immediate persistence to get generated ID
            
            LOGGER.log(Level.INFO, "Created new sale property with ID: {0}, Price: {1}", 
                      new Object[]{property.getId(), property.getSalePrice()});
            return property;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating sale property", e);
            throw new RuntimeException("Failed to create sale property", e);
        }
    }

    /**
     * Update an existing sale property
     * @param property SaleProperty entity to update
     * @return Updated sale property
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public SaleProperty updateSaleProperty(SaleProperty property) {
        if (property == null || property.getId() == null) {
            throw new IllegalArgumentException("Sale property and ID cannot be null");
        }
        
        validateSaleProperty(property);
        
        try {
            // Check if property exists
            SaleProperty existingProperty = findSalePropertyById(property.getId());
            if (existingProperty == null) {
                throw new IllegalArgumentException("Sale property with ID " + property.getId() + " not found");
            }
            
            SaleProperty updatedProperty = em.merge(property);
            LOGGER.log(Level.INFO, "Updated sale property with ID: {0}", property.getId());
            return updatedProperty;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating sale property: " + property.getId(), e);
            throw new RuntimeException("Failed to update sale property", e);
        }
    }

    /**
     * Delete a sale property by ID
     * @param id Sale property ID to delete
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteSaleProperty(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Sale property ID cannot be null");
        }
        
        try {
            SaleProperty property = findSalePropertyById(id);
            if (property != null) {
                em.remove(property);
                LOGGER.log(Level.INFO, "Deleted sale property with ID: {0}", id);
            } else {
                LOGGER.log(Level.WARNING, "Attempted to delete non-existent sale property with ID: {0}", id);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting sale property: " + id, e);
            throw new RuntimeException("Failed to delete sale property", e);
        }
    }

    /**
     * Find sale property by ID
     * @param id Sale property ID
     * @return SaleProperty entity or null if not found
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public SaleProperty findSalePropertyById(Long id) {
        if (id == null) {
            LOGGER.log(Level.WARNING, "Attempted to find sale property with null ID");
            return null;
        }
        
        try {
            SaleProperty property = em.find(SaleProperty.class, id);
            if (property != null) {
                LOGGER.log(Level.INFO, "Found sale property with ID: {0}", id);
            }
            return property;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding sale property by ID: " + id, e);
            return null;
        }
    }

    /**
     * Get all sale properties
     * @return List of all sale properties ordered by price
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<SaleProperty> findAllSaleProperties() {
        try {
            TypedQuery<SaleProperty> query = em.createNamedQuery("findAllSalePropertys", SaleProperty.class);
            List<SaleProperty> properties = query.getResultList();
            LOGGER.log(Level.INFO, "Retrieved {0} sale properties", properties.size());
            return properties;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all sale properties", e);
            return new ArrayList<>();
        }
    }

    /**
     * Search sale properties by price range
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @return List of sale properties within price range
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<SaleProperty> findSalePropertiesByPriceRange(Long minPrice, Long maxPrice) {
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }
        
        try {
            StringBuilder queryStr = new StringBuilder("SELECT sp FROM SaleProperty sp WHERE 1=1");
            
            if (minPrice != null) {
                queryStr.append(" AND sp.salePrice >= :minPrice");
            }
            if (maxPrice != null) {
                queryStr.append(" AND sp.salePrice <= :maxPrice");
            }
            
            queryStr.append(" ORDER BY sp.salePrice ASC");
            
            TypedQuery<SaleProperty> query = em.createQuery(queryStr.toString(), SaleProperty.class);
            
            if (minPrice != null) {
                query.setParameter("minPrice", minPrice);
            }
            if (maxPrice != null) {
                query.setParameter("maxPrice", maxPrice);
            }
            
            List<SaleProperty> results = query.getResultList();
            LOGGER.log(Level.INFO, "Found {0} sale properties in price range {1}-{2}", 
                      new Object[]{results.size(), minPrice, maxPrice});
            return results;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error searching sale properties by price range", e);
            return new ArrayList<>();
        }
    }

    /**
     * Search sale properties by location (city)
     * @param city City name
     * @return List of sale properties in the specified city
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<SaleProperty> findSalePropertiesByCity(String city) {
        if (city == null || city.trim().isEmpty()) {
            LOGGER.log(Level.WARNING, "Search attempted with empty city");
            return new ArrayList<>();
        }
        
        try {
            TypedQuery<SaleProperty> query = em.createQuery(
                "SELECT sp FROM SaleProperty sp JOIN sp.address a WHERE LOWER(a.city) = LOWER(:city) ORDER BY sp.salePrice ASC", 
                SaleProperty.class);
            query.setParameter("city", city.trim());
            
            List<SaleProperty> results = query.getResultList();
            LOGGER.log(Level.INFO, "Found {0} sale properties in city: {1}", 
                      new Object[]{results.size(), city});
            return results;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error searching sale properties by city: " + city, e);
            return new ArrayList<>();
        }
    }

    /**
     * Search sale properties by bedroom count
     * @param bedrooms Number of bedrooms
     * @return List of sale properties with specified bedroom count
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<SaleProperty> findSalePropertiesByBedrooms(int bedrooms) {
        if (bedrooms < 0) {
            throw new IllegalArgumentException("Number of bedrooms cannot be negative");
        }
        
        try {
            TypedQuery<SaleProperty> query = em.createQuery(
                "SELECT sp FROM SaleProperty sp WHERE sp.noOfBedrooms = :bedrooms ORDER BY sp.salePrice ASC", 
                SaleProperty.class);
            query.setParameter("bedrooms", bedrooms);
            
            List<SaleProperty> results = query.getResultList();
            LOGGER.log(Level.INFO, "Found {0} sale properties with {1} bedrooms", 
                      new Object[]{results.size(), bedrooms});
            return results;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error searching sale properties by bedrooms: " + bedrooms, e);
            return new ArrayList<>();
        }
    }

    // ==================== RENT PROPERTY OPERATIONS ====================

    /**
     * Create a new rent property
     * @param property RentProperty entity to create
     * @return Created rent property with generated ID
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public RentProperty createRentProperty(RentProperty property) {
        if (property == null) {
            throw new IllegalArgumentException("Rent property cannot be null");
        }
        
        validateRentProperty(property);
        
        try {
            em.persist(property);
            em.flush(); // Force immediate persistence to get generated ID
            
            LOGGER.log(Level.INFO, "Created new rent property with ID: {0}, Weekly Rent: {1}", 
                      new Object[]{property.getId(), property.getWeeklyRent()});
            return property;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating rent property", e);
            throw new RuntimeException("Failed to create rent property", e);
        }
    }

    /**
     * Update an existing rent property
     * @param property RentProperty entity to update
     * @return Updated rent property
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public RentProperty updateRentProperty(RentProperty property) {
        if (property == null || property.getId() == null) {
            throw new IllegalArgumentException("Rent property and ID cannot be null");
        }
        
        validateRentProperty(property);
        
        try {
            // Check if property exists
            RentProperty existingProperty = findRentPropertyById(property.getId());
            if (existingProperty == null) {
                throw new IllegalArgumentException("Rent property with ID " + property.getId() + " not found");
            }
            
            RentProperty updatedProperty = em.merge(property);
            LOGGER.log(Level.INFO, "Updated rent property with ID: {0}", property.getId());
            return updatedProperty;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating rent property: " + property.getId(), e);
            throw new RuntimeException("Failed to update rent property", e);
        }
    }

    /**
     * Delete a rent property by ID
     * @param id Rent property ID to delete
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteRentProperty(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Rent property ID cannot be null");
        }
        
        try {
            RentProperty property = findRentPropertyById(id);
            if (property != null) {
                em.remove(property);
                LOGGER.log(Level.INFO, "Deleted rent property with ID: {0}", id);
            } else {
                LOGGER.log(Level.WARNING, "Attempted to delete non-existent rent property with ID: {0}", id);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting rent property: " + id, e);
            throw new RuntimeException("Failed to delete rent property", e);
        }
    }

    /**
     * Find rent property by ID
     * @param id Rent property ID
     * @return RentProperty entity or null if not found
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public RentProperty findRentPropertyById(Long id) {
        if (id == null) {
            LOGGER.log(Level.WARNING, "Attempted to find rent property with null ID");
            return null;
        }
        
        try {
            RentProperty property = em.find(RentProperty.class, id);
            if (property != null) {
                LOGGER.log(Level.INFO, "Found rent property with ID: {0}", id);
            }
            return property;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding rent property by ID: " + id, e);
            return null;
        }
    }

    /**
     * Get all rent properties
     * @return List of all rent properties ordered by weekly rent
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<RentProperty> findAllRentProperties() {
        try {
            TypedQuery<RentProperty> query = em.createNamedQuery("findAllRentPropertys", RentProperty.class);
            List<RentProperty> properties = query.getResultList();
            LOGGER.log(Level.INFO, "Retrieved {0} rent properties", properties.size());
            return properties;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all rent properties", e);
            return new ArrayList<>();
        }
    }

    /**
     * Search rent properties by weekly rent range
     * @param minRent Minimum weekly rent
     * @param maxRent Maximum weekly rent
     * @return List of rent properties within rent range
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<RentProperty> findRentPropertiesByRentRange(Long minRent, Long maxRent) {
        if (minRent != null && maxRent != null && minRent > maxRent) {
            throw new IllegalArgumentException("Minimum rent cannot be greater than maximum rent");
        }
        
        try {
            StringBuilder queryStr = new StringBuilder("SELECT rp FROM RentProperty rp WHERE 1=1");
            
            if (minRent != null) {
                queryStr.append(" AND rp.weeklyRent >= :minRent");
            }
            if (maxRent != null) {
                queryStr.append(" AND rp.weeklyRent <= :maxRent");
            }
            
            queryStr.append(" ORDER BY rp.weeklyRent ASC");
            
            TypedQuery<RentProperty> query = em.createQuery(queryStr.toString(), RentProperty.class);
            
            if (minRent != null) {
                query.setParameter("minRent", minRent);
            }
            if (maxRent != null) {
                query.setParameter("maxRent", maxRent);
            }
            
            List<RentProperty> results = query.getResultList();
            LOGGER.log(Level.INFO, "Found {0} rent properties in rent range {1}-{2}", 
                      new Object[]{results.size(), minRent, maxRent});
            return results;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error searching rent properties by rent range", e);
            return new ArrayList<>();
        }
    }

    /**
     * Search rent properties by furnished status
     * @param isFurnished Furnished status
     * @return List of rent properties with specified furnished status
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<RentProperty> findRentPropertiesByFurnishedStatus(Boolean isFurnished) {
        try {
            TypedQuery<RentProperty> query = em.createQuery(
                "SELECT rp FROM RentProperty rp WHERE rp.isFurnished = :isFurnished ORDER BY rp.weeklyRent ASC", 
                RentProperty.class);
            query.setParameter("isFurnished", isFurnished);
            
            List<RentProperty> results = query.getResultList();
            LOGGER.log(Level.INFO, "Found {0} rent properties with furnished status: {1}", 
                      new Object[]{results.size(), isFurnished});
            return results;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error searching rent properties by furnished status: " + isFurnished, e);
            return new ArrayList<>();
        }
    }

    /**
     * Search rent properties by location (city)
     * @param city City name
     * @return List of rent properties in the specified city
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<RentProperty> findRentPropertiesByCity(String city) {
        if (city == null || city.trim().isEmpty()) {
            LOGGER.log(Level.WARNING, "Search attempted with empty city");
            return new ArrayList<>();
        }
        
        try {
            TypedQuery<RentProperty> query = em.createQuery(
                "SELECT rp FROM RentProperty rp JOIN rp.address a WHERE LOWER(a.city) = LOWER(:city) ORDER BY rp.weeklyRent ASC", 
                RentProperty.class);
            query.setParameter("city", city.trim());
            
            List<RentProperty> results = query.getResultList();
            LOGGER.log(Level.INFO, "Found {0} rent properties in city: {1}", 
                      new Object[]{results.size(), city});
            return results;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error searching rent properties by city: " + city, e);
            return new ArrayList<>();
        }
    }

    // ==================== GENERAL PROPERTY OPERATIONS ====================

    /**
     * Search all properties by property type
     * @param propertyType Property type (e.g., "House", "Apartment", "Townhouse")
     * @return List of properties with specified type
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Property> findPropertiesByType(String propertyType) {
        if (propertyType == null || propertyType.trim().isEmpty()) {
            LOGGER.log(Level.WARNING, "Search attempted with empty property type");
            return new ArrayList<>();
        }
        
        try {
            TypedQuery<Property> query = em.createQuery(
                "SELECT p FROM Property p WHERE LOWER(p.propertyType) = LOWER(:propertyType)", 
                Property.class);
            query.setParameter("propertyType", propertyType.trim());
            
            List<Property> results = query.getResultList();
            LOGGER.log(Level.INFO, "Found {0} properties of type: {1}", 
                      new Object[]{results.size(), propertyType});
            return results;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error searching properties by type: " + propertyType, e);
            return new ArrayList<>();
        }
    }

    /**
     * Get total count of sale properties
     * @return Total number of sale properties
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public long getTotalSalePropertyCount() {
        try {
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(sp) FROM SaleProperty sp", Long.class);
            Long result = query.getSingleResult();
            return result != null ? result : 0L;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting sale property count", e);
            return 0L;
        }
    }

    /**
     * Get total count of rent properties
     * @return Total number of rent properties
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public long getTotalRentPropertyCount() {
        try {
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(rp) FROM RentProperty rp", Long.class);
            Long result = query.getSingleResult();
            return result != null ? result : 0L;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting rent property count", e);
            return 0L;
        }
    }

    /**
     * Get average sale price
     * @return Average sale price or 0 if no properties
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public double getAverageSalePrice() {
        try {
            TypedQuery<Double> query = em.createQuery("SELECT AVG(sp.salePrice) FROM SaleProperty sp", Double.class);
            Double result = query.getSingleResult();
            return result != null ? result : 0.0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting average sale price", e);
            return 0.0;
        }
    }

    /**
     * Get average weekly rent
     * @return Average weekly rent or 0 if no properties
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public double getAverageWeeklyRent() {
        try {
            TypedQuery<Double> query = em.createQuery("SELECT AVG(rp.weeklyRent) FROM RentProperty rp", Double.class);
            Double result = query.getSingleResult();
            return result != null ? result : 0.0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting average weekly rent", e);
            return 0.0;
        }
    }

    // ==================== VALIDATION METHODS ====================

    /**
     * Validate sale property data
     * @param property SaleProperty to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateSaleProperty(SaleProperty property) {
        validateBaseProperty(property);
        
        if (property.getSalePrice() == null || property.getSalePrice() <= 0) {
            throw new IllegalArgumentException("Sale price must be greater than 0");
        }
    }

    /**
     * Validate rent property data
     * @param property RentProperty to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateRentProperty(RentProperty property) {
        validateBaseProperty(property);
        
        if (property.getWeeklyRent() == null || property.getWeeklyRent() <= 0) {
            throw new IllegalArgumentException("Weekly rent must be greater than 0");
        }
        
        if (property.getIsFurnished() == null) {
            throw new IllegalArgumentException("Furnished status must be specified");
        }
    }

    /**
     * Validate base property data
     * @param property Property to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateBaseProperty(Property property) {
        if (property.getPropertyType() == null || property.getPropertyType().trim().isEmpty()) {
            throw new IllegalArgumentException("Property type is required");
        }
        
        if (property.getNoOfBedrooms() < 0) {
            throw new IllegalArgumentException("Number of bedrooms cannot be negative");
        }
        
        if (property.getNoOfBathrooms() < 0) {
            throw new IllegalArgumentException("Number of bathrooms cannot be negative");
        }
        
        if (property.getPropertyDescription() == null || property.getPropertyDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Property description is required");
        }
        
        if (property.getAddress() == null) {
            throw new IllegalArgumentException("Property address is required");
        }
        
        // Validate address
        validateAddress(property.getAddress());
    }

    /**
     * Validate address data
     * @param address Address to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateAddress(realestatemanagement.model.Address address) {
        if (address.getStreetNumber() <= 0) {
            throw new IllegalArgumentException("Street number must be greater than 0");
        }
        
        if (address.getStreetName() == null || address.getStreetName().trim().isEmpty()) {
            throw new IllegalArgumentException("Street name is required");
        }
        
        if (address.getCity() == null || address.getCity().trim().isEmpty()) {
            throw new IllegalArgumentException("City is required");
        }
        
        if (address.getPostcode() == null || address.getPostcode() <= 0) {
            throw new IllegalArgumentException("Valid postcode is required");
        }
        
        if (address.getCountry() == null || address.getCountry().trim().isEmpty()) {
            throw new IllegalArgumentException("Country is required");
        }
    }
}
