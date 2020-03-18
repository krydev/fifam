package ukma.project.fifam.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ukma.project.fifam.Frequency;

import javax.persistence.*;


@Entity
public class PeriodicPays {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "last_pay_date", nullable = false)
    private long lastPayDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "freq", nullable = false)
    private Frequency freq;

    @Column(name = "sum", nullable = false, precision = 10, scale = 2)
    private String sum;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    private Category category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLastPayDate() {
        return lastPayDate;
    }

    public void setLastPayDate(long lastPayDate) {
        this.lastPayDate = lastPayDate;
    }

    public Frequency getFreq() {
        return freq;
    }

    public void setFreq(Frequency freq) {
        this.freq = freq;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "PeriodicPays{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastPayDate=" + lastPayDate +
                ", freq=" + freq +
                ", sum='" + sum + '\'' +
                '}';
    }
}
