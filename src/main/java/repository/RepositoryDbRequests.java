package repository;


import domain.Request;
import domain.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RepositoryDbRequests implements Repository<Request<UUID>> {
    private final String urlDb;
    private final String usernameDb;
    private final String passwdDb;

    public RepositoryDbRequests(String urlDb, String usernameDb, String passwdDb) {
        this.urlDb = urlDb;
        this.usernameDb = usernameDb;
        this.passwdDb = passwdDb;
    }

    @Override
    public Request<UUID> add(Request<UUID> entity) throws RepositoryException {
        if(find(entity).isPresent())
            throw new RepositoryException("Entity exists");
        String sql="insert into requests(sender,receiver,status) values(?,?,?)";
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setObject(1, entity.getSender());
            preparedStatement.setObject(2, entity.getReceiver());
            preparedStatement.setObject(3, entity.getStatus());
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            throw new RepositoryException(e.getMessage());
        }
        return entity;
    }

    @Override
    public Request<UUID> update(Request<UUID> entity) throws RepositoryException {
        String sql="UPDATE requests set status=?, date=?  where id=?";

        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setString(1, entity.getStatus());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setObject(3, entity.getId());
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            throw new RepositoryException(e.getMessage());
        }
        return entity;
    }


    @Override
    public Request<UUID> delete(Request<UUID> entity) throws RepositoryException {
        if(find(entity).isEmpty())
            throw new RepositoryException("Nonexistent entity");
        String sql="delete from requests where( sender=? and receiver=? and status=?)";
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setObject(1, entity.getSender());
            preparedStatement.setObject(2, entity.getReceiver());
            preparedStatement.setString(3, entity.getStatus());
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            throw new RepositoryException(e.getMessage());
        }
        return entity;
    }

    @Override
    public Optional<Request<UUID>> find(Request<UUID> entity) {
        Request<UUID> req=new Request<>();
        String sql="SELECT * from requests where( sender=? and receiver=? and status=?)";
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setObject(1, entity.getSender());
            preparedStatement.setObject(2,  entity.getReceiver());
            preparedStatement.setObject(3,  entity.getStatus());

            ResultSet resultSet= preparedStatement.executeQuery();
            resultSet.next();

            req.setId(resultSet.getObject("id", UUID.class));
            req.setSender(resultSet.getObject("sender",UUID.class));
            req.setReceiver(resultSet.getObject("receiver",UUID.class));
            req.setDate(resultSet.getTimestamp("date").toLocalDateTime());
            req.setStatus(resultSet.getString("status"));
        }catch (SQLException e){

            return Optional.empty();
        }
        return Optional.of(req);
    }

    @Override
    public List<Request<UUID>> getAll() throws RepositoryException {
        List<Request<UUID>> requestList= new ArrayList<>();
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement("SELECT * FROM requests");
            ResultSet resultSet= preparedStatement.executeQuery()){
            while(resultSet.next()){

                var req=new Request<>(
                                            resultSet.getObject("sender", UUID.class),
                                            resultSet.getObject("receiver", UUID.class));

                req.setId(resultSet.getObject("id", UUID.class));
                req.setStatus(resultSet.getString("status"));
                req.setDate(resultSet.getTimestamp("data").toLocalDateTime());
                requestList.add(req);
            }

        }catch(SQLException e){
            throw new RepositoryException(e.getMessage());
        }
        return requestList;
    }

    public Request<UUID> getRequest(Request<UUID> request) throws RepositoryException {
        Request<UUID> requestFound= new Request<>();
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement("SELECT * FROM requests where sender=? and receiver=? ")){
            preparedStatement.setObject(1, request.getSender());
            preparedStatement.setObject(2, request.getReceiver());
            ResultSet resultSet= preparedStatement.executeQuery();
            while(resultSet.next()){

                requestFound.setSender(resultSet.getObject("sender", UUID.class));
                requestFound.setReceiver(resultSet.getObject("receiver", UUID.class));
                requestFound.setStatus(resultSet.getString("status"));
                requestFound.setDate(resultSet.getTimestamp("date").toLocalDateTime());
                requestFound.setId(resultSet.getObject("id", UUID.class));
            }

        }catch(SQLException e){
            throw new RepositoryException(e.getMessage());
        }
        return requestFound;
    }

    public List<Request<UUID>> getAllFor(User account) throws RepositoryException {
        List<Request<UUID>> requestList= new ArrayList<>();
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement("SELECT * FROM requests where receiver=? ")){
            preparedStatement.setObject(1, account.getId());
            ResultSet resultSet= preparedStatement.executeQuery();
            while(resultSet.next()){

                var req=new Request<>(
                        resultSet.getObject("sender", UUID.class),
                        resultSet.getObject("receiver", UUID.class));

                req.setId(resultSet.getObject("id", UUID.class));
                req.setStatus(resultSet.getString("status"));
                req.setDate(resultSet.getTimestamp("date").toLocalDateTime());
                requestList.add(req);
            }

        }catch(SQLException e){
            throw new RepositoryException(e.getMessage());
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


    public List<Request<UUID>> getAllSent(User account) throws RepositoryException {
        List<Request<UUID>> requestList= new ArrayList<>();
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement("SELECT * FROM requests where sender=? ")){
            preparedStatement.setObject(1, account.getId());
            ResultSet resultSet= preparedStatement.executeQuery();
            while(resultSet.next()){

                var req=new Request<>(
                        resultSet.getObject("sender", UUID.class),
                        resultSet.getObject("receiver", UUID.class));

                req.setId(resultSet.getObject("id", UUID.class));
                req.setStatus(resultSet.getString("status"));
                req.setDate(resultSet.getTimestamp("date").toLocalDateTime());
                requestList.add(req);
            }

        }catch(SQLException e){
            throw new RepositoryException(e.getMessage());
        }
        return requestList;
    }
}
