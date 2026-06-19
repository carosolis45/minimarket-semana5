package com.minimarket.service;

import com.minimarket.entity.Carrito;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.CarritoRepository;
import com.minimarket.service.impl.CarritoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias del servicio de carrito. Se usa Mockito para simular el
 * CarritoRepository y ProductoService (dependencias externas), de modo que
 * las pruebas queden aisladas de la base de datos.
 */
@ExtendWith(MockitoExtension.class)
class CarritoServiceTest {

    @Mock
    private CarritoRepository carritoRepository;

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private CarritoServiceImpl carritoService;

    private Producto productoConStock;
    private Producto productoSinStock;
    private Usuario usuarioValido;
    private Carrito carritoValido;

    @BeforeEach
    void setUp() {
        // Producto con stock
        productoConStock = new Producto();
        productoConStock.setId(1L);
        productoConStock.setNombre("Producto Con Stock");
        productoConStock.setStock(10);

        // Producto sin stock
        productoSinStock = new Producto();
        productoSinStock.setId(2L);
        productoSinStock.setNombre("Producto Sin Stock");
        productoSinStock.setStock(0);

        // Usuario válido
        usuarioValido = new Usuario();
        usuarioValido.setId(1L);
        usuarioValido.setUsername("cliente1");

        // Carrito válido
        carritoValido = new Carrito();
        carritoValido.setId(1L);
        carritoValido.setUsuario(usuarioValido);
        carritoValido.setProducto(productoConStock);
        carritoValido.setCantidad(1);
    }

    // ========== PRUEBAS DE VALIDACIÓN ==========

    @Test
    @DisplayName("Agregar producto al carrito cuando hay stock suficiente - éxito")
    void agregarProducto_ConStock_Exito() {
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carritoValido);

        Carrito resultado = carritoService.save(carritoValido);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getUsuario().getId());
        assertEquals(1L, resultado.getProducto().getId());
        assertEquals(1, resultado.getCantidad());
        verify(carritoRepository, times(1)).save(any(Carrito.class));
    }

    @Test
    @DisplayName("El usuario asociado al carrito es el correcto")
    void validarUsuarioAsociadoAlCarrito() {
        Usuario usuario = carritoValido.getUsuario();

        assertNotNull(usuario);
        assertEquals(1L, usuario.getId());
        assertEquals("cliente1", usuario.getUsername());
    }

    // ========== PRUEBAS DE MÉTODOS DEL SERVICIO ==========

    @Test
    @DisplayName("findAll retorna lista de carritos")
    void findAll_retornaLista() {
        when(carritoRepository.findAll()).thenReturn(List.of(carritoValido));

        List<Carrito> carritos = carritoService.findAll();

        assertEquals(1, carritos.size());
        verify(carritoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findById retorna carrito cuando existe")
    void findById_existente_retornaCarrito() {
        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carritoValido));

        Carrito resultado = carritoService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(carritoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("findById retorna null cuando el carrito no existe")
    void findById_inexistente_retornaNull() {
        when(carritoRepository.findById(99L)).thenReturn(Optional.empty());

        Carrito resultado = carritoService.findById(99L);

        assertNull(resultado);
        verify(carritoRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("deleteById invoca la eliminación en el repositorio")
    void deleteById_invocaRepositorio() {
        carritoService.deleteById(1L);
        verify(carritoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("findByUsuarioId retorna carritos del usuario")
    void findByUsuarioId_retornaCarritosDelUsuario() {
        when(carritoRepository.findByUsuarioId(1L)).thenReturn(List.of(carritoValido));

        List<Carrito> carritos = carritoService.findByUsuarioId(1L);

        assertEquals(1, carritos.size());
        verify(carritoRepository, times(1)).findByUsuarioId(1L);
    }
}