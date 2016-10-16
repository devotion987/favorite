package com.devotion.rws.wapper;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * 数据库连接池包装器
 */
public class ConnectionWrapper implements Connection {

    /**
     * 当前连接
     */
    private Connection currentConnection;

    /**
     * 构造方法
     *
     * @param currentConnection 当前连接
     */
    public ConnectionWrapper(Connection currentConnection) {
        super();
        this.currentConnection = currentConnection;
    }

    /**
     * Returns an object that implements the given interface to allow access to
     * non-standard methods, or standard methods not exposed by the proxy.
     * <p>
     * If the receiver implements the interface then the result is the receiver
     * or a proxy for the receiver. If the receiver is a wrapper and the wrapped
     * object implements the interface then the result is the wrapped object or
     * a proxy for the wrapped object. Otherwise return the the result of
     * calling <code>unwrap</code> recursively on the wrapped object or a proxy
     * for that result. If the receiver is not a wrapper and does not implement
     * the interface, then an <code>SQLException</code> is thrown.
     *
     * @param iFace A Class defining an interface that the result must implement.
     * @param <T>   泛型对象
     * @return an object that implements the interface. May be a proxy for the
     * actual implementing object.
     * @throws SQLException If no object found that implements the interface
     * @since 1.6
     */
    public <T> T unwrap(Class<T> iFace) throws SQLException {
        return currentConnection.unwrap(iFace);
    }

    /**
     * Returns true if this either implements the interface argument or is
     * directly or indirectly a wrapper for an object that does. Returns false
     * otherwise. If this implements the interface then return true, else if
     * this is a wrapper then return the result of recursively calling
     * <code>isWrapperFor</code> on the wrapped object. If this does not
     * implement the interface and is not a wrapper, return false. This method
     * should be implemented as a low-cost operation compared to
     * <code>unwrap</code> so that callers can use this method to avoid
     * expensive <code>unwrap</code> calls that may fail. If this method returns
     * true then calling <code>unwrap</code> with the same argument should
     * succeed.
     *
     * @param iFace a Class defining an interface.
     * @return true if this implements the interface or directly or indirectly
     * wraps an object that does.
     * @throws SQLException if an error occurs while determining whether this is a
     *                      wrapper for an object with the given interface.
     * @since 1.6
     */
    public boolean isWrapperFor(Class<?> iFace) throws SQLException {
        return currentConnection.isWrapperFor(iFace);
    }

    /**
     * Creates a <code>Statement</code> object for sending SQL statements to the
     * database. SQL statements without parameters are normally executed using
     * <code>Statement</code> objects. If the same SQL statement is executed
     * many times, it may be more efficient to use a
     * <code>PreparedStatement</code> object.
     * <p>
     * Result sets created using the returned <code>Statement</code> object will
     * by default be type <code>TYPE_FORWARD_ONLY</code> and have a concurrency
     * level of <code>CONCUR_READ_ONLY</code>. The holdability of the created
     * result sets can be determined by calling {@link #getHoldability}.
     *
     * @return a new default <code>Statement</code> object
     * @throws SQLException if a database access error occurs or this method is called
     *                      on a closed connection
     */
    public Statement createStatement() throws SQLException {
        return currentConnection.createStatement();
    }

