package service;

import anexe.Observer;
import anexe.Subject;
import domain.*;
import domain.validators.*;
import repository.*;

import java.util.ArrayList;
import java.util.List;

import java.util.UUID;


public class Service implements Subject {

    List<Observer> observersList = new ArrayList<>();
    //" Observer list

    RepositoryDBUsers repositoryDBUsers = new RepositoryDBUsers("jdbc:postgresql://localhost:5432/Frappe", "postgres", "postgres");
    RepositoryDBFriendship repositoryDBFriendship = new RepositoryDBFriendship("jdbc:postgresql://localhost:5432/Frappe", "postgres", "postgres");
    RepositoryDbRequests repositoryDbRequests = new RepositoryDbRequests("jdbc:postgresql://localhost:5432/Frappe", "postgres", "postgres");
    RepositoryDBChatroom repositoryDBChatroom = new RepositoryDBChatroom("jdbc:postgresql://localhost:5432/Frappe", "postgres", "postgres");
    //" All the repository I could  want

    ValidatorUser validatorUser = new ValidatorUser();
    ValidatorFriendship validatorFriendship = new ValidatorFriendship();
    ValidatorChatroom validatorChatroom = new ValidatorChatroom();
    //"Validators. P.s: the friendship validator works for request because request is just a 'need the other side to know we are friends'

    public Service() {
    }

    /***
     * Register a User to the network
     * @param arg: Relevant data for a user
     * @throws ServiceException: Handles any validation or repository exception
     */
    public void registerUser(List<String> arg) throws ServiceException {
        try {
            validatorUser.validate(arg);
            var aux = new User(arg.get(0), arg.get(1), arg.get(2), arg.get(3));
            aux.setPictureReference(arg.get(4));
            repositoryDBUsers.add(aux);

        } catch (RepositoryException | ValidationException e) {
            throw new ServiceException(e.getMessage());

        }
    }

