package tests;


import base.Base;
import db.DbUtils;
import db.HikariDataSourceProvider;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.math.BigDecimal;

@Epic("Database validation")
@Feature("User and salary checks")
@Story("Ensure database integrity for users and salaries")
public class DbValidationTest extends Base {
    private static DataSource ds;
    private static DbUtils db;

    @BeforeClass
    @Step("Initialize hikari datasource and dbutils")
    public static void setup() {
        // Initialize HikariCP datasource here
        ds = HikariDataSourceProvider.get();
        db = new DbUtils(ds);
        logInfo("DataSource Initialized successfully");
    }

    @AfterClass
    @Step("Close hikari datasource cleanly")
    public static void tearDown() throws Exception {
        if (ds instanceof AutoCloseable) {
            ((AutoCloseable) ds).close(); // close HikariCP pool cleanly
            logInfo("Datasource closed successfully");
        }
    }

    @Test(priority = 1)
    @Step("Validate active users")
    public void testActiveUsersExist() throws Exception {
        long activeCount = db.countActiveUsers();
        logInfo("Active count users available : "+activeCount);
        Assert.assertTrue(activeCount > 0, "There should be at least one ACTIVE user");
    }

    @Test(priority = 2)
    public void testDuplicateUsersExit() throws Exception {
        boolean hasDuplicates = db.findDuplicates();
        logInfo("Duplicate users found : "+hasDuplicates);
        Assert.assertFalse(hasDuplicates,"There are duplicates existing ");

    }

    @Test(priority = 3)
    public void testSecondHighestSalary() throws Exception {
        BigDecimal salary = db.findSecondHighestSalary();
        logInfo("Second highest salary found : "+salary);
        Assert.assertEquals(String.valueOf(salary),getPropValue("second.highest.salary"));
    }

    @Test(priority = 4)
    public void testDeleteDuplicate() throws Exception {
        boolean isDeleted = db.deleteDuplicates();
        logInfo("Is duplicate rows deleted : "+isDeleted);
        Assert.assertTrue(isDeleted,"duplicate rows deleted");
    }




}

