package com.AMHON.springboot.app.view.pdf;

import java.awt.Color;
import java.io.FileOutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.AMHON.springboot.app.models.entity.DetalleEntrada;
import com.AMHON.springboot.app.models.entity.Entrada;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfCell;
import com.lowagie.text.pdf.PdfDocument;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

//Clase para la creacion de pdf del detalle de entrada
@Component("entrada/verDetalleEntrada")// anotamos la clase como componente para que pueda ser auto detectada
public class EntradaPdfView extends AbstractPdfView{

	//método que construye el documento pdf
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, //función que nos ayudara a generar el documento pdf
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		Entrada entrada = (Entrada)model.get("entrada"); //obtenemos el model de entrada que contiene la informacion del detalle de entrada
		response.setHeader("Content-Disposition", "attachment; filename=\"DetalleEntrada-NumFactura-"+entrada.getNum_factura()+".pdf\""); //esta linea se cambia el nombre del archivo pdf que se genera
		Font boldFont = new Font(Font.TIMES_ROMAN, 12, Font.BOLD);
		Font boldFontT = new Font(Font.TIMES_ROMAN, 22, Font.BOLD); // tipos de letras personalizadas a utilizar en el pdf
		Font normalFont = new Font(Font.TIMES_ROMAN, 10, Font.ITALIC);
		
		/* sirve en casa
		 * Image img = Image.getInstance((
		 * "E:\\eclipse\\eclipse-workspace\\Spring-boot-data-jpa-AMHON-SGI\\src\\main\\resources\\static\\img\\logo.png"
		 * ));
		 */		//Image  img = Image.getInstance(("C:\\Spring5\\eclipse-workspace\\Spring-boot-data-jpa-AMHON-SGI\\src\\main\\resources\\static\\img\\logo.png"));//obtenemos la imagen del logo desde el directorio en el que se encuentra
		 // amhon Image  img = Image.getInstance("logo/img/logo.png"); //correcto
		
		Image  img = Image.getInstance("src/main/resources/static/logo.png");
		 img.scalePercent(35f);//código sirve para escalar el tamaño de la imagen en el archivo pdf
		document.add(img); // añadimos la imagen al documento que estamos creando
		
		document.add(new Paragraph("\n\n")); // código para un salto de linea
		
		Paragraph preface = new Paragraph( "Reporte de Entrada y Detalle",boldFontT ); //añadimos una linea de texto con el titulo del documento
		preface.setAlignment(Element.ALIGN_CENTER); //alineamos al centro el párrafo de texto que antes de agregarlo al documento
		document.add( preface ); //añadimos al documento el párrafo que se creo anteriormente
		
		document.add(new Paragraph("\n\n"));// introducmios otro salto de linea despues del párrafo de texto
		
		
		
		PdfPTable tabla = new PdfPTable(1); // creamos la primer tabla, la cual solo tendrá una celda
		tabla.setSpacingAfter(20);//agregamos espacio despues de la creacion de la tabla
		
		PdfPCell cell = null; //instanciamos PdfPCell el cual sirve para ingresar elementos a las celdas
		cell = new PdfPCell(new Phrase("Datos de Entrada", boldFont)); // Ingresamos un subtitulo para la tabla en la primer celda
		cell.setBackgroundColor(new Color(184,218,255));//agregamos color a la celda donde colocamos el subtitulo.
		
		cell.setPadding(8f); // le damos espacio al parrafo de texto
		tabla.addCell(cell); //agregamos las celdas a la tabla
		tabla.addCell("Número de Factura: "+entrada.getNum_factura()); //agregamos la columna Número de factura
		tabla.addCell("Responsable: "+entrada.getResponsbl()); // agregamos la columna Responsable	
		tabla.addCell("Proveedor: "+entrada.getProveedor().getNombre_prov()); //agregamos la columna proveedor
		tabla.addCell("Fecha: "+entrada.getFecha()); //agregamos la columna fecha
		
		document.add(tabla); // Agregamos la tablas al documento que estamos generando
		
		PdfPTable tabla2 = new PdfPTable(4); //Creamos una segunda tabla para agregar los articulos.
		tabla2.setWidths(new float[] {3.5f, 1, 1, 1}); //Le asignamos dimensiones a las tablas.
		cell = new PdfPCell(new Phrase("Producto",boldFont)); // Agregamos la columna producto.
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); //Le damos una alineación a la celda.
		tabla2.addCell(cell);//Agregamos la celda a la  segunda tabla.
		
		cell = new PdfPCell(new Phrase("Precio",boldFont)); //Agregamos la columna Precio
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); //Le damos una alineación a la celda.
		tabla2.addCell(cell);//Agregamos la celda a la  segunda tabla.
		
		
		cell = new PdfPCell(new Phrase("Cantidad",boldFont));// Agregamos la columna Cantidad.
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); //Le damos una alineación a la celda.
		tabla2.addCell(cell);//Agregamos la celda a la  segunda tabla.
		
		
		cell = new PdfPCell(new Phrase("Total",boldFont));//Agregamos la columna Total
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);//Le damos una alineación a la celda.
		tabla2.addCell(cell); //Agregamos la celda a la  segunda tabla.
		
		for(DetalleEntrada detmov: entrada.getLineasM()) { //recorremos las lineas del detalle de entrada para obtener cada registro y lo guardamos en detmov
			tabla2.addCell(detmov.getArticulo().getNombre()); // agregamos el nombre de cada articulo en secuencia
			tabla2.addCell(String.format("%.2f", detmov.getPrecio()));// agregamos el precio del articulo con un formato de 2 decimales después del punto
			
			cell = new PdfPCell(new Phrase(detmov.getCantidad().toString())); // agregamos la cantidad de manera secuencial
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); //le damos una alineacion centrada a la celda
			tabla2.addCell(cell); //agregamos la celda a la tabla2
			tabla2.addCell(String.format("%.2f", detmov.calcularImporte())); //agregamos el total del importe dandole formato de 2 cifras despues del punto
	}

		cell = new PdfPCell(new Phrase("Gran total: ",boldFont)); // agregamos el texto de Gran total
		cell.setColspan(3); // fucionamos 3 columnas para que se muestre justo antes del total
		cell.setHorizontalAlignment(PdfCell.ALIGN_RIGHT);// le damos un alineado a la derecha
		tabla2.addCell(cell);// agregamos la celda a la tablas
		tabla2.addCell(String.format("%.2f", entrada.getTotal()));// agregamos el total de la entrada ala tabla
		
		document.add(tabla2); //agregamos la segunda tabla al documento
		
		//cabe mencionar que spring se encarga de abrir y cerrar el documento automaticamente.
		
	}

	
}
