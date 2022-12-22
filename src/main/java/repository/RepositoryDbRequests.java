package repository;


import domain.Request;
import repository.Repository;
import repository.RepositoryException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RepositoryDbRequests implements Repository<Request<Integer>> {
    private final String urlDb;
    private final String usernameDb;
    private final String passwdDb;

    public RepositoryDbRequests(String urlDb, String usernameDb, String passwdDb) {
        this.urlDb = urlDb;
        this.usernameDb = usernameDb;
        this.passwdDb = passwdDb;
    }

    @Override
    public Request<Integer> add(Request<Integer> entity) throws RepositoryException {

        if(find(entity)!=null)
            throw new RepositoryException("Entity exists");
        String sql="insert into requests(sender,receiver,status) values(?,?,?)";
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setObject(1, entity.getSender());
            preparedStatement.setObject(2, entity.getReceiver());
            preparedStatement.setObject(3, entity.getStatus());
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public Request<Integer> update(Request<Integer> entity) throws RepositoryException {
        String sql="UPDATE requests set status=?, data=?  where id=?";

        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setString(1, entity.getStatus());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setInt(3, entity.getId());
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            throw new RepositoryException(e.getMessage());
        }
        return entity;
    }


    @Override
    public Request<Integer> delete(Request<Integer> entity) throws RepositoryException {
        if(find(entity)==null)
            throw new RepositoryException("Nonexistent entity");
        String sql="delete from requests where( sender=? and receiver=? and status='ACCEPTED')";
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setObject(1, entity.getSender());
            preparedStatement.setObject(2, entity.getReceiver());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            throw new RepositoryException(e.getMessage());
        }
        return entity;
    }

    public List<Request<Integer>> findRequests(UUID Receiver) {
        List<Request<Integer>> requestList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(urlDb, usernameDb, passwdDb);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM requests where receiver=?")){
            preparedStatement.setObject(1, Receiver);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                UUID sender = resultSet.getObject("sender", UUID.class);
                UUID receiver = resultSet.getObject("receiver", UUID.class);
                String status= resultSet.getString("status");
                Request<Integer> req = new Request<>(sender, receiver,status);
                req.setId(id);
                req.setDate(resultSet.getTimestamp("data").toLocalDateTime());
                requestList.add(req);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requestList;
    }
    @Override
    public Request<Integer> find(Request<Integer> entity) {
        Request<Integer> req=new Request<>();
        String sql="SELECT * from requests where( sender=? and receiver=? and status='PENDING' )";
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setObject(1, entity.getSender());
            preparedStatement.setObject(2,  entity.getReceiver());
            ResultSet resultSet= preparedStatement.executeQuery();
            resultSet.next();

            req.setId(resultSet.getInt("id"));
            req.setSender(resultSet.getObject("sender",UUID.class));
            req.setReceiver(resultSet.getObject("receiver",UUID.class));
            req.setDate(resultSet.getTimestamp("data").toLocalDateTime());
            req.setStatus(resultSet.getString("status"));
        }catch (SQLException e){

            return null;
        }
        return req;
    }

    @Override
    public List<Request<Integer>> getAll() {
        List<Request<Integer>> requestList= new ArrayList<>();
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement("SELECT * FROM requests");
            ResultSet resultSet= preparedStatement.executeQuery()){
            while(resultSet.next()){

                Request<Integer> req=new Request<>(
                                            resultSet.getObject("sender", UUID.class),
                                            resultSet.getObject("receiver", UUID.class));

                req.setId(resultSet.getInt("id"));
                req.setStatus(resultSet.getString("status"));
                req.setDate(resultSet.getTimestamp("data").toLocalDateTime());
                requestList.add(req);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return requestList;
    }

    @Override
    public int size() {
        int dim=0;
        try( Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
             PreparedStatement preparedStatement= connection.prepareStatement("SELECT COUNT(*) as total FROM requests");
             ResultSet resultSet= preparedStatement.executeQuery()){
            resultSet.next();
            dim=resultSet.getInt("total");

        }catch(SQLException e){
            e.printStackTrace();
        }
        return dim;
    }
}
