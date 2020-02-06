package ukma.project.fifam.models;

import ukma.project.fifam.Frequency;

import javax.persistence.*;
import java.util.Date;

@Entity
public class PeriodicPays {
    @EmbeddedId
    private UserCategoryIdentity id;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "last_pay_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPayDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "freq", nullable = false)
    private Frequency freq;

    @Column(name = "sum", nullable = false, precision = 10, scale = 2)
    private String sum;

    public UserCategoryIdentity getId() {
        return id;
    }

    public void setId(UserCategoryIdentity id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastPayDate() {
        return lastPayDate;
    }

    public void setLastPayDate(Date lastPayDate) {
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
