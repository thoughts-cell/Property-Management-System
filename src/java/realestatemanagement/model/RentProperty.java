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
    @NamedQuery(name = "findAllRentPropertys", query = "SELECT rp FROM RentProperty rp")
})
public class RentProperty extends  Property {


    
    private Long weeklyRent;
    
    private Boolean isFurnished;

    /**
     * @return the weeklyRent
     */
    public Long getWeeklyRent() {
        return weeklyRent;
    }

    /**
     * @param weeklyRent the weeklyRent to set
     */
    public void setWeeklyRent(Long weeklyRent) {
        this.weeklyRent = weeklyRent;
    }

    /**
     * @return the isFurnished
     */
    public Boolean getIsFurnished() {
        return isFurnished;
    }

    /**
     * @param isFurnished the isFurnished to set
     */
    public void setIsFurnished(Boolean isFurnished) {
        this.isFurnished = isFurnished;
    }

}
