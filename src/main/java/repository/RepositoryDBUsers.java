package repository;

import domain.User;
import repository.Repository;
import repository.RepositoryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
        if(find(entity)!=null)
            throw new RepositoryException("Existent entity");
        String sql="insert into users(id,user_name,passwd,first_name,last_name) values (?,?,?,?,?)";
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setObject(1, entity.getId());
            preparedStatement.setString(2, entity.getUserName());
            preparedStatement.setString(3, entity.getPasswd());
            preparedStatement.setString(4, entity.getFirstName());
            preparedStatement.setString(5, entity.getLastName());
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return entity;

    }

    @Override
    public User update(User entity) throws RepositoryException {

        String sql="UPDATE users set user_name=?,passwd=?,first_name=?,last_name=? where id=?";

        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setString(1, entity.getUserName());
            preparedStatement.setString(2, entity.getPasswd());
            preparedStatement.setString(3, entity.getFirstName());
            preparedStatement.setString(4, entity.getLastName());
            preparedStatement.setObject(5, entity.getId());
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return entity;
    }
    @Override
    public User delete(User entity) throws RepositoryException {
        if(find(entity)==null)
            throw new RepositoryException("Nonexistent entity");

        String sql="delete from users where id=?";
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setObject(1, entity.getId());
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public User find(User entity) {
        User user=new User();
        var _id=entity.getId();
        String sql="SELECT * from users where id=?";

        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setObject(1, _id);
            ResultSet resultSet= preparedStatement.executeQuery();
            resultSet.next();
            user.setId(resultSet.getObject("id", UUID.class));
            user.setUserName(resultSet.getString("user_name"));
            user.setPasswd(resultSet.getString("passwd"));
            user.setFirstName(resultSet.getString("first_name"));
            user.setLastName(resultSet.getString("last_name"));
        }catch (SQLException e){
            return null;
        }
        return user;

    }

    public User findUsername(String username){
        User user=new User();
        String sql="SELECT * from users where user_name=?";

        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setObject(1, username);
            ResultSet resultSet= preparedStatement.executeQuery();

            resultSet.next();
            user.setId(resultSet.getObject("id", UUID.class));
            user.setUserName(resultSet.getString("user_name"));
            user.setPasswd(resultSet.getString("passwd"));
            user.setFirstName(resultSet.getString("first_name"));
            user.setLastName(resultSet.getString("last_name"));

        }catch (SQLException e){
           return null;

        }
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> usersSet= new ArrayList<>();
        try( Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
             PreparedStatement preparedStatement= connection.prepareStatement("SELECT * FROM users");
             ResultSet resultSet= preparedStatement.executeQuery()){
             while(resultSet.next()){
                UUID id=UUID.fromString(resultSet.getString("id"));
                String userName=resultSet.getString("user_name");
                String passwd=resultSet.getString("passwd");
                String firstName=resultSet.getString("first_name");
                String lastName=resultSet.getString("last_name");
                User user=new User(userName,passwd,firstName,lastName);
                user.setId(id);
                usersSet.add(user);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return usersSet;
    }

    @Override
    public int size() {
        int dim=0;
        try( Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
             PreparedStatement preparedStatement= connection.prepareStatement("SELECT COUNT(*) as total FROM users");
             ResultSet resultSet= preparedStatement.executeQuery()){
             resultSet.next();
             dim=resultSet.getInt("total");

        }catch(SQLException e){
            e.printStackTrace();
        }
        return dim;
    }



}
