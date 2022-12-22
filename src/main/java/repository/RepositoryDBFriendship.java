/*
package repository;

import domain.Friendship;
import repository.Repository;
import repository.RepositoryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RepositoryDBFriendship implements Repository<Friendship<UUID>> {
    private final String urlDb;
    private final String usernameDb;
    private final String passwdDb;

    public RepositoryDBFriendship(String urlDb, String usernameDb, String passwdDb) {
        this.urlDb = urlDb;
        this.usernameDb = usernameDb;
        this.passwdDb = passwdDb;
    }

    @Override
    public Friendship<UUID> add(Friendship<UUID> entity) throws RepositoryException {
        if(find(entity)!=null)
            throw new RepositoryException("Entity exists");
        String sql="insert into friendships(first_user,second_user) values (?,?)";
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setObject(1, entity.getFirst());
            preparedStatement.setObject(2, entity.getSecond());
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public Friendship<UUID> update(Friendship<UUID> entity) throws RepositoryException {
        return null;
    }

    @Override
    public Friendship<UUID> delete(Friendship<UUID> entity) throws RepositoryException {
        if(find(entity)==null)
            throw new RepositoryException("Nonexistent entity");
        String sql="delete from friendships where( first_user in(?) and second_user in (?))";
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
                preparedStatement.setObject(1, entity.getFirst());
                preparedStatement.setObject(2, entity.getSecond());
                preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public Friendship<UUID> find(Friendship<UUID> entity) {
        Friendship<UUID> friendship=new Friendship<>();

        String sql="SELECT * from friendships where( first_user in(?) and second_user in (?))";
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setObject(1, entity.getFirst());
            preparedStatement.setObject(2,  entity.getSecond());
            ResultSet resultSet= preparedStatement.executeQuery();
            resultSet.next();
            friendship.setFirst(resultSet.getObject("first_user",UUID.class));
            friendship.setSecond(resultSet.getObject("second_user",UUID.class));
            friendship.setData(resultSet.getTimestamp("date").toLocalDateTime());
        }catch (SQLException e){

            return null;
        }
        return friendship;
    }

    public List<Friendship<UUID>> getFriends( UUID user) {
        List<Friendship<UUID>> friendshipsSet= new ArrayList<>();
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement("SELECT * FROM friendships where (first_user=? or second_user=?)")){
            preparedStatement.setObject(1, user);
            preparedStatement.setObject(2, user);
            ResultSet resultSet= preparedStatement.executeQuery();
            while(resultSet.next()){
                UUID user1=resultSet.getObject("first_user", UUID.class);
                UUID user2=resultSet.getObject("second_user", UUID.class);
                Friendship<UUID> friendship=new Friendship<>(user1,user2);
                friendship.setData(resultSet.getTimestamp("date").toLocalDateTime());
                friendshipsSet.add(friendship);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return friendshipsSet;
    }

    @Override
    public List<Friendship<UUID>> getAll() {
        List<Friendship<UUID>> friendshipsSet= new ArrayList<>();
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement("SELECT * FROM friendships");
            ResultSet resultSet= preparedStatement.executeQuery()){
            while(resultSet.next()){
                UUID user1=resultSet.getObject("first_user", UUID.class);
                UUID user2=resultSet.getObject("second_user", UUID.class);
                Friendship<UUID> friendship=new Friendship<>(user1,user2);
                friendship.setData(resultSet.getTimestamp("date").toLocalDateTime());
                friendshipsSet.add(friendship);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return friendshipsSet;
    }

    @Override
    public int size() {
        int dim=0;
        try( Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
             PreparedStatement preparedStatement= connection.prepareStatement("SELECT COUNT(*) as total FROM friendships");
             ResultSet resultSet= preparedStatement.executeQuery()){
            resultSet.next();
            dim=resultSet.getInt("total");

        }catch(SQLException e){
            e.printStackTrace();
        }
        return dim;
    }


}
*/
