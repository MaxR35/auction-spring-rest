package fr.rougeux.projet.auction.bo;

import fr.rougeux.projet.auction.dto.bo.ItemDto;

/**
 * BO représentant un objet mis en vente.
 * Contient les informations de base et sa catégorie.
 */
public class Item {

    /** Identifiant unique de l'objet */
    private long itemId;

    /** Nom de l'objet */
    private String itemName;

    /** Description de l'objet */
    private String itemDesc;

    /** URL ou chemin de l'image de l'objet */
    private String itemImg;

    /** Catégorie de l'objet */
    private Category category;

    // =========================
    // Constructors
    // =========================

    /** Constructeur par défaut */
    public Item() {}

    /**
     * Constructeur de copie pour cloner un article.
     *
     * @param source instance de Item à cloner
     */
    public Item(Item source) {
        this.itemId = source.itemId;
        this.itemName = source.itemName;
        this.itemDesc = source.itemDesc;
        this.itemImg = source.itemImg;
        this.category = (source.category != null) ? new Category(source.category) : null;
    }

    // =========================
    // Getters et Setters
    // =========================

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemImg() {
        return itemImg;
    }

    public void setItemImg(String itemImg) {
        this.itemImg = itemImg;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    // =========================
    // Conversion vers DTO
    // =========================

    /**
     * Convertit ce BO en ItemDTO pour transfert vers le controller ou l'API.
     * @return ItemDTO correspondant
     */
    public ItemDto toDTO() {
        ItemDto dto = new ItemDto();
        dto.setItemId(this.itemId);
        dto.setItemName(this.itemName);
        dto.setItemDesc(this.itemDesc);
        dto.setItemImg(this.itemImg);
        dto.setCategory(this.category != null ? this.category.toDTO() : null);
        return dto;
    }
}
