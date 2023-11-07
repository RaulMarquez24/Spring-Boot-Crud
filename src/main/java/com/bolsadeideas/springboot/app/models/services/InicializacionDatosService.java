package com.bolsadeideas.springboot.app.models.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bolsadeideas.springboot.app.models.entity.Producto;
import com.bolsadeideas.springboot.app.models.entity.Role;
import com.bolsadeideas.springboot.app.models.entity.Usuario;

import jakarta.annotation.PostConstruct;

@Service
public class InicializacionDatosService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IProductoService productoService;

    @PostConstruct
    public void init() {
        productListStart();
        crateStartAdmin();
    }

    private void crateStartAdmin () {
        // Crea un nuevo usuario administrador y guárdalo en la base de datos
        List<Usuario> users = usuarioService.findAll();
        if (users.isEmpty()) {

            String startUsername = "admin";
            String startPassword = "12345";
            List<Role> startRoles = Arrays.asList(new Role("ROLE_ADMIN"), new Role("ROLE_USER"));

            Usuario firstUser = new Usuario();
            firstUser.setUsername(startUsername);
            firstUser.setPassword(passwordEncoder.encode(startPassword));
            firstUser.setRoles(startRoles);
            firstUser.setEnabled(true);
            firstUser.setAdmin(true);
            usuarioService.save(firstUser);

            System.out.println("Crea un nuevo administrador propio y borre esté de inicio");
            System.out.println("Usuario de Inicio: " + startUsername);
            System.out.println("Contraseña de Inicio: " + startPassword);
            System.out.println("En el archivo 'db.sql' tienes ejemplos de insert de clientes y facturas para realizar las pruebas si no quieres hacerlo manual ");

        }
    }

    private void productListStart() {
        // Inicializar lista de productos en bbdd
        List<Producto> productos = productoService.findAll();
        if (productos.isEmpty()) {

            productos.add(new Producto("Camiseta de algodón", 19.99));
            productos.add(new Producto("Zapatillas deportivas", 59.99));
            productos.add(new Producto("Pantalones vaqueros", 29.99));
            productos.add(new Producto("Chaqueta de cuero", 89.99));
            productos.add(new Producto("Bolso de mano", 39.99));
            productos.add(new Producto("Reloj de pulsera", 79.99));
            productos.add(new Producto("Gafas de sol", 14.99));
            productos.add(new Producto("Sombrero de verano", 9.99));
            productos.add(new Producto("Zapatos de vestir", 49.99));
            productos.add(new Producto("Bicicleta de montaña", 349.99));
            productos.add(new Producto("Mesa de comedor", 149.99));
            productos.add(new Producto("Sofá de tres plazas", 299.99));
            productos.add(new Producto("Smartphone Android", 349.99));
            productos.add(new Producto("Portátil de 15 pulgadas", 599.99));
            productos.add(new Producto("Televisor LED de 55 pulgadas", 799.99));

            productoService.saveAll(productos);

            System.out.println("Productos de ejemplo: (InicializacionDatosSevice.class)");

            for (Producto producto : productos) {
                System.out.println(producto.toString());
            }

        }
    }
}
