package com.imcapp.imc.controlador;

import com.imcapp.imc.Repositorio.HistorialIMCRepository;
import com.imcapp.imc.modelo.HistorialIMC;
import com.imcapp.imc.modelo.Usuario;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Controller
public class LoginController {

    @Autowired
    private UsuarioController usuarioController;
    @Autowired
    private HistorialIMCRepository historialRepo;

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String nombreUsuario,
                                 @RequestParam String contrasena,
                                 HttpSession session,
                                 Model model) {

        Usuario usuarioRegistrado = usuarioController.getUsuarioRegistrado();

        if (usuarioRegistrado != null &&
            usuarioRegistrado.getNombreUsuario().equals(nombreUsuario) &&
            usuarioRegistrado.getContrasena().equals(contrasena)) {

            session.setAttribute("usuario", usuarioRegistrado);
            return "redirect:/imc";
        }

        model.addAttribute("error", "Nombre de usuario o contraseña incorrectos");
        return "login";
    }

    @GetMapping("/imc")
public String mostrarFormularioImc(HttpSession session, Model model) {
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) {
        return "redirect:/login";
    }

    model.addAttribute("usuario", usuario);

    List<HistorialIMC> historial = historialRepo.findByNombreUsuario(usuario.getNombreUsuario());
    model.addAttribute("historial", historial);

    return "imc";
}

    @PostMapping("/imc")
    public String calcularImc(@RequestParam double peso, HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || peso <= 0) {
            return "redirect:/login";
        }

        double estatura = usuario.getEstatura();
        double imc = peso / (estatura * estatura);
        String clasificacion = clasificarIMC(imc);


        HistorialIMC registro = new HistorialIMC();
registro.setNombreUsuario(usuario.getNombreUsuario());
registro.setPeso(peso);
registro.setImc(imc);
registro.setClasificacion(clasificacion);
registro.setFecha(LocalDateTime.now());

historialRepo.save(registro);

List<HistorialIMC> historial = historialRepo.findByNombreUsuario(usuario.getNombreUsuario());
model.addAttribute("historial", historial);

        model.addAttribute("usuario", usuario);
        model.addAttribute("imc", String.format("%.2f", imc));
        model.addAttribute("clasificacion", clasificacion);

        return "imc";
}


private String clasificarIMC(double imc) {
    if (imc < 18.5) return "Bajo peso";
    else if (imc < 25) return "Normal";
    else if (imc < 30) return "Sobrepeso";
    else return "Obesidad";
}

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}