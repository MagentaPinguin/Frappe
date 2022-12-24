package repository;

import java.util.List;
import java.util.Optional;

public interface Repository<E> {

    E add(E entity) throws RepositoryException;

    E update(E entity) throws RepositoryException;

    E delete(E entity) throws RepositoryException;

    Optional<E> find(E entity);

    List<E> getAll() throws RepositoryException;

    int size() throws RepositoryException;
}
//" General repository interface