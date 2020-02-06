package ukma.project.fifam.models;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Journal {

    @EmbeddedId
    private UserCategoryIdentity id;

    @Column(name = "record_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date recordDate;

    @Column(name = "sum", nullable = false, precision = 10, scale = 2)
    private String sum;

    @Column(name = "description", length = 512, nullable = false)
    private String desc;

    @Column(name = "currentBalance", nullable = false, precision = 10, scale = 2)
    private String currBalance;

    public UserCategoryIdentity getId() {
        return id;
    }

    public void setId(UserCategoryIdentity id) {
        this.id = id;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCurrBalance() {
        return currBalance;
    }

    public void setCurrBalance(String currBalance) {
        this.currBalance = currBalance;
    }

    @Override
    public String toString() {
        return "Journal{" +
                "id=" + id +
                ", recordDate=" + recordDate +
                ", sum='" + sum + '\'' +
                ", desc='" + desc + '\'' +
                ", currBalance='" + currBalance + '\'' +
                '}';
    }
}
