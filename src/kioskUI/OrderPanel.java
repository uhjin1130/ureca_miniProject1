package kioskUI;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OrderPanel extends JPanel {

    private static final Color BG_COLOR     = new Color(0xF5, 0xF0, 0xE1);
    private static final Color PANEL_BORDER = new Color(0xD4, 0xA8, 0x4A);
    private static final Color LIST_BG      = new Color(0xFF, 0xFB, 0xF2);
    private static final Color ROW_BG       = new Color(0xFF, 0xFB, 0xF2);
    private static final Color ROW_DIVIDER  = new Color(0xE8, 0xDC, 0xC0);
    private static final Color BTN_BG       = new Color(0xF0, 0xE8, 0xD0);
    private static final Color BTN_BORDER   = new Color(0xD4, 0xA8, 0x4A);
    private static final Color DEL_COLOR    = new Color(0xC0, 0x39, 0x13);
    private static final Color PRICE_COLOR  = new Color(0xC0, 0x39, 0x13);
    private static final Color TOTAL_BG     = new Color(0xF0, 0xE8, 0xD0);
    private static final Color ORDER_BTN_BG = new Color(0xC0, 0x39, 0x13);
    private static final Color ADMIN_BTN_BG = new Color(0xF5, 0xF0, 0xE1);
    private static final Color MUTED_COLOR  = new Color(0x8B, 0x6E, 0x3A);

    private final List<String>  orderNames   = new ArrayList<>();
    private final List<Integer> orderPrices  = new ArrayList<>();
    private final List<Integer> orderCounts  = new ArrayList<>();
    private final List<Integer> orderMenuIds = new ArrayList<>();

    public final JPanel  orderListPanel;
    public final JLabel  totalPriceLabel;
    public final JButton orderBtn;
    private final JScrollPane scrollPane;

    public OrderPanel() {
        setLayout(new BorderLayout(0, 0));
        setBackground(BG_COLOR);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(2, 0, 0, 0, PANEL_BORDER),
            new EmptyBorder(12, 20, 16, 20)
        ));
        setPreferredSize(new Dimension(0, 260));

        // ── 주문 목록 ─────────────────────────────────────────────
        orderListPanel = new JPanel();
        orderListPanel.setLayout(new BoxLayout(orderListPanel, BoxLayout.Y_AXIS));
        orderListPanel.setBackground(LIST_BG);

        scrollPane = new JScrollPane(orderListPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(PANEL_BORDER));
        scrollPane.getViewport().setBackground(LIST_BG);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // ── 오른쪽 사이드 패널 ────────────────────────────────────
        JPanel sidePanel = new JPanel(new BorderLayout(0, 8));
        sidePanel.setBackground(BG_COLOR);
        sidePanel.setPreferredSize(new Dimension(160, 0));
        sidePanel.setBorder(new EmptyBorder(0, 12, 0, 0));

        // 총 금액
        totalPriceLabel = new JLabel("0원", SwingConstants.CENTER);
        totalPriceLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        totalPriceLabel.setForeground(PRICE_COLOR);
        totalPriceLabel.setOpaque(true);
        totalPriceLabel.setBackground(TOTAL_BG);
        totalPriceLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PANEL_BORDER, 1, true),
            new EmptyBorder(8, 8, 8, 8)
        ));

        // 관리자 페이지 버튼
        JButton adminBtn = new JButton("관리자 페이지");
        adminBtn.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        adminBtn.setForeground(new Color(0x1A, 0x1A, 0x1A));
        adminBtn.setBackground(ADMIN_BTN_BG);
        adminBtn.setFocusPainted(false);
        adminBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PANEL_BORDER, 1, true),
            new EmptyBorder(8, 0, 8, 0)
        ));
        adminBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        adminBtn.addActionListener(e -> {
            adminUI.AdminUI adminUI = new adminUI.AdminUI();
            adminUI.setVisible(true);
        });

        // 주문 버튼
        orderBtn = new JButton("주문");
        orderBtn.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        orderBtn.setForeground(Color.BLACK);
        orderBtn.setBackground(ORDER_BTN_BG);
        orderBtn.setFocusPainted(false);
        orderBtn.setBorder(new EmptyBorder(10, 0, 10, 0));
        orderBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // 관리자 + 주문 버튼 묶음
        JPanel bottomBtns = new JPanel(new GridLayout(2, 1, 0, 8));
        bottomBtns.setBackground(BG_COLOR);
        bottomBtns.add(adminBtn);
        bottomBtns.add(orderBtn);

        sidePanel.add(totalPriceLabel, BorderLayout.NORTH);
        sidePanel.add(bottomBtns,      BorderLayout.SOUTH);

        add(scrollPane, BorderLayout.CENTER);
        add(sidePanel,  BorderLayout.EAST);
    }

    // ── 공개 메서드 ───────────────────────────────────────────────

    public int findOrderIndex(String name) {
        return orderNames.indexOf(name);
    }

    public void increaseCount(int idx) {
        orderCounts.set(idx, orderCounts.get(idx) + 1);
        refreshOrderList();
    }

    public void addOrderRow(String name, int price, int menuId) {
        orderNames.add(name);
        orderPrices.add(price);
        orderCounts.add(1);
        orderMenuIds.add(menuId);
        refreshOrderList();
    }

    public void updateTotal() {
        int total = 0;
        for (int i = 0; i < orderNames.size(); i++) {
            total += orderPrices.get(i) * orderCounts.get(i);
        }
        totalPriceLabel.setText(String.format("%,d원", total));
    }

    // ── 내부 UI 갱신 ─────────────────────────────────────────────

    private void refreshOrderList() {
        orderListPanel.removeAll();
        for (int i = 0; i < orderNames.size(); i++) {
            orderListPanel.add(buildOrderRow(i));
        }
        orderListPanel.revalidate();
        orderListPanel.repaint();
        SwingUtilities.invokeLater(() -> {
            JScrollBar sb = scrollPane.getVerticalScrollBar();
            sb.setValue(sb.getMaximum());
        });
    }

    private JPanel buildOrderRow(int idx) {
        String name     = orderNames.get(idx);
        int    count    = orderCounts.get(idx);
        int    subtotal = orderPrices.get(idx) * count;

        JPanel row = new JPanel(new GridBagLayout());
        row.setBackground(ROW_BG);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        row.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, ROW_DIVIDER),
            new EmptyBorder(6, 10, 6, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 4, 0, 4);

        // 음식 이름
        JLabel nameLabel = new JLabel((idx + 1) + ". " + name);
        nameLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        nameLabel.setForeground(new Color(0x1A, 0x1A, 0x1A));
        gbc.gridx = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        row.add(nameLabel, gbc);

        // 빼기 버튼
        JButton minusBtn = makeSmallBtn("-");
        minusBtn.addActionListener(e -> {
            if (orderCounts.get(idx) > 1) {
                orderCounts.set(idx, orderCounts.get(idx) - 1);
            } else {
                orderNames.remove(idx);
                orderPrices.remove(idx);
                orderCounts.remove(idx);
                orderMenuIds.remove(idx);
            }
            refreshOrderList();
            updateTotal();
        });
        gbc.gridx = 1; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        row.add(minusBtn, gbc);

        // 개수
        JLabel countLabel = new JLabel(String.valueOf(count), SwingConstants.CENTER);
        countLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        countLabel.setPreferredSize(new Dimension(30, 28));
        gbc.gridx = 2;
        row.add(countLabel, gbc);

        // 추가 버튼
        JButton plusBtn = makeSmallBtn("+");
        plusBtn.addActionListener(e -> {
            orderCounts.set(idx, orderCounts.get(idx) + 1);
            refreshOrderList();
            updateTotal();
        });
        gbc.gridx = 3;
        row.add(plusBtn, gbc);

        // 삭제 버튼
        JButton delBtn = makeSmallBtn("삭제");
        delBtn.setForeground(DEL_COLOR);
        delBtn.addActionListener(e -> {
            orderNames.remove(idx);
            orderPrices.remove(idx);
            orderCounts.remove(idx);
            orderMenuIds.remove(idx);
            refreshOrderList();
            updateTotal();
        });
        gbc.gridx = 4;
        row.add(delBtn, gbc);

        // 소계
        JLabel subtotalLabel = new JLabel(String.format("%,d원", subtotal), SwingConstants.RIGHT);
        subtotalLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        subtotalLabel.setForeground(MUTED_COLOR);
        subtotalLabel.setPreferredSize(new Dimension(70, 28));
        gbc.gridx = 5; gbc.insets = new Insets(0, 8, 0, 0);
        row.add(subtotalLabel, gbc);

        return row;
    }

    private JButton makeSmallBtn(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(42, 28));
        btn.setFocusPainted(false);
        btn.setBackground(BTN_BG);
        btn.setForeground(new Color(0x1A, 0x1A, 0x1A));
        btn.setBorder(BorderFactory.createLineBorder(BTN_BORDER, 1, true));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ── 영수증 팝업 ───────────────────────────────────────────────

    public JDialog buildReceiptDialog(JFrame owner) {
        if (orderNames.isEmpty()) {
            JOptionPane.showMessageDialog(owner, "주문 항목이 없습니다.", "알림",
                JOptionPane.WARNING_MESSAGE);
            return null;
        }

        JDialog dialog = new JDialog(owner, "영수증", true);
        dialog.setSize(360, 540);
        dialog.setLocationRelativeTo(owner);
        dialog.setResizable(false);
        dialog.setLayout(new BorderLayout());

        int total = 0;
        for (int i = 0; i < orderNames.size(); i++) {
            total += orderPrices.get(i) * orderCounts.get(i);
        }
        final int finalTotal = total;

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BG_COLOR);
        content.setBorder(new EmptyBorder(20, 24, 20, 24));

        JLabel shopName = new JLabel("BURGER KING", SwingConstants.CENTER);
        shopName.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        shopName.setForeground(PRICE_COLOR);
        shopName.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(shopName);
        content.add(Box.createVerticalStrut(4));

        JLabel receiptTitle = new JLabel("영  수  증", SwingConstants.CENTER);
        receiptTitle.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        receiptTitle.setForeground(new Color(0x1A, 0x1A, 0x1A));
        receiptTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(receiptTitle);
        content.add(Box.createVerticalStrut(4));

        JLabel dateLabel = new JLabel(new java.util.Date().toString(), SwingConstants.CENTER);
        dateLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
        dateLabel.setForeground(MUTED_COLOR);
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(dateLabel);
        content.add(Box.createVerticalStrut(12));

        content.add(makeSeparator());
        content.add(Box.createVerticalStrut(10));

        for (int i = 0; i < orderNames.size(); i++) {
            int subtotal = orderPrices.get(i) * orderCounts.get(i);
            content.add(buildReceiptRow(orderNames.get(i) + " × " + orderCounts.get(i), subtotal));
            content.add(Box.createVerticalStrut(4));
        }

        content.add(Box.createVerticalStrut(10));
        content.add(makeSeparator());
        content.add(Box.createVerticalStrut(10));

        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBackground(BG_COLOR);
        totalPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        JLabel lTotal = new JLabel("합  계");
        lTotal.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        lTotal.setForeground(new Color(0x1A, 0x1A, 0x1A));

        JLabel rTotal = new JLabel(String.format("%,d원", finalTotal));
        rTotal.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        rTotal.setForeground(PRICE_COLOR);
        rTotal.setHorizontalAlignment(SwingConstants.RIGHT);

        totalPanel.add(lTotal, BorderLayout.WEST);
        totalPanel.add(rTotal, BorderLayout.EAST);
        content.add(totalPanel);
        content.add(Box.createVerticalStrut(20));

        JButton confirmBtn = new JButton("주문하시겠습니까?");
        confirmBtn.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        confirmBtn.setForeground(Color.BLACK);
        confirmBtn.setBackground(ORDER_BTN_BG);
        confirmBtn.setFocusPainted(false);
        confirmBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        confirmBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmBtn.setBorder(new EmptyBorder(10, 0, 10, 0));
        confirmBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        confirmBtn.addActionListener(e -> {
            List<KingOrderDTO> dtoList = new ArrayList<>();
            for (int i = 0; i < orderNames.size(); i++) {
                int subtotal = orderPrices.get(i) * orderCounts.get(i);
                dtoList.add(new KingOrderDTO(
                    orderMenuIds.get(i),
                    orderCounts.get(i),
                    subtotal
                ));
            }

            KingOrderDAO dao = new KingOrderDAO();
            long orderId = dao.insertOrder(dtoList, finalTotal);

            showCompletionPanel(dialog, orderId);

            orderNames.clear();
            orderPrices.clear();
            orderCounts.clear();
            orderMenuIds.clear();
            refreshOrderList();
            updateTotal();
        });

        content.add(confirmBtn);

        JScrollPane sp = new JScrollPane(content);
        sp.setBorder(null);
        sp.getViewport().setBackground(BG_COLOR);
        dialog.add(sp, BorderLayout.CENTER);

        return dialog;
    }

    private void showCompletionPanel(JDialog dialog, long orderId) {
        dialog.getContentPane().removeAll();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_COLOR);
        panel.setBorder(new EmptyBorder(60, 30, 60, 30));

        JLabel iconLabel = new JLabel("✅", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(iconLabel);
        panel.add(Box.createVerticalStrut(20));

        String orderIdText = (orderId > 0) ? String.valueOf(orderId) : "확인 불가";
        JLabel orderIdLabel = new JLabel("주문번호 : " + orderIdText, SwingConstants.CENTER);
        orderIdLabel.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        orderIdLabel.setForeground(PRICE_COLOR);
        orderIdLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(orderIdLabel);
        panel.add(Box.createVerticalStrut(14));

        JLabel doneLabel = new JLabel("주문이 완료되었습니다.", SwingConstants.CENTER);
        doneLabel.setFont(new Font("맑은 고딕", Font.BOLD, 17));
        doneLabel.setForeground(new Color(0x1A, 0x1A, 0x1A));
        doneLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(doneLabel);
        panel.add(Box.createVerticalStrut(40));

        JButton closeBtn = new JButton("확인");
        closeBtn.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setBackground(ORDER_BTN_BG);
        closeBtn.setFocusPainted(false);
        closeBtn.setMaximumSize(new Dimension(200, 44));
        closeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeBtn.setBorder(new EmptyBorder(10, 0, 10, 0));
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(ev -> dialog.dispose());
        panel.add(closeBtn);

        dialog.getContentPane().add(panel);
        dialog.getContentPane().revalidate();
        dialog.getContentPane().repaint();
    }

    private JPanel buildReceiptRow(String itemText, int subtotal) {
        JPanel line = new JPanel(new BorderLayout());
        line.setBackground(BG_COLOR);
        line.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));

        JLabel left = new JLabel(itemText);
        left.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        left.setForeground(new Color(0x1A, 0x1A, 0x1A));

        JLabel right = new JLabel(String.format("%,d원", subtotal));
        right.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        right.setForeground(MUTED_COLOR);
        right.setHorizontalAlignment(SwingConstants.RIGHT);

        line.add(left,  BorderLayout.WEST);
        line.add(right, BorderLayout.EAST);
        return line;
    }

    private JSeparator makeSeparator() {
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(PANEL_BORDER);
        return sep;
    }
}