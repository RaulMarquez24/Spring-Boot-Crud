package com.bolsadeideas.springboot.app.models.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bolsadeideas.springboot.app.models.entity.Producto;

public interface IProductoService {

    public List<Producto> findAll();

    public Page<Producto> findAll(Pageable pageable);

    public Producto findOne(Long id);

    public Boolean alreadyExist(String name);

    public Producto save(Producto producto);

    public List<Producto> saveAll(List<Producto> productos);

    public void delete(Long id);

}
