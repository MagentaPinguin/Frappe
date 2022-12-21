package domain.validators;

public interface Validator<E> {
    void validate(E e) throws ValidationException;
}
//"General validator interface
