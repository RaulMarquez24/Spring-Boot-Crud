package com.bolsadeideas.springboot.app.controllers;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Role;
import com.bolsadeideas.springboot.app.models.entity.Usuario;
import com.bolsadeideas.springboot.app.models.services.IUsuarioService;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@SessionAttributes("usuario")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
	private BCryptPasswordEncoder passwordEncoder;

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/usuarios/listar", method = RequestMethod.GET)
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page,
            Model model,
            Authentication authentication,
            HttpServletRequest request,
            Locale locale) {

        Pageable pageRequest = PageRequest.of(page, 5);

        Page<Usuario> usuarios = usuarioService.findAll(pageRequest);

        PageRender<Usuario> pageRender = new PageRender<>("/usuarios/listar", usuarios);

        model.addAttribute("titulo", messageSource.getMessage("text.usuario.listar.titulo", null, locale));
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("page", pageRender);

        return "usuarios/listar";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/usuarios/form", method = RequestMethod.GET)
    public String crear(Map<String, Object> model, Locale locale) {

        Usuario usuario = new Usuario();

        model.put("usuario", usuario);
        model.put("titulo", messageSource.getMessage("text.usuario.form.titulo.crear", null, locale));

        return "usuarios/form";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/usuarios/form", method = RequestMethod.POST)
    public String guardar(@Valid Usuario usuario, BindingResult result, Model model,
            RedirectAttributes flash,
            SessionStatus status, Locale locale) {

        if (usuarioService.findByUsername(usuario.getUsername()) != null) {
            result.rejectValue("username", "usuario.username.duplicate");
        }

        if (result.hasErrors()) {
            model.addAttribute("titulo", messageSource.getMessage("text.usuario.form.titulo.crear", null, locale));
            return "usuarios/form";
        }

        String mensajeFlash = "";

        if (usuario.getId() == null) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            usuario.setRoles(usuario.isAdmin() ? Arrays.asList(new Role("ROLE_ADMIN"), new Role("ROLE_USER")) : Arrays.asList(new Role("ROLE_USER")));
            usuario.setEnabled(true);
            mensajeFlash = messageSource.getMessage("text.usuario.flash.crear.success", null, locale);
        } else {
            mensajeFlash = messageSource.getMessage("text.usuario.flash.editar.success", null, locale);
        }

        usuarioService.save(usuario);
        status.setComplete();
        flash.addFlashAttribute("success", mensajeFlash);
        return "redirect:/usuarios/listar";
    }

}
