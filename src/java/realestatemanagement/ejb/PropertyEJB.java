package realestatemanagement.ejb;

/**
 *
 * @author harsh patel
 */
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import realestatemanagement.model.RentProperty;
import realestatemanagement.model.SaleProperty;

@Stateless
public class PropertyEJB {

    @PersistenceContext(unitName = "RealEstateManagementPU")
    private EntityManager em;

    public void createSaleProperty(SaleProperty property) {
//        System.out.println("Persisting SaleProperty: " + property.getName());  // Log property details
        em.persist(property);   
    }
    
    // Fetch all sale properties
    public List<SaleProperty> findAllSaleProperties() {
        return em.createNamedQuery("findAllSalePropertys", SaleProperty.class).getResultList();
    }

    // Other methods for property management...

    public SaleProperty findSalePropertyById(Long id) {
        return em.find(SaleProperty.class, id);
    }
    
    // RentProperty related methods
    public void createRentProperty(RentProperty property) {
        em.persist(property);   
    }

    public List<RentProperty> findAllRentProperties() {
        return em.createNamedQuery("findAllRentPropertys", RentProperty.class).getResultList();
    }

    public RentProperty findRentPropertyById(Long id) {
        return em.find(RentProperty.class, id);
    }
}
