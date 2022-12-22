package domain.validators;

import java.util.List;

public interface Validator {
    void validate(List<String> args) throws ValidationException;
}
//"General validator interface
