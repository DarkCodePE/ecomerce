package com.example.ecomerce.services.base;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * clase envoltorio para que extienda otras clases
 *
 * @author Orlando
 *
 * @param <T> clase entidad
 * @param <ID> indetificador de clase entidad
 * @param <R> interfaz
 */

public abstract class BaseService <T, ID, R extends JpaRepository<T, ID>> {
  @Autowired
  protected R repository;

  public T save(T t) {
    return repository.save(t);
  }

  public Optional<T> findById(ID id){
    return repository.findById(id);
  }

  public List<T> findAll(){
    return repository.findAll();
  }

  public T update(T t) {
    return repository.save(t);
  }

  public Page<T> findAll(Pageable pageable){
    return repository.findAll(pageable);
  }

  public void delete(T t) {
    repository.delete(t);
  }

  public void deleteById(ID id) {
    repository.deleteById(id);
  }
}
