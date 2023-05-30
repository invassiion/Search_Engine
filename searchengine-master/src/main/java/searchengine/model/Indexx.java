package searchengine.model;

import lombok.*;
import org.hibernate.engine.profile.Fetch;

import javax.persistence.*;


@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "`index`")
@Getter
@Setter
public class Indexx {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(cascade = {CascadeType.REMOVE, CascadeType.MERGE, CascadeType.REFRESH}, fetch  = FetchType.LAZY)
    @JoinColumn( name = "page_id", nullable = false)
    private PageEntity page;

    @ManyToOne(cascade = {CascadeType.REMOVE, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private LemmaEntity lemma;
    @Column(name = "lemma_rank" , nullable = false)
    private float rank;
    public static Indexx newIndex(PageEntity page, LemmaEntity lemma, Integer i){
        return Indexx.builder()
                .page(page)
                .lemma(lemma)
                .rank(i.floatValue())
                .build();
    }
}