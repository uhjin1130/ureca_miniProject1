package kioskUI;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

public class MenuPanel extends JPanel {

    private static final Color BG_COLOR     = new Color(0xF5, 0xF0, 0xE1);
    private static final Color CARD_BG      = new Color(0xFF, 0xFB, 0xF2);
    private static final Color CARD_INFO_BG = new Color(0xF0, 0xE8, 0xD0);
    private static final Color CARD_BORDER  = new Color(0xD4, 0xA8, 0x4A);
    private static final Color IMG_BG       = new Color(0xEE, 0xE6, 0xCE);
    private static final Color NAME_COLOR   = new Color(0x1A, 0x1A, 0x1A);
    private static final Color PRICE_COLOR  = new Color(0xC0, 0x39, 0x13);
    private static final Color TITLE_COLOR  = new Color(0xC0, 0x39, 0x13);

    // ── 기존 static 배열 대신 DB에서 로드한 리스트 사용 ──────────
    private List<MenuDTO> menuList;
    public  JPanel[]      menuCards;

    public MenuPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);
        setBorder(new EmptyBorder(20, 20, 10, 20));

        JLabel title = new JLabel("BURGER KING", SwingConstants.CENTER);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        title.setForeground(TITLE_COLOR);
        title.setBorder(new EmptyBorder(0, 0, 16, 0));
        add(title, BorderLayout.NORTH);

        // DB에서 메뉴 로드
        menuList  = new MenuDAO().getAllMenus();
        menuCards = new JPanel[menuList.size()];

        int rows = (int) Math.ceil(menuList.size() / 2.0);
        JPanel grid = new JPanel(new GridLayout(rows == 0 ? 1 : rows, 2, 16, 16));
        grid.setBackground(BG_COLOR);
        grid.setBorder(new EmptyBorder(0, 0, 8, 0));

        for (int i = 0; i < menuList.size(); i++) {
            menuCards[i] = buildMenuCard(i);
            grid.add(menuCards[i]);
        }

        // 메뉴가 홀수개일 때 빈 칸 채우기
        if (menuList.size() % 2 != 0) {
            JPanel empty = new JPanel();
            empty.setBackground(BG_COLOR);
            grid.add(empty);
        }

        JScrollPane scrollPane = new JScrollPane(grid);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(BG_COLOR);

        add(scrollPane, BorderLayout.CENTER);
    }

    public List<MenuDTO> getMenuList() {
        return menuList;
    }

    public void attachEvents(OrderPanel orderPanel) {
        for (int i = 0; i < menuCards.length; i++) {
            MenuMouseEvent handler = new MenuMouseEvent(i, orderPanel, menuCards[i], this);
            addMouseListenerToAll(menuCards[i], handler);
        }
    }

    private void addMouseListenerToAll(java.awt.Component comp, MenuMouseEvent handler) {
        comp.addMouseListener(handler);
        if (comp instanceof java.awt.Container) {
            for (java.awt.Component child : ((java.awt.Container) comp).getComponents()) {
                addMouseListenerToAll(child, handler);
            }
        }
    }

    private JPanel buildMenuCard(int idx) {
        MenuDTO menu = menuList.get(idx);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createLineBorder(CARD_BORDER, 2, true));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel imgLabel = new JLabel();
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imgLabel.setPreferredSize(new Dimension(0, 165));
        imgLabel.setBackground(IMG_BG);
        imgLabel.setOpaque(true);
        loadMenuImage(imgLabel, menu.getImgUrl(), menu.getMenuName());
        card.add(imgLabel, BorderLayout.CENTER);

        JPanel info = new JPanel(new BorderLayout(6, 0));
        info.setBackground(CARD_INFO_BG);
        info.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, CARD_BORDER),
            new EmptyBorder(8, 12, 8, 12)
        ));

        JLabel nameLabel  = new JLabel(menu.getMenuName());
        nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        nameLabel.setForeground(NAME_COLOR);

        JLabel priceLabel = new JLabel(String.format("%,d원", menu.getPrice()));
        priceLabel.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        priceLabel.setForeground(PRICE_COLOR);

        info.add(nameLabel,  BorderLayout.WEST);
        info.add(priceLabel, BorderLayout.EAST);
        card.add(info, BorderLayout.SOUTH);

        return card;
    }

    private void loadMenuImage(JLabel label, String path, String name) {
        java.io.File f = new java.io.File(path);
        if (f.exists()) {
            ImageIcon icon   = new ImageIcon(path);
            Image     scaled = icon.getImage().getScaledInstance(240, 165, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(scaled));
        } else {
            label.setText("<html><center><span style='font-size:36px'>🍔</span><br>"
                + "<span style='color:#8B6914; font-size:12px'>" + name + "</span></center></html>");
        }
    }
}