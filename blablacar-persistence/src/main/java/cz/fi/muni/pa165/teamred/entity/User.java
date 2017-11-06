package cz.fi.muni.pa165.teamred.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * This class represents a User in our application.
 *
 * Created by Jozef Cibík on 25.10.2017.
 */
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;


    @NotNull(message = "User Name cannot be null.")
    @Column(nullable = false)
    @Size(
            min = 2,
            max = 255,
            message = "Name must be longer than 2 characters and shorter than 255 characters."
    )
    private String name;

    @NotNull(message = "User Surname cannot be null.")
    @Column(nullable = false)
    private String surname;

    @NotNull(message = "User Nickname cannot be null.")
    @Column(nullable = false, unique = true)
    private String nickname;

    @OneToMany(mappedBy = "driver")
    private Set<Ride> ridesAsDriver;

    @ManyToMany(mappedBy = "passengers")
    private Set<Ride> ridesAsPassenger;

    @OneToMany(mappedBy = "author")
    private Set<Comment> userComments;

    //______________________________________________________________________________________________________Constructors

    /**
    * Default Constructor
    */
    public User() {
    }

    public User(String nickname) {
        this.nickname = nickname;
    }


    //_______________________________________________________________________________________________Getters and Setters

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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Set<Comment> getUserComments() {
        return userComments;
    }

    public void setUserComments(Set<Comment> userComments) {
        this.userComments = userComments;
    }

    //________________________________________________________________________________________Equals and HashCode method

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return Objects.equals(getNickname(), user.getNickname());
    }

    @Override
    public int hashCode() {
        return getNickname() != null ? getNickname().hashCode() : 0;
    }
}
