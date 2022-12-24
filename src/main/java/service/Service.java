package service;

import anexe.Observer;
import anexe.Subject;
import domain.Friendship;
import domain.Request;
import domain.User;
import domain.validators.ValidationException;
import domain.validators.ValidatorFriendship;
import domain.validators.ValidatorUser;
import repository.RepositoryDBFriendship;
import repository.RepositoryDBUsers;
import repository.RepositoryDbRequests;
import repository.RepositoryException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Service implements Subject {

    List<Observer> observersList=new ArrayList<>();
    //" Obs list
    RepositoryDBUsers repositoryDBUsers=new RepositoryDBUsers("jdbc:postgresql://localhost:5432/Frappe","postgres","postgres");
    RepositoryDBFriendship repositoryDBFriendship=new RepositoryDBFriendship("jdbc:postgresql://localhost:5432/Frappe","postgres","postgres");
    RepositoryDbRequests repositoryDbRequests=new RepositoryDbRequests("jdbc:postgresql://localhost:5432/Frappe","postgres","postgres");


    ValidatorUser validatorUser=new ValidatorUser();
    ValidatorFriendship validatorFriendship=new ValidatorFriendship();
    public Service(){}

    public void addUser(List<String> arg) throws ServiceException {
        try {
            validatorUser.validate(arg);
            repositoryDBUsers.add(new User(arg.get(0),arg.get(1),arg.get(2),arg.get(3)));

        }catch (RepositoryException | ValidationException e){
            System.out.println(e.getMessage());
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

    public void updateRequest(String acepted) {


    }
}
