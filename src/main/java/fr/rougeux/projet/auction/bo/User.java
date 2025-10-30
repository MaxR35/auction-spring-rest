package fr.rougeux.projet.auction.bo;

import fr.rougeux.projet.auction.dto.bo.UserDto;

import java.time.LocalDateTime;

/**
 * Représente un utilisateur dans le système d'enchères.
 * Contient les informations personnelles et les crédits de l'utilisateur.
 */
public class User {

    /**
     * Identifiant unique de l'utilisateur
     */
    private long userId;

    /**
     * Prénom de l'utilisateur
     */
    private String firstName;

    /**
     * Nom de famille de l'utilisateur
     */
    private String lastName;

    /**
     * URL ou chemin de l'image de profil
     */
    private String userImg;

    /**
     * Adresse email de l'utilisateur
     */
    private String email;

    /**
     * Numéro de téléphone
     */
    private String phone;

    /**
     * Mot de passe (haché de préférence)
     */
    private String password;

    /**
     * Crédit disponible pour les enchères
     */
    private int credit;

    /**
     * Indique si l'utilisateur a des droits administrateur
     */
    private boolean isAdmin;

    /**
     * Date de création du compte
     */
    private LocalDateTime createAt;

    // =========================
    // Getters et Setters
    // =========================

    /**
     * @return l'identifiant unique de l'utilisateur
     */
    public long getUserId() {
        return userId;
    }

    /**
     * @param userId définit l'identifiant unique de l'utilisateur
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * @return le mot de passe de l'utilisateur
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password définit le mot de passe de l'utilisateur
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return l'adresse email de l'utilisateur
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email définit l'adresse email de l'utilisateur
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return le prénom de l'utilisateur
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName définit le prénom de l'utilisateur
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return le nom de famille de l'utilisateur
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName définit le nom de famille de l'utilisateur
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return l'image de profil de l'utilisateur
     */
    public String getUserImg() {
        return userImg;
    }

    /**
     * @param userImg définit l'image de profil de l'utilisateur
     */
    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    /**
     * @return le numéro de téléphone de l'utilisateur
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone définit le numéro de téléphone de l'utilisateur
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return le crédit disponible de l'utilisateur
     */
    public int getCredit() {
        return credit;
    }

    /**
     * @param credit définit le crédit disponible de l'utilisateur
     */
    public void setCredit(int credit) {
        this.credit = credit;
    }

    /**
     * @return vrai si l'utilisateur est administrateur
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * @param isAdmin définit si l'utilisateur est administrateur
     */
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     * @return la date de création du compte
     */
    public LocalDateTime getCreateAt() {
        return createAt;
    }

    /**
     * @param createAt définit la date de création du compte
     */
    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    // =========================
    // Logique métier
    // =========================

    /**
     * Vérifie si l'utilisateur peut participer à une enchère.
     *
     * @return vrai si le crédit est supérieur à zéro
     */
    public boolean canBid() {
        return credit > 0;
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
