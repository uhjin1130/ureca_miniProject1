package adminUI;

import java.sql.*;

import adminUI.DBUtil; // 네 프로젝트의 DBUtil 패키지 경로에 맞게 확인!
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class orderDAO {

    // AdminDAO.java 클래스 내부에 추가
    public int getTotalSales() {
        String sql = "SELECT SUM(total_price) AS total_revenue FROM orders WHERE status = 'COMPLETED'";
        // properties 방식을 쓴다면: String sql = DBUtil.getSQL("getTotalSales");

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total_revenue");
            }
        } catch (SQLException e) {
            System.out.println(">> 전체 총매출 조회 중 DB 에러!");
            e.printStackTrace();
        }
        return 0;
    }


    // 1. 오늘의 총매출 가져오기
    public int getTodaySales() {
        String sql = "SELECT SUM(total_price) AS today_sales FROM orders WHERE DATE(order_time) = CURDATE() AND status = 'COMPLETED'";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("today_sales");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 2. 오늘의 주문 건수 가져오기
    public int getTodayOrderCount() {
        String sql = "SELECT COUNT(*) AS today_count FROM orders WHERE DATE(order_time) = CURDATE() AND status = 'COMPLETED'";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("today_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean updateOrderStatus(java.util.List<String> orderIds, String newStatus) {
        if (orderIds == null || orderIds.isEmpty()) return false;

        // SQL 인문을 IN (?, ?, ...) 형태로 동적 생성
        StringBuilder sql = new StringBuilder("UPDATE orders SET status = ? WHERE order_id IN (");
        for (int i = 0; i < orderIds.size(); i++) {
            sql.append("?");
            if (i < orderIds.size() - 1) sql.append(", ");
        }
        sql.append(")");

        try (Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            
            // 1. 첫 번째 파라미터에 변경할 상태 세팅
            pstmt.setString(1, newStatus);
            
            // 2. IN 절에 들어갈 order_id 세팅 (문자열 형태 "#0001"에서 숫자만 추출)
            for (int i = 0; i < orderIds.size(); i++) {
                String rawId = orderIds.get(i).replace("#", ""); // "#0003" -> "0003"
                int id = Integer.parseInt(rawId);                 // "0003" -> 3
                pstmt.setInt(i + 2, id);
            }

            int updatedRows = pstmt.executeUpdate();
            return updatedRows > 0;

        } catch (SQLException e) {
            System.out.println(">> 주문 상태 업데이트 중 DB 에러!");
            e.printStackTrace();
        }
        return false;
    }

    // orderDAO.java 또는 AdminDAO.java 내부에 추가
    public Object[][] getRealtimeOrderList() {
        // properties 방식을 쓴다면: String sql = minni1_2.util.DBUtil.getSQL("getOrderList");
        String sql = "SELECT o.order_id, " +
                "       TIME(o.order_time) as o_time, " +
                "       GROUP_CONCAT(CONCAT(m.name, ' ', oi.quantity) SEPARATOR ', ') AS order_details, " +
                "       o.total_price, " +
                "       o.status " +
                "FROM orders o " +
                "JOIN order_items oi ON o.order_id = oi.order_id " +
                "JOIN menus m ON oi.menu_id = m.menu_id " +
                "GROUP BY o.order_id " +
                "ORDER BY o.order_id DESC"; // 최근 주문이 맨 위로 오도록 정렬

        java.util.List<Object[]> list = new java.util.ArrayList<>();

        try (java.sql.Connection conn = adminUI.DBUtil.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
             java.sql.ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String orderId = String.format("#%04d", rs.getInt("order_id")); // #0001 형식
                String orderTime = rs.getString("o_time");
                String details = rs.getString("order_details");

                // 숫자에 콤마 찍어서 원 붙이기 (예: 41,000원)
                String totalPrice = String.format("%,d원", rs.getInt("total_price"));

                // 한글 상태 매핑
                String status = rs.getString("status");
                if ("PENDING".equals(status)) status = "대기";
                else if ("COOKING".equals(status)) status = "조리중";
                else if ("COMPLETED".equals(status)) status = "완료";

                list.add(new Object[]{orderId, orderTime, details, totalPrice, status});
            }
        } catch (java.sql.SQLException e) {
            System.out.println(">> 실시간 주문 현황 조회 중 DB 에러!");
            e.printStackTrace();
        }

        // List를 JTable이 먹을 수 있는 Object[][] 배열로 변환
        return list.toArray(new Object[0][]);
    }
    /**
     * 특정 주문(orderId)을 DB에서 완전히 삭제합니다.
     */
    public void deleteOrder(long orderId) {
        // 자식(상세) 테이블부터 지우고, 부모(마스터) 테이블을 지우는 쿼리
        String deleteItemsSQL = "DELETE FROM order_items WHERE order_id = ?";
        String deleteOrderSQL = "DELETE FROM orders WHERE order_id = ?";

        // 본인의 DB 연결 코드 방식에 맞춰 Connection을 가져오세요 (예: DBUtil.getConnection())
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/kingorder", "root", "root")) {

            // 트랜잭션 시작: 두 쿼리가 모두 성공해야만 DB에 반영되도록 설정
            conn.setAutoCommit(false);

            try (PreparedStatement pstmtItem = conn.prepareStatement(deleteItemsSQL);
                 PreparedStatement pstmtOrder = conn.prepareStatement(deleteOrderSQL)) {

                // 1. 주문에 엮인 상세 메뉴들(자식) 모두 삭제
                pstmtItem.setLong(1, orderId);
                pstmtItem.executeUpdate();

                // 2. 주문 정보(부모) 삭제
                pstmtOrder.setLong(1, orderId);
                pstmtOrder.executeUpdate();

                // 여기까지 에러가 없으면 완벽하게 DB 반영
                conn.commit();
            } catch (Exception ex) {
                // 도중에 에러가 나면 롤백(취소)
                conn.rollback();
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int[] getMonthlySalesValues() {
        int[] monthlySales = new int[12]; // 1월부터 12월까지 저장할 배열 공간 생성
        
        String sql = "SELECT MONTH(order_time) AS month, SUM(total_price) AS sales " +
                    "FROM orders " +
                    "WHERE status = 'COMPLETED' AND YEAR(order_time) = YEAR(CURDATE()) " +
                    "GROUP BY MONTH(order_time)";
                    
        try (Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                int month = rs.getInt("month"); // DB에서 추출한 월 (1 ~ 12)
                int sales = rs.getInt("sales"); // 해당 월의 매출 합계
                
                // 자바 배열은 0부터 시작하므로 [월 - 1] 인덱스에 매핑하여 저장합니다.
                monthlySales[month - 1] = sales; 
            }
        } catch (SQLException e) {
            System.out.println(">> 월별 매출 통계 조회 중 DB 에러 발생!");
            e.printStackTrace();
        }
        
        return monthlySales;
    }
}