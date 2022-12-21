package domain.validators;

import java.util.ArrayList;
import java.util.List;

public class ValidatorRegister {

    public void validate(String args[]) throws ValidationException {
        List<String> msg =new ArrayList<>();
        if (args[0].isBlank())
            msg.add("ID null.");
        if (args[1].isBlank())
            msg.add("Username blank.");
        if (args[2].isBlank())
            msg.add("Passwd blank.");
        if (args[3].isBlank())
            msg.add("Firstname blank.");
        if (args[4].isBlank())
            msg.add("Lastname blank.");
        if(!msg.isEmpty())
            throw new ValidationException(msg.stream().reduce((a,b)->a+'\n'+b).get());

    }
}
//" Validator for user.