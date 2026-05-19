package adminUI;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AdminNorthPanel extends JPanel {
    private static final Color COLOR_BG_WHITE = Color.WHITE;
    private static final Color COLOR_BG_LIGHT = new Color(250, 250, 250);
    private static final Color COLOR_BORDER = new Color(210, 210, 210);
    private static final Color COLOR_TEXT_MAIN = new Color(50, 50, 50);
    private static final Color COLOR_PRIMARY = new Color(220, 60, 30);

    private static final Font FONT_TITLE = new Font("맑은 고딕", Font.BOLD, 22);
    private static final Font FONT_BODY = new Font("맑은 고딕", Font.PLAIN, 13);

    public AdminNorthPanel() {
        setLayout(new BorderLayout());
        setBackground(COLOR_BG_WHITE);
        setBorder(new EmptyBorder(20, 20, 10, 20));

        JLabel titleLabel = new JLabel("킹오더 관리 대시보드", SwingConstants.LEFT);
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(COLOR_TEXT_MAIN);
        titleLabel.setBorder(new EmptyBorder(0, 0, 16, 0));
        add(titleLabel, BorderLayout.NORTH);

        orderDAO adminDAO = new orderDAO();
        int todaySales = adminDAO.getTodaySales();
        int totalSales = adminDAO.getTotalSales(); // 👈 새로 만든 총매출 메서드 호출!
        int orderCount = adminDAO.getTodayOrderCount();

        JPanel cardGrid = new JPanel(new GridLayout(1, 3, 16, 0));
        cardGrid.setBackground(COLOR_BG_WHITE);

        // ⭐️ [UI 매핑] 기존 가짜 데이터 지우고 진짜 DB 데이터 꽂아넣기!
        cardGrid.add(buildSummaryCard("오늘의 주문", orderCount + " 건", new Color(70, 120, 230)));
        cardGrid.add(buildSummaryCard("오늘의 매출", String.format("%,d 원", todaySales), COLOR_PRIMARY));
        cardGrid.add(buildSummaryCard("누적 총매출", String.format("%,d 원", totalSales), new Color(40, 160, 100))); // 👈 "인기 메뉴" 대신 "누적 총매출"로 변신!

        add(cardGrid, BorderLayout.CENTER);
    }

    private JPanel buildSummaryCard(String title, String value, Color vColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(0xFF, 0xFB, 0xF2)); // 카드 배경 (CARD_BG)
        card.setBorder(BorderFactory.createLineBorder(new Color(0xD4, 0xA8, 0x4A), 1, true)); // 골드 테두리 (CARD_BORDER)

        JLabel t = new JLabel(title);
        t.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        t.setForeground(new Color(0x8B, 0x6E, 0x3A)); // 보조 텍스트 색상 (MUTED_COLOR)
        t.setBorder(new EmptyBorder(12, 16, 4, 16));

        JLabel v = new JLabel(value);
        v.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        v.setForeground(vColor);
        v.setBorder(new EmptyBorder(0, 16, 12, 16));

        card.add(t, BorderLayout.NORTH);
        card.add(v, BorderLayout.CENTER);
        return card;
    }
}