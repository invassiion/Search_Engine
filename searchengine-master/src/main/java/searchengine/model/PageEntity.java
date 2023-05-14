package searchengine.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "page")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @ManyToOne(targetEntity = SiteEntity.class)
    @JoinColumn(name = "site_id", nullable = false, insertable = false,updatable = false)
    private SiteEntity siteId;

    @OneToMany(mappedBy = "page", cascade = CascadeType.REMOVE)
    private List<searchengine.model.Indexx> indexxes;

    @Column(columnDefinition = "TEXT NOT NULL, UNIQUE KEY pathIndex (path(512),site_id)")
    private String path;


    @Column( nullable = false, columnDefinition = "VARCHAR(255)")
    private int code;

    @Column( columnDefinition = "MEDIUMTEXT NOT NULL")
    private String content;


}
