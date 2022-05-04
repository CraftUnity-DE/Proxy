package de.verklickt.proxy.utils;

import lombok.Getter;
import lombok.NonNull;
import lombok.var;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
public class Datenbank {

    private String username;
    private String password;
    private String database;
    private String table;
    private String host;
    private int port;

    private Connection connection;
    private String url;
    private Logger logger;
    private DatenbankType datenbankType;

    private URLType[] urltypes;

    public Datenbank setUser(@NonNull String username, @NonNull String password) {
        this.username = username;
        this.password = password;
        return this;
    }

    public Datenbank setTable(@NonNull String database, @NonNull String table) {
        this.database = database;
        this.table = table;
        return this;
    }

    public Datenbank setTable(@NonNull String database) {
        this.database = database;
        return this;
    }

    public Datenbank setServer(@NonNull String host, int port) {
        this.host = host;
        this.port = port;
        for (DatenbankType type : DatenbankType.values()) {
            if (type.getPort() != port) continue;
            this.datenbankType = type;
        }
        return this;
    }

    public Datenbank setServer(@NonNull String host, @NonNull DatenbankType type) {
        this.host = host;
        this.port = type.getPort();
        this.datenbankType = type;
        return this;
    }

    public Datenbank setExtra(@NonNull URLType... types) {
        this.urltypes = types;
        return this;
    }

    public Datenbank buildURL() {
        this.url = "jdbc:" + this.datenbankType.name().toLowerCase() + "://" + this.host + ":" + this.port + "/" + this.database;
        if (this.urltypes == null) return this;
        this.url += "?";

        for (URLType type : this.urltypes) {
            var chars = "";
            if (!this.url.endsWith("?")) chars = "&";
            this.url += chars + type.getKey() + "=true";
        }

        return this;
    }

    public boolean isConnected() {
        return this.connection != null;
    }

    private void connectHelper() {
        this.logger = Logger.getGlobal();
        if (this.url == null) this.buildURL();
    }

