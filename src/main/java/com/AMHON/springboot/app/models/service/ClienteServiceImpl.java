package com.AMHON.springboot.app.models.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.hibernate.envers.AuditReaderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.AMHON.springboot.app.models.dao.IArticuloDao;
import com.AMHON.springboot.app.models.dao.IBitacoraDao;
import com.AMHON.springboot.app.models.dao.ICategoriaDao;
import com.AMHON.springboot.app.models.dao.IDepartamentoDao;
import com.AMHON.springboot.app.models.dao.IDetalleEntrDao;
import com.AMHON.springboot.app.models.dao.IDetalleReqDao;
import com.AMHON.springboot.app.models.dao.IEntradaDao;
import com.AMHON.springboot.app.models.dao.IInfoDao;
import com.AMHON.springboot.app.models.dao.IIsvDao;
import com.AMHON.springboot.app.models.dao.IKardexDao;
import com.AMHON.springboot.app.models.dao.IProveedorDao;
import com.AMHON.springboot.app.models.dao.IRequisicionDao;
import com.AMHON.springboot.app.models.dao.IRoleDao;
import com.AMHON.springboot.app.models.dao.IUnidadMedidaDao;
import com.AMHON.springboot.app.models.dao.IUsuarioDao;
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
import com.AMHON.springboot.app.models.entity.UnidadMedida;
import com.AMHON.springboot.app.models.entity.Usuario;

@Service
public class ClienteServiceImpl implements IClienteService{

	@PersistenceContext
    private EntityManager entityManager;
	
	
	@Autowired
	private IUsuarioDao usuarioDao;
	
	@Autowired
	private IBitacoraDao bitacoraDao;
	
	@Autowired
	private IProveedorDao proveedorDao;
	
	@Autowired
	private IEntradaDao entradaDao;
	
	@Autowired
	private IArticuloDao articuloDao;
	
	@Autowired
	private IUnidadMedidaDao unidadMDao;
	
	@Autowired
	private ICategoriaDao categoriaDao;
	
	@Autowired
	private IKardexDao kardexDao;
	
	@Autowired
	private IDetalleEntrDao detMovDao;
	
	@Autowired
	private IDetalleReqDao detReqDao;
	
	@Autowired
	private IRequisicionDao reqDao;
	
	@Autowired
	private IIsvDao isvDao;
	
	@Autowired
	private IDepartamentoDao deptoDao;
	
	@Autowired
	private IRoleDao roleDao;
	
	@Autowired
	private IInfoDao infoDao;
	
	
	//métodos Usuario

	@Override
	@Transactional(readOnly=true)// para que no permita cambios de la base de datos por ejemplo llamar update, delete.
	public List<Usuario> encontrarUsuarios() {
		return (List<Usuario>) usuarioDao.findAll();	}

	@Override
	@Transactional(readOnly= true)
	public Page<Usuario> encontrarUsuarios(Pageable pageable) {
		return usuarioDao.findAll(pageable);
	}

