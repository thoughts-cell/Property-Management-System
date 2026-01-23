package realestatemanagement.ejb;

import jakarta.ejb.Stateless;
import jakarta.ejb.LocalBean;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;
import realestatemanagement.model.Allocation;

/**
 *
 * @author 12233612
 */
@Stateless
@LocalBean
public class AllocationEJB {

    @PersistenceContext(unitName = "RealEstateManagementPU")
    private EntityManager em;

    public List<Allocation> findAllocations() {
        Query query = em.createNamedQuery("findAllAllocations");
        return query.getResultList();
    }
    public List<Allocation> findAllocationsByPropertyManagerId(Long propertyManagerId) {
        return em.createQuery("SELECT a FROM Allocation a WHERE a.propertyManager.id = :propertyManagerId", Allocation.class)
        .setParameter("propertyManagerId", propertyManagerId).getResultList();
        
    }
    public Allocation findAllocationsById(Long id) {
        return em.find(Allocation.class, id);
        
    }
    public Allocation createAllocation(Allocation allocation) {
        em.persist(allocation);
        return allocation;
    }


    public void deleteAllocation(Allocation allocation) {
        Allocation a = em.find(Allocation.class, allocation.getId());
        em.remove(a);
        
    }
    public Allocation updateAllocation(Allocation allocation) {
        return em.merge(allocation);
    }
    public int  countAllocations() {

        Query query = em.createNamedQuery("getTotalAllocations");
        Long count = (Long) query.getSingleResult();
        return count.intValue();
    }
}
