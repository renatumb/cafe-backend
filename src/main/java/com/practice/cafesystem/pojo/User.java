package com.practice.cafesystem.pojo;

import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@NamedQuery(name = "User.findByEmailId", query = "select rec from User rec where rec.email=:email")

@NamedQuery(name = "User.getAllUsers", query = "select new com.practice.cafesystem.wrapper.UserWrapper(rec.id, rec.name,rec.email, rec.contactNumber, rec.status) from User rec where rec.role='user'")

@NamedQuery(name = "User.getAllAdmin", query = "select email from User rec where rec.role='admin'")

@NamedQuery( name = "User.updateStatus", query = "update User u set u.status=:status where u.id=:id" )

@Data
@Entity
@DynamicInsert
@DynamicUpdate

@Table(name = "usuario")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "contactNumber")
    private String contactNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private String status;

    @Column(name = "role")
    private String role;
}
