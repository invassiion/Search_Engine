package searchengine.model;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "page")
@Getter
@Setter
@NoArgsConstructor
public class PageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @ManyToOne(targetEntity = SiteEntity.class)
    @JoinColumn(name = "site_id", nullable = false, insertable = false,updatable = false)
    private SiteEntity siteId;

    @Column(name = "path", nullable = false, columnDefinition = "VARCHAR(255")
    @PrimaryKeyJoinColumn(name ="path" ,columnDefinition = "VARCHAR 255")
    private String path;


    @Column( nullable = false, columnDefinition = "VARCHAR(255")
    private int code;

    @Column( columnDefinition = "MEDIUMTEXT", nullable = false)
    private String content;

    public PageEntity(int id, String path, String content,int code)
    {
        this.id = id;
        this.path = path;
        this.content= content;
        this.code = code;
    }
}