    /**
     * Creates a <code>PreparedStatement</code> object for sending parameterized
     * SQL statements to the database.
     * <p>
     * A SQL statement with or without IN parameters can be pre-compiled and
     * stored in a <code>PreparedStatement</code> object. This object can then
     * be used to efficiently execute this statement multiple times.
     * <p>
     * <p>
     * <B>Note:</B> This method is optimized for handling parametric SQL
     * statements that benefit from precompilation. If the driver supports
     * precompilation, the method <code>prepareStatement</code> will send the
     * statement to the database for precompilation. Some drivers may not
     * support precompilation. In this case, the statement may not be sent to
     * the database until the <code>PreparedStatement</code> object is executed.
     * This has no direct effect on users; however, it does affect which methods
     * throw certain <code>SQLException</code> objects.
     * <p>
     * Result sets created using the returned <code>PreparedStatement</code>
     * object will by default be type <code>TYPE_FORWARD_ONLY</code> and have a
     * concurrency level of <code>CONCUR_READ_ONLY</code>. The holdability of
     * the created result sets can be determined by calling
     * {@link #getHoldability}.
     *
     * @param sql an SQL statement that may contain one or more '?' IN parameter
     *            placeholders
     * @return a new default <code>PreparedStatement</code> object containing
     * the pre-compiled SQL statement
     * @throws SQLException if a database access error occurs or this method is called
     *                      on a closed connection
     */
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return new PreparedStatementWrapper(currentConnection.prepareStatement(sql));
    }

    /**
     * Creates a <code>CallableStatement</code> object for calling database
     * stored procedures. The <code>CallableStatement</code> object provides
     * methods for setting up its IN and OUT parameters, and methods for
     * executing the call to a stored procedure.
     * <p>
     * <p>
     * <B>Note:</B> This method is optimized for handling stored procedure call
     * statements. Some drivers may send the call statement to the database when
     * the method <code>prepareCall</code> is done; others may wait until the
     * <code>CallableStatement</code> object is executed. This has no direct
     * effect on users; however, it does affect which method throws certain
     * SQLExceptions.
     * <p>
     * Result sets created using the returned <code>CallableStatement</code>
     * object will by default be type <code>TYPE_FORWARD_ONLY</code> and have a
     * concurrency level of <code>CONCUR_READ_ONLY</code>. The holdability of
     * the created result sets can be determined by calling
     * {@link #getHoldability}.
     *
     * @param sql an SQL statement that may contain one or more '?' parameter
     *            placeholders. Typically this statement is specified using JDBC
     *            call escape syntax.
     * @return a new default <code>CallableStatement</code> object containing
     * the pre-compiled SQL statement
     * @throws SQLException if a database access error occurs or this method is called
     *                      on a closed connection
     */
    public CallableStatement prepareCall(String sql) throws SQLException {
        return currentConnection.prepareCall(sql);
    }

    /**
     * Converts the given SQL statement into the system's native SQL grammar. A
     * driver may convert the JDBC SQL grammar into its system's native SQL
     * grammar prior to sending it. This method returns the native form of the
     * statement that the driver would have sent.
     *
     * @param sql an SQL statement that may contain one or more '?' parameter
     *            placeholders
     * @return the native form of this statement
     * @throws SQLException if a database access error occurs or this method is called
     *                      on a closed connection
     */
    public String nativeSQL(String sql) throws SQLException {
        return currentConnection.nativeSQL(sql);
    }

    /**
     * Sets this connection's auto-commit mode to the given state. If a
     * connection is in auto-commit mode, then all its SQL statements will be
     * executed and committed as individual transactions. Otherwise, its SQL
     * statements are grouped into transactions that are terminated by a call to
     * either the method <code>commit</code> or the method <code>rollback</code>
     * . By default, new connections are in auto-commit mode.
     * <p>
     * The commit occurs when the statement completes. The time when the
     * statement completes depends on the type of SQL Statement:
     * <ul>
     * <li>For DML statements, such as Insert, Update or Delete, and DDL
     * statements, the statement is complete as soon as it has finished
     * executing.
     * <li>For Select statements, the statement is complete when the associated
     * result set is closed.
     * <li>For <code>CallableStatement</code> objects or for statements that
     * return multiple results, the statement is complete when all of the
     * associated result sets have been closed, and all update counts and output
     * parameters have been retrieved.
     * </ul>
     * <p>
     * <B>NOTE:</B> If this method is called during a transaction and the
     * auto-commit mode is changed, the transaction is committed. If
     * <code>setAutoCommit</code> is called and the auto-commit mode is not
     * changed, the call is a no-op.
     *
     * @param autoCommit <code>true</code> to enable auto-commit mode;
     *                   <code>false</code> to disable it
     * @throws SQLException if a database access error occurs, setAutoCommit(true) is
     *                      called while participating in a distributed transaction,
     *                      or this method is called on a closed connection
     * @see #getAutoCommit
     */
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        currentConnection.setAutoCommit(autoCommit);
    }

    /**
     * Retrieves the current auto-commit mode for this <code>Connection</code>
     * object.
     *
     * @return the current state of this <code>Connection</code> object's
     * auto-commit mode
     * @throws SQLException if a database access error occurs or this method is called
     *                      on a closed connection
     * @see #setAutoCommit
     */
    public boolean getAutoCommit() throws SQLException {
        return currentConnection.getAutoCommit();
    }

    /**
     * Makes all changes made since the previous commit/rollback permanent and
     * releases any database locks currently held by this
     * <code>Connection</code> object. This method should be used only when
     * auto-commit mode has been disabled.
     *
     * @throws SQLException if a database access error occurs, this method is called
     *                      while participating in a distributed transaction, if this
     *                      method is called on a closed conection or this
     *                      <code>Connection</code> object is in auto-commit mode
     * @see #setAutoCommit
     */
    public void commit() throws SQLException {
        currentConnection.commit();
    }

    /**
     * Undoes all changes made in the current transaction and releases any
     * database locks currently held by this <code>Connection</code> object.
     * This method should be used only when auto-commit mode has been disabled.
     *
     * @throws SQLException if a database access error occurs, this method is called
     *                      while participating in a distributed transaction, this
     *                      method is called on a closed connection or this
     *                      <code>Connection</code> object is in auto-commit mode
     * @see #setAutoCommit
     */
    public void rollback() throws SQLException {
        currentConnection.rollback();
    }

    /**
     * Releases this <code>Connection</code> object's database and JDBC
     * resources immediately instead of waiting for them to be automatically
     * released.
     * <p>
     * Calling the method <code>close</code> on a <code>Connection</code> object
     * that is already closed is a no-op.
     * <p>
     * It is <b>strongly recommended</b> that an application explicitly commits
     * or rolls back an active transaction prior to calling the
     * <code>close</code> method. If the <code>close</code> method is called and
     * there is an active transaction, the results are implementation-defined.
     * <p>
     *
     * @throws SQLException SQLException if a database access error occurs
     */
    public void close() throws SQLException {
        currentConnection.close();
    }

    /**
     * Retrieves whether this <code>Connection</code> object has been closed. A
     * connection is closed if the method <code>close</code> has been called on
     * it or if certain fatal errors have occurred. This method is guaranteed to
     * return <code>true</code> only when it is called after the method
     * <code>Connection.close</code> has been called.
     * <p>
     * This method generally cannot be called to determine whether a connection
     * to a database is valid or invalid. A typical client can determine that a
     * connection is invalid by catching any exceptions that might be thrown
     * when an operation is attempted.
     *
     * @return <code>true</code> if this <code>Connection</code> object is
     * closed; <code>false</code> if it is still open
     * @throws SQLException if a database access error occurs
     */
    public boolean isClosed() throws SQLException {
        return currentConnection.isClosed();
    }

    /**
     * Retrieves a <code>DatabaseMetaData</code> object that contains metadata
     * about the database to which this <code>Connection</code> object
     * represents a connection. The metadata includes information about the
     * database's tables, its supported SQL grammar, its stored procedures, the
     * capabilities of this connection, and so on.
     *
     * @return a <code>DatabaseMetaData</code> object for this
     * <code>Connection</code> object
     * @throws SQLException if a database access error occurs or this method is called
     *                      on a closed connection
     */
    public DatabaseMetaData getMetaData() throws SQLException {
        return currentConnection.getMetaData();
    }

    /**
     * Puts this connection in read-only mode as a hint to the driver to enable
     * database optimizations.
     * <p>
     * <p>
     * <B>Note:</B> This method cannot be called during a transaction.
     *
     * @param readOnly <code>true</code> enables read-only mode; <code>false</code>
     *                 disables it
     * @throws SQLException if a database access error occurs, this method is called
     *                      on a closed connection or this method is called during a
     *                      transaction
     */
    public void setReadOnly(boolean readOnly) throws SQLException {
        currentConnection.setReadOnly(readOnly);
    }

    /**
     * Retrieves whether this <code>Connection</code> object is in read-only
     * mode.
     *
     * @return <code>true</code> if this <code>Connection</code> object is
     * read-only; <code>false</code> otherwise
     * @throws SQLException SQLException if a database access error occurs or this
     *                      method is called on a closed connection
     */
    public boolean isReadOnly() throws SQLException {
        return currentConnection.isReadOnly();
    }

    /**
     * Retrieves whether this <code>Connection</code> object is in read-only
     * mode.
     *
     * @param catalog catalogs
     * @throws SQLException SQLException if a database access error occurs or this
     *                      method is called on a closed connection
     */
    public void setCatalog(String catalog) throws SQLException {
        currentConnection.setCatalog(catalog);
    }

    /**
     * Retrieves this <code>Connection</code> object's current catalog name.
     *
     * @return the current catalog name or <code>null</code> if there is none
     * @throws SQLException if a database access error occurs or this method is called
     *                      on a closed connection
     * @see #setCatalog
     */
    public String getCatalog() throws SQLException {
        return currentConnection.getCatalog();
    }

    /**
     * Attempts to change the transaction isolation level for this
     * <code>Connection</code> object to the one given. The constants defined in
     * the interface <code>Connection</code> are the possible transaction
     * isolation levels.
     * <p>
     * <B>Note:</B> If this method is called during a transaction, the result is
     * implementation-defined.
     *
     * @param level one of the following <code>Connection</code> constants:
     *              <code>Connection.TRANSACTION_READ_UNCOMMITTED</code>,
     *              <code>Connection.TRANSACTION_READ_COMMITTED</code>,
     *              <code>Connection.TRANSACTION_REPEATABLE_READ</code>, or
     *              <code>Connection.TRANSACTION_SERIALIZABLE</code>. (Note that
     *              <code>Connection.TRANSACTION_NONE</code> cannot be used
     *              because it specifies that transactions are not supported.)
     * @throws SQLException if a database access error occurs, this method is called
     *                      on a closed connection or the given parameter is not one
     *                      of the <code>Connection</code> constants
     * @see DatabaseMetaData#supportsTransactionIsolationLevel
     * @see #getTransactionIsolation
     */
    public void setTransactionIsolation(int level) throws SQLException {
        currentConnection.setTransactionIsolation(level);
    }

    /**
     * Retrieves this <code>Connection</code> object's current transaction
     * isolation level.
     *
     * @return the current transaction isolation level, which will be one of the
     * following constants:
     * <code>Connection.TRANSACTION_READ_UNCOMMITTED</code>,
     * <code>Connection.TRANSACTION_READ_COMMITTED</code>,
     * <code>Connection.TRANSACTION_REPEATABLE_READ</code>,
     * <code>Connection.TRANSACTION_SERIALIZABLE</code>, or
     * <code>Connection.TRANSACTION_NONE</code>.
     * @throws SQLException if a database access error occurs or this method is called
     *                      on a closed connection
     * @see #setTransactionIsolation
     */
    public int getTransactionIsolation() throws SQLException {
        return currentConnection.getTransactionIsolation();
    }

    /**
     * Retrieves the first warning reported by calls on this
     * <code>Connection</code> object. If there is more than one warning,
     * subsequent warnings will be chained to the first one and can be retrieved
     * by calling the method <code>SQLWarning.getNextWarning</code> on the
     * warning that was retrieved previously.
     * <p>
     * This method may not be called on a closed connection; doing so will cause
     * an <code>SQLException</code> to be thrown.
     * <p>
     * <p>
     * <B>Note:</B> Subsequent warnings will be chained to this SQLWarning.
     *
     * @return the first <code>SQLWarning</code> object or <code>null</code> if
     * there are none
     * @throws SQLException if a database access error occurs or this method is called
     *                      on a closed connection
     * @see SQLWarning
     */
    public SQLWarning getWarnings() throws SQLException {
        return currentConnection.getWarnings();
    }

    /**
     * Clears all warnings reported for this <code>Connection</code> object.
     * After a call to this method, the method <code>getWarnings</code> returns
     * <code>null</code> until a new warning is reported for this
     * <code>Connection</code> object.
     *
     * @throws SQLException SQLException if a database access error occurs or this
     *                      method is called on a closed connection
     */
    public void clearWarnings() throws SQLException {
        currentConnection.clearWarnings();
    }

    /**
     * Creates a <code>Statement</code> object that will generate
     * <code>ResultSet</code> objects with the given type and concurrency. This
     * method is the same as the <code>createStatement</code> method above, but
     * it allows the default result set type and concurrency to be overridden.
     * The holdability of the created result sets can be determined by calling
     * {@link #getHoldability}.
     *
     * @param resultSetType        a result set type; one of
     *                             <code>ResultSet.TYPE_FORWARD_ONLY</code>,
     *                             <code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or
     *                             <code>ResultSet.TYPE_SCROLL_SENSITIVE</code>
     * @param resultSetConcurrency a concurrency type; one of
     *                             <code>ResultSet.CONCUR_READ_ONLY</code> or
     *                             <code>ResultSet.CONCUR_UPDATABLE</code>
     * @return a new <code>Statement</code> object that will generate
     * <code>ResultSet</code> objects with the given type and
     * concurrency
     * @throws SQLException if a database access error occurs, this method is called
     *                      on a closed connection or the given parameters are not
     *                      <code>ResultSet</code> constants indicating type and
     *                      concurrency
     * @since 1.2
     */
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return currentConnection.createStatement(resultSetType, resultSetConcurrency);
    }

    /**
     * Creates a <code>PreparedStatement</code> object that will generate
     * <code>ResultSet</code> objects with the given type and concurrency. This
     * method is the same as the <code>prepareStatement</code> method above, but
     * it allows the default result set type and concurrency to be overridden.
     * The holdability of the created result sets can be determined by calling
     * {@link #getHoldability}.
     *
     * @param sql                  a <code>String</code> object that is the SQL statement to be
     *                             sent to the database; may contain one or more '?' IN
     *                             parameters
     * @param resultSetType        a result set type; one of
     *                             <code>ResultSet.TYPE_FORWARD_ONLY</code>,
     *                             <code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or
     *                             <code>ResultSet.TYPE_SCROLL_SENSITIVE</code>
     * @param resultSetConcurrency a concurrency type; one of
     *                             <code>ResultSet.CONCUR_READ_ONLY</code> or
     *                             <code>ResultSet.CONCUR_UPDATABLE</code>
     * @return a new PreparedStatement object containing the pre-compiled SQL
     * statement that will produce <code>ResultSet</code> objects with
     * the given type and concurrency
     * @throws SQLException if a database access error occurs, this method is called
     *                      on a closed connection or the given parameters are not
     *                      <code>ResultSet</code> constants indicating type and
     *                      concurrency
     * @since 1.2
     */
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
            throws SQLException {
        return new PreparedStatementWrapper(
                currentConnection.prepareStatement(sql, resultSetType, resultSetConcurrency));
    }

    /**
     * Creates a <code>CallableStatement</code> object for calling database
     * stored procedures. The <code>CallableStatement</code> object provides
     * methods for setting up its IN and OUT parameters, and methods for
     * executing the call to a stored procedure.
     * <p>
     * <p>
     * <B>Note:</B> This method is optimized for handling stored procedure call
     * statements. Some drivers may send the call statement to the database when
     * the method <code>prepareCall</code> is done; others may wait until the
     * <code>CallableStatement</code> object is executed. This has no direct
     * effect on users; however, it does affect which method throws certain
     * SQLExceptions.
     * <p>
     * Result sets created using the returned <code>CallableStatement</code>
     * object will by default be type <code>TYPE_FORWARD_ONLY</code> and have a
     * concurrency level of <code>CONCUR_READ_ONLY</code>. The holdability of
     * the created result sets can be determined by calling
     * {@link #getHoldability}.
     *
     * @param sql                  an SQL statement that may contain one or more '?' parameter
     *                             placeholders. Typically this statement is specified using JDBC
     *                             call escape syntax.
     * @param resultSetType        resultSetTypes
     * @param resultSetConcurrency resultSetConcurrencys
     * @return a new default <code>CallableStatement</code> object containing
     * the pre-compiled SQL statement
     * @throws SQLException if a database access error occurs or this method is called
     *                      on a closed connection
     */
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return currentConnection.prepareCall(sql);
    }

    /**
     * Retrieves the <code>Map</code> object associated with this
     * <code>Connection</code> object. Unless the application has added an
     * entry, the type map returned will be empty.
     * <p>
     * You must invoke <code>setTypeMap</code> after making changes to the
     * <code>Map</code> object returned from <code>getTypeMap</code> as a JDBC
     * driver may create an internal copy of the <code>Map</code> object passed
     * to <code>setTypeMap</code>:
     * <p>
     * <p>
     * <pre>
     * Map&lt;String, Class&lt;?&gt;&gt; myMap = con.getTypeMap();
     * myMap.put(&quot;mySchemaName.ATHLETES&quot;, Athletes.class);
     * con.setTypeMap(myMap);
     * </pre>
     *
     * @return the <code>java.util.Map</code> object associated with this
     * <code>Connection</code> object
     * @throws SQLException if a database access error occurs or this method is called
     *                      on a closed connection
     * @see #setTypeMap
     * @since 1.2
     */
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return currentConnection.getTypeMap();
    }

    /**
     * Installs the given <code>TypeMap</code> object as the type map for this
     * <code>Connection</code> object. The type map will be used for the custom
     * mapping of SQL structured types and distinct types.
     * <p>
     * You must set the the values for the <code>TypeMap</code> prior to callng
     * <code>setMap</code> as a JDBC driver may create an internal copy of the
     * <code>TypeMap</code>:
     * <p>
     * <p>
     * <pre>
     *      Map myMap&lt;String,Class&lt;?&gt;&gt; = new HashMap&lt;String,Class&lt;?&gt;&gt;();
     *      myMap.put("mySchemaName.ATHLETES", Athletes.class);
     *      con.setTypeMap(myMap);
     * </pre>
     *
     * @param map the <code>java.util.Map</code> object to install as the
     *            replacement for this <code>Connection</code> object's default
     *            type map
     * @throws SQLException if a database access error occurs, this method is called
     *                      on a closed connection or the given parameter is not a
     *                      <code>java.util.Map</code> object
     * @see #getTypeMap
     * @since 1.2
     */
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        currentConnection.setTypeMap(map);
    }

    /**
     * Changes the default holdability of <code>ResultSet</code> objects created
     * using this <code>Connection</code> object to the given holdability. The
     * default holdability of <code>ResultSet</code> objects can be be
     * determined by invoking {@link DatabaseMetaData#getResultSetHoldability}.
     *
     * @param holdability a <code>ResultSet</code> holdability constant; one of
     *                    <code>ResultSet.HOLD_CURSORS_OVER_COMMIT</code> or
     *                    <code>ResultSet.CLOSE_CURSORS_AT_COMMIT</code>
     * @throws SQLException if a database access occurs, this method is called on a
     *                      closed connection, or the given parameter is not a
     *                      <code>ResultSet</code> constant indicating holdability
     * @see #getHoldability
     * @see DatabaseMetaData#getResultSetHoldability
     * @see ResultSet
     * @since 1.4
     */
    public void setHoldability(int holdability) throws SQLException {
        currentConnection.setHoldability(holdability);
    }

    /**
     * Retrieves the current holdability of <code>ResultSet</code> objects
     * created using this <code>Connection</code> object.
     *
     * @return the holdability, one of
     * <code>ResultSet.HOLD_CURSORS_OVER_COMMIT</code> or
     * <code>ResultSet.CLOSE_CURSORS_AT_COMMIT</code>
     * @throws SQLException if a database access error occurs or this method is called on
     *                      a closed connection
     * @see #setHoldability
     * @see DatabaseMetaData#getResultSetHoldability
     * @see ResultSet
     * @since 1.4
     */
    public int getHoldability() throws SQLException {
        return currentConnection.getHoldability();
    }

    /**
     * Creates an unnamed savepoint in the current transaction and returns the
     * new <code>Savepoint</code> object that represents it.
     * <p>
     * <p>
     * if setSavepoint is invoked outside of an active transaction, a
     * transaction will be started at this newly created savepoint.
     *
     * @return the new <code>Savepoint</code> object
     * @throws SQLException if a database access error occurs, this method is called
     *                      while participating in a distributed transaction, this
     *                      method is called on a closed connection or this
     *                      <code>Connection</code> object is currently in auto-commit
     *                      mode
     * @see Savepoint
     * @since 1.4
     */
    public Savepoint setSavepoint() throws SQLException {
        return currentConnection.setSavepoint();
    }

    /**
     * Creates a savepoint with the given name in the current transaction and
     * returns the new <code>Savepoint</code> object that represents it.
     * <p>
     * <p>
     * if setSavepoint is invoked outside of an active transaction, a
     * transaction will be started at this newly created savepoint.
     *
     * @param name a <code>String</code> containing the name of the savepoint
     * @return the new <code>Savepoint</code> object
     * @throws SQLException if a database access error occurs, this method is called
     *                      while participating in a distributed transaction, this
     *                      method is called on a closed connection or this
     *                      <code>Connection</code> object is currently in auto-commit
     *                      mode
     * @see Savepoint
     * @since 1.4
     */
    public Savepoint setSavepoint(String name) throws SQLException {
        return currentConnection.setSavepoint(name);
    }

    /**
     * Undoes all changes made after the given <code>Savepoint</code> object was
     * set.
     * <p>
     * This method should be used only when auto-commit has been disabled.
     *
     * @param savepoint the <code>Savepoint</code> object to roll back to
     * @throws SQLException if a database access error occurs, this method is called
     *                      while participating in a distributed transaction, this
     *                      method is called on a closed connection, the
     *                      <code>Savepoint</code> object is no longer valid, or this
     *                      <code>Connection</code> object is currently in auto-commit
     *                      mode
     * @see Savepoint
     * @see #rollback
     * @since 1.4
     */
    public void rollback(Savepoint savepoint) throws SQLException {
        currentConnection.rollback(savepoint);
    }

    /**
     * Removes the specified <code>Savepoint</code> and subsequent
     * <code>Savepoint</code> objects from the current transaction. Any
     * reference to the savepoint after it have been removed will cause an
     * <code>SQLException</code> to be thrown.
     *
     * @param savepoint the <code>Savepoint</code> object to be removed
     * @throws SQLException if a database access error occurs, this method is called
     *                      on a closed connection or the given <code>Savepoint</code>
     *                      object is not a valid savepoint in the current transaction
     * @since 1.4
     */
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        currentConnection.releaseSavepoint(savepoint);
    }

    /**
     * Creates a <code>Statement</code> object that will generate
     * <code>ResultSet</code> objects with the given type, concurrency, and
     * holdability. This method is the same as the <code>createStatement</code>
     * method above, but it allows the default result set type, concurrency, and
     * holdability to be overridden.
     *
     * @param resultSetType        one of the following <code>ResultSet</code> constants:
     *                             <code>ResultSet.TYPE_FORWARD_ONLY</code>,
     *                             <code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or
     *                             <code>ResultSet.TYPE_SCROLL_SENSITIVE</code>
     * @param resultSetConcurrency one of the following <code>ResultSet</code> constants:
     *                             <code>ResultSet.CONCUR_READ_ONLY</code> or
     *                             <code>ResultSet.CONCUR_UPDATABLE</code>
     * @param resultSetHoldability one of the following <code>ResultSet</code> constants:
     *                             <code>ResultSet.HOLD_CURSORS_OVER_COMMIT</code> or
     *                             <code>ResultSet.CLOSE_CURSORS_AT_COMMIT</code>
     * @return a new <code>Statement</code> object that will generate
     * <code>ResultSet</code> objects with the given type, concurrency,
     * and holdability
     * @throws SQLException if a database access error occurs, this method is called
     *                      on a closed connection or the given parameters are not
     *                      <code>ResultSet</code> constants indicating type,
     *                      concurrency, and holdability
     * @see ResultSet
     * @since 1.4
     */
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        return currentConnection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    /**
     * Creates a <code>PreparedStatement</code> object that will generate
     * <code>ResultSet</code> objects with the given type and concurrency. This
     * method is the same as the <code>prepareStatement</code> method above, but
     * it allows the default result set type and concurrency to be overridden.
     * The holdability of the created result sets can be determined by calling
     * {@link #getHoldability}.
     *
     * @param sql                  a <code>String</code> object that is the SQL statement to be
     *                             sent to the database; may contain one or more '?' IN
     *                             parameters
     * @param resultSetType        a result set type; one of
     *                             <code>ResultSet.TYPE_FORWARD_ONLY</code>,
     *                             <code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or
     *                             <code>ResultSet.TYPE_SCROLL_SENSITIVE</code>
     * @param resultSetConcurrency a concurrency type; one of
     *                             <code>ResultSet.CONCUR_READ_ONLY</code> or
     *                             <code>ResultSet.CONCUR_UPDATABLE</code>
     * @param resultSetHoldability resultSetHoldabilitys
     * @return a new PreparedStatement object containing the pre-compiled SQL
     * statement that will produce <code>ResultSet</code> objects with
     * the given type and concurrency
     * @throws SQLException if a database access error occurs, this method is called
     *                      on a closed connection or the given parameters are not
     *                      <code>ResultSet</code> constants indicating type and
     *                      concurrency
     * @since 1.2
     */
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
                                              int resultSetHoldability) throws SQLException {
        return new PreparedStatementWrapper(
                currentConnection.prepareStatement(sql, resultSetType, resultSetConcurrency));
    }

    /**
     * Creates a <code>CallableStatement</code> object that will generate
     * <code>ResultSet</code> objects with the given type and concurrency. This
     * method is the same as the <code>prepareCall</code> method above, but it
     * allows the default result set type, result set concurrency type and
     * holdability to be overridden.
     *
     * @param sql                  a <code>String</code> object that is the SQL statement to be
     *                             sent to the database; may contain on or more '?' parameters
     * @param resultSetType        one of the following <code>ResultSet</code> constants:
     *                             <code>ResultSet.TYPE_FORWARD_ONLY</code>,
     *                             <code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or
     *                             <code>ResultSet.TYPE_SCROLL_SENSITIVE</code>
     * @param resultSetConcurrency one of the following <code>ResultSet</code> constants:
     *                             <code>ResultSet.CONCUR_READ_ONLY</code> or
     *                             <code>ResultSet.CONCUR_UPDATABLE</code>
     * @param resultSetHoldability one of the following <code>ResultSet</code> constants:
     *                             <code>ResultSet.HOLD_CURSORS_OVER_COMMIT</code> or
     *                             <code>ResultSet.CLOSE_CURSORS_AT_COMMIT</code>
     * @return a new <code>CallableStatement</code> object, containing the
     * pre-compiled SQL statement, that will generate
     * <code>ResultSet</code> objects with the given type, concurrency,
     * and holdability
     * @throws SQLException if a database access error occurs, this method is called
     *                      on a closed connection or the given parameters are not
     *                      <code>ResultSet</code> constants indicating type,
     *                      concurrency, and holdability
     * @see ResultSet
     * @since 1.4
     */
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
                                         int resultSetHoldability) throws SQLException {
        return currentConnection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    /**
     * Creates a default <code>PreparedStatement</code> object that has the
     * capability to retrieve auto-generated keys. The given constant tells the
     * driver whether it should make auto-generated keys available for
     * retrieval. This parameter is ignored if the SQL statement is not an
     * <code>INSERT</code> statement, or an SQL statement able to return
     * auto-generated keys (the list of such statements is vendor-specific).
     * <p>
     * <B>Note:</B> This method is optimized for handling parametric SQL
     * statements that benefit from precompilation. If the driver supports
     * precompilation, the method <code>prepareStatement</code> will send the
     * statement to the database for precompilation. Some drivers may not
     * support precompilation. In this case, the statement may not be sent to
     * the database until the <code>PreparedStatement</code> object is executed.
     * This has no direct effect on users; however, it does affect which methods
     * throw certain SQLExceptions.
     * <p>
     * Result sets created using the returned <code>PreparedStatement</code>
     * object will by default be type <code>TYPE_FORWARD_ONLY</code> and have a
     * concurrency level of <code>CONCUR_READ_ONLY</code>. The holdability of
     * the created result sets can be determined by calling
     * {@link #getHoldability}.
     *
     * @param sql               an SQL statement that may contain one or more '?' IN parameter
     *                          placeholders
     * @param autoGeneratedKeys a flag indicating whether auto-generated keys should be
     *                          returned; one of <code>Statement.RETURN_GENERATED_KEYS</code>
     *                          or <code>Statement.NO_GENERATED_KEYS</code>
     * @return a new <code>PreparedStatement</code> object, containing the
     * pre-compiled SQL statement, that will have the capability of
     * returning auto-generated keys
     * @throws SQLException if a database access error occurs, this method is called
     *                      on a closed connection or the given parameter is not a
     *                      <code>Statement</code> constant indicating whether
     *                      auto-generated keys should be returned
     * @since 1.4
     */
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return new PreparedStatementWrapper(currentConnection.prepareStatement(sql, autoGeneratedKeys));
    }

    /**
     * Creates a default <code>PreparedStatement</code> object capable of
     * returning the auto-generated keys designated by the given array. This
     * array contains the indexes of the columns in the target table that
     * contain the auto-generated keys that should be made available. The driver
     * will ignore the array if the SQL statement is not an <code>INSERT</code>
     * statement, or an SQL statement able to return auto-generated keys (the
     * list of such statements is vendor-specific).
     * <p>
     * An SQL statement with or without IN parameters can be pre-compiled and
     * stored in a <code>PreparedStatement</code> object. This object can then
     * be used to efficiently execute this statement multiple times.
     * <p>
     * <B>Note:</B> This method is optimized for handling parametric SQL
     * statements that benefit from precompilation. If the driver supports
     * precompilation, the method <code>prepareStatement</code> will send the
     * statement to the database for precompilation. Some drivers may not
     * support precompilation. In this case, the statement may not be sent to
     * the database until the <code>PreparedStatement</code> object is executed.
     * This has no direct effect on users; however, it does affect which methods
     * throw certain SQLExceptions.
     * <p>
     * Result sets created using the returned <code>PreparedStatement</code>
     * object will by default be type <code>TYPE_FORWARD_ONLY</code> and have a
     * concurrency level of <code>CONCUR_READ_ONLY</code>. The holdability of
     * the created result sets can be determined by calling
     * {@link #getHoldability}.
     *
     * @param sql           an SQL statement that may contain one or more '?' IN parameter
     *                      placeholders
     * @param columnIndexes an array of column indexes indicating the columns that should
     *                      be returned from the inserted row or rows
     * @return a new <code>PreparedStatement</code> object, containing the
     * pre-compiled statement, that is capable of returning the
     * auto-generated keys designated by the given array of column
     * indexes
     * @throws SQLException if a database access error occurs or this method is called
     *                      on a closed connection
     * @since 1.4
     */
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return new PreparedStatementWrapper(currentConnection.prepareStatement(sql, columnIndexes));
    }

    /**
     * Creates a default <code>PreparedStatement</code> object capable of
     * returning the auto-generated keys designated by the given array. This
     * array contains the names of the columns in the target table that contain
     * the auto-generated keys that should be returned. The driver will ignore
     * the array if the SQL statement is not an <code>INSERT</code> statement,
     * or an SQL statement able to return auto-generated keys (the list of such
     * statements is vendor-specific).
     * <p>
     * An SQL statement with or without IN parameters can be pre-compiled and
     * stored in a <code>PreparedStatement</code> object. This object can then
     * be used to efficiently execute this statement multiple times.
     * <p>
     * <B>Note:</B> This method is optimized for handling parametric SQL
     * statements that benefit from precompilation. If the driver supports
     * precompilation, the method <code>prepareStatement</code> will send the
     * statement to the database for precompilation. Some drivers may not
     * support precompilation. In this case, the statement may not be sent to
     * the database until the <code>PreparedStatement</code> object is executed.
     * This has no direct effect on users; however, it does affect which methods
     * throw certain SQLExceptions.
     * <p>
     * Result sets created using the returned <code>PreparedStatement</code>
     * object will by default be type <code>TYPE_FORWARD_ONLY</code> and have a
     * concurrency level of <code>CONCUR_READ_ONLY</code>. The holdability of
     * the created result sets can be determined by calling
     * {@link #getHoldability}.
     *
     * @param sql         an SQL statement that may contain one or more '?' IN parameter
     *                    placeholders
     * @param columnNames an array of column names indicating the columns that should be
     *                    returned from the inserted row or rows
     * @return a new <code>PreparedStatement</code> object, containing the
     * pre-compiled statement, that is capable of returning the
     * auto-generated keys designated by the given array of column names
     * @throws SQLException if a database access error occurs or this method is called
     *                      on a closed connection
     * @since 1.4
     */
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return new PreparedStatementWrapper(currentConnection.prepareStatement(sql, columnNames));
    }

    /**
     * Constructs an object that implements the <code>Clob</code> interface. The
     * object returned initially contains no data. The
     * <code>setAsciiStream</code>, <code>setCharacterStream</code> and
     * <code>setString</code> methods of the <code>Clob</code> interface may be
     * used to add data to the <code>Clob</code>.
     *
     * @return An object that implements the <code>Clob</code> interface
     * @throws SQLException if an object that implements the <code>Clob</code> interface
     *                      can not be constructed, this method is called on a closed
     *                      connection or a database access error occurs.
     * @since 1.6
     */
    public Clob createClob() throws SQLException {
        return currentConnection.createClob();
    }

    /**
     * Constructs an object that implements the <code>Blob</code> interface. The
     * object returned initially contains no data. The
     * <code>setBinaryStream</code> and <code>setBytes</code> methods of the
     * <code>Blob</code> interface may be used to add data to the
     * <code>Blob</code>.
     *
     * @return An object that implements the <code>Blob</code> interface
     * @throws SQLException if an object that implements the <code>Blob</code> interface
     *                      can not be constructed, this method is called on a closed
     *                      connection or a database access error occurs.
     * @since 1.6
     */
    public Blob createBlob() throws SQLException {
        return currentConnection.createBlob();
    }

    /**
     * Constructs an object that implements the <code>NClob</code> interface.
     * The object returned initially contains no data. The
     * <code>setAsciiStream</code>, <code>setCharacterStream</code> and
     * <code>setString</code> methods of the <code>NClob</code> interface may be
     * used to add data to the <code>NClob</code>.
     *
     * @return An object that implements the <code>NClob</code> interface
     * @throws SQLException if an object that implements the <code>NClob</code> interface
     *                      can not be constructed, this method is called on a closed
     *                      connection or a database access error occurs.
     * @since 1.6
     */
    public NClob createNClob() throws SQLException {
        return currentConnection.createNClob();
    }

    /**
     * Constructs an object that implements the <code>SQLXML</code> interface.
     * The object returned initially contains no data. The
     * <code>createXmlStreamWriter</code> object and <code>setString</code>
     * method of the <code>SQLXML</code> interface may be used to add data to
     * the <code>SQLXML</code> object.
     *
     * @return An object that implements the <code>SQLXML</code> interface
     * @throws SQLException if an object that implements the <code>SQLXML</code>
     *                      interface can not be constructed, this method is called on a
     *                      closed connection or a database access error occurs.
     * @since 1.6
     */
    public SQLXML createSQLXML() throws SQLException {
        return currentConnection.createSQLXML();
    }

    /**
     * Returns true if the connection has not been closed and is still valid.
     * The driver shall submit a query on the connection or use some other
     * mechanism that positively verifies the connection is still valid when
     * this method is called.
     * <p>
     * The query submitted by the driver to validate the connection shall be
     * executed in the context of the current transaction.
     *
     * @param timeout - The time in seconds to wait for the database operation used
     *                to validate the connection to complete. If the timeout period
     *                expires before the operation completes, this method returns
     *                false. A value of 0 indicates a timeout is not applied to the
     *                database operation.
     *                <p>
     * @return true if the connection is valid, false otherwise
     * @throws SQLException if the value supplied for <code>timeout</code> is less
     *                      then 0
     * @see DatabaseMetaData#getClientInfoProperties
     * @since 1.6
     * <p>
     */
    public boolean isValid(int timeout) throws SQLException {
        return currentConnection.isValid(timeout);
    }

    /**
     * Sets the value of the client info property specified by name to the value
     * specified by value.
     * <p>
     * Applications may use the
     * <code>DatabaseMetaData.getClientInfoProperties</code> method to determine
     * the client info properties supported by the driver and the maximum length
     * that may be specified for each property.
     * <p>
     * The driver stores the value specified in a suitable location in the
     * database. For example in a special register, session parameter, or system
     * table column. For efficiency the driver may defer setting the value in
     * the database until the next time a statement is executed or prepared.
     * Other than storing the client information in the appropriate place in the
     * database, these methods shall not alter the behavior of the connection in
     * anyway. The values supplied to these methods are used for accounting,
     * diagnostics and debugging purposes only.
     * <p>
     * The driver shall generate a warning if the client info name specified is
     * not recognized by the driver.
     * <p>
     * If the value specified to this method is greater than the maximum length
     * for the property the driver may either truncate the value and generate a
     * warning or generate a <code>SQLClientInfoException</code>. If the driver
     * generates a <code>SQLClientInfoException</code>, the value specified was
     * not set on the connection.
     * <p>
     * The following are standard client info properties. Drivers are not
     * required to support these properties however if the driver supports a
     * client info property that can be described by one of the standard
     * properties, the standard property name should be used.
     * <p>
     * <ul>
     * <li>ApplicationName - The name of the application currently utilizing the
     * connection</li>
     * <li>ClientUser - The name of the user that the application using the
     * connection is performing work for. This may not be the same as the user
     * name that was used in establishing the connection.</li>
     * <li>ClientHostname - The hostname of the computer the application using
     * the connection is running on.</li>
     * </ul>
     * <p>
     *
     * @param name  The name of the client info property to set
     * @param value The value to set the client info property to. If the value is
     *              null, the current value of the specified property is cleared.
     *              <p>
     * @throws SQLClientInfoException if the database server returns an error while setting the
     *                                client info value on the database server or this method is
     *                                called on a closed connection
     *                                <p>
     * @since 1.6
     */
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        currentConnection.setClientInfo(name, value);
    }

    /**
     * Sets the value of the connection's client info properties. The
     * <code>Properties</code> object contains the names and values of the
     * client info properties to be set. The set of client info properties
     * contained in the properties list replaces the current set of client info
     * properties on the connection. If a property that is currently set on the
     * connection is not present in the properties list, that property is
     * cleared. Specifying an empty properties list will clear all of the
     * properties on the connection. See
     * <code>setClientInfo (String, String)</code> for more information.
     * <p>
     * If an error occurs in setting any of the client info properties, a
     * <code>SQLClientInfoException</code> is thrown. The
     * <code>SQLClientInfoException</code> contains information indicating which
     * client info properties were not set. The state of the client information
     * is unknown because some databases do not allow multiple client info
     * properties to be set atomically. For those databases, one or more
     * properties may have been set before the error occurred.
     * <p>
     *
     * @param properties the list of client info properties to set
     *                   <p>
     * @throws SQLClientInfoException if the database server returns an error while setting the
     *                                clientInfo values on the database server or this method is
     *                                called on a closed connection
     *                                <p>
     * @see Connection#setClientInfo(String, String)
     * setClientInfo(String, String)
     * @since 1.6
     * <p>
     */
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        currentConnection.setClientInfo(properties);
    }

    /**
     * Returns the value of the client info property specified by name. This
     * method may return null if the specified client info property has not been
     * set and does not have a default value. This method will also return null
     * if the specified client info property name is not supported by the
     * driver.
     * <p>
     * Applications may use the
     * <code>DatabaseMetaData.getClientInfoProperties</code> method to determine
     * the client info properties supported by the driver.
     * <p>
     *
     * @param name The name of the client info property to retrieve
     *             <p>
     * @return The value of the client info property specified
     * <p>
     * @throws SQLException if the database server returns an error when fetching the
     *                      client info value from the database or this method is called
     *                      on a closed connection
     *                      <p>
     * @see DatabaseMetaData#getClientInfoProperties
     * @since 1.6
     * <p>
     */
    public String getClientInfo(String name) throws SQLException {
        return currentConnection.getClientInfo(name);
    }

    /**
     * Returns a list containing the name and current value of each client info
     * property supported by the driver. The value of a client info property may
     * be null if the property has not been set and does not have a default
     * value.
     * <p>
     *
     * @return A <code>Properties</code> object that contains the name and
     * current value of each of the client info properties supported by
     * the driver.
     * <p>
     * @throws SQLException if the database server returns an error when fetching the
     *                      client info values from the database or this method is called
     *                      on a closed connection
     *                      <p>
     * @since 1.6
     */
    public Properties getClientInfo() throws SQLException {
        return currentConnection.getClientInfo();
    }

    /**
     * Factory method for creating Array objects.
     * <p>
     * <b>Note: </b>When <code>createArrayOf</code> is used to create an array
     * object that maps to a primitive data type, then it is
     * implementation-defined whether the <code>Array</code> object is an array
     * of that primitive data type or an array of <code>Object</code>.
     * <p>
     * <b>Note: </b>The JDBC driver is responsible for mapping the elements
     * <code>Object</code> array to the default JDBC SQL type defined in
     * java.sql.Types for the given class of <code>Object</code>. The default
     * mapping is specified in Appendix B of the JDBC specification. If the
     * resulting JDBC type is not the appropriate type for the given typeName
     * then it is implementation defined whether an <code>SQLException</code> is
     * thrown or the driver supports the resulting conversion.
     *
     * @param typeName the SQL name of the type the elements of the array map to. The
     *                 typeName is a database-specific name which may be the name of
     *                 a built-in type, a user-defined type or a standard SQL type
     *                 supported by this database. This is the value returned by
     *                 <code>Array.getBaseTypeName</code>
     * @param elements the elements that populate the returned object
     * @return an Array object whose elements map to the specified SQL type
     * @throws SQLException if a database error occurs, the JDBC type is not appropriate
     *                      for the typeName and the conversion is not supported, the
     *                      typeName is null or this method is called on a closed
     *                      connection
     * @since 1.6
     */
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return currentConnection.createArrayOf(typeName, elements);
    }

    /**
     * Factory method for creating Struct objects.
     *
     * @param typeName   the SQL type name of the SQL structured type that this
     *                   <code>Struct</code> object maps to. The typeName is the name
     *                   of a user-defined type that has been defined for this
     *                   database. It is the value returned by
     *                   <code>Struct.getSQLTypeName</code>.
     * @param attributes the attributes that populate the returned object
     * @return a Struct object that maps to the given SQL type and is populated
     * with the given attributes
     * @throws SQLException if a database error occurs, the typeName is null or this
     *                      method is called on a closed connection
     * @since 1.6
     */
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return currentConnection.createStruct(typeName, attributes);
    }

    /**
     * 设置表空间
     *
     * @param schema 表空间
     * @throws SQLException SQL异常
     */
    @Override
    public void setSchema(String schema) throws SQLException {

    }

    /**
     * 获取表空间
     *
     * @return 表空间
     * @throws SQLException SQL异常
     */
    @Override
    public String getSchema() throws SQLException {
        return null;
    }

    /**
     * 放弃执行
     *
     * @param executor 数据库任务执行者
     * @throws SQLException SQL异常
     */
    @Override
    public void abort(Executor executor) throws SQLException {

    }

    /**
     * 设置连接超时时间
     *
     * @param executor     任务执行者
     * @param milliseconds 连接超时时间
     * @throws SQLException SQL异常
     */
    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {

    }

    /**
     * 获取连接超时时间
     *
     * @return 连接超时时间
     * @throws SQLException SQL异常
     */
    @Override
    public int getNetworkTimeout() throws SQLException {
        return 0;
    }

}
