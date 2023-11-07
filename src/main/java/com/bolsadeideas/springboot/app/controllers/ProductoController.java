package com.bolsadeideas.springboot.app.controllers;

import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Producto;
import com.bolsadeideas.springboot.app.models.services.IProductoService;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@SessionAttributes("producto")
public class ProductoController {

    @Autowired
    private IProductoService productoService;

    @Autowired
    private MessageSource messageSource;

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/productos/listar", method = RequestMethod.GET)
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page,
            Model model,
            Authentication authentication,
            HttpServletRequest request,
            Locale locale) {

        Pageable pageRequest = PageRequest.of(page, 5);

        Page<Producto> productos = productoService.findAll(pageRequest);

        PageRender<Producto> pageRender = new PageRender<>("/productos/listar", productos);

        model.addAttribute("titulo", messageSource.getMessage("text.producto.listar.titulo", null, locale));
        model.addAttribute("productos", productos);
        model.addAttribute("page", pageRender);

        return "productos/listar";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/productos/form")
    public String crear(Map<String, Object> model, Locale locale) {

        Producto producto = new Producto();

        model.put("producto", producto);
        model.put("titulo", messageSource.getMessage("text.producto.form.titulo.crear", null, locale));

        return "productos/form";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/productos/form", method = RequestMethod.POST)
    public String guardar(@Valid Producto producto, BindingResult result, Model model,
            RedirectAttributes flash,
            SessionStatus status, Locale locale) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", messageSource.getMessage("text.producto.form.titulo.crear", null, locale));
            return "productos/form";
        }

        String mensajeFlash = "";

        if (producto.getId() == null) {
            mensajeFlash = messageSource.getMessage("text.producto.flash.crear.success", null, locale);
            if (productoService.alreadyExist(producto.getNombre())) {
                result.rejectValue("nombre", "producto.nombre.duplicate");
                model.addAttribute("titulo", messageSource.getMessage("text.producto.form.titulo.crear", null, locale));
                return "productos/form";
            }
        } else {
            mensajeFlash = messageSource.getMessage("text.producto.flash.editar.success", null, locale);
        }

        productoService.save(producto);
        status.setComplete();
        flash.addFlashAttribute("success", mensajeFlash);
        return "redirect:/productos/listar";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/productos/form/{id}")
    public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash,
            Locale locale) {

        Producto producto = null;

        if (id > 0) {
            producto = productoService.findOne(id);
            if (producto == null) {
                flash.addFlashAttribute("error",
                        messageSource.getMessage("text.producto.flash.db.error", null, locale));
                return "redirect:/productos/listar";
            }
        } else {
            flash.addFlashAttribute("error", messageSource.getMessage("text.producto.flash.id.error", null, locale));
            return "redirect:/productos/listar";
        }

        model.put("producto", producto);
        model.put("titulo", messageSource.getMessage("text.producto.form.titulo.editar", null, locale));

        return "productos/form";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/productos/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash, Locale locale) {

        if (productoService.findOne(id) == null) {
            flash.addFlashAttribute("error", messageSource.getMessage("text.producto.flash.db.error", null, locale));
            return "redirect:/productos/listar";
        }

        if (id > 0) {
            productoService.delete(id);
            flash.addFlashAttribute("success",
                    messageSource.getMessage("text.producto.flash.eliminar.success", null, locale));
        }

        return "redirect:/productos/listar";
    }

}
