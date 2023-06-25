package searchengine.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Table(name = "indexx")
@Getter
@Setter
public class IndexEntity implements Comparable<IndexEntity> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column(name = "page_id",nullable = false)
    private int pageId;

    @Column(name = "lemma_id",nullable = false)
    private int lemmaId;

    @Column(nullable = false)
    private float lemmaCount;
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id",insertable = false,updatable = false,nullable = false)
    private PageEntity pageEntity;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "lemma_id",insertable = false,updatable = false,nullable = false)
    private LemmaEntity lemmaEntity;

    @Column(name ="rank", nullable = false)
    private float rank;

    @Override
    public int compareTo(IndexEntity o) {
        return Float.compare(o.getRank(), this.getRank());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndexEntity that = (IndexEntity) o;
        return lemmaEntity.equals(that.lemmaEntity) && pageEntity.equals(that.pageEntity);
    }

    @Override
    public int hashCode(){
        return Objects.hash(lemmaId, pageId);
    }
}
