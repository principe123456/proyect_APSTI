package com.example.proyect_APSTI.controller;

import com.example.proyect_APSTI.model.Usuario;
import com.example.proyect_APSTI.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class usuarioocontroller {
    private final UsuarioRepository repositorio;
    private final PasswordEncoder passwordEncoder;

    public usuarioocontroller(UsuarioRepository repositorio, PasswordEncoder passwordEncoder) {
        this.repositorio = repositorio;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/usuarios")
    public String listar(Model model) {
        if (repositorio.count() == 0) {
            Usuario u1 = new Usuario("Carlos Ruiz", "carlos@correo.com", "ADMIN", "Activo");
            Usuario u2 = new Usuario("María Pérez", "maria@correo.com", "EDITOR", "Activo");
            Usuario u3 = new Usuario("Juan Silva", "juan@correo.com", "USUARIO", "Activo");
            u1.setContrasena(passwordEncoder.encode("Demo123!"));
            u2.setContrasena(passwordEncoder.encode("Demo123!"));
            u3.setContrasena(passwordEncoder.encode("Demo123!"));
            repositorio.save(u1);
            repositorio.save(u2);
            repositorio.save(u3);
        }
        model.addAttribute("usuarios", repositorio.findAll());
        return "usuarios";
    }

    @GetMapping("/usuarios/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("titulo", "Registro de usuario");
        return "formulario";
    }

    @PostMapping("/usuarios/guardar")
    public String guardar(@ModelAttribute Usuario usuario, RedirectAttributes aviso) {
        if (usuario.getNombre() == null || usuario.getNombre().isBlank()
                || usuario.getCorreo() == null || usuario.getCorreo().isBlank()) {
            aviso.addFlashAttribute("error", "Completa el nombre y el correo electrónico.");
            return "redirect:/usuarios/nuevo";
        }

        if (usuario.getId() == null) {
            if (usuario.getContrasena() == null || usuario.getContrasena().isBlank()) {
                aviso.addFlashAttribute("error", "La contraseña es obligatoria para un usuario nuevo.");
                return "redirect:/usuarios/nuevo";
            }
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        } else {
            Usuario existente = repositorio.findById(usuario.getId()).orElseThrow();
            if (usuario.getContrasena() == null || usuario.getContrasena().isBlank()) {
                usuario.setContrasena(existente.getContrasena());
            } else {
                usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
            }
        }

        repositorio.save(usuario);
        aviso.addFlashAttribute("mensaje", "Usuario guardado correctamente.");
        return "redirect:/usuarios";
    }

    @GetMapping("/usuarios/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Usuario usuario = repositorio.findById(id).orElseThrow();
        usuario.setContrasena("");
        model.addAttribute("usuario", usuario);
        model.addAttribute("titulo", "Editar usuario");
        return "formulario";
    }

    @PostMapping("/usuarios/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes aviso) {
        repositorio.deleteById(id);
        aviso.addFlashAttribute("mensaje", "Usuario eliminado correctamente.");
        return "redirect:/usuarios";
    }
}
