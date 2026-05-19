package adminUI; //

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
    private static Properties dbProperties;
    private static Properties sqlProperties;

    static {
        try {
            // 1. MySQL 드라이버 로드
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 외부 프로퍼티 설정 파일 로드 (인텔리제이 경로 버그 우회 방식)
            dbProperties = new Properties();
            dbProperties.load(new FileInputStream("db.properties"));

            sqlProperties = new Properties();
            sqlProperties.load(new FileInputStream("sql.properties"));

            System.out.println(">> [성공] DB 및 SQL 설정 파일 로드 완료!");
        } catch (ClassNotFoundException e) {
            System.out.println(">> [오류] MySQL 드라이버를 찾을 수 없습니다.");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.out.println(">> [오류] db.properties 또는 sql.properties 파일이 프로젝트 최상위 폴더에 없습니다!");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(">> [오류] 파일을 읽는 중 입출력 에러 발생.");
            e.printStackTrace();
        }
    }

    // 데이터베이스 연결 객체(Connection)를 반환하는 메서드
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                dbProperties.getProperty("url"),
                dbProperties.getProperty("user"),
                dbProperties.getProperty("password")
        );
    }

    // sql.properties에 정의된 쿼리문을 꺼내오는 메서드
    public static String getSQL(String key) {
        return sqlProperties.getProperty(key);
    }
}