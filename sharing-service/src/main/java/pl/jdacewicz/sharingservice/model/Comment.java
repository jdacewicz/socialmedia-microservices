package pl.jdacewicz.sharingservice.model;

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

    public static final String COMMENTS_DIRECTORY_PATH = "uploads/comments";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String content;

    private String image;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User creator;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "advertisement_id")
    private Advertisement advertisement;

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

    public String getImagePath() {
        if (image == null) {
            return null;
        }
        return "/" + COMMENTS_DIRECTORY_PATH + "/" + id + "/" + image;
    }

    public void addReaction(Reaction reaction) {
        reactions.add(reaction);
        reaction.getCommentList().add(this);
    }
}
