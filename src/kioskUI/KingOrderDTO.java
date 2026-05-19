package kioskUI;


public class KingOrderDTO {
	
	private long menuId;   // menu_id 매핑
    private int quantity;  // quantity 매핑
    private int subtotal;  // subtotal 매핑

    public KingOrderDTO(long menuId, int quantity, int subtotal) {
        this.menuId = menuId;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    public long getMenuId() { return menuId; }
    public int getQuantity() { return quantity; }
    public int getSubtotal() { return subtotal; }
}