    /***
     * Sent a friend request.
     * @param sender: the user that sends the friendship proposal
     * @param receiver: the user that receive the friendship proposal.
     * @throws ServiceException: Handles any validation or repository exception
     */
    public void sentRequest(User sender, User receiver) throws ServiceException {
        try {
            validatorFriendship.validate(List.of(sender.getId().toString(), receiver.getId().toString()));
            repositoryDbRequests.add(new Request<>(sender.getId(), receiver.getId()));
        } catch (ValidationException | RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /***
     * Add a friendship to the count.
     * @param request: The request that was accepted
     * @throws ServiceException: Handles any validation or repository exception
     */
    public void addFriendship(Request<UUID> request) throws ServiceException {
        try {

            validatorFriendship.validate(List.of(request.getSender().toString(), request.getReceiver().toString()));
            repositoryDBFriendship.add(new Friendship<>(request.getSender(), request.getReceiver()));
        } catch (RepositoryException | ValidationException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }

    }

    /***
     * Handle the creation of a chatroom.
     * @param arg:  Relevant data for a chatroom.
     * @param owner: The first member of the chatroom.
     * @throws ServiceException: Handles any validation or repository exception
     */
    public void createChatRoom(List<String> arg, User owner) throws ServiceException {
        try {
            validatorChatroom.validate(arg);
            int type = Integer.parseInt(arg.get(1));
            var aux = new Chatroom<UUID>(arg.get(0), Integer.parseInt(arg.get(1)));
            if (type == 1)
                aux.setPasswd(arg.get(2));
            repositoryDBChatroom.add(aux);
            var success = repositoryDBChatroom.find(aux);
            repositoryDBChatroom.joinChatroom(success.get().getId(), owner.getId());
        } catch (RepositoryException | ValidationException e) {
            throw new ServiceException(e.getMessage());

        }
    }
    /***
     * Adds a user to the chat
     * @param selected: The chatroom where the new member joins.
     * @param account:  The new member.
     * @throws ServiceException: Handles any validation or repository exception
     */
    public void addMemberToChat(Chatroom<UUID> selected, User account) throws ServiceException {
        if (selected.isMember(account.getId())) {
            throw new ServiceException("Already member");
        }
        try {
            repositoryDBChatroom.joinChatroom(selected.getId(), account.getId());
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    /***
     * Handle the message sending procedure
     * @param chatroomId: Chatroom id.
     * @param userId: Member that sent the message.
     * @param message: The message
     * @throws ServiceException: Handles any validation or repository exception
     */
    public void addMessage(UUID chatroomId, UUID userId, String message) throws ServiceException {
        var messageObj = new Message<UUID>(chatroomId, userId, message);
        try {
            repositoryDBChatroom.addMessage(messageObj);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }
    //" Add functions

    /***
     * Handles the end of a friendship
     * @param owner: The user that ends it
     * @param friend: The innocent one that does not know what is about to happend
     * @throws ServiceException: Handles any validation or repository exception
     */
    public void deleteFriendship(User owner, User friend) throws ServiceException {
        try {
            var friendshipFound = getFriendsShip(owner, friend);
            var requestFound = getRequest(owner, friend);
            if (requestFound.getId() == null)
                requestFound = getRequest(friend, owner);
            repositoryDBFriendship.delete(friendshipFound);
            repositoryDbRequests.delete(requestFound);
        } catch (ServiceException | RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }

    }


    public void deleteChat(Chatroom<UUID> chat){
        try {
            repositoryDBChatroom.deleteChatroom(chat);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }
    //" Delete

    /***
     * Handles the update of a user
     * @param updated: The "new data seme id object" for user
     * @throws ServiceException: Handles any validation or repository exception
     */
    public void updateUser(User updated) throws ServiceException {
        try {
            repositoryDBUsers.update(updated);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /***
     *  Handles the update of a request
     * @param req: updated request
     * @throws ServiceException: Handles any validation or repository exception
     */
    public void updateRequest(Request<UUID> req) throws ServiceException {

        try {
            repositoryDbRequests.update(req);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }

    }
    //" Updates

    public List<User> getUsers() throws ServiceException {
        try {
            return repositoryDBUsers.getAll();
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public User getUserByUsername(String text) throws ServiceException {
        var found = repositoryDBUsers.findUsername(text);
        if (found.isEmpty())
            throw new ServiceException("User not found!");
        else
            return found.get();
    }

    public User getUserById(UUID sender) {
        var user = new User();
        user.setId(sender);
        var x = repositoryDBUsers.find(user);
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
    //" Getters


    public List<Friendship<UUID>> getFriendsFor(User account) throws ServiceException {
        try {
            return repositoryDBFriendship.getFriends(account.getId());
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public Request<UUID> getRequest(User owner, User other) throws ServiceException {
        try {
            return repositoryDbRequests.getRequest(new Request<>(owner.getId(), other.getId()));
        } catch (RepositoryException e) {
            try {
                return repositoryDbRequests.getRequest(new Request<>(other.getId(), owner.getId()));
            } catch (RepositoryException ex) {
                throw new ServiceException(ex);
            }
        }
    }


    public Friendship<UUID> getFriendsShip(User acc, User other) throws ServiceException {

        var x = repositoryDBFriendship.find(new Friendship<>(acc.getId(), other.getId()));
        if (x.isEmpty())
            throw new ServiceException("Nonexistent friendship!");
        return x.get();

    }


    public List<Chatroom<UUID>> getAllChatroom() throws ServiceException {
        try {
            return repositoryDBChatroom.getAll();
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Message<UUID>> getAllMessagesFor(Chatroom<UUID> openedChatroom) throws ServiceException {
        try {
            return repositoryDBChatroom.getAllMessagesFor(openedChatroom);

        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }
    //"Getters

    public void exitChat(Chatroom<UUID> chat ,User account) throws ServiceException {
        try {
            repositoryDBChatroom.exitChatroom(chat,account);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
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
        for (Observer x : observersList) {
            x.update();
        }
    }

    //" Observer
}
