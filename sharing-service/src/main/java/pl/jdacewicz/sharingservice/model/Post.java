package pl.jdacewicz.sharingservice.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "t_posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Builder.Default
    private LocalDateTime creationTime = LocalDateTime.now();

    private String content;

    //image

    //User creator

    @ManyToMany
    @JoinTable(
            name = "posts_reactions",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "reaction_id"))
    @Builder.Default
    private List<Reaction> reactions = new LinkedList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Comment> comments = new LinkedList<>();

    @ManyToMany(mappedBy = "posts")
    private Set<PostGroup> postGroupList = new HashSet<>();

    @Builder.Default
    private boolean visible = true;

    public void addReaction(Reaction reaction) {
        reactions.add(reaction);
        reaction.getPostList().add(this);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
    }

    public void addPostGroup(PostGroup postGroup) {
        postGroupList.add(postGroup);
        postGroup.getPosts().add(this);
    }

    public void removePostGroup(PostGroup postGroup) {
        postGroupList.remove(postGroup);
        postGroup.getPosts().remove(this);
    }
}
