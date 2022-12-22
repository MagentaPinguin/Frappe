package repository;

import domain.Entity;

import java.util.Collection;
import java.util.List;


public abstract class RepositoryAbstract<E extends Entity<?>> implements Repository<E> {
    protected Collection<E> repository;
    @Override
    public E add(E entity) throws RepositoryException {
        var aux = find(entity);
        if (aux != null)
            throw new RepositoryException("Existent entity");
        repository.add(entity);
        return entity;
    }
    @Override
    public abstract  E update(E entity) throws RepositoryException;

    @Override
    public E delete(E entity) throws RepositoryException {
        var aux = find(entity);
        if (aux == null)
            throw new RepositoryException("Nonexistent entity");
        repository.remove(aux);
        return entity;
    }
    @Override
    public List<E> getAll() {
        return repository.stream().toList();
    }

    @Override
    public int size() {
        return repository.size();
    }
}
