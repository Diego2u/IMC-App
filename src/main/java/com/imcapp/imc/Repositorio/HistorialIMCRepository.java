package com.imcapp.imc.Repositorio;

import com.imcapp.imc.modelo.HistorialIMC;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HistorialIMCRepository extends JpaRepository<HistorialIMC, Long> {
    List<HistorialIMC> findByNombreUsuario(String nombreUsuario);
}