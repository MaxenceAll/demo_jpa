package com.workshop.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue
    private Long id;
    private String streetName;
    private String houseNumber;
    private String zipCode;

    //@OneToOne
    //@JoinColumn(name="employee_id")
    //private Employee employee;
}
