package pl.jdacewicz.postservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_advertisements")
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String content;

    //img

    @ManyToMany
    @JoinTable(
            name = "advertisements_reactions",
            joinColumns = @JoinColumn(name = "advertisement_id"),
            inverseJoinColumns = @JoinColumn(name = "reaction_id"))
    @Builder.Default
    private List<Reaction> reactions = new LinkedList<>();

    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @Builder.Default
    private boolean active = true;
}
