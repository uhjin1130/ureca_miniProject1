package kioskUI;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDAO {
	private final String URL      = "jdbc:mysql://localhost:3306/kingorder";
    private final String USER     = "root";
    private final String PASSWORD = "940913";
    //private final String PASSWORD = "111111";

    public List<MenuDTO> getAllMenus() {
        List<MenuDTO> list = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return list;
        }

        String sql = "SELECT menu_id, name, price, image_url FROM menus ORDER BY menu_id";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                list.add(new MenuDTO(
                    rs.getInt("menu_id"),
                    rs.getString("name"),
                    rs.getInt("price"),
                    rs.getString("image_url")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}