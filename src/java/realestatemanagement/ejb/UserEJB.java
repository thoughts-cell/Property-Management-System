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
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import realestatemanagement.model.User;

/**
 * Enterprise Java Bean for User management operations
 * Provides CRUD operations and business logic for User entities
 * 
 * @author Zhengxu
 */
@Stateless
@LocalBean
public class UserEJB {

    private static final Logger LOGGER = Logger.getLogger(UserEJB.class.getName());
    
    @PersistenceContext(unitName = "RealEstateManagementPU")
    private EntityManager em;

    /**
     * Find user by ID
     * @param id User ID
     * @return User entity or null if not found
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public User findUserById(Integer id) {
        if (id == null) {
            LOGGER.log(Level.WARNING, "Attempted to find user with null ID");
            return null;
        }
        
        try {
            return em.find(User.class, id);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding user by ID: " + id, e);
            return null;
        }
    }
    
    /**
     * Find user by email address using named query
     * @param email User email
     * @return User entity or null if not found
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public User findUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            LOGGER.log(Level.WARNING, "Attempted to find user with null or empty email");
            return null;
        }
        
        try {
            TypedQuery<User> query = em.createNamedQuery("User.findByEmail", User.class);
            query.setParameter("email", email.trim().toLowerCase());
            return query.getSingleResult();
        } catch (NoResultException e) {
            LOGGER.log(Level.INFO, "No user found with email: " + email);
            return null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding user by email: " + email, e);
            return null;
        }
    }

    /**
     * Find user by username
     * @param username Username
     * @return User entity or null if not found
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public User findUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            LOGGER.log(Level.WARNING, "Attempted to find user with null or empty username");
            return null;
        }
        
        try {
            TypedQuery<User> query = em.createNamedQuery("User.findByUsername", User.class);
            query.setParameter("username", username.trim());
            return query.getSingleResult();
        } catch (NoResultException e) {
            LOGGER.log(Level.INFO, "No user found with username: " + username);
            return null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding user by username: " + username, e);
            return null;
        }
    }

    /**
     * Create a new user
     * @param user User entity to create
     * @return Created user with generated ID
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public User createUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        
        // Validate required fields
        validateUser(user);
        
        // Check for duplicate email
        if (findUserByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("User with email " + user.getEmail() + " already exists");
        }
        
        // Check for duplicate username
        if (findUserByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("User with username " + user.getUsername() + " already exists");
        }
        
        try {
            // Set creation timestamp
            user.setSince(new Date());
            
            // Normalize email to lowercase
            user.setEmail(user.getEmail().trim().toLowerCase());
            
            em.persist(user);
            em.flush(); // Force immediate persistence to get generated ID
            
            LOGGER.log(Level.INFO, "Created new user with ID: " + user.getId());
            return user;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating user: " + user.getEmail(), e);
            throw new RuntimeException("Failed to create user", e);
        }
    }

    /**
     * Update an existing user
     * @param user User entity to update
     * @return Updated user
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public User updateUser(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("User and user ID cannot be null");
        }
        
        validateUser(user);
        
        try {
            // Check if user exists
            User existingUser = findUserById(user.getId());
            if (existingUser == null) {
                throw new IllegalArgumentException("User with ID " + user.getId() + " not found");
            }
            
            // Check for duplicate email (excluding current user)
            User emailUser = findUserByEmail(user.getEmail());
            if (emailUser != null && !emailUser.getId().equals(user.getId())) {
                throw new IllegalArgumentException("Email " + user.getEmail() + " is already in use");
            }
            
            // Check for duplicate username (excluding current user)
            User usernameUser = findUserByUsername(user.getUsername());
            if (usernameUser != null && !usernameUser.getId().equals(user.getId())) {
                throw new IllegalArgumentException("Username " + user.getUsername() + " is already in use");
            }
            
            // Normalize email
            user.setEmail(user.getEmail().trim().toLowerCase());
            
            User updatedUser = em.merge(user);
            LOGGER.log(Level.INFO, "Updated user with ID: " + user.getId());
            return updatedUser;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating user: " + user.getId(), e);
            throw new RuntimeException("Failed to update user", e);
        }
    }

    /**
     * Delete a user by ID
     * @param id User ID to delete
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteUser(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        
        try {
            User user = findUserById(id);
            if (user != null) {
                em.remove(user);
                LOGGER.log(Level.INFO, "Deleted user with ID: " + id);
            } else {
                LOGGER.log(Level.WARNING, "Attempted to delete non-existent user with ID: " + id);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting user: " + id, e);
            throw new RuntimeException("Failed to delete user", e);
        }
    }

    /**
     * Get all users
     * @return List of all users
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<User> findAllUsers() {
        try {
            Query query = em.createQuery("SELECT u FROM User u ORDER BY u.lastname, u.firstname");
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all users", e);
            throw new RuntimeException("Failed to retrieve users", e);
        }
    }

    /**
     * Search users by name
     * @param firstName First name (can be partial)
     * @param lastName Last name (can be partial)
     * @return List of matching users
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<User> searchUsersByName(String firstName, String lastName) {
        try {
            StringBuilder queryStr = new StringBuilder("SELECT u FROM User u WHERE 1=1");
            
            if (firstName != null && !firstName.trim().isEmpty()) {
                queryStr.append(" AND LOWER(u.firstname) LIKE LOWER(:firstName)");
            }
            if (lastName != null && !lastName.trim().isEmpty()) {
                queryStr.append(" AND LOWER(u.lastname) LIKE LOWER(:lastName)");
            }
            
            queryStr.append(" ORDER BY u.lastname, u.firstname");
            
            TypedQuery<User> query = em.createQuery(queryStr.toString(), User.class);
            
            if (firstName != null && !firstName.trim().isEmpty()) {
                query.setParameter("firstName", "%" + firstName.trim() + "%");
            }
            if (lastName != null && !lastName.trim().isEmpty()) {
                query.setParameter("lastName", "%" + lastName.trim() + "%");
            }
            
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error searching users by name", e);
            throw new RuntimeException("Failed to search users", e);
        }
    }

    /**
     * Get total count of users
     * @return Total number of users
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public long getTotalUserCount() {
        try {
            Query query = em.createQuery("SELECT COUNT(u) FROM User u");
            return (Long) query.getSingleResult();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting user count", e);
            return 0;
        }
    }

    /**
     * Validate user data
     * @param user User to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateUser(User user) {
        if (user.getFirstname() == null || user.getFirstname().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (user.getLastname() == null || user.getLastname().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        
        // Basic email validation
        if (!user.getEmail().contains("@") || !user.getEmail().contains(".")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        // Username validation (no spaces, minimum length)
        if (user.getUsername().contains(" ") || user.getUsername().length() < 3) {
            throw new IllegalArgumentException("Username must be at least 3 characters and contain no spaces");
        }
    }
}