package pl.jdacewicz.sharingservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.jdacewicz.sharingservice.util.FileStorageUtils;

import java.util.LinkedList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_users")
public class User {

    public static final String USERS_DIRECTORY_PATH = "uploads/profiles";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String email;

    private String profilePicture;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Post> posts = new LinkedList<>();

    public String getImagePath() {
        return FileStorageUtils.getImagePath(this.id, this.profilePicture, USERS_DIRECTORY_PATH);
    }

    public String getDirectoryPath() {
        return FileStorageUtils.getDirectoryPath(this.id, USERS_DIRECTORY_PATH);
    }
}
