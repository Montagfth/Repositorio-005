package com.cursoIntegradorI.proyectoFinal.service;

import com.cursoIntegradorI.proyectoFinal.model.Personal;
import com.cursoIntegradorI.proyectoFinal.repository.PersonalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonalService {

    private final PersonalRepository personalRepository;

    @Transactional(readOnly = true)
    public List<Personal> listarTodos() {
        return personalRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Personal> buscarPorId(Integer id) {
        return personalRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Personal> buscarPorCorreo(String correo) {
        return personalRepository.findByCorreo(correo);
    }

    @Transactional(readOnly = true)
    public List<Personal> buscarPorCargo(String cargo) {
        return personalRepository.findByCargo(cargo);
    }

    @Transactional(readOnly = true)
    public List<Personal> buscarPorTelefono(String telefono) {
        return personalRepository.findByCargo(telefono);
    }

    @Transactional
    public Personal guardar(Personal personal) {
        // Validación de correo duplicado
        personalRepository.findByCorreo(personal.getCorreo())
                .ifPresent(p -> {
                    if (!p.getIdPersonal().equals(personal.getIdPersonal())) {
                        throw new IllegalArgumentException("El correo ya está registrado");
                    }
                });
        return personalRepository.save(personal);
    }

    @Transactional
    public void eliminar(Integer id) {
        personalRepository.deleteById(id);
    }
}