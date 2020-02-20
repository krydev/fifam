package ukma.project.fifam.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ukma.project.fifam.Frequency;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Category {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "budget", precision = 10, scale = 2, nullable = false)
    private String budget;

    @Column(name = "currentExpenses", columnDefinition = "Decimal(10,2) default '0.00'", nullable = false)
    private String currentExpenses;

    @Enumerated(EnumType.STRING)
    @Column(name = "freq", nullable = false)
    private Frequency freq;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    public Category(){}

    public Category(String name, String budget, Frequency freq, User user) {
        this.name = name;
        this.budget = budget;
        this.freq = freq;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getCurrentExpenses() {
        return currentExpenses;
    }

    public void setCurrentExpenses(String currentExpenses) {
        this.currentExpenses = currentExpenses;
    }

    public Frequency getFreq() {
        return freq;
    }

    public void setFreq(Frequency freq) {
        this.freq = freq;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", budget='" + budget + '\'' +
                ", currentExpenses='" + currentExpenses + '\'' +
                ", freq=" + freq +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id == category.id &&
                name.equals(category.name) &&
                currentExpenses.equals(category.currentExpenses) &&
                budget.equals(category.budget) &&
                freq == category.freq;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, budget, currentExpenses, freq);
    }
}
