package repository;

import domain.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class RepositoryDBUsers implements Repository<User> {
    private final String urlDb;
    private final String usernameDb;
    private final String passwdDb;


    public RepositoryDBUsers(String urlDb, String usernameDb, String passwdDb) {
        this.urlDb = urlDb;
        this.usernameDb = usernameDb;
        this.passwdDb = passwdDb;
    }

    @Override
    public User add(User entity) throws RepositoryException {

        if (findUsername(entity.getUsername()).isPresent())
            throw new RepositoryException("Entity exists!");
        String sql = "insert into users(username,passwd,firstname,lastname,picturepath) values (?,?,?,?,?)";
        try(Connection connection = DriverManager.getConnection(urlDb, usernameDb, passwdDb);
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setString(2, entity.getPasswd());
            preparedStatement.setString(3, entity.getFirstname());
            preparedStatement.setString(4, entity.getLastname());
            preparedStatement.setString(5, entity.getPictureReference());
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            throw  new RepositoryException(e.getMessage());
        }
        return entity;

    }

    @Override
    public User update(User entity) throws RepositoryException {

        String sql = "UPDATE users SET username=?,passwd=?,firstname=?, lastname=? where users.id=?";

        try (Connection connection = DriverManager.getConnection(urlDb, usernameDb, passwdDb);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setString(2, entity.getPasswd());
            preparedStatement.setString(3, entity.getFirstname());
            preparedStatement.setString(4, entity.getLastname());
            preparedStatement.setObject(5, entity.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return entity;
    }

    @Override
    public User delete(User entity) throws RepositoryException {
        if (find(entity).isEmpty())
            throw new RepositoryException("Nonexistent entity");

        String sql = "delete from Users where Id=?";
        try (Connection connection = DriverManager.getConnection(urlDb, usernameDb, passwdDb);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, entity.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public Optional<User> find(User entity) {
        User user = new User();
        var _id = entity.getId();
        String sql = "SELECT * from users where Id=?";

        try (Connection connection = DriverManager.getConnection(urlDb, usernameDb, passwdDb);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, _id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            user.setId(resultSet.getObject("id", UUID.class));
            user.setUsername(resultSet.getString("username"));
            user.setPasswd(resultSet.getString("passwd"));
            user.setFirstname(resultSet.getString("firstname"));
            user.setLastname(resultSet.getString("lastname"));
            user.setPictureReference(resultSet.getString("picturepath"));
        } catch (SQLException e) {
            return Optional.empty();
        }

        return Optional.of(user);

    }

    public Optional<User> findUsername(String username) {
        User user = new User();
        String sql = "SELECT * from Users where Username=?";

        try (Connection connection = DriverManager.getConnection(urlDb, usernameDb, passwdDb);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            user.setId(resultSet.getObject("id", UUID.class));
            user.setUsername(resultSet.getString("username"));
            user.setPasswd(resultSet.getString("passwd"));
            user.setFirstname(resultSet.getString("firstname"));
            user.setLastname(resultSet.getString("lastname"));
            user.setPictureReference(resultSet.getString("picturepath"));

        } catch (SQLException e) {
            return Optional.empty();

        }
        return Optional.of(user);
    }

    @Override
    public List<User> getAll() throws RepositoryException {
        List<User> usersSet = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(urlDb, usernameDb, passwdDb);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String userName = resultSet.getString("username");
                String passwd = resultSet.getString("passwd");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                User user = new User(userName, passwd, firstName, lastName);
                user.setId(resultSet.getObject("id", UUID.class));
                user.setPictureReference(resultSet.getString("picturePath"));
                usersSet.add(user);
            }

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return usersSet;
    }

    @Override
    public int size() throws RepositoryException {
        int dim = 0;
        try (Connection connection = DriverManager.getConnection(urlDb, usernameDb, passwdDb);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) as total FROM users");
            ResultSet resultSet = preparedStatement.executeQuery()) {

            resultSet.next();
            dim = resultSet.getInt("total");

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return dim;
    }


}
