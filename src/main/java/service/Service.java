package service;

import anexe.Observer;
import anexe.Subject;
import domain.Chatroom;
import domain.Friendship;
import domain.Request;
import domain.User;
import domain.validators.*;
import repository.*;

import java.util.ArrayList;
import java.util.List;

import java.util.UUID;


public class Service implements Subject {

    List<Observer> observersList=new ArrayList<>();
    //" Obs list
    RepositoryDBUsers repositoryDBUsers=new RepositoryDBUsers("jdbc:postgresql://localhost:5432/Frappe","postgres","postgres");
    RepositoryDBFriendship repositoryDBFriendship=new RepositoryDBFriendship("jdbc:postgresql://localhost:5432/Frappe","postgres","postgres");
    RepositoryDbRequests repositoryDbRequests=new RepositoryDbRequests("jdbc:postgresql://localhost:5432/Frappe","postgres","postgres");
    RepositoryDBChatroom repositoryDBChatroom=new RepositoryDBChatroom("jdbc:postgresql://localhost:5432/Frappe","postgres","postgres");


    ValidatorUser validatorUser=new ValidatorUser();
    ValidatorFriendship validatorFriendship=new ValidatorFriendship();
    ValidatorChatroom validatorChatroom=new ValidatorChatroom();

    public Service(){}

    public void addUser(List<String> arg) throws ServiceException {
        try {
            validatorUser.validate(arg);
            var aux=new User(arg.get(0),arg.get(1),arg.get(2),arg.get(3));
            aux.setPictureReference(arg.get(4));
            repositoryDBUsers.add(aux);

        }catch (RepositoryException | ValidationException e){
            throw new ServiceException(e.getMessage());

        }
    }

    public void addRequest(User sender, User receiver) throws ServiceException {
        try {
            validatorFriendship.validate(List.of(sender.getId().toString(),receiver.getId().toString()));
            repositoryDbRequests.add(new Request<>(sender.getId(),receiver.getId()));
        } catch (ValidationException | RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }



    public void addFriend(Request<UUID> request) throws ServiceException {
        try {

            validatorFriendship.validate(List.of(request.getSender().toString(),request.getReceiver().toString()));
            repositoryDBFriendship.add(new Friendship<>(request.getSender(),request.getReceiver()));
        } catch (RepositoryException | ValidationException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }

    }
    public void createChatRoom(List<String> arg,User owner) throws ServiceException {
        try {
            validatorChatroom.validate(arg);
            int type=Integer.parseInt(arg.get(1));
            var aux=new Chatroom<UUID>(arg.get(0),Integer.parseInt(arg.get(1)));
            if(type==1)
                aux.setPasswd(arg.get(2));
            repositoryDBChatroom.add(aux);
            var success=repositoryDBChatroom.find(aux);
            repositoryDBChatroom.joinChatroom(success.get().getId(),owner.getId());
        }catch (RepositoryException | ValidationException e){
            throw new ServiceException(e.getMessage());

        }
    }



    public User getUserByUsername(String text) throws ServiceException {
        var found= repositoryDBUsers.findUsername(text);
        if(found.isEmpty())
            throw new ServiceException("User not found!");
        else
            return found.get();
    }

    public void update(User updated) throws ServiceException {
        try {
            repositoryDBUsers.update(updated);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public List<User> getUsers() throws ServiceException {
        try {
            return repositoryDBUsers.getAll();
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void updateRequest( Request<UUID> req) throws ServiceException {

        try {
            repositoryDbRequests.update(req);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }

    }
    public User getUserById(UUID sender) {
        var user=new User();
        user.setId(sender);
        var x=repositoryDBUsers.find(user);
        return x.orElse(null);

    }

    public List<Request<UUID>> getRequestsFor(User account) throws ServiceException {
        try {
            return repositoryDbRequests.getAllFor(account);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }
    public List<Request<UUID>> getRequestsSent(User account) throws ServiceException {
        try {
            return repositoryDbRequests.getAllSent(account);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }


    @Override
    public void register(Observer obj) {
        observersList.add(obj);
    }

    @Override
    public void unregister(Observer obj) {
        observersList.remove(obj);
    }

    @Override
    public void notifyObservers() {
        for(Observer x:observersList){
            x.update();
        }
    }

    public List<Friendship<UUID>> getFriendsFor(User account) throws ServiceException {
        try {
            return repositoryDBFriendship.getFriends(account.getId());
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public Request<UUID> getRequest(User owner, User other) throws ServiceException {
        try {
            return repositoryDbRequests.getRequest(new Request<>(owner.getId(),other.getId()));
        } catch (RepositoryException e) {
            try {
                return repositoryDbRequests.getRequest(new Request<>(other.getId(),owner.getId()));
            } catch (RepositoryException ex) {
                throw new ServiceException(ex);
            }
        }
    }


    public Friendship<UUID> getFriendsShip(User acc,User other) throws ServiceException {

        var x=repositoryDBFriendship.find(new Friendship<>(acc.getId(),other.getId()));
        if(x.isEmpty())
            throw new ServiceException("Nonexistent friendship!");
        return x.get();

    }

    public void deleteFriendship(User owner, User friend) throws ServiceException {
        try {
            var friendshipFound= getFriendsShip(owner,friend);
            var requestFound =getRequest(owner, friend);
            if(requestFound.getId()==null)
                requestFound = getRequest(friend, owner);
            repositoryDBFriendship.delete(friendshipFound);
            repositoryDbRequests.delete(requestFound);
            } catch (ServiceException | RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }

    }

    public List<Chatroom<UUID>> getAllChatroom() throws ServiceException {
        try {
            return repositoryDBChatroom.getAll();
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void addMemberToChat(Chatroom<UUID> selected, User account) throws ServiceException {
        if(selected.isMember(account.getId())) {
            throw new ServiceException("Already member");
        }
        try {

            repositoryDBChatroom.joinChatroom(selected.getId(),account.getId());
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }
}
