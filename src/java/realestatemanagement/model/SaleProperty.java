package realestatemanagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;

/**
 *
 * @author akshay benny
 *
 */
@Entity

@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries({
    @NamedQuery(name = "findAllSalePropertys", query = "SELECT sp FROM SaleProperty sp")
})
public class SaleProperty extends Property {


    private Long salePrice;

    /**
     * @return the salePrice
     */
    public Long getSalePrice() {
        return salePrice;
    }

    /**
     * @param salePrice the salePrice to set
     */
    public void setSalePrice(Long salePrice) {
        this.salePrice = salePrice;
    }

}
