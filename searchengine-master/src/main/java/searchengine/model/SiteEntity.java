package searchengine.model;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "site")
@Getter
@Setter
@NoArgsConstructor
public class SiteEntity   {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column( nullable = false)
    private int id;

    @Column( nullable = false, columnDefinition = "VARCHAR(255")
    private String name;

    @Column( nullable = false, columnDefinition = "VARCHAR(255")
    private String url;

    @Enumerated(EnumType.STRING)
    @Column( columnDefinition = "enum('INDEXING', 'INDEXED', 'FAILED')", nullable = false)
   IndexedStatus status;

    @Column( nullable = false)
    private LocalDateTime status_time;

    @Column( columnDefinition = "VARCHAR(255")
    private String last_error;

    public SiteEntity(IndexedStatus status, String last_error, String url, String name,LocalDateTime status_time ){
        this.status = status;
        this.last_error = last_error;
        this.url = url;
        this.name = name;
        this.status_time = status_time;
    }

}
