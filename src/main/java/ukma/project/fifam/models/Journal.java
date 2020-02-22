package ukma.project.fifam.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Journal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "record_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date recordDate;

    @Column(name = "sum", nullable = false, precision = 10, scale = 2)
    private String sum;

    @Column(name = "description", length = 512, nullable = false)
    private String desc;

    @Column(name = "currentBalance", nullable = false, precision = 10, scale = 2)
    private String currBalance;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;

    public Journal(User user, Category category, Date recordDate, String sum, String description, String currBalance){
        this.user = user;
        this.category = category;
        this.recordDate = recordDate;
        this.sum = sum;
        this.desc = description;
        this.currBalance = currBalance;
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
