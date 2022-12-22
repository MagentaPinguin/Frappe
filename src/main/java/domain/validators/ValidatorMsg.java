package domain.validators;

import domain.Message;
import domain.User;

import java.util.ArrayList;
import java.util.UUID;

public class ValidatorMsg implements Validator<Message<UUID>>{
    @Override
    public void validate(Message<UUID> message) throws ValidationException {

        ArrayList<String> msg =new ArrayList<>();
        if (message.getId() == null)
            msg.add("ID null.");

        if(message.getSender().toString().isBlank())
            msg.add("Sender null");

        if (message.getData().toString().isBlank())
            msg.add("Data null.");

        if(!msg.isEmpty())
            throw new ValidationException(msg.stream().reduce((a,b)->a+'\n'+b).get());

    }


}
