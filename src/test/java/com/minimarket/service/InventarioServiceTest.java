package com.minimarket.service;

import com.minimarket.entity.Inventario;
import com.minimarket.entity.Producto;
import com.minimarket.repository.InventarioRepository;
import com.minimarket.service.impl.InventarioServiceImpl;
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

@ExtendWith(MockitoExtension.class)
class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private InventarioServiceImpl inventarioService;

    private Producto productoValido;
    private Inventario inventarioValido;

    @BeforeEach
    void setUp() {
        productoValido = new Producto();
        productoValido.setId(1L);
        productoValido.setNombre("Producto Test");

        inventarioValido = new Inventario();
        inventarioValido.setId(1L);
        inventarioValido.setProducto(productoValido);
        inventarioValido.setTipoMovimiento("ENTRADA");
        inventarioValido.setCantidad(10);
    }

    // ========== PRUEBAS DE VALIDACIÓN DE CAMPOS ==========
    @Test
    @DisplayName("El tipo de movimiento no debe ser nulo")
    void validarTipoMovimientoNoNulo() {
        assertNotNull(inventarioValido.getTipoMovimiento());
    }

    @Test
    @DisplayName("La cantidad no debe ser nula")
    void validarCantidadNoNula() {
        assertNotNull(inventarioValido.getCantidad());
    }

    @Test
    @DisplayName("El tipo de movimiento no debe estar vacío")
    void validarTipoMovimientoNoVacio() {
        assertFalse(inventarioValido.getTipoMovimiento().isEmpty());
    }

    @Test
    @DisplayName("La cantidad debe ser mayor a cero")
    void validarCantidadMayorACero() {
        assertTrue(inventarioValido.getCantidad() > 0);
    }

    @Test
    @DisplayName("El producto asociado al inventario es el correcto")
    void validarProductoAsociado() {
        Producto producto = inventarioValido.getProducto();

        assertNotNull(producto);
        assertEquals(1L, producto.getId());
        assertEquals("Producto Test", producto.getNombre());
    }

    // ========== PRUEBAS DE MÉTODOS DEL SERVICIO ==========
    @Test
    @DisplayName("findAll retorna lista de movimientos de inventario")
    void findAll_retornaLista() {
        when(inventarioRepository.findAll()).thenReturn(List.of(inventarioValido));

        List<Inventario> movimientos = inventarioService.findAll();

        assertEquals(1, movimientos.size());
        verify(inventarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findById retorna movimiento cuando existe")
    void findById_existente_retornaMovimiento() {
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventarioValido));

        Inventario resultado = inventarioService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(inventarioRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("findById retorna null cuando el movimiento no existe")
    void findById_inexistente_retornaNull() {
        when(inventarioRepository.findById(99L)).thenReturn(Optional.empty());

        Inventario resultado = inventarioService.findById(99L);

        assertNull(resultado);
        verify(inventarioRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("deleteById invoca la eliminación en el repositorio")
    void deleteById_invocaRepositorio() {
        inventarioService.deleteById(1L);
        verify(inventarioRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("save guarda un movimiento de inventario")
    void save_guardaMovimiento() {
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventarioValido);

        Inventario resultado = inventarioService.save(inventarioValido);

        assertNotNull(resultado);
        verify(inventarioRepository, times(1)).save(any(Inventario.class));
    }
}