package kioskUI;

import javax.swing.*;
import java.awt.*;

public class KioskUI extends JFrame {

    public KioskUI() {
    	setTitle("BURGER KING");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(720, 820);
        setMinimumSize(new Dimension(660, 720));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ── 패널 생성 ────────────────────────────────────────────
        MenuPanel  menuPanel  = new MenuPanel();
        OrderPanel orderPanel = new OrderPanel();

        // ── 이벤트 연결 ──────────────────────────────────────────
        menuPanel.attachEvents(orderPanel); // 메뉴 클릭 → 주문 목록 추가

        // ── 주문 버튼 → 영수증 팝업 ──────────────────────────────
        orderPanel.orderBtn.addActionListener(e ->
            orderPanel.buildReceiptDialog(this).setVisible(true)
        );

        // ── 패널 배치 ──────────────────────────────────────────
        add(menuPanel,  BorderLayout.CENTER);
        add(orderPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(KioskUI::new);
    }
}