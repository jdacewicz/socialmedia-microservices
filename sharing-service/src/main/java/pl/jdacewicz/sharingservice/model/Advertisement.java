package pl.jdacewicz.sharingservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.jdacewicz.sharingservice.util.FileStorageUtils;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "t_advertisements")
public class Advertisement extends Post {

    public static final String ADVERTISEMENTS_DIRECTORY_PATH = "uploads/advertisements";

    private String name;

    @Builder.Default
    private boolean active = true;

    @Override
    public String getDirectoryPath() {
        return FileStorageUtils.getDirectoryPath(super.getId(), ADVERTISEMENTS_DIRECTORY_PATH);
    }

}
