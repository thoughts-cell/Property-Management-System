package realestatemanagement.ejb;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import realestatemanagement.model.User;

/**
 *
 * @author akshay benny
 * 
 */

@Stateless
@LocalBean
public class UserEJB {

        
    @PersistenceContext(unitName = "RealEstateManagementPU")
    private EntityManager em;

    //@Override
    public User findUserById(Long id) {
        return em.find(User.class, id);
    }
    
    public User findUserByEmail(String email) {
        return em.find(User.class, email);
    }

    //@Override
    public void createUser(User user) {
        em.persist(user);
    }

    public List<User> findUsers() {
     throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}