    public void connect(@NonNull Runnable callback) {
        this.connectHelper();
            try {
                this.logger.log(Level.INFO, "Connecting... ");
                if (this.url == null) {
                    this.logger.log(Level.INFO, "The connection to the database is failed! URL: Null");
                    return;
                }

                if (this.isConnected()) {
                    this.logger.log(Level.INFO, "The connection to the database is already!");
                    return;
                }

                this.connection = DriverManager.getConnection(this.url, this.username, this.password);
                callback.run();
                this.logger.log(Level.INFO, "The Connection with Datenbank was connected!");
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
    }

    public void connect() {
        this.connectHelper();
            try {
                if (this.url == null) {
                    this.logger.log(Level.INFO, "The connection to the database is failed! URL: Null");
                    return;
                }

                if (this.isConnected()) {
                    this.logger.log(Level.INFO, "The connection to the database is already!");
                    return;
                }

                this.connection = DriverManager.getConnection(this.url, this.username, this.password);
                this.logger.log(Level.INFO, "The Connection with Datenbank was connected!");
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
    }

    public void close() {
            try {
                if (!this.isConnected()) {
                    this.logger.log(Level.INFO, "The connection to the database is already closed!");
                    return;
                }

                this.connection.close();
                this.connection = null;
                this.logger.log(Level.INFO, "The connection to the database has been closed!");
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
    }

    @Getter
    public enum DatenbankType {
        MYSQL(3306), POSTGRESQL(5432), MONGODB(27017);
        private int port;

        private DatenbankType(int port) {
            this.port = port;
        }
    }

    public enum DatenType {
        TINYINT, SMALLINT, MEDIUMINT, INTEGER, BIGINT,
        FLOAT, DOUBLE, REAL, DECIMAL, NUMERIC,
        DATE, DATETIME, TIMESTAMP, TIME, YEAR,
        CHAR, VARCHAR, BINARY, BLOB, TEXT, ENUM, SET;
    }

    @Getter
    public enum URLType {
        AUTO_RECONNECT("autoReconnect"),
        USE_SSL("useSSL"),
        TRUST_SERVER("trustServerCertificate"),
        INTEGRATED_SECURITY("integratedSecurity");
        private String key;

        private URLType(String key) {
            this.key = key;
        }
    }

    @Getter
    public static class DatenbankData {

        private String table;
        private List<String> arguments;
        private Connection connection;
        private Datenbank datenbank;

        public DatenbankData(Datenbank datenbank, String table) {
            this.table = table;
            this.arguments = new ArrayList<>();
            this.connection = datenbank.getConnection();
            this.datenbank = datenbank;
        }

        public DatenbankData setArgument(String key, DatenType type, int value) {
            var arg = key + " " + type.name();
            if (value >= 0) arg += "(" + value + ")";
            this.arguments.add(arg);
            return this;
        }

        public boolean exists(@NonNull String valueKey, @NonNull Object value) {
            if (!this.datenbank.isConnected()) return false;
            try {
                var statement = this.connection.prepareStatement("SELECT * FROM `" + this.table + "` WHERE `" + valueKey + "` LIKE \"" + value + "\";");
                if (statement.executeQuery().next()) {
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        public void update(@NonNull String key, @NonNull Object value, @NonNull String keyValue, @NonNull Object valueKey) {
                if (!this.exists(keyValue, valueKey)) return;
                try {
                    connection.prepareStatement("UPDATE `" + this.table + "` SET `" + key + "`='" + value + "' WHERE `" + keyValue + "` LIKE \"" + valueKey + "\";").executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }

        public void deleteObject(String valueKey, Object value) {
            if (!this.datenbank.isConnected()) return;
                try {
                    this.connection.prepareStatement("DELETE FROM `" + this.table + "` WHERE `" + valueKey + "` LIKE \"" + value + "\";").executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }

        public void createObject(@NonNull Object... keys) {
            if (!this.datenbank.isConnected()) return;
            if (keys.length != this.arguments.size()) return;
                try {
                    var argString = new StringBuilder();
                    var keyString = new StringBuilder();
                    var size = 0;
                    var argSize = this.arguments.size() - 1;

                    for (String argValues : this.arguments) {
                        var symbole = "";
                        if (size != argSize) symbole += ", ";
                        argString.append("`" + argValues.split(" ")[0] + "`" + symbole);
                        keyString.append("'" + keys[size] + "'" + symbole);
                        size++;
                    }

                    this.connection.prepareStatement("INSERT INTO `" + this.table + "` (" + argString.toString() + ") VALUES (" + keyString.toString() + ");").executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }

        public void createTable() {
            if (!this.datenbank.isConnected()) return;
            if (this.arguments.isEmpty()) return;
                var builder = new StringBuilder();
                var size = 0;
                var argSize = this.arguments.size() - 1;

                for (String args : this.arguments) {
                    var symbole = "";
                    if (size != argSize) symbole += ", ";
                    builder.append(args + symbole);
                    size++;
                }

                try {
                    this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + this.table + " (" + builder.toString() + ");").executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }

        public Object getObject(@NonNull String key, @NonNull String valueKey, @NonNull Object value) {
            try {
                var statement = this.connection.prepareStatement("SELECT `" + key + "` FROM `" + this.table + "` WHERE `" + valueKey + "` LIKE \"" + value + "\";");
                var resultValue = statement.executeQuery();
                if (resultValue.next()) {
                    return resultValue.getObject(key);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        public boolean getBoolean(@NonNull String key, @NonNull String valueKey, @NonNull Object value) {
            try {
                var statement = this.connection.prepareStatement("SELECT `" + key + "` FROM `" + this.table + "` WHERE `" + valueKey + "` LIKE \"" + value + "\";");
                var resultValue = statement.executeQuery();
                if (resultValue.next()) {
                    return resultValue.getBoolean(key);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        public String getString(@NonNull String key, @NonNull String valueKey, @NonNull Object value) {
            try {
                var statement = this.connection.prepareStatement("SELECT `" + key + "` FROM `" + this.table + "` WHERE `" + valueKey + "` LIKE \"" + value + "\";");
                var resultValue = statement.executeQuery();
                if (resultValue.next()) {
                    return resultValue.getString(key);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return "";
        }

        public int getInteger(@NonNull String key, @NonNull String valueKey, @NonNull Object value) {
            try {
                var statement = this.connection.prepareStatement("SELECT `" + key + "` FROM `" + this.table + "` WHERE `" + valueKey + "` LIKE \"" + value + "\";");
                var resultValue = statement.executeQuery();
                if (resultValue.next()) {
                    return resultValue.getInt(key);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        }

        public float getFloat(@NonNull String key, @NonNull String valueKey, @NonNull Object value) {
            try {
                var statement = this.connection.prepareStatement("SELECT `" + key + "` FROM `" + this.table + "` WHERE `" + valueKey + "` LIKE \"" + value + "\";");
                var resultValue = statement.executeQuery();
                if (resultValue.next()) {
                    return resultValue.getFloat(key);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        }

        public double getDouble(@NonNull String key, @NonNull String valueKey, @NonNull Object value) {
            try {
                var statement = this.connection.prepareStatement("SELECT `" + key + "` FROM `" + this.table + "` WHERE `" + valueKey + "` LIKE \"" + value + "\";");
                var resultValue = statement.executeQuery();
                if (resultValue.next()) {
                    return resultValue.getDouble(key);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        }

        public long getLong(@NonNull String key, @NonNull String valueKey, @NonNull Object value) {
            try {
                var statement = this.connection.prepareStatement("SELECT `" + key + "` FROM `" + this.table + "` WHERE `" + valueKey + "` LIKE \"" + value + "\";");
                var resultValue = statement.executeQuery();
                if (resultValue.next()) {
                    return resultValue.getLong(key);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        }

        public ResultSet getResult(@NonNull String valueKey, @NonNull Object value) {
            try {
                return this.connection.prepareStatement("SELECT * FROM `" + this.table + "` WHERE `" + valueKey + "` LIKE \"" + value + "\";").executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}