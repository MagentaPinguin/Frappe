package service;

import anexe.Observer;
import anexe.Subject;
import domain.User;
import domain.validators.ValidationException;
import domain.validators.ValidatorUser;
import repository.RepositoryDBUsers;
import repository.RepositoryException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Service implements Subject {

    List<Observer> observersList=new ArrayList<>();
    //" Obs list
    RepositoryDBUsers repositoryDBUsers=new RepositoryDBUsers("jdbc:postgresql://localhost:5432/Frappe","postgres","postgres");
    ValidatorUser validatorUser=new ValidatorUser();
    public Service(){}

    public void addUser(List<String> arg) throws ServiceException {
        try {
            validatorUser.validate(arg);
            repositoryDBUsers.add(new User(arg.get(0),arg.get(1),arg.get(2),arg.get(3)));

        }catch (RepositoryException | ValidationException e){
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

    public User getUserByUsername(String text) throws ServiceException {
        var found= repositoryDBUsers.findUsername(text);
        if(found.isEmpty())
            throw new ServiceException("User not found!");
        else
            return found.get();
    }
}
