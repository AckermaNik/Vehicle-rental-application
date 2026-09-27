package database;

import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class DB_Connection {

    private static final Properties DOTENV = loadDotEnv();
    private static final String host = setting("DB_HOST", "localhost");
    private static final int port = configuredPort();
    private static final String databaseName = configuredDatabaseName();
    private static final String username = requiredSetting("DB_USERNAME");
    private static final String password = requiredSetting("DB_PASSWORD");

    /**
     * Load simple KEY=VALUE settings from the project's local .env file.
     * Process environment variables take precedence over values in that file.
     */
    private static Properties loadDotEnv() {
        Properties properties = new Properties();
        Path envFile = Paths.get(System.getProperty("user.dir"), ".env");

        if (!Files.isRegularFile(envFile)) {
            return properties;
        }

        try (BufferedReader reader = Files.newBufferedReader(envFile, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }

                int separator = trimmed.indexOf('=');
                if (separator <= 0) {
                    continue;
                }

                String key = trimmed.substring(0, separator).trim();
                String value = trimmed.substring(separator + 1).trim();
                if (value.length() >= 2
                        && ((value.startsWith("\"") && value.endsWith("\""))
                        || (value.startsWith("'") && value.endsWith("'")))) {
                    value = value.substring(1, value.length() - 1);
                }
                properties.setProperty(key, value);
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to read database configuration from .env", ex);
        }
        return properties;
    }

    private static String setting(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value == null) {
            value = DOTENV.getProperty(key);
        }
        return value == null ? defaultValue : value;
    }

    private static String requiredSetting(String key) {
        String value = setting(key, null);
        if (value == null) {
            throw new IllegalStateException("Missing required database setting: " + key
                    + ". Set it as an environment variable or in the local .env file.");
        }
        return value;
    }

    private static int configuredPort() {
        try {
            int configuredPort = Integer.parseInt(setting("DB_PORT", "3306"));
            if (configuredPort < 1 || configuredPort > 65535) {
                throw new NumberFormatException("port out of range");
            }
            return configuredPort;
        } catch (NumberFormatException ex) {
            throw new IllegalStateException("DB_PORT must be an integer from 1 to 65535", ex);
        }
    }

    private static String configuredDatabaseName() {
        String name = setting("DB_NAME", "HY360_2023");
        if (!name.matches("[A-Za-z0-9_]+")) {
            throw new IllegalStateException("DB_NAME may contain only letters, digits, and underscores");
        }
        return name;
    }

    public static String getDatabaseName() {
        return databaseName;
    }

    private static String getServerUrl() {
        return "jdbc:mysql://" + host + ":" + port;
    }

    /**
     * Attempts to establish a database connection
     *
     * @return a connection to the database
     * @throws SQLException
     * @throws java.lang.ClassNotFoundException
     */
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(getServerUrl() + "/" + databaseName, username, password);
    }
    
    public static Connection getInitialConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(getServerUrl(), username, password);
    }
    
      public static void printResults(ResultSet rs) throws SQLException {
        ResultSetMetaData metadata = rs.getMetaData();
        int columnCount = metadata.getColumnCount();
        
        String row = "";
        for (int i = 1; i <= columnCount; i++) {
            String name = metadata.getColumnName(i);
            String value = rs.getString(i);
            System.out.println(name + " " + value);
        }
    }
      
     public static String getResultsToJSON(ResultSet rs) throws SQLException {
       ResultSetMetaData metadata = rs.getMetaData();
        int columnCount = metadata.getColumnCount();
          JsonObject object = new JsonObject();
        
        
        String row = "";
        for (int i = 1; i <= columnCount; i++) {
            String name = metadata.getColumnName(i);
            String value = rs.getString(i);
            object.addProperty(name,value);
        }
        return object.toString();
    }
     
     
        
     public static JsonObject getResultsToJSONObject(ResultSet rs) throws SQLException {
       ResultSetMetaData metadata = rs.getMetaData();
         int columnCount = metadata.getColumnCount(); //posa gnwrismata exei h pleiada
         JsonObject object = new JsonObject();
        
        
        String row = "";
        for (int i = 1; i <= columnCount; i++) {
            String name = metadata.getColumnName(i);
            String value = rs.getString(i);
            object.addProperty(name,value);
        }
        return object;
    }
}
