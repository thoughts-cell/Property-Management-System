package realestatemanagement.ejb;

import jakarta.ejb.Stateless;
import jakarta.ejb.LocalBean;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;
import realestatemanagement.model.PropertyManager;

/**
 *
 * @author 12233612
 *
 *
 */
@Stateless
@LocalBean
public class PropertyManagerEJB {

    //attributes
    @PersistenceContext(unitName = "RealEstateManagementPU")
    private EntityManager em;

    public List findManagers() {
        Query query = em.createNamedQuery("findAllPropertyManagers");
        return query.getResultList();
    }

    public PropertyManager findPropertyManagerById(Long id) {
        return em.find(PropertyManager.class, id);
    }

    public List<PropertyManager> searchManager(String firstName, String lastName) {
        Query query = em.createNamedQuery("findPropertyManagerByName", PropertyManager.class);
        query.setParameter("firstName", firstName);
        query.setParameter("lastName", lastName);
        return query.getResultList();
    }

    public PropertyManager createManager(PropertyManager manager) {
        em.persist(manager);
        return manager;
    }

    public void deleteManager(PropertyManager manager) {
        PropertyManager pm = em.find(PropertyManager.class, manager.getId());
        em.remove(pm);
    }

    public int totalManagers() {
        Query query = em.createNamedQuery("getTotalManagers");
        return (int) query.getSingleResult();
    }

    public int totalProperties(PropertyManager manager) {
        Query query = em.createNamedQuery("getTotalProperties");
        query.setParameter("manager", manager);
        return (int) query.getSingleResult();
    }
}
