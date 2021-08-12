import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedList;

class QueryManager {
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/todolistschema";
    private static final String USER = "root";
    private static final String PASSWORD = "Hw7X3JQW3k3Lj5Zb";

    private static LinkedList<Task> collectRecords(String query) throws SQLException{
        LinkedList<Task> result = new LinkedList<>();
        Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            int id = resultSet.getInt(1);
            String title = resultSet.getString(2);
            LocalDate dueDate = LocalDate.parse(resultSet.getDate(3).toString());
            boolean status = resultSet.getBoolean(4);
            Timestamp createdAt = resultSet.getTimestamp(5);
            Task fill = new Task(id, title, dueDate, status, createdAt);
            result.add(fill);
        }
        return result;
    }

    static LinkedList<Task> selectAllQuery()throws SQLException{
        return collectRecords("SELECT * FROM task");
    }

    static void deleteQuery(int id){
        try(Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate("DELETE FROM task WHERE (task_id = "+id+")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static LinkedList<Task> recordsByDateQuery(LocalDate date) throws SQLException{
        return collectRecords("SELECT * FROM task WHERE (due_date = \""+date.toString()+"\")");
    }

    static void insertQuery(String title, LocalDate date, boolean status){
        try(Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO task (title, due_date, status) VALUES (?, ?, ?)")
        ) {
            pstmt.setString(1, title);
            pstmt.setDate(2, Date.valueOf(date));
            pstmt.setBoolean(3, status);
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

