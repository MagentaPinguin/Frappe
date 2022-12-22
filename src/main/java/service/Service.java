package service;

import domain.User;
import domain.validators.ValidationException;
import domain.validators.ValidatorUser;
import repository.RepositoryDBUsers;
import repository.RepositoryException;

import java.util.List;

public class Service {

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

}
