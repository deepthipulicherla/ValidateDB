package db;

import base.Base;
import io.qameta.allure.Step;
import io.qameta.allure.Story;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;

public class DbUtils extends Base {
    private final DataSource ds;

    public DbUtils(DataSource ds) {
        this.ds = ds;
    }

    @Step("Count active users")
    public long countActiveUsers() throws Exception {
        try (Connection conn = ds.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE status='ACTIVE'");
        ResultSet rs = ps.executeQuery())
        {
            rs.next();
            logInfo("Number of active user : "+rs.getLong(1));
            return rs.getLong(1);
        }
    }

    @Step("To check for duplicates")
    public boolean findDuplicates() throws Exception {
        int count=0;
        try(Connection conn = ds.getConnection();
        PreparedStatement ps = conn.prepareStatement("select * from users where id not in (select id from (select max(id) as id from users group by email having count(*)>1) as id ) and email in (select email from" +
                "users group by email having count(*)>1)");
        ResultSet rs = ps.executeQuery()){
            while (rs.next()){
                count++;
                logInfo("Id : "+rs.getInt("id"+"*** email : "+rs.getString("email")));
            }

        }
        return count>0;
    }

    @Step("Find the second highest salary")
    public BigDecimal findSecondHighestSalary() throws Exception {
        try(Connection conn = ds.getConnection();
        PreparedStatement ps = conn.prepareStatement("select max(salary) as salary from users where salary < (select max(salary) from users )");
        ResultSet rs = ps.executeQuery()){
            rs.next();
            return rs.getBigDecimal("salary");
        }
    }

    @Step("Delete the duplicates")
    public boolean deleteDuplicates() throws Exception {
        int rowsDeleted = 0;
        try(Connection conn = ds.getConnection();
        PreparedStatement ps = conn.prepareStatement("delete from users where id not in (select id from (select max(id) as id from users group by email having count(*)>1) as id ) and email in (select email from (select email as email from users group by email having count(*)>1) as email )"))
        {
            rowsDeleted = ps.executeUpdate();
           logInfo("Number of rows deleted : "+rowsDeleted);
            return rowsDeleted>0;

        }

    }
}

