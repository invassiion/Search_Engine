package searchengine.model;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import javax.persistence.*;

@Entity
@Table(name = "page",uniqueConstraints=@UniqueConstraint(columnNames={"site_id", "path"}))
@Data
@NoArgsConstructor
public class PageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( unique = true,nullable = false)
    private Integer id;

    @Column(columnDefinition = "TEXT NOT NULL")
    private String path;

    @Column( nullable = false)
    private int code;

    @Column( columnDefinition = "MEDIUMTEXT",nullable = false)
    private String content;

//    @Column(name = "site_id", nullable = false)
//    private int siteId;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", nullable = false, insertable = false,updatable = false)
    private SiteEntity siteId;
}
