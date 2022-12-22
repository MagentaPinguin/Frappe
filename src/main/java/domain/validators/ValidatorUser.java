package domain.validators;

import domain.User;

import java.util.ArrayList;
import java.util.List;

public class ValidatorUser implements Validator {

    @Override
    public void validate(List<String> args) throws ValidationException {

        ArrayList<String> msg =new ArrayList<>();
        if (args.get(0).isBlank())
             msg.add("Username blank.");
        if (args.get(1).isBlank())
            msg.add("Passwd blank.");
        if (args.get(2).isBlank())
           msg.add("Firstname blank.");
        if (args.get(3).isBlank())
            msg.add("Lastname blank.");

       if(!msg.isEmpty())
            throw new ValidationException(msg.stream().reduce((a,b)->a+'\n'+b).get());

    }


}
//" Validator for user.