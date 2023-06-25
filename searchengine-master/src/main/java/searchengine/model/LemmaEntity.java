package searchengine.model;
import lombok.*;
import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "lemma",uniqueConstraints = @UniqueConstraint(columnNames = "lemma"))
@NoArgsConstructor
@Data
public class LemmaEntity implements Comparable<LemmaEntity>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private String lemma;

    @Column(nullable = false)
    private int frequency;

//    @Column(name = "site_id", nullable = false)
//    private int siteId;

    @ManyToOne
    @JoinColumn(name = "site_id",insertable = false,updatable = false,nullable = false)
    private SiteEntity siteId;

    @Override
    public int compareTo(LemmaEntity lemmaEntity) {
        return this.frequency - lemmaEntity.getFrequency();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LemmaEntity that = (LemmaEntity) o;
        return siteId.equals(that.siteId) && lemma.equals(that.lemma);
    }

    @Override
    public int hashCode() {
        return Objects.hash(siteId, lemma);
    }

}
