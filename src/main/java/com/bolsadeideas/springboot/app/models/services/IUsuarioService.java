package com.bolsadeideas.springboot.app.models.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bolsadeideas.springboot.app.models.entity.Usuario;

public interface IUsuarioService {

    public List<Usuario> findAll();

    public Page<Usuario> findAll(Pageable pageable);

    public void save(Usuario usuario);

    public Usuario findOne(Long id);

    public void delete(Long id);

}
