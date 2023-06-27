package pl.jdacewicz.postservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //creator User

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    @ManyToMany
    @JoinTable(
            name = "comments_reactions",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "reaction_id"))
    @Builder.Default
    private List<Reaction> reactions = new LinkedList<>();

    @Builder.Default
    private LocalDateTime creationTime = LocalDateTime.now();

    @Builder.Default
    private boolean visible = true;

    private String content;

    //img

    public void addReaction(Reaction reaction) {
        reactions.add(reaction);
        reaction.getCommentList().add(this);
    }
}
