package com.tmm.myre.base.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MYRE_COUNTERS")
public class CountersModel {

    @Id
    @Column(name = "counterName", length = 50)
    private String counterName;

    @Column(name = "lastValue", nullable = false)
    private Long lastValue;

}

