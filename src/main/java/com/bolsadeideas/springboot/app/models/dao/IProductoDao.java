package com.bolsadeideas.springboot.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bolsadeideas.springboot.app.models.entity.Producto;

public interface IProductoDao extends PagingAndSortingRepository<Producto, Long>, CrudRepository<Producto, Long> {
    
    @Query("select p from Producto p where p.nombre like %?1%")
    public List<Producto> findByName(String term);

    // public List<Producto> findByNameLikeIgnoreCase(String term);

}
