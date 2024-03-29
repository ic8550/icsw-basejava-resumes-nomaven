package club.swdev.webapp.sql;

import java.sql.Connection;
import java.sql.SQLException;

public interface SqlTransaction<T> {
    T executeTransaction(Connection dbConn) throws SQLException;
}
