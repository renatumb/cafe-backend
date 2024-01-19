package com.practice.cafesystem.pojo;

import java.io.Serializable;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@NamedQuery( name = "Bill.getAllBills", query="select bl from Bill bl order by bl.id desc")

@NamedQuery( name = "Bill.getBillsByUserName", query="select bl from Bill bl where bl.createdBy =:username order by bl.id desc")

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "_bill")
public class Bill implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String uuid;

    private String name;

    private String email;

    private String contactNumber;

    private String paymentMethod;

    private Integer totalAmount;

    //@Column(name="product_detail", columnDefinition = "json")
    @Column(columnDefinition = "text")
    private String productDetails;

    private String createdBy;
}
