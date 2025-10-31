package fr.rougeux.projet.auction.bo;

import fr.rougeux.projet.auction.dto.bo.UserDto;
import fr.rougeux.projet.auction.exception.BusinessException;

import java.time.LocalDateTime;

/**
 * Représente un utilisateur dans le système d'enchères.
 * Contient les informations personnelles et les crédits de l'utilisateur.
 */
public class User {

    /** Identifiant unique de l'utilisateur */
    private long userId;

    /** Prénom de l'utilisateur */
    private String firstName;

    /** Nom de famille de l'utilisateur */
    private String lastName;

    /** URL ou chemin de l'image de profil */
    private String userImg;

    /** Adresse email de l'utilisateur */
    private String email;

    /** Numéro de téléphone */
    private String phone;

    /** Mot de passe haché */
    private String password;

    /** Crédit disponible pour les enchères */
    private int credit;

    /** Indique si l'utilisateur a des droits administrateur */
    private boolean isAdmin;

    /** Date de création du compte */
    private LocalDateTime createAt;

    // =========================
    // Constructors
    // =========================

    /** Constructeur par défaut */
    public User() {}

    /**
     * Constructeur de copie pour cloner un utilisateur.
     *
     * @param source instance de User à cloner
     */
    public User(User source) {
        this.userId = source.userId;
        this.firstName = source.firstName;
        this.lastName = source.lastName;
        this.userImg = source.userImg;
        this.email = source.email;
        this.phone = source.phone;
        this.password = source.password;
        this.credit = source.credit;
        this.isAdmin = source.isAdmin;
        this.createAt = source.createAt;
    }

    // =========================
    // Getters et Setters
    // =========================

    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserImg() {
        return userImg;
    }
    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getCredit() {
        return credit;
    }
    public void setCredit(int credit) {
        this.credit = credit;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }
    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    // =========================
    // Logique métier
    // =========================

    /**
     * Vérifie si l'utilisateur peut participer à une enchère.
     */
    public void canBid(int amount) {
        if (this.credit - amount < 0) {
            throw new BusinessException("user.credit.insufficient");
        }
        this.credit -= amount;
    }

    /**
     * Retourne le nom complet de l'utilisateur.
     *
     * @return prénom + nom
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Convertit ce BO en DTO pour transfert vers les couches supérieures (API, controller).
     *
     * @return un UserDTO contenant les informations nécessaires
     */
    public UserDto toDTO() {
        UserDto dto = new UserDto();
        dto.setUserId(this.userId);
        dto.setFirstName(this.firstName);
        dto.setLastName(this.lastName);
        dto.setUserImg(this.userImg);
        dto.setEmail(this.email);
        dto.setPhone(this.phone);
        dto.setCredit(this.credit);
        dto.setCreateAt(this.createAt);

        return dto;
    }
}
