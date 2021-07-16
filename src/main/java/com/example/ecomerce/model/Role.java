package com.example.ecomerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name="roles")
public class Role {
  @Id
  @Column(name = "role_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "role_name")
  @Enumerated(EnumType.STRING)
  @NaturalId
  private RoleName role;

  @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
  private Set<User> userList = new HashSet<>();

  public Role(RoleName role) {
    this.role = role;
  }

  public Role() {

  }

  public boolean isAdminRole() {
    return null != this && this.role.equals(RoleName.ROLE_ADMIN);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public RoleName getRole() {
    return role;
  }

  public void setRole(RoleName role) {
    this.role = role;
  }

  public Set<User> getUserList() {
    return userList;
  }

  public void setUserList(Set<User> userList) {
    this.userList = userList;
  }
}
