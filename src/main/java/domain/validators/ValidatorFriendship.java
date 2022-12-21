package domain.validators;

import domain.Friendship;

import java.util.ArrayList;
import java.util.UUID;

public class ValidatorFriendship implements Validator<Friendship<UUID>> {

    @Override
    public void validate(Friendship<UUID> entity) throws ValidationException {
        ArrayList<String> msg =new ArrayList<>();
        if (entity.getFirst() == entity.getSecond())
            msg.add("Id1 is equal with id2");
        if (entity.getFirst() == null || entity.getSecond() == null)
            msg.add("Id null");
        if(!msg.isEmpty())
            throw new ValidationException(msg.stream().reduce((a,b)->a+'\n'+b).get());

    }
}
//" Validator for friendship