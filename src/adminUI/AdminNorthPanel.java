package adminUI;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AdminNorthPanel extends JPanel {
    public AdminNorthPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(0xF5, 0xF0, 0xE1)); // 크림 베이지 배경 (BG_COLOR)
        setBorder(new EmptyBorder(20, 20, 10, 20));

        JLabel titleLabel = new JLabel("킹오더 관리 대시보드", SwingConstants.LEFT);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        titleLabel.setForeground(new Color(0xC0, 0x39, 0x13)); // 버거킹 레드 적용 (TITLE_COLOR)
        titleLabel.setBorder(new EmptyBorder(0, 0, 16, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel cardGrid = new JPanel(new GridLayout(1, 3, 16, 0));
        cardGrid.setBackground(new Color(0xF5, 0xF0, 0xE1)); // 동일한 크림 베이지 적용
        
        Color textColorMain = new Color(0x1A, 0x1A, 0x1A); // 진한 텍스트
        Color textColorRed = new Color(0xC0, 0x39, 0x13);  // 버거킹 레드

        cardGrid.add(buildSummaryCard("오늘의 주문", "12 건", textColorMain));
        cardGrid.add(buildSummaryCard("오늘의 매출", "48,500 원", textColorRed));
        cardGrid.add(buildSummaryCard("인기 메뉴", "떡볶이", textColorMain));

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