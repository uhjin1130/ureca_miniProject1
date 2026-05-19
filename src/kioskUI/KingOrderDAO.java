package kioskUI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class KingOrderDAO {
    private final String URL      = "jdbc:mysql://localhost:3306/kingorder";
    private final String USER     = "root";
    private final String PASSWORD = "1234";
    //private final String PASSWORD = "111111";

    /**
     * 주문 삽입 후 생성된 orderId(AutoIncrement) 반환
     * 실패 시 -1 반환
     */
    public long insertOrder(List<KingOrderDTO> itemList, int totalPrice) {
    	String insertOrderSQL = "INSERT INTO orders (total_price) VALUES (?)";
        String insertItemSQL  = "INSERT INTO order_items (order_id, menu_id, quantity, subtotal) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false);
            long orderId = 0;

            // 1. orders 테이블에 마스터 데이터 삽입
            try (PreparedStatement pstmtOrder = conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS)) {
                pstmtOrder.setInt(1, totalPrice);
                pstmtOrder.executeUpdate();

                try (ResultSet rs = pstmtOrder.getGeneratedKeys()) {
                    if (rs.next()) {
                        orderId = rs.getLong(1);
                    }
                }
            }

            // 2. order_items 테이블에 상세 배열 데이터 batch 삽입
            try (PreparedStatement pstmtItem = conn.prepareStatement(insertItemSQL)) {
                for (KingOrderDTO item : itemList) {
                    pstmtItem.setLong(1, orderId);
                    pstmtItem.setLong(2, item.getMenuId());
                    pstmtItem.setInt(3, item.getQuantity());
                    pstmtItem.setInt(4, item.getSubtotal());
                    pstmtItem.addBatch();
                }
                pstmtItem.executeBatch();
            }

            conn.commit();
            System.out.println("DB 전송 성공! 주문 번호: " + orderId);
            return orderId; // ← 주문번호 반환

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DB 오류로 인한 롤백을 수행합니다.");
            return -1; // ← 실패 시 -1
        }
    }
}