package fr.rougeux.projet.auction.bo;


import fr.rougeux.projet.auction.dto.bo.CategoryDto;

/**
 * BO représentant une catégorie d'objet.
 */
public class Category {

    /** Identifiant unique de la catégorie */
    private long categoryId;

    /** Nom ou libellé de la catégorie */
    private String label;

    // =========================
    // Getters et Setters
    // =========================

    /**
     * @return l'identifiant unique de la catégorie
     */
    public long getCategorieId() {
        return categoryId;
    }

    /**
     * @param categorieId définit l'identifiant unique de la catégorie
     */
    public void setCategorieId(long categorieId) {
        this.categoryId = categorieId;
    }

    /**
     * @return le libellé de la catégorie
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label définit le libellé de la catégorie
     */
    public void setLabel(String label) {
        this.label = label;
    }

    // =========================
    // Conversion vers DTO
    // =========================

    /**
     * Convertit ce BO en CategorieDTO pour transfert vers le controller ou l'API.
     * @return CategorieDTO correspondant
     */
    public CategoryDto toDTO() {
        CategoryDto dto = new CategoryDto();
        dto.setCategoryId(this.categoryId);
        dto.setLabel(this.label);
        return dto;
    }
}
