package ukma.project.fifam.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ukma.project.fifam.Frequency;

import javax.persistence.*;

@Entity
public class BalanceFiller {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "last_pay_date", nullable = false)
    private long lastPayDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "freq", nullable = false)
    private Frequency freq;

    @Column(name = "sum", nullable = false, precision = 10, scale = 2)
    private String sum;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "BalanceFiller{" +
                "id=" + id +
                ", lastPayDate=" + lastPayDate +
                ", freq=" + freq +
                ", sum='" + sum + '\'' +
                ", user=" + user +
                '}';
    }
}
