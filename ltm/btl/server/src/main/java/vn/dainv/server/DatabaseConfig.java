package vn.dainv.server;

public record DatabaseConfig(String host, int port, String database, String username, String password) {
    private static final String DEFAULT_HOST = "14.224.166.195";
    private static final int DEFAULT_PORT = 3300;
    private static final String DEFAULT_DATABASE = "lap_trinh_mang";
    private static final String DEFAULT_USERNAME = "root";
    private static final String DEFAULT_PASSWORD = "rootpass_dev";

    public static DatabaseConfig fromEnv() {
        String host = getOrDefault("DB_HOST", DEFAULT_HOST);
        int port = parsePort(getOrDefault("DB_PORT", String.valueOf(DEFAULT_PORT)));
        String database = getOrDefault("DB_NAME", DEFAULT_DATABASE);
        String username = getOrDefault("DB_USER", DEFAULT_USERNAME);
        String password = getOrDefault("DB_PASSWORD", DEFAULT_PASSWORD);
        return new DatabaseConfig(host, port, database, username, password);
    }

    public String jdbcUrl() {
        return String.format(
                "jdbc:mysql://%s:%d/%s?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                host, port, database);
    }

    private static String getOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return value == null || value.isBlank() ? defaultValue : value.trim();
    }

    private static int parsePort(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ex) {
            return DEFAULT_PORT;
        }
    }
}
