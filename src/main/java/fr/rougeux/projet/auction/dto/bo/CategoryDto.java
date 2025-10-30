package fr.rougeux.projet.auction.dto.bo;

/**
 * DTO représentant une catégorie.
 */
public class CategoryDto {

    private long categoryId;
    private String label;

    public long getCategoryId() { return categoryId; }
    public void setCategoryId(long categoryId) { this.categoryId = categoryId; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
}
