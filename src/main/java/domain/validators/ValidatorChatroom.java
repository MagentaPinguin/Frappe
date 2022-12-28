package domain.validators;

import java.util.List;

public class ValidatorChatroom implements Validator{
    @Override
    public void validate(List<String> args) throws ValidationException {
        if (args.get(0).isBlank())
            throw new ValidationException("Chatroom name empty.");
    }
}
