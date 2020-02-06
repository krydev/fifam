package ukma.project.fifam.models;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserCategoryIdentity implements Serializable {
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserCategoryIdentity that = (UserCategoryIdentity) o;
        return user.equals(that.user) &&
                category.equals(that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, category);
    }
}
