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
@Table(name = "comment_properties")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentPropertiesEntity {

    @Id
    @Column(name = "comment_id")
    private String commentId;

    @Basic(optional = false)
    @Column(name = "important")
    private boolean important;

    @Basic(optional = false)
    @Column(name = "hidden")
    private boolean hidden;
}
