package adminUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TotalGraph extends JDialog {
    // ⭐️ 12칸에서 24시간(0시~23시)을 수용할 수 있도록 배열 확장
    private int[] sales;

    public TotalGraph(JFrame parent) {
        super(parent, "시간별 매출 통계", true);
        setSize(950, 600); // 24개가 들어가야 하므로 가로 너비를 900 -> 950으로 살짝 확장!
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // [DB 연동] 오늘 자 시간별 진짜 매출 데이터를 가져옵니다.
        orderDAO dao = new orderDAO();
        int[] rawSales = dao.getHourlySalesValues();

        // 시간별 매출은 만원 단위면 0원으로 묻히는 경우가 많아 단위를 [천원]으로 조정합니다.
        sales = new int[24];
        for (int i = 0; i < 24; i++) {
            sales[i] = rawSales[i] / 1000; // 예: 45,000원 대출 시 그래프에는 45(천원)으로 표시!
        }

        getContentPane().setBackground(new Color(0xF5, 0xF0, 0xE1));

        // 타이틀 변경 (단위: 천원)
        JLabel title = new JLabel("오늘의 시간별 매출 현황 (단위: 천원)", SwingConstants.CENTER);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        title.setForeground(new Color(0xC0, 0x39, 0x13));
        title.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JPanel graphPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int padding = 60;
                int width = getWidth() - (padding * 2);
                int height = getHeight() - (padding * 2);

                // ⭐️ 24개의 막대가 촘촘하게 들어갈 수 있도록 너비와 간격 재계산 (막대 너비 20, 간격 10)
                int barWidth = (width / 24) - 10;

                int max = 0;
                for (int s : sales) if (s > max) max = s;
                if (max == 0) max = 1;

                for (int i = 0; i < 24; i++) {
                    int barHeight = (int) ((double) sales[i] / max * height);
                    int x = padding + i * (barWidth + 10);
                    int y = getHeight() - padding - barHeight;

                    // 막대 색상
                    g2.setColor(new Color(192, 57, 19, 220));
                    g2.fillRect(x, y, barWidth, barHeight);

                    // ⭐️ 하단 시간 표시 (0시, 1시 ... 23시) - 글자가 안 겹치도록 폰트 크기 10으로 조절
                    g2.setColor(new Color(0x8B, 0x6E, 0x3A));
                    g2.setFont(new Font("맑은 고딕", Font.BOLD, 10));
                    g2.drawString(i + "시", x - 2, getHeight() - padding + 20);

                    // ⭐️ 상단 금액 표시 - 매출이 있을 때(>0)만 숫자를 표시하여 화면 복잡도 감소
                    if (sales[i] > 0) {
                        g2.setColor(new Color(0x1A, 0x1A, 0x1A));
                        g2.setFont(new Font("맑은 고딕", Font.BOLD, 10));
                        g2.drawString(String.format("%,d", sales[i]), x - 2, y - 5);
                    }
                }

                // 바닥 기준선
                g2.setColor(new Color(0xD4, 0xA8, 0x4A));
                g2.setStroke(new BasicStroke(2f));
                g2.drawLine(padding, getHeight() - padding, getWidth() - padding, getHeight() - padding);
            }
        };

        graphPanel.setBackground(new Color(0xFF, 0xFB, 0xF2));
        graphPanel.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(0, 20, 20, 20),
                BorderFactory.createLineBorder(new Color(0xD4, 0xA8, 0x4A), 1, true)
        ));

        add(graphPanel, BorderLayout.CENTER);
    }
}