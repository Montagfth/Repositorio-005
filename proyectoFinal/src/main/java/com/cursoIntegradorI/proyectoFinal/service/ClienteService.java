package com.cursoIntegradorI.proyectoFinal.service;

import com.cursoIntegradorI.proyectoFinal.model.Cliente;
import com.cursoIntegradorI.proyectoFinal.repository.ClienteRepository;
import com.cursoIntegradorI.proyectoFinal.repository.ProyectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ProyectoRepository proyectoRepository;  // ✅ AGREGAR

    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorId(Integer id) {
        return clienteRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorRuc(String ruc) {
        return clienteRepository.findByRuc(ruc);
    }

    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorCorreo(String correo) {
        return clienteRepository.findByCorreo(correo);
    }

    @Transactional(readOnly = true)
    public List<Cliente> buscarPorNombre(String nombre) {
        return clienteRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Transactional
    public Cliente guardar(Cliente cliente) {
        // Validar RUC duplicado
        Optional<Cliente> clienteExistenteRuc = clienteRepository.findByRuc(cliente.getRuc());
        if (clienteExistenteRuc.isPresent()) {
            Cliente existente = clienteExistenteRuc.get();
            // Solo lanzar error si es un cliente DIFERENTE
            if (cliente.getIdCliente() == null || !existente.getIdCliente().equals(cliente.getIdCliente())) {
                throw new IllegalArgumentException("Ya existe un cliente con el RUC: " + cliente.getRuc());
            }
        }

        // Validar correo duplicado (si está presente)
        if (cliente.getCorreo() != null && !cliente.getCorreo().trim().isEmpty()) {
            Optional<Cliente> clienteExistenteCorreo = clienteRepository.findByCorreo(cliente.getCorreo());
            if (clienteExistenteCorreo.isPresent()) {
                Cliente existente = clienteExistenteCorreo.get();
                if (cliente.getIdCliente() == null || !existente.getIdCliente().equals(cliente.getIdCliente())) {
                    throw new IllegalArgumentException("Ya existe un cliente con el correo: " + cliente.getCorreo());
                }
            }
        }

        // ✅ IMPORTANTE: No cargar la lista de proyectos antes de guardar
        // Esto evita que Hibernate intente actualizar los proyectos
        cliente.setProyectos(null);

        return clienteRepository.save(cliente);
    }

    @Transactional
    public void eliminar(Integer id) {
        // ✅ Usar consulta directa al repositorio de proyectos en lugar de cargar la relación
        long proyectosAsociados = proyectoRepository.countByCliente_IdCliente(id);

        if (proyectosAsociados > 0) {
            throw new IllegalStateException(
                    "No se puede eliminar el cliente porque tiene " +
                            proyectosAsociados +
                            " proyecto(s) asociado(s). Primero elimine o reasigne los proyectos."
            );
        }

        clienteRepository.deleteById(id);
    }

    /**
     * Cuenta cuántos proyectos tiene asociados un cliente
     */
    @Transactional(readOnly = true)
    public long contarProyectosAsociados(Integer idCliente) {
        return proyectoRepository.countByCliente_IdCliente(idCliente);
    }
}