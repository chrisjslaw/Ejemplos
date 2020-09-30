package com.AMHON.springboot.app.models.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;

import com.AMHON.springboot.app.models.entity.Articulo;
import com.AMHON.springboot.app.models.entity.Audit;
import com.AMHON.springboot.app.models.entity.Categoria;
import com.AMHON.springboot.app.models.entity.Departamento;
import com.AMHON.springboot.app.models.entity.DetalleEntrada;
import com.AMHON.springboot.app.models.entity.DetalleRequisicion;
import com.AMHON.springboot.app.models.entity.Isv;
import com.AMHON.springboot.app.models.entity.Kardex;
import com.AMHON.springboot.app.models.entity.Entrada;
import com.AMHON.springboot.app.models.entity.Info;
import com.AMHON.springboot.app.models.entity.Proveedor;
import com.AMHON.springboot.app.models.entity.Requisicion;
import com.AMHON.springboot.app.models.entity.Role;
import com.AMHON.springboot.app.models.entity.UnidadMedida;
import com.AMHON.springboot.app.models.entity.Usuario;

public interface IClienteService {



	//usuario

	public List<Usuario> encontrarUsuarios();

	public Page<Usuario> encontrarUsuarios(Pageable pageable);

	public void save(Usuario usuario);// guardar usuario

	public Usuario findOneUser(Long id);
	
	public Usuario findByUsername(String username);

	
	public List<Usuario> UsuariosxDepartamento(Long id);

	// public Usuario findByRole(Long term);

	public List<Proveedor> encontrarProveedores();

	public Page<Proveedor> encontrarProveedores(Pageable pageable);

	public void saveProveedor(Proveedor proveedor);// guardar proveedor

	public Proveedor encontrarProveedor(Long id);

	public void EliminarProv(Long id);

	// metodos de movimientos entrada
	public void saveMovimiento(Entrada entrada);
	
	public List<Entrada> encontrarEntradas();

	public Page<Entrada> encontrarEntradas(Pageable pageable);
	
	public Entrada encontrarEntrada(Long id);

	public void EliminarMovimiento(Long id);
	
	public List<Entrada> findByFechasEntrada(Date fechaIn, Date fechaFin);
	
	public List<Entrada> findByCantidadxArticulosEntrantexfechas(Date fechaIn, Date fechaFin);
	
	public List<Entrada> findByCantidadxArticulosEntrantexfechasGrafic(Date fechaIn, Date fechaFin);
	
	// metodos articulos

	public List<Articulo> encontrarArticulos();

	public List<Articulo> findByNombreArt(String term);
	
	public List<Articulo> findByNombreArtExist(String term);

	public Articulo findArticuloById(Long id);

	public void saveArticulo(Articulo articulo);// guardar articulo

	public void EliminarArtic(Long id);
	
	//Metodos Departamento
	public void saveDepartamento(Departamento depto);
	
	public List<Departamento> encontrarDepartamentos();

	public Page<Departamento> encontrarDepartamentos(Pageable pageable);
	
	public Departamento encontrarDepartamento(Long id);

	public void EliminarDepartamento(Long id);

	// Metodos Unidad de medida

	public List<UnidadMedida> encontrarUM();

	public UnidadMedida findUMById(Long id);

	public void saveUM(UnidadMedida unidad);// guardar Unidad de medida

	public void EliminarUM(Long id);
	
	//Metodos de categoria
	
	public List<Categoria> encontrarCategorias();

	public Page<Categoria> encontrarCategorias(Pageable pageable);

	public void saveCategoria(Categoria categoria);// guardar categoria

	public Categoria encontrarCategoria(Long id);

	public void EliminarCategoria(Long id);
	
	//Metodos de Kardex
	
	public List<Kardex> encontrarKardexList();

	public Page<Kardex> encontrarKardexpag(Pageable pageable);

	public void saveKardex(Kardex Kardex);// guardar kardex

	public Kardex encontrarKardex(Long id);

	public void EliminarKardex(Long id);
	
	public void deleteByDetalleReq(Long id);
	
	public List<Kardex> findByArticulo(Long id);
	
	public Kardex findByDetalleMov(Long id);
	
	public Kardex findByDetalleReq(Long id);
	
	public List<Kardex> findByFechasKardex(Date fechaIn, Date fechaFin);
	
	//Metodos DetalleEntrada
	
	public List<DetalleEntrada> findById_Artic(Long id);
	public DetalleEntrada encontrarDetalleEntrada(Long id);
	public void saveDetalleEntrada(DetalleEntrada detmov);
	public void deleteDetalleEntrada(Long id);
	public List<DetalleEntrada> findByIdEntrada(Long id);
	public List<DetalleEntrada> findByProductxProveedor(Date fechaIn, Date fechaFin, Long id);
	public List<DetalleEntrada> findByEntradasxProductxFecha(Date fechaIn, Date fechaFin, Long id);
	public List<DetalleEntrada> GastosxEntradaxproveedorxFecha(Date fechaIn, Date fechaFin);

	
	//Métodos de Requisicion
	
public void saveRequisicion(Requisicion req);
	
	public List<Requisicion> encontrarRequisiciones();

	public Page<Requisicion> encontrarRequisiciones(Pageable pageable);
	
	public Requisicion encontrarRequisicion(Long id);

	public void EliminarRequisicion(Long id);
	
	public List<Requisicion> findByEstado(String term);
	
	public List<Requisicion> findByFechaTresMeses(Long id);
	
	public List<Requisicion> findByFechasRequisicion(Date fechaIn, Date fechaFin);
	
	public List<Requisicion> findByFechasRequisicionesEntregadas(Date fechaIn, Date fechaFin);
	
	public List<Requisicion> findByFechasRequisicionesEntregadasGrafic(Date fechaIn, Date fechaFin);
	
	
	//métodos DetalleRequisicion
	
	public List<DetalleRequisicion> findById_ArticReq(Long id);
	
	public List<DetalleRequisicion> findById_Req(Long id);
	
	public DetalleRequisicion encontrarDetalleReq(Long id); 
	 
	public void saveDetalleRequisicion(DetalleRequisicion detreq);
	
	public void deleteDetalleRequisicion(Long id);
	
	public List<DetalleRequisicion> findByRequisicionesxArticulosxUsuarioxFecha(Date fechaIn, Date fechaFin);
	public List<DetalleRequisicion> GastosxRequisicionxdepartamentoxFecha(Date fechaIn, Date fechaFin);
	public List<DetalleRequisicion> findByRequisicionesxUsuarioxFecha(Date fechaIn, Date fechaFin, Long id);
	public List<DetalleRequisicion> findByDepartamentoEspecificoxFecha(Date fechaIn, Date fechaFin, String depto);
	public List<DetalleRequisicion> GastosxRequisicionxdepartamentoxFechamodifc(Date fechaIn, Date fechaFin);

	
	
	//métodos de isv
	public Isv encontrarIsv(Long id);
	public void saveIsv(Isv isv);
	
	
	// métodos Info
	public Info encontrarInfo(Long id);
	public void saveInfo(Info info);
	

}
