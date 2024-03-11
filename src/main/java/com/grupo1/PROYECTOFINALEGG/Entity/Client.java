package com.grupo1.PROYECTOFINALEGG.Entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
@DiscriminatorValue("CLIENT")
public class Client extends User {

}
