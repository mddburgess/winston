package ca.metricalsky.winston.database.entity

import jakarta.persistence.Basic
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "settings")
class SettingsEntity (

    @Id
    @Column(name = "setting_name")
    var name: String,

    @Basic
    @Column(name = "setting_value")
    var value: String
)
