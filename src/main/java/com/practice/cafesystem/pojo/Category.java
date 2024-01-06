package com.practice.cafesystem.pojo;

import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@NamedQuery(name = "Category.findAllforProduct", query = "select rec from Category rec where rec.id in ( select category from Product where status = 'true') " )

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table( name= "_category")
public class Category implements Serializable {

    private static final long serialVersionUID=1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    private String name;
}
