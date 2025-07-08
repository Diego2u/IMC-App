package com.imcapp.imc.controlador;

import com.imcapp.imc.modelo.Usuario;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class UsuarioController {

    private Usuario usuarioRegistrado;

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@Valid @ModelAttribute Usuario usuario, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "registro";
        }

        
        double imc = calcularIMC(usuario);
        String clasificacion = clasificarIMC(imc);
        usuario.setImc(imc);
        usuario.setClasificacion(clasificacion);

        
        this.usuarioRegistrado = usuario;

        model.addAttribute("usuario", usuario);
        return "resultado";
    }

    private double calcularIMC(Usuario usuario) {
        return usuario.getPeso() / Math.pow(usuario.getEstatura(), 2);
    }

    private String clasificarIMC(double imc) {
        if (imc < 18.5) return "Bajo peso";
        else if (imc < 25) return "Normal";
        else if (imc < 30) return "Sobrepeso";
        else return "Obesidad";
    }

    
    public Usuario getUsuarioRegistrado() {
        return usuarioRegistrado;
    }
}