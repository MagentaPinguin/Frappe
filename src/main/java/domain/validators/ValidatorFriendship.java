package domain.validators;

import domain.Friendship;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ValidatorFriendship implements Validator {



    @Override
    public void validate(List<String> args) throws ValidationException {
        ArrayList<String> msg =new ArrayList<>();

        if (args.get(0).equals(args.get(1)))
            msg.add("Id1 is equal with id2");
        if (args.get(0) == null || args.get(1)== null)
            msg.add("Id null");
        if(!msg.isEmpty())
            throw new ValidationException(msg.stream().reduce((a,b)->a+'\n'+b).get());
    }
}
//" Validator for friendship