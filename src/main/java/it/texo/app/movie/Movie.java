package it.texo.app.movie;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Movie extends PanacheEntity {

    @Column(nullable = false)
    Integer year;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    String studios;

    @Column(nullable = false)
    String producers;

    boolean winner;

}
