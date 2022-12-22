package repository;

import java.util.List;

public interface Repository<E> {

    E add(E entity) throws RepositoryException;

    E update(E entity) throws RepositoryException;

    E delete(E entity) throws RepositoryException;

    E find(E entity);

    List<E> getAll();

    int size();
}
//" General repository interface