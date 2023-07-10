package pl.jdacewicz.postservice.model;

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

    //Creator

    @ManyToMany
    @JoinTable(
            name = "groups_posts",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    @Builder.Default
    private List<Post> posts = new LinkedList<>();
}
