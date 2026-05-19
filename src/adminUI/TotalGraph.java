package adminUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TotalGraph extends JDialog {
    // 임시 데이터 (나중에 DB연결)
    private int[] sales = {1200, 1800, 2200, 1500, 2800, 3500, 4200, 3800, 4500, 5000, 4800, 5500};

    public TotalGraph(JFrame parent) {
        super(parent, "연간 매출 통계", true);
        setSize(900, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        
        // 다이얼로그 전체 배경색 (크림 베이지)
        getContentPane().setBackground(new Color(0xF5, 0xF0, 0xE1));

        JLabel title = new JLabel("월별 총 매출 현황 (단위: 만원)", SwingConstants.CENTER);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        title.setForeground(new Color(0xC0, 0x39, 0x13)); // 타이틀 색상 (버거킹 레드)
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
                int barWidth = width / 12 - 15;

                // 최대값 찾기
                int max = 0;
                for (int s : sales) if (s > max) max = s;

                for (int i = 0; i < 12; i++) {
                    int barHeight = (int) ((double) sales[i] / max * height);
                    int x = padding + i * (barWidth + 15);
                    int y = getHeight() - padding - barHeight;

                    // 막대 색상 (버거킹 레드에 투명도 220 적용)
                    g2.setColor(new Color(192, 57, 19, 220)); 
                    g2.fillRect(x, y, barWidth, barHeight);
                    
                    // 하단 월 표시 (갈색 계열 보조 텍스트)
                    g2.setColor(new Color(0x8B, 0x6E, 0x3A));
                    g2.setFont(new Font("맑은 고딕", Font.BOLD, 12));
                    g2.drawString((i + 1) + "월", x + (barWidth / 4), getHeight() - padding + 20);
                    
                    // 상단 금액 표시 (진한 검정)
                    g2.setColor(new Color(0x1A, 0x1A, 0x1A));
                    g2.setFont(new Font("맑은 고딕", Font.BOLD, 11));
                    g2.drawString(String.format("%,d", sales[i]), x, y - 5);
                }
                
                // 바닥 기준선 (골드 라인)
                g2.setColor(new Color(0xD4, 0xA8, 0x4A)); 
                g2.setStroke(new BasicStroke(2f)); // 선을 조금 더 또렷하게 처리
                g2.drawLine(padding, getHeight() - padding, getWidth() - padding, getHeight() - padding);
            }
        };
        
        // 그래프 영역 배경 (카드 배경색)
        graphPanel.setBackground(new Color(0xFF, 0xFB, 0xF2));
        // 여백 확보 및 패널을 감싸는 골드 테두리 추가
        graphPanel.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(0, 20, 20, 20),
            BorderFactory.createLineBorder(new Color(0xD4, 0xA8, 0x4A), 1, true)
        ));

        add(graphPanel, BorderLayout.CENTER);
    }
}