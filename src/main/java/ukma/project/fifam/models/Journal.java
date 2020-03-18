package ukma.project.fifam.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
public class Journal {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "record_date", nullable = false)
    private long recordDate;

    @Column(name = "sum", nullable = false, precision = 10, scale = 2)
    private String sum;

    @Column(name = "description", length = 512)
    private String desc;

    @Column(name = "currentBalance", nullable = false, precision = 10, scale = 2)
    private String currBalance;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    private Category category;

    public Journal(){}

    public Journal(User user, Category category, long recordDate, String sum,
                   String description, String currBalance){
        this.user = user;
        this.category = category;
        this.recordDate = recordDate;
        this.sum = sum;
        this.desc = description;
        this.currBalance = currBalance;
    }

    public long getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(long recordDate) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Journal{" +
                "id=" + id +
                ", recordDate=" + recordDate +
                ", sum='" + sum + '\'' +
                ", desc='" + desc + '\'' +
                ", currBalance='" + currBalance + '\'' +
                ", category=" + category +
                '}';
    }
}
