package searchengine.model;


import lombok.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "lemma",uniqueConstraints = @UniqueConstraint(columnNames = "lemma"))
@NoArgsConstructor
@Data
public class LemmaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;


@Table(name = "lemma")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LemmaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private SiteEntity site;

 
    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private String lemma;

    @Column(nullable = false)

    private int frequency;

    @Column(name = "site_id", nullable = false)
    private int siteId;


    @ManyToOne
    @JoinColumn(name = "site_id",insertable = false,updatable = false,nullable = false)
    private SiteEntity siteEntity;


    private int frecuency;

    @OneToMany(mappedBy = "lemma" , cascade = CascadeType.REMOVE)
    private List<Indexx> indexxes;

}
