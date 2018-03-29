package org.gpaulo.springdemo.models;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "users")
public class User implements Serializable {
  private static final long serialVersionUID = 1709299723906950395L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @NotNull @NotEmpty @Column private String firstName;

  @NotNull @NotEmpty @Column private String lastName;

  @Positive @Column private int age;

  protected User() {}

  public User(String firstName, String lastName) {
    this(firstName, lastName, 1);
  }

  public User(String firstName, String lastName, int age) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.age = age;
  }

  public String toString() {
    return "User[" + firstName + ", " + lastName + ", " + age + "]";
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }
}
