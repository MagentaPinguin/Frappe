package domain.validators;

import domain.User;

import java.util.ArrayList;

public class ValidatorUser implements Validator<User> {

    @Override
    public void validate(User user) throws ValidationException {

        ArrayList<String> msg =new ArrayList<>();
        if (user.getId() == null)
            msg.add("ID null.");
        if (user.getUserName().isBlank())
             msg.add("Username blank.");
        if (user.getPasswd().isBlank())
            msg.add("Passwd blank.");
        if (user.getFirstName().isBlank())
           msg.add("Firstname blank.");
        if (user.getLastName().isBlank())
            msg.add("Lastname blank.");

       if(!msg.isEmpty())
            throw new ValidationException(msg.stream().reduce((a,b)->a+'\n'+b).get());

    }
}
//" Validator for user.