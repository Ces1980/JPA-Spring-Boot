package com.ideasbolsa.springboot.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "clientes")
public class Cliente implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	private String nombre;
	
	@NotEmpty
	private String apellido;
	
	@NotEmpty
	@Email
	private String email;

	@NotNull
	@Column(name = "create_at")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-mm-dd")
	private Date createAt;

	/*Anotación que indica que un cliente puede generar muchas facturas
	 * One = > un cliente
	 * ToMany = > muchas facturas*/
	/* fetch = FetchType.LAZY = > Estrategia de carga de datos: peude ser EAGER o LAZY
	* Se recomienda EAZY */
	
	/* cascade=CascadeType.ALL:
	 * 
	 * (Opcional) Las operaciones que deben conectarse en cascada 
	 * con el objetivo de la asociación. 
	 * El valor predeterminado es que no hay operaciones en cascada.
	 * Cuando la colección de destino es un java.util.Map, 
	 * el elemento en cascada se aplica al valor del mapa.
	 * */
	
	/*mappedBy ="cliente"
	 * 
	 * Para relacionar ambas llaves en la base de datos
	 * */
	@OneToMany(mappedBy ="cliente" ,fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	private List<Factura> facturas;
	
	/**
	 * Se crea un constructor para inicializar 
	 * una factura cuando se crea un cliente*/
	public Cliente() {
		facturas = new ArrayList<Factura>();
	}

	private String foto;

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public List<Factura> getFacturas() {
		return facturas;
	}

	public void setFacturas(List<Factura> facturas) {
		this.facturas = facturas;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	/**
	 * Método que guarda la factura generada una por una*/
	public void addFactura(Factura factura) {
		facturas.add(factura);
	}
	
	private static final long serialVersionUID = 1L;

}
