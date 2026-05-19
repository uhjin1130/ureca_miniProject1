package kioskUI;

public class MenuDTO {
    private final int    menuId;
    private final String menuName;
    private final int    price;
    private final String imgUrl;

    public MenuDTO(int menuId, String menuName, int price, String imgUrl) {
        this.menuId   = menuId;
        this.menuName = menuName;
        this.price    = price;
        this.imgUrl   = imgUrl;
    }

    public int    getMenuId()   { return menuId; }
    public String getMenuName() { return menuName; }
    public int    getPrice()    { return price; }
    public String getImgUrl()   { return imgUrl; }
}