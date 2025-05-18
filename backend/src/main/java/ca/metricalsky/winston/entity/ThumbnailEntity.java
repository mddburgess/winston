package ca.metricalsky.winston.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "thumbnails")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThumbnailEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Basic(optional = false)
    @Column(name = "url")
    private String url;

    @Basic(optional = false)
    @Column(name = "image")
    private byte[] image;
}
