package com.easysoft.ecommerce.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: vtran
 * Date: Jun 1, 2010
 * Time: 5:23:20 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "relation_type")
public class RelationType implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
