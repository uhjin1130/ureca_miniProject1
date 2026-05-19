package kioskUI;

import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

/**
 * 메뉴 카드 클릭 이벤트 핸들러
 * - 메뉴 클릭 시 OrderPanel의 주문 목록에 항목 추가 / 수량 증가
 * - hover 시 카드 테두리 색 변경
 */
public class MenuMouseEvent extends MouseAdapter {

    private final int        menuIdx;    // 클릭된 메뉴 인덱스
    private final OrderPanel orderPanel; // 주문 패널 참조
    private final JPanel     card;       // hover 효과용 카드 참조
    private final MenuPanel  menuPanel;

    // 버거킹 색상
    private static final Color BORDER_DEFAULT = new Color(0xD4, 0xA8, 0x4A); // 골드
    private static final Color BORDER_HOVER   = new Color(0xC0, 0x39, 0x13); // 레드

    public MenuMouseEvent(int menuIdx, OrderPanel orderPanel, JPanel card, MenuPanel menuPanel) {
        this.menuIdx    = menuIdx;
        this.orderPanel = orderPanel;
        this.card       = card;
        this.menuPanel  = menuPanel;
    }

    // ── 클릭: 주문 목록에 추가 ───────────────────────────────────
    @Override
    public void mouseClicked(MouseEvent e) {
    	MenuDTO menu = menuPanel.getMenuList().get(menuIdx);
    	String name  = menu.getMenuName();
    	int    price = menu.getPrice();
    	int    menuId = menu.getMenuId();

        // 이미 목록에 있으면 수량 +1, 없으면 새 행 추가
        int existingIdx = orderPanel.findOrderIndex(name);
        if (existingIdx >= 0) {
            orderPanel.increaseCount(existingIdx);
        } else {
        	orderPanel.addOrderRow(name, price, menuId);
        }
        orderPanel.updateTotal();
    }

    // ── hover: 테두리 색 변경 ────────────────────────────────────
    @Override
    public void mouseEntered(MouseEvent e) {
        card.setBorder(BorderFactory.createLineBorder(BORDER_HOVER, 2, true));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        card.setBorder(BorderFactory.createLineBorder(BORDER_DEFAULT, 2, true));
    }
}