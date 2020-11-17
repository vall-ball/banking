package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.*;

public class DBWorking {

    public void createNewDatabase(String fileName) {
        String url = "jdbc:sqlite:" + fileName;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        try (Connection con = dataSource.getConnection()) {
            if (con != null) {
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void createTable(String dbName) {
        String url = "jdbc:sqlite:" + dbName;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                        "id INTEGER not null," +
                        "number text NOT NULL," +
                        "balance INTEGER DEFAULT 0," +
                        "pin text not null);");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCard(Card card, String dbName) {
        String url = "jdbc:sqlite:" + dbName;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                statement.executeUpdate("INSERT INTO card VALUES " +
                        "("+ card.id+", '" + card.cardNumber+ "', " + card.balance + ", '" + card.pin + "');");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Card getCard(String dbName, String cardNumber) {
        String url = "jdbc:sqlite:" + dbName;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery("SELECT * FROM card where number = '" + cardNumber + "';")) {
                    while (resultSet.next()) {
                        Card card = new Card();
                        card.id = resultSet.getInt("id");
                        card.cardNumber = resultSet.getString("number");
                        card.pin = resultSet.getString("pin");
                        card.balance = resultSet.getInt("balance");
                        return card;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateBalance(Card card, String dbName, int income) {
        String update = "UPDATE card SET balance = ? WHERE number = ?";
        String url = "jdbc:sqlite:" + dbName;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = con.prepareStatement(update)) {
                //System.out.println(income);
                //System.out.println(card.cardNumber);
                preparedStatement.setInt(1, card.balance + income);
                preparedStatement.setString(2, card.cardNumber);
                preparedStatement.executeUpdate();
                System.out.println("done");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void transfer(String dbName, Card sourceCard, Card destinationCard, int money) {
        String url = "jdbc:sqlite:" + dbName;;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        String outcome = "UPDATE card SET balance = ? WHERE number = ?";
        String income = "UPDATE card SET balance = ? WHERE number = ?";

        try (Connection con = dataSource.getConnection()) {

            // Disable auto-commit mode
            con.setAutoCommit(false);

            try (PreparedStatement outPreparedStatement = con.prepareStatement(outcome);
                 PreparedStatement inPreparedStatement = con.prepareStatement(income)) {

                outPreparedStatement.setInt(1, sourceCard.balance - money);
                outPreparedStatement.setString(2, sourceCard.cardNumber);
                outPreparedStatement.executeUpdate();

                inPreparedStatement.setInt(1, destinationCard.balance + money);
                inPreparedStatement.setString(2, destinationCard.cardNumber);

                inPreparedStatement.executeUpdate();


                con.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeAccount(String dbName, Card card) {
        String url = "jdbc:sqlite:" + dbName;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        String sql = "DELETE FROM card WHERE number = ?";
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
                 preparedStatement.setString(1, card.cardNumber);
                 preparedStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
