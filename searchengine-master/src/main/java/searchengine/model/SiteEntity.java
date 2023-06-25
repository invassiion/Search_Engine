package searchengine.model;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "site")
@Data
@NoArgsConstructor
public class SiteEntity   {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( unique = true ,nullable = false)
    private Integer id;


    @Enumerated(EnumType.STRING)
    @Column( nullable = false)
    private IndexedStatus status;

    @Column(name = "status_time")
    private Timestamp statusTime;

    @Column(name = "last_error")
    private String lastError = null;

    @Column( nullable = false, columnDefinition = "VARCHAR(255)")
    private String name;

    @Column( nullable = false, columnDefinition = "VARCHAR(255)")
    private String url;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "site_id")
    private List<PageEntity> pages;

    @OneToMany
    @JoinColumn(name = "site_id")
    private List<LemmaEntity> lemmas;


    @Column( columnDefinition = "VARCHAR(255)")
    private String last_error;
  

}
