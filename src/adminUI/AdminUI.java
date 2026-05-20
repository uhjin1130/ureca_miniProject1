package adminUI;

import java.awt.*;
import javax.swing.*;

public class AdminUI extends JFrame {
    public AdminUI() {
        setTitle("킹오더 관리자 시스템");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(850, 700);
        setMinimumSize(new Dimension(750, 600));
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // 분리된 패널 조립
        add(new AdminNorthPanel(), BorderLayout.NORTH);
        add(new OrderManagement(), BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(AdminUI::new);
    }
}