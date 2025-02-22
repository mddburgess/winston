package ca.metricalsky.yt.comments.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Keyword {

    @Id
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
