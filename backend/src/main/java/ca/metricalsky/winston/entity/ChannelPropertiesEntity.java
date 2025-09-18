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
@Table(name = "channel_properties")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelPropertiesEntity {

    @Id
    @Column(name = "channel_id")
    private String channelId;

    @Basic(optional = false)
    @Column(name = "archived")
    private boolean archived;
}
