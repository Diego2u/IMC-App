package com.imcapp.imc.controlador;

import com.imcapp.imc.modelo.HistorialIMC;
import com.imcapp.imc.Repositorio.HistorialIMCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historial")
public class HistorialController {

    @Autowired
    private HistorialIMCRepository historialRepo;

    @GetMapping("/{nombreUsuario}")
    public List<HistorialIMC> obtenerHistorial(@PathVariable String nombreUsuario) {
        return historialRepo.findByNombreUsuario(nombreUsuario);
    }
}