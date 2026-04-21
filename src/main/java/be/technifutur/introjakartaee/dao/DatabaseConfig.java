package be.technifutur.introjakartaee.dao;

import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseConfig {

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver PostgreSQL introuvable", e);
        }

        Properties props = new Properties();
        try (InputStream in = DatabaseConfig.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new RuntimeException("db.properties introuvable dans le classpath");
            }
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lecture db.properties", e);
        }

        URL      = props.getProperty("db.url");
        USER     = props.getProperty("db.user");
        PASSWORD = props.getProperty("db.password");

        try {
            initDatabase();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'initialisation de la base de données : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void createTableJeux() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS jeux (
                    id          INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                    titre       VARCHAR(255) NOT NULL UNIQUE,
                    annee       INT          NOT NULL,
                    description TEXT,
                    image_url   VARCHAR(500)
                )
                """;
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    public static void createTableUser() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                    id       INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                    email    VARCHAR(255) NOT NULL UNIQUE,
                    password VARCHAR(255) NOT NULL,
                    role     VARCHAR(50)  NOT NULL DEFAULT 'USER'
                )
                """;
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    public static void createTables() throws SQLException {
        createTableJeux();
        createTableUser();
    }

    public static void insertSampleData() throws SQLException {
        String insertJeux = """
                INSERT INTO jeux (titre, annee, description, image_url) VALUES
                    ('Dark Souls',       2011, 'Un RPG d''action exigeant se déroulant dans le royaume maudit de Lordran.',                '/images/DarkSouls.jpg'),
                    ('Dark Souls II',    2014, 'La suite spirituelle de Dark Souls, avec de nouveaux royaumes et défis.',                  '/images/DarkSouls2.jpg'),
                    ('Dark Souls III',   2016, 'Le chapitre final de la saga Dark Souls, plus rapide et spectaculaire.',                   '/images/DarkSouls3.jpg'),
                    ('Bloodborne',       2015, 'Un action-RPG gothique et lovecraftien dans la ville maudite de Yharnam.',                 '/images/bloodborne.jpg'),
                    ('Sekiro',           2019, 'Un samouraï du Japon féodal affronte des dieux et des démons dans une quête de rédemption.', '/images/Sekiro.jpg'),
                    ('Elden Ring',       2022, 'Un monde ouvert entre les dieux déchus et les terres intermédiaires de l''Entre-terre.',   '/images/eldenring.jpg')
                ON CONFLICT DO NOTHING
                """;

        String adminHash = BCrypt.hashpw("admin1234", BCrypt.gensalt());
        String userHash  = BCrypt.hashpw("user1234",  BCrypt.gensalt());

        String insertUsers = "INSERT INTO users (email, password, role) VALUES " +
                "('admin@fromsoftware.com', '" + adminHash + "', 'ADMIN'), " +
                "('user@fromsoftware.com',  '" + userHash  + "', 'USER')  " +
                "ON CONFLICT (email) DO NOTHING";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(insertJeux);
            stmt.execute(insertUsers);
        }
    }

    public static void initDatabase() throws SQLException {
        createTables();
        insertSampleData();
    }
}
