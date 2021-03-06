package cz.fi.muni.pa165.teamred.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * This class represents a User in our application.
 *
 * @author Jozef Cibík
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @NotNull(message = "User Name cannot be null.")
    @Column(nullable = false)
    @Size(
            min = 1,
            max = 255,
            message = "Name must be longer than 2 characters and shorter than 255 characters."
    )
    private String name;

    @NotNull(message = "User Surname cannot be null.")
    @Column(nullable = false)
    @Size(min = 1, max = 255)
    private String surname;

    @NotNull(message = "User Nickname cannot be null.")
    @Column(nullable = false, unique = true)
    @Size(min = 3, max = 255)
    private String nickname;

    @NotNull(message = "User Login ID cannot be null.")
    @Column(name = "login", updatable = false, nullable = false)
    private String loginId;

    private boolean isAdmin;

    @OneToMany(mappedBy = "driver")
    private Set<Ride> ridesAsDriver = new HashSet<>();

    @ManyToMany(mappedBy = "passengers")
    private Set<Ride> ridesAsPassenger = new HashSet<>();

    @OneToMany(mappedBy = "author")
    private Set<Comment> userComments = new HashSet<>();

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
        return new HashSet<>(this.userComments);
    }

    public void setUserComments(Set<Comment> userComments) {
        this.userComments = userComments;
    }

    public Set<Ride> getRidesAsDriver() {
        return new HashSet<>(ridesAsDriver);
    }

    public void setRidesAsDriver(Set<Ride> ridesAsDriver) {
        this.ridesAsDriver = ridesAsDriver;
    }

    public Set<Ride> getRidesAsPassenger() {
        return new HashSet<>(ridesAsPassenger);
    }

    public void setRidesAsPassenger(Set<Ride> ridesAsPassenger) {
        this.ridesAsPassenger = ridesAsPassenger;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    //__________________________________________________________________________________________________Adds and Removes

    public void addComment(Comment comment){
        this.userComments.add(comment);
    }

    public boolean removeComment(Comment comment){
        return this.userComments.remove(comment);
    }

    public void addRideAsPassenger(Ride ride){
        this.ridesAsPassenger.add(ride);
    }

    public boolean removeRideAsPassenger(Ride ride){ return this.ridesAsPassenger.remove(ride); }

    public void addRideAsDriver(Ride ride){
        this.ridesAsDriver.add(ride);
    }

    public boolean removeRideAsDriver(Ride ride){ return this.ridesAsDriver.remove(ride); }

    //________________________________________________________________________________________Equals and HashCode method

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return Objects.equals(getNickname(), user.getNickname());
    }

    @Override
    public int hashCode() {
        return getNickname() != null ? getNickname().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
