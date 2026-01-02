package realestatemanagement.ejb;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import realestatemanagement.model.PropertyManager;

/**
 * Enterprise Java Bean for Property Manager management operations
 * Provides CRUD operations and business logic for PropertyManager entities
 * 
 * @author 12233612 Zhengxu
 * @version 2.0 - Enhanced with comprehensive error handling and validation
 */
@Stateless
@LocalBean
public class PropertyManagerEJB {

    private static final Logger LOGGER = Logger.getLogger(PropertyManagerEJB.class.getName());
    
    @PersistenceContext(unitName = "RealEstateManagementPU")
    private EntityManager em;

    /**
     * Get all property managers
     * @return List of all property managers ordered by last name, first name
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<PropertyManager> findAllManagers() {
        try {
            TypedQuery<PropertyManager> query = em.createNamedQuery("findAllPropertyManagers", PropertyManager.class);
            List<PropertyManager> managers = query.getResultList();
            LOGGER.log(Level.INFO, "Retrieved {0} property managers", managers.size());
            return managers;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all property managers", e);
            return new ArrayList<>();
        }
    }

    /**
     * Find property manager by ID
     * @param id Property manager ID
     * @return PropertyManager entity or null if not found
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public PropertyManager findPropertyManagerById(Long id) {
        if (id == null) {
            LOGGER.log(Level.WARNING, "Attempted to find property manager with null ID");
            return null;
        }
        
        try {
            PropertyManager manager = em.find(PropertyManager.class, id);
            if (manager != null) {
                LOGGER.log(Level.INFO, "Found property manager with ID: {0}", id);
            } else {
                LOGGER.log(Level.INFO, "No property manager found with ID: {0}", id);
            }
            return manager;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding property manager by ID: " + id, e);
            return null;
        }
    }

    /**
     * Search property managers by first name and last name (exact match)
     * @param firstName First name
     * @param lastName Last name
     * @return List of matching property managers
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<PropertyManager> searchManagerByName(String firstName, String lastName) {
        if ((firstName == null || firstName.trim().isEmpty()) && 
            (lastName == null || lastName.trim().isEmpty())) {
            LOGGER.log(Level.WARNING, "Search attempted with both first name and last name empty");
            return new ArrayList<>();
        }
        
        try {
            TypedQuery<PropertyManager> query = em.createNamedQuery("findPropertyManagerByName", PropertyManager.class);
            query.setParameter("firstName", firstName != null ? firstName.trim() : "");
            query.setParameter("lastName", lastName != null ? lastName.trim() : "");
            
            List<PropertyManager> results = query.getResultList();
            LOGGER.log(Level.INFO, "Found {0} property managers matching name search", results.size());
            return results;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error searching property managers by name", e);
            return new ArrayList<>();
        }
    }

    /**
     * Search property managers by first name only
     * @param firstName First name
     * @return List of matching property managers
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<PropertyManager> searchManagerByFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            LOGGER.log(Level.WARNING, "Search attempted with empty first name");
            return new ArrayList<>();
        }
        
        try {
            TypedQuery<PropertyManager> query = em.createNamedQuery("findPropertyManagerByFirstName", PropertyManager.class);
            query.setParameter("firstName", firstName.trim());
            
            List<PropertyManager> results = query.getResultList();
            LOGGER.log(Level.INFO, "Found {0} property managers with first name: {1}", 
                      new Object[]{results.size(), firstName});
            return results;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error searching property managers by first name: " + firstName, e);
            return new ArrayList<>();
        }
    }

    /**
     * Search property managers by last name only
     * @param lastName Last name
     * @return List of matching property managers
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<PropertyManager> searchManagerByLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            LOGGER.log(Level.WARNING, "Search attempted with empty last name");
            return new ArrayList<>();
        }
        
        try {
            TypedQuery<PropertyManager> query = em.createNamedQuery("findPropertyManagerByLastName", PropertyManager.class);
            query.setParameter("lastName", lastName.trim());
            
            List<PropertyManager> results = query.getResultList();
            LOGGER.log(Level.INFO, "Found {0} property managers with last name: {1}", 
                      new Object[]{results.size(), lastName});
            return results;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error searching property managers by last name: " + lastName, e);
            return new ArrayList<>();
        }
    }

    /**
     * Flexible search for property managers with partial name matching
     * @param searchTerm Search term to match against first name, last name, or email
     * @return List of matching property managers
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<PropertyManager> searchManagers(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return findAllManagers();
        }
        
        try {
            String searchPattern = "%" + searchTerm.trim().toLowerCase() + "%";
            
            TypedQuery<PropertyManager> query = em.createQuery(
                "SELECT pm FROM PropertyManager pm WHERE " +
                "LOWER(pm.firstName) LIKE :searchTerm OR " +
                "LOWER(pm.lastName) LIKE :searchTerm OR " +
                "LOWER(pm.email) LIKE :searchTerm " +
                "ORDER BY pm.lastName, pm.firstName", 
                PropertyManager.class);
            
            query.setParameter("searchTerm", searchPattern);
            
            List<PropertyManager> results = query.getResultList();
            LOGGER.log(Level.INFO, "Found {0} property managers matching search term: {1}", 
                      new Object[]{results.size(), searchTerm});
            return results;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in flexible search for property managers: " + searchTerm, e);
            return new ArrayList<>();
        }
    }

    /**
     * Find property manager by email
     * @param email Email address
     * @return PropertyManager or null if not found
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public PropertyManager findManagerByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            LOGGER.log(Level.WARNING, "Attempted to find property manager with null or empty email");
            return null;
        }
        
        try {
            TypedQuery<PropertyManager> query = em.createQuery(
                "SELECT pm FROM PropertyManager pm WHERE LOWER(pm.email) = LOWER(:email)", 
                PropertyManager.class);
            query.setParameter("email", email.trim());
            
            return query.getSingleResult();
        } catch (NoResultException e) {
            LOGGER.log(Level.INFO, "No property manager found with email: {0}", email);
            return null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding property manager by email: " + email, e);
            return null;
        }
    }

    /**
     * Create a new property manager
     * @param manager PropertyManager entity to create
     * @return Created property manager with generated ID
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public PropertyManager createManager(PropertyManager manager) {
        if (manager == null) {
            throw new IllegalArgumentException("Property manager cannot be null");
        }
        
        // Validate required fields
        validatePropertyManager(manager);
        
        // Check for duplicate email if provided
        if (manager.getEmail() != null && !manager.getEmail().trim().isEmpty()) {
            PropertyManager existingManager = findManagerByEmail(manager.getEmail());
            if (existingManager != null) {
                throw new IllegalArgumentException("Property manager with email " + manager.getEmail() + " already exists");
            }
        }
        
        try {
            // Normalize email to lowercase if provided
            if (manager.getEmail() != null && !manager.getEmail().trim().isEmpty()) {
                manager.setEmail(manager.getEmail().trim().toLowerCase());
            }
            
            em.persist(manager);
            em.flush(); // Force immediate persistence to get generated ID
            
            LOGGER.log(Level.INFO, "Created new property manager with ID: {0}", manager.getId());
            return manager;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating property manager", e);
            throw new RuntimeException("Failed to create property manager", e);
        }
    }

    /**
     * Update an existing property manager
     * @param manager PropertyManager entity to update
     * @return Updated property manager
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public PropertyManager updateManager(PropertyManager manager) {
        if (manager == null || manager.getId() == null) {
            throw new IllegalArgumentException("Property manager and ID cannot be null");
        }
        
        validatePropertyManager(manager);
        
        try {
            // Check if manager exists
            PropertyManager existingManager = findPropertyManagerById(manager.getId());
            if (existingManager == null) {
                throw new IllegalArgumentException("Property manager with ID " + manager.getId() + " not found");
            }
            
            // Check for duplicate email (excluding current manager)
            if (manager.getEmail() != null && !manager.getEmail().trim().isEmpty()) {
                PropertyManager emailManager = findManagerByEmail(manager.getEmail());
                if (emailManager != null && !emailManager.getId().equals(manager.getId())) {
                    throw new IllegalArgumentException("Email " + manager.getEmail() + " is already in use");
                }
                // Normalize email
                manager.setEmail(manager.getEmail().trim().toLowerCase());
            }
            
            PropertyManager updatedManager = em.merge(manager);
            LOGGER.log(Level.INFO, "Updated property manager with ID: {0}", manager.getId());
            return updatedManager;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating property manager: " + manager.getId(), e);
            throw new RuntimeException("Failed to update property manager", e);
        }
    }

    /**
     * Delete a property manager by ID
     * @param id Property manager ID to delete
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteManagerById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Property manager ID cannot be null");
        }
        
        try {
            PropertyManager manager = findPropertyManagerById(id);
            if (manager != null) {
                // Check if manager has allocations
                long allocationCount = getTotalPropertiesForManager(manager);
                if (allocationCount > 0) {
                    throw new IllegalStateException("Cannot delete property manager with " + allocationCount + " property allocations");
                }
                
                em.remove(manager);
                LOGGER.log(Level.INFO, "Deleted property manager with ID: {0}", id);
            } else {
                LOGGER.log(Level.WARNING, "Attempted to delete non-existent property manager with ID: {0}", id);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting property manager: " + id, e);
            throw new RuntimeException("Failed to delete property manager", e);
        }
    }

    /**
     * Delete a property manager entity
     * @param manager PropertyManager entity to delete
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteManager(PropertyManager manager) {
        if (manager == null || manager.getId() == null) {
            throw new IllegalArgumentException("Property manager and ID cannot be null");
        }
        
        deleteManagerById(manager.getId());
    }

    /**
     * Get total count of property managers
     * @return Total number of property managers
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public long getTotalManagerCount() {
        try {
            TypedQuery<Long> query = em.createNamedQuery("getTotalManagers", Long.class);
            Long result = query.getSingleResult();
            return result != null ? result : 0L;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting total manager count", e);
            return 0L;
        }
    }

    /**
     * Get total number of properties managed by a specific property manager
     * @param manager PropertyManager entity
     * @return Number of properties managed
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public long getTotalPropertiesForManager(PropertyManager manager) {
        if (manager == null) {
            LOGGER.log(Level.WARNING, "Attempted to get property count for null manager");
            return 0L;
        }
        
        try {
            TypedQuery<Long> query = em.createNamedQuery("getTotalProperties", Long.class);
            query.setParameter("manager", manager);
            Long result = query.getSingleResult();
            return result != null ? result : 0L;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting property count for manager: " + manager.getId(), e);
            return 0L;
        }
    }

    /**
     * Get property managers with the most properties
     * @param limit Maximum number of results to return
     * @return List of property managers ordered by property count (descending)
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<PropertyManager> getTopManagersByPropertyCount(int limit) {
        if (limit <= 0) {
            limit = 10; // Default limit
        }
        
        try {
            TypedQuery<PropertyManager> query = em.createQuery(
                "SELECT pm FROM PropertyManager pm " +
                "LEFT JOIN pm.allocations a " +
                "GROUP BY pm " +
                "ORDER BY COUNT(a) DESC", 
                PropertyManager.class);
            
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting top managers by property count", e);
            return new ArrayList<>();
        }
    }

    /**
     * Validate property manager data
     * @param manager PropertyManager to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validatePropertyManager(PropertyManager manager) {
        if (manager.getFirstName() == null || manager.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (manager.getLastName() == null || manager.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (manager.getPhone() == null || manager.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number is required");
        }
        if (manager.getMobile() == null || manager.getMobile().trim().isEmpty()) {
            throw new IllegalArgumentException("Mobile number is required");
        }
        
        // Basic email validation if provided
        if (manager.getEmail() != null && !manager.getEmail().trim().isEmpty()) {
            if (!manager.getEmail().contains("@") || !manager.getEmail().contains(".")) {
                throw new IllegalArgumentException("Invalid email format");
            }
        }
        
        // Phone number validation (basic)
        if (!isValidPhoneNumber(manager.getPhone())) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
        if (!isValidPhoneNumber(manager.getMobile())) {
            throw new IllegalArgumentException("Invalid mobile number format");
        }
    }

    /**
     * Basic phone number validation
     * @param phoneNumber Phone number to validate
     * @return true if valid format
     */
    private boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        
        // Remove common formatting characters
        String cleanNumber = phoneNumber.replaceAll("[\\s\\-\\(\\)\\+]", "");
        
        // Check if it contains only digits and is reasonable length
        return cleanNumber.matches("\\d{8,15}");
    }
}
