package adminUI;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;

public class OrderManagement extends JPanel {
    // 버거킹 테마 색상 적용
    private static final Color COLOR_BG_WHITE = new Color(0xFF, 0xFB, 0xF2); // 카드/리스트 배경 (CARD_BG)
    private static final Color COLOR_BG_LIGHT = new Color(0xF5, 0xF0, 0xE1); // 크림 베이지 배경 (BG_COLOR)
    private static final Color COLOR_BORDER = new Color(0xD4, 0xA8, 0x4A);   // 골드 테두리 (PANEL_BORDER)
    private static final Color COLOR_TEXT_MAIN = new Color(0x1A, 0x1A, 0x1A); // 기본 텍스트 (NAME_COLOR)
    
    private static final Font FONT_SUBTITLE = new Font("맑은 고딕", Font.BOLD, 14);
    private static final Font FONT_BODY = new Font("맑은 고딕", Font.PLAIN, 13);

    public OrderManagement() {
        setLayout(new BorderLayout());
        setBackground(COLOR_BG_WHITE);

        // 중앙 테이블 영역
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(COLOR_BG_WHITE);
        wrapper.setBorder(new EmptyBorder(15, 20, 10, 20));

        JLabel sectionTitle = new JLabel("실시간 주문 접수 현황");
        sectionTitle.setFont(FONT_SUBTITLE);
        sectionTitle.setForeground(new Color(0xC0, 0x39, 0x13)); // 버거킹 레드 적용
        sectionTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        wrapper.add(sectionTitle, BorderLayout.NORTH);

        String[] columns = {"", "주문 번호", "주문 시간", "상세 내역", "총 결제 금액", "상태"};
        Object[][] data = {
            {false, "#0001", "11:20:15", "떡볶이 2, 순대 1", "5,500원", "완료"},
            {false, "#0002", "11:35:42", "라면 1, 튀김 3", "4,500원", "조리중"},
            {false, "#0003", "11:51:04", "떡볶이 1, 라면 2", "8,000원", "대기"}
        };

        DefaultTableModel tableModel = new DefaultTableModel(data, columns) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class;// 첫 번째 열은 체크박스
                }
                return String.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // 첫 번째 열(체크박스)만 편집 가능
            }
        };

        JTable table = new JTable(tableModel);
        table.setFont(FONT_BODY);
        table.setRowHeight(35);
        table.setShowGrid(true);
        table.setGridColor(new Color(0xE8, 0xDC, 0xC0)); // 행 구분선 (ROW_DIVIDER)
        table.setSelectionBackground(new Color(0xEE, 0xE6, 0xCE)); // 선택 시 배경색 (IMG_BG)
        table.setSelectionForeground(COLOR_TEXT_MAIN);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(0).setMaxWidth(30);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_SUBTITLE);
        header.setBackground(COLOR_BG_LIGHT);
        header.setForeground(COLOR_TEXT_MAIN);
        header.setPreferredSize(new Dimension(0, 36));
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for(int i=1; i<table.getColumnCount(); i++) table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER));
        scrollPane.getViewport().setBackground(COLOR_BG_WHITE);
        wrapper.add(scrollPane, BorderLayout.CENTER);

        add(wrapper, BorderLayout.CENTER);

        // 하단 버튼 영역
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        btnPanel.setBackground(COLOR_BG_WHITE);
        btnPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        Color btnBg = new Color(0xF0, 0xE8, 0xD0); // 소형 버튼 배경 (BTN_BG)
        Color btnFg = new Color(0xC0, 0x39, 0x13); // 버튼 텍스트 (버거킹 레드)

        JButton btnTotal = makeStyledButton("총매출", btnBg, btnFg);
        btnPanel.add(makeStyledButton("주문 승인", btnBg, btnFg));
        btnPanel.add(makeStyledButton("조리 완료", btnBg, btnFg));
        btnPanel.add(makeStyledButton("주문 취소", btnBg, btnFg));
        btnTotal.addActionListener(e -> {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            new TotalGraph(parent).setVisible(true);
        });
        btnPanel.add(btnTotal);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private JButton makeStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_SUBTITLE);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setBorder(BorderFactory.createLineBorder(COLOR_BORDER)); // 골드 테두리 추가
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(110, 38));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}