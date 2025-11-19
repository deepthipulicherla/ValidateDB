package db;

import base.Base;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariDataSourceProvider extends Base {
    private static HikariDataSource ds;

    public static synchronized HikariDataSource get() {
        if (ds == null) {
            HikariConfig cfg = new HikariConfig();
            cfg.setJdbcUrl(System.getProperty("DB_URL", getPropValue("db.url")));
            cfg.setUsername(System.getProperty("DB_USER", getPropValue("db.user")));
            cfg.setPassword(System.getProperty("DB_PASSWORD", getPropValue("db.pass")));
            cfg.setMaximumPoolSize(5);
            cfg.setMinimumIdle(1);
            cfg.setConnectionTimeout(10000);
            cfg.setIdleTimeout(30000);
            cfg.setPoolName("testng-hikari");
            ds = new HikariDataSource(cfg);
        }
        return ds;
    }

    public static synchronized void close() {
        if (ds != null) {
            ds.close();
            ds = null;
        }
    }
}
