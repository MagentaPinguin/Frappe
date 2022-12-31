package repository;

import domain.Chatroom;
import domain.Friendship;
import domain.Message;
import domain.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RepositoryDBChatroom implements Repository<Chatroom<UUID>>{
    private final String urlDb;
    private final String usernameDb;
    private final String passwdDb;

    public RepositoryDBChatroom(String urlDb, String usernameDb, String passwdDb) {
        this.urlDb = urlDb;
        this.usernameDb = usernameDb;
        this.passwdDb = passwdDb;
    }

    public Message<UUID> addMessage(Message<UUID> messageObj) throws RepositoryException {
        String sql="INSERT INTO messages( chatroom, sender,context, date) values (?,?,?,?)";
        List<Message<UUID>> list=new ArrayList<>();
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setObject(1, messageObj.getChatroom());
            preparedStatement.setObject(2, messageObj.getSender());
            preparedStatement.setObject(3, messageObj.getContext());
            preparedStatement.setObject(4, messageObj.getData());
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            throw new RepositoryException(e.getMessage());
        }
        return messageObj;

    }

    @Override
    public Chatroom<UUID> add(Chatroom<UUID> entity) throws RepositoryException {
        if(find(entity).isPresent())
            throw new RepositoryException("Entity exists");
        String sql="insert into chatrooms(name,type,passwd) values (?,?,?)";
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setObject(1, entity.getName());
            preparedStatement.setObject(2, entity.getType());
            preparedStatement.setObject(3, entity.getPasswd());
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            throw new RepositoryException(e.getMessage());
        }
        return entity;
    }

    @Override
    public Chatroom<UUID> update(Chatroom<UUID> entity) throws RepositoryException {
        return null;
    }

    @Override
    public Chatroom<UUID> delete(Chatroom<UUID> entity) throws RepositoryException {
        if (find(entity).isEmpty())
            throw new RepositoryException("Nonexistent entity");

        String sql = "delete from chatrooms where Id=?";
        try (Connection connection = DriverManager.getConnection(urlDb, usernameDb, passwdDb);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, entity.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return entity;
    }
    public void deleteChatroom(Chatroom<UUID> entity) throws RepositoryException {
        if (find(entity).isEmpty())
            throw new RepositoryException("Nonexistent entity");

        String sql = "delete from chatrooms where Id=?";
        try (Connection connection = DriverManager.getConnection(urlDb, usernameDb, passwdDb);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, entity.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Chatroom<UUID>> find(Chatroom<UUID> entity) {
        String sql="SELECT * from chatrooms where name=?";
        Chatroom<UUID> chatRoom=new Chatroom<>();
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setObject(1, entity.getName());

            ResultSet resultSet= preparedStatement.executeQuery();
            resultSet.next();
            chatRoom.setId(resultSet.getObject("id",UUID.class));
            chatRoom.setName(resultSet.getString("name"));
            chatRoom.setPasswd(resultSet.getString("passwd"));
            chatRoom.setType(resultSet.getInt("type"));
            chatRoom.setParticipants(getMembers(chatRoom.getId()).get());
        }catch (SQLException | RepositoryException e){

            return Optional.empty();
        }
        return Optional.of(chatRoom);
    }
    public Optional<List<UUID>> getMembers(UUID chat) throws RepositoryException {

        String sql="SELECT * from chatmembers where id=?";
        List<UUID> list=new ArrayList<>();
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setObject(1, chat);
            ResultSet resultSet= preparedStatement.executeQuery();
            while(resultSet.next()){
                list.add(resultSet.getObject("id_member", UUID.class));
            }

        }catch (SQLException e){
            throw new RepositoryException(e.getMessage());
        }
        return Optional.of(list);


    }
    public void joinChatroom(UUID chat,UUID user) throws RepositoryException {
        String sql="insert into chatmembers(id,id_member) values (?,?)";
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setObject(1, chat);
            preparedStatement.setObject(2, user);
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            throw new RepositoryException(e.getMessage());
        }
    }


    @Override
    public List<Chatroom<UUID>> getAll() throws RepositoryException {
        String sql="SELECT * from chatrooms";
        List<Chatroom<UUID>> list=new ArrayList<>();

        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){

            ResultSet resultSet= preparedStatement.executeQuery();
            while(resultSet.next()){
                var chat=new Chatroom<UUID>(resultSet.getString("name"),resultSet.getInt("type"));
                chat.setId(resultSet.getObject("id", UUID.class));
                chat.setPasswd(resultSet.getString("passwd"));
                chat.setParticipants(getMembers(chat.getId()).get());
                list.add(chat);
            }

        }catch (SQLException e){
            throw new RepositoryException(e.getMessage());
        }
        return list;
    }

    @Override
    public int size() throws RepositoryException {
        int dim = 0;
        try (Connection connection = DriverManager.getConnection(urlDb, usernameDb, passwdDb);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) as total FROM chatrooms");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            resultSet.next();
            dim = resultSet.getInt("total");

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return dim;
    }

    public List<Message<UUID>> getAllMessagesFor(Chatroom<UUID> openedChatroom) throws RepositoryException {
        String sql="SELECT * from messages where chatroom=?";
        List<Message<UUID>> list=new ArrayList<>();

        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setObject(1, openedChatroom.getId());
            ResultSet resultSet= preparedStatement.executeQuery();

            while(resultSet.next()){
                Message<UUID> message=new Message<>();
                message.setChatroom(openedChatroom.getId());
                message.setSender(resultSet.getObject("sender", UUID.class));
                message.setContext(resultSet.getString("context"));
                message.setData(resultSet.getObject("date", LocalDateTime.class));
                list.add(message);
            }

        }catch (SQLException e){
            throw new RepositoryException(e.getMessage());
        }
        return list;

    }



    public void exitChatroom(Chatroom<UUID> chat, User user) throws RepositoryException {
        String sql=" delete from chatmembers where id=? and id_member=? ";
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setObject(1, chat.getId());
            preparedStatement.setObject(2, user.getId());
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            throw new RepositoryException(e.getMessage());
        }
    }


}
