package org.dungha.blooddonateweb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "storage_blood")
public class StorageBlood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int aPlusBlood;
    private int aMinusBlood;
    private int bPlusBlood;
    private int bMinusBlood;
    private int oPlusBlood;
    private int oMinusBlood;
    private int abPlusBlood;
    private int abMinusBlood;

}