package fr.rougeux.projet.auction.dto.bo;

public class ItemDto {

    private long itemId;
    private String itemName;
    private String itemDesc;
    private String itemImg;
    private CategoryDto category;

    public long getItemId() { return itemId; }
    public void setItemId(long itemId) { this.itemId = itemId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getItemDesc() { return itemDesc; }
    public void setItemDesc(String itemDesc) { this.itemDesc = itemDesc; }

    public String getItemImg() { return itemImg; }
    public void setItemImg(String itemImg) { this.itemImg = itemImg; }

    public CategoryDto getCategory() { return category; }
    public void setCategory(CategoryDto category) { this.category = category; }
}
