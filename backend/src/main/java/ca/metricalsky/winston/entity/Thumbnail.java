package ca.metricalsky.winston.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "thumbnails")
@Getter
@Setter
public class Thumbnail {

    @Id
    private String id;

    private String url;

    private byte[] image;
}
