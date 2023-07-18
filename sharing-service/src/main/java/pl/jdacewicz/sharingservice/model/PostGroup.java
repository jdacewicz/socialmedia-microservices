package pl.jdacewicz.sharingservice.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.LinkedList;
import java.util.List;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "t_posts_groups")
public class PostGroup extends Group{

    public static final String POST_GROUPS_DIRECTORY_PATH = "uploads/groups/post-groups/";

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User creator;

    private String image;

    @ManyToMany
    @JoinTable(
            name = "groups_posts",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    @Builder.Default
    private List<Post> posts = new LinkedList<>();

    public void addPost(Post post) {
        this.posts.add(post);
        post.getPostGroupList().add(this);
    }

    public void removePost(Post post) {
        this.posts.remove(post);
        post.getPostGroupList().remove(this);
    }

    public String getImagePath() {
        if (image == null) {
            return null;
        }
        return getDirectoryPath() + "/" + image;
    }

    public String getDirectoryPath() {
        return POST_GROUPS_DIRECTORY_PATH + "/" + super.getId();
    }
}
