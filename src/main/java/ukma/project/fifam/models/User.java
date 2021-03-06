package ukma.project.fifam.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
public class User {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "email", length = 255, nullable = false)
    private String email;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "balance", columnDefinition = "Decimal(10,2) default '0.00'", nullable = false)
    private String balance;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Category> categories;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Journal> journalRecords;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BalanceFiller> balanceFillers;


    public User() {
    }

    public User(String email, String password, String balance) {
        this.email = email;
        this.password = password;
        this.balance = balance;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBalance() {
        return balance;
    }

    public void increaseBalance(String value) {
        BigDecimal newBalance = new BigDecimal(balance).add(new BigDecimal(value));
        this.balance = newBalance.toString();
    }

    public void decreaseBalance(String value) {
        BigDecimal newBalance = new BigDecimal(balance).subtract(new BigDecimal(value));
        this.balance = newBalance.toString();
    }

    public List getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<BalanceFiller> getBalanceFillers() {
        return balanceFillers;
    }

    public void setBalanceFillers(List<BalanceFiller> balanceFillers) {
        this.balanceFillers = balanceFillers;
    }

    public List<Journal> getJournalRecords() {
        return journalRecords;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", balance='" + balance + '\'' +
                ", categories=" + categories +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                email.equals(user.email) &&
                Objects.equals(password, user.password) &&
                balance.equals(user.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, balance);
    }
}