	@Override
	@Transactional //sin readonly ya que es para insertar por ende se modifica la tabla
	public void save(Usuario usuario) {
		 usuarioDao.save(usuario);
	}

	
	@Override
	@Transactional(readOnly=true)
	public Usuario findOneUser(Long id) {
		return usuarioDao.findById(id).orElse(null);
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public List<Usuario> UsuariosxDepartamento(Long id) {
		return usuarioDao.UsuariosxDepartamento(id);
	}


	@Override
	@Transactional(readOnly=true)
	public Usuario findByUsername(String username) {
		return usuarioDao.findByUsername(username);
	}


	

	//métodos de proveedor
	
	@Override
	@Transactional(readOnly=true)
	public List<Proveedor> encontrarProveedores() {
		return (List<Proveedor>) proveedorDao.findAll();
	}

	@Override
	@Transactional(readOnly=true)
	public Page<Proveedor> encontrarProveedores(Pageable pageable) {
		
		return proveedorDao.findAll(pageable);
	}

	@Override
	@Transactional
	public void saveProveedor(Proveedor proveedor) {
        proveedorDao.save(proveedor);		
	}

	@Override
	@Transactional(readOnly=true)
	public Proveedor encontrarProveedor(Long id) {
		return proveedorDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void EliminarProv(Long id) {
		proveedorDao.deleteById(id);		
	}

	
	// métodos de Entrada

	@Override
	@Transactional
	public void saveMovimiento(Entrada entrada) {
		entradaDao.save(entrada);
		
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Entrada> encontrarEntradas() {
		return (List<Entrada>) entradaDao.findAll();
	}

	@Override
	@Transactional(readOnly=true)
	public Page<Entrada> encontrarEntradas(Pageable pageable) {
		return entradaDao.findAll(pageable);
	}

	@Override
	@Transactional(readOnly=true)
	public Entrada encontrarEntrada(Long id) {
		return entradaDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void EliminarMovimiento(Long id) {
		entradaDao.deleteById(id);	
		
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Entrada> findByFechasEntrada(Date fechaIn, Date fechaFin) {
		return entradaDao.findByFechasEntrada(fechaIn, fechaFin);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Entrada> findByCantidadxArticulosEntrantexfechas(Date fechaIn, Date fechaFin) {
		return entradaDao.findByCantidadxArticulosEntrantexfechas(fechaIn, fechaFin);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Entrada> findByCantidadxArticulosEntrantexfechasGrafic(Date fechaIn, Date fechaFin) {
		
		return entradaDao.findByCantidadxArticulosEntrantexfechasGrafic(fechaIn, fechaFin);
	}


	
	//metodos de articulos
	@Override
	@Transactional(readOnly=true)
	public List<Articulo> findByNombreArt(String term) {
		return articuloDao.findByNombreArt(term);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Articulo> findByNombreArtExist(String term) {
		// TODO Auto-generated method stub
		return articuloDao.findByNombreArtExist(term);
	}

	@Override
	@Transactional(readOnly=true)
	public Articulo findArticuloById(Long id) {
		return articuloDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void saveArticulo(Articulo articulo) {
		articuloDao.save(articulo);
	}

	@Override
	@Transactional
	public void EliminarArtic(Long id) {
		articuloDao.deleteById(id);
		
	}

	@Override
	@Transactional(readOnly=true)
	public List<Articulo> encontrarArticulos() {
	 return (List<Articulo>) articuloDao.findAll();
	}

	@Override
	@Transactional(readOnly=true)
	public List<UnidadMedida> encontrarUM() {
		return (List<UnidadMedida>) unidadMDao.findAll();
	}

	@Override
	@Transactional(readOnly=true)
	public UnidadMedida findUMById(Long id) {
		return unidadMDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void saveUM(UnidadMedida unidad) {
		
		unidadMDao.save(unidad);
	}

	@Override
	@Transactional
	public void EliminarUM(Long id) {

		unidadMDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly=true)
	public List<Categoria> encontrarCategorias() {
		return (List<Categoria>) categoriaDao.findAll();
		
	}

	@Override
	@Transactional(readOnly=true)
	public Page<Categoria> encontrarCategorias(Pageable pageable) {
		return categoriaDao.findAll(pageable);
	}

	@Override
	@Transactional
	public void saveCategoria(Categoria categoria) {
		categoriaDao.save(categoria);
	}

	@Override
	@Transactional(readOnly=true)
	public Categoria encontrarCategoria(Long id) {
		return categoriaDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void EliminarCategoria(Long id) {
		categoriaDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly=true)
	public List<Kardex> encontrarKardexList() {
		return (List<Kardex>) kardexDao.findAll();
	}

	@Override
	@Transactional(readOnly=true)
	public Page<Kardex> encontrarKardexpag(Pageable pageable) {
		return kardexDao.findAll(pageable);
	}

	@Override
	@Transactional
	public void saveKardex(Kardex Kardex) {
		kardexDao.save(Kardex);
		
	}

	@Override
	@Transactional(readOnly=true)
	public Kardex encontrarKardex(Long id) {
		return kardexDao.findById(id).orElse(null);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Kardex> findByArticulo(Long id) {
		return (List<Kardex>) kardexDao.findByArticulo(id);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Kardex findByDetalleMov(Long id) {
		return kardexDao.findByDetalleMov(id);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Kardex findByDetalleReq(Long id) {
		return kardexDao.findByDetalleReq(id);

	}
	

	@Override
	@Transactional
	public void EliminarKardex(Long id) {
		kardexDao.deleteById(id);
	}
	
	@Override
	@Transactional
	public void deleteByDetalleReq(Long id) {
		kardexDao.deleteByDetalleReq(id);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Kardex> findByFechasKardex(Date fechaIn, Date fechaFin) {
		return kardexDao.findByFechasKardex(fechaIn, fechaFin);
	}
	

	//detalleEntradas
	@Override
	@Transactional(readOnly=true)
	public List<DetalleEntrada> findById_Artic(Long id) {
		return (List<DetalleEntrada>) detMovDao.findByArticulo(id);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<DetalleEntrada> findByProductxProveedor( Date fechaIn, Date fechaFin, Long id) {
		
		return detMovDao.findByProductxProveedor(fechaIn, fechaFin, id);
	}

	@Override
	@Transactional(readOnly=true)
	public List<DetalleEntrada> findByEntradasxProductxFecha(Date fechaIn, Date fechaFin, Long id) {
		
		return detMovDao.findByEntradasxProductxFecha(fechaIn, fechaFin, id);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<DetalleEntrada> GastosxEntradaxproveedorxFecha(Date fechaIn, Date fechaFin) {
		return detMovDao.GastosxEntradaxproveedorxFecha(fechaIn, fechaFin);
	}


	
	
	@Override
	@Transactional(readOnly=true)
	public List<DetalleEntrada> findByIdEntrada(Long id) {
		return (List<DetalleEntrada>) detMovDao.findByIdEntrada(id);
	}
	
	@Override
	@Transactional(readOnly=true)
	public DetalleEntrada encontrarDetalleEntrada(Long id) {
		 return detMovDao.findById(id).orElse(null);
	}
	
	@Override
	@Transactional
	public void saveDetalleEntrada(DetalleEntrada detmov) {
		detMovDao.save(detmov);
	}
	
	@Override
	@Transactional
	public void deleteDetalleEntrada(Long id) {
		   detMovDao.deleteById(id);		
		
	}
	
	/* requisicion */

	@Override
	@Transactional
	public void saveRequisicion(Requisicion req) {
		reqDao.save(req);
		
	}

	@Override
	@Transactional(readOnly=true)
	public List<Requisicion> encontrarRequisiciones() {
		return (List<Requisicion>) reqDao.findAll();
	}

	@Override
	@Transactional(readOnly=true)
	public Page<Requisicion> encontrarRequisiciones(Pageable pageable) {
		return reqDao.findAll(pageable);
	}

	@Override
	@Transactional(readOnly=true)
	public Requisicion encontrarRequisicion(Long id) {
		return reqDao.findById(id).orElse(null);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Requisicion> findByFechasRequisicion(Date fechaIn, Date fechaFin) {
		return reqDao.findByFechasRequisicion(fechaIn, fechaFin);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Requisicion> findByFechasRequisicionesEntregadas(Date fechaIn, Date fechaFin) {
		// TODO Auto-generated method stub
		return reqDao.findByFechasRequisicionesEntregadas(fechaIn, fechaFin);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Requisicion> findByFechasRequisicionesEntregadasGrafic(Date fechaIn, Date fechaFin) {
		return reqDao.findByFechasRequisicionesEntregadasGrafic(fechaIn, fechaFin);
	}
	
	
	@Override
	@Transactional
	public void EliminarRequisicion(Long id) {
		reqDao.deleteById(id);
		
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Requisicion> findByFechaTresMeses(Long id) {
		return reqDao.findByFechaTresMeses(id);
	}

	@Override
	@Transactional(readOnly=true)
	public List<Requisicion> findByEstado(String term) {
		return reqDao.findByEstado(term);
	}
	
	//detalleRequisicion

	@Override
	@Transactional(readOnly=true)
	public DetalleRequisicion encontrarDetalleReq(Long id) {
		return detReqDao.findById(id).orElse(null);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<DetalleRequisicion> findById_ArticReq(Long id) {
		return (List<DetalleRequisicion>) detReqDao.findByArticuloReq(id);
	}

	
	

	@Override
	@Transactional(readOnly=true)
	public List<DetalleRequisicion> findById_Req(Long id) {
		return (List<DetalleRequisicion>) detReqDao.findByIdReq(id);
	}
	
	@Override
	@Transactional
	public void saveDetalleRequisicion(DetalleRequisicion detreq) {
		detReqDao.save(detreq);
		
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<DetalleRequisicion> findByRequisicionesxArticulosxUsuarioxFecha(Date fechaIn, Date fechaFin) {
		return detReqDao.findByRequisicionesxArticulosxUsuarioxFecha(fechaIn, fechaFin);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<DetalleRequisicion> GastosxRequisicionxdepartamentoxFecha(Date fechaIn, Date fechaFin) {
		return detReqDao.GastosxRequisicionxdepartamentoxFecha(fechaIn, fechaFin);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<DetalleRequisicion> GastosxRequisicionxdepartamentoxFechamodifc(Date fechaIn, Date fechaFin) {
		// 
		return detReqDao.GastosxRequisicionxdepartamentoxFechamodifc(fechaIn, fechaFin);
	}

	
	@Override
	@Transactional(readOnly=true)
	public List<DetalleRequisicion> findByRequisicionesxUsuarioxFecha(Date fechaIn, Date fechaFin, Long id) {
		return detReqDao.findByRequisicionesxUsuarioxFecha(fechaIn, fechaFin, id);
	}
	
	@Override
	public List<DetalleRequisicion> findByDepartamentoEspecificoxFecha(Date fechaIn, Date fechaFin, String depto) {
		return detReqDao.findByDepartamentoEspecificoxFecha(fechaIn, fechaFin, depto);
	}
	
	@Override
	@Transactional
	public void deleteDetalleRequisicion(Long id) {
        detReqDao.deleteById(id);		
	}
	
	// metodos de isv

	@Override
	@Transactional(readOnly=true)
	public Isv encontrarIsv(Long id) {
		return isvDao.findById(id).orElse(null);

	}

	@Override
	@Transactional
	public void saveIsv(Isv isv) {
		isvDao.save(isv);
		
	}

	

	//Métodos para departamento
	@Override
	@Transactional
	public void saveDepartamento(Departamento depto) {
	      deptoDao.save(depto);
		
	}

	@Override
	@Transactional(readOnly=true)
	public List<Departamento> encontrarDepartamentos() {
		return (List<Departamento>) deptoDao.findAll();
	}

	@Override
	@Transactional(readOnly=true)
	public Page<Departamento> encontrarDepartamentos(Pageable pageable) {
		return deptoDao.findAll(pageable);
	}

	@Override
	@Transactional(readOnly=true)
	public Departamento encontrarDepartamento(Long id) {
		return deptoDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void EliminarDepartamento(Long id) {
		deptoDao.deleteById(id);
		
	}

	
	//Métodos para info
	@Override
	@Transactional(readOnly=true)
	public Info encontrarInfo(Long id) {
		return infoDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void saveInfo(Info info) {
		infoDao.save(info);
		
	}


}
