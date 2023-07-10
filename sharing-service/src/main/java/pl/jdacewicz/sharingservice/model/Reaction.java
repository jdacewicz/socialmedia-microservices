package pl.jdacewicz.sharingservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_reactions")
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    //image

    @ManyToMany(mappedBy = "reactions")
    private List<Post> postList;

    @ManyToMany(mappedBy = "reactions")
    private List<Comment> commentList;

    @ManyToMany(mappedBy = "reactions")
    private List<Advertisement> advertisementList;
}
