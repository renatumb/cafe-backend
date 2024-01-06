package com.practice.cafesystem.pojo;

import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@NamedQuery(name = "Product.getAllProducts", query = "select new com.practice.cafesystem.wrapper.ProductWrapper(prod.id, prod.name, prod.desccription, prod.price,prod.status, prod.category.Id, prod.category.name ) from  Product prod")

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "_product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_fk", nullable = false)
    Category category;

    String desccription;

    Integer price;

    String status;
}
