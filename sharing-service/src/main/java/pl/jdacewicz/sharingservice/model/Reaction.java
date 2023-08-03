package pl.jdacewicz.sharingservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.jdacewicz.sharingservice.util.FileStorageUtils;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_reactions")
public class Reaction {

    public static final String REACTIONS_DIRECTORY_PATH = "uploads/reactions";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String image;

    @ManyToMany(mappedBy = "reactions")
    private List<Post> postList;

    @ManyToMany(mappedBy = "reactions")
    private List<Comment> commentList;

    @ManyToMany(mappedBy = "reactions")
    private List<Advertisement> advertisementList;

    public String getImagePath() {
        return FileStorageUtils.getImagePath(this.id, this.image, REACTIONS_DIRECTORY_PATH);
    }

    public String getDirectoryPath() {
        return FileStorageUtils.getDirectoryPath(this.id, REACTIONS_DIRECTORY_PATH);
    }
}
