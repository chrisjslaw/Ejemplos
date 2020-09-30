package com.AMHON.springboot.app.view.pdf;

import java.awt.Color;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.AMHON.springboot.app.models.entity.DetalleRequisicion;
import com.AMHON.springboot.app.models.entity.Requisicion;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfCell;
import com.lowagie.text.pdf.PdfDocument;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.VerticalPositionMark;

//Clase para la creacion de pdf del detalle de entrada
@Component("requisicion/verDetalleRequisicion")// anotamos la clase como componente para que pueda ser auto detectada
public class RequisicionPdfView extends AbstractPdfView{

	//método que construye el documento pdf
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, //función que nos ayudara a generar el documento pdf
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		Requisicion requisicion = (Requisicion)model.get("requisicion"); //obtenemos el model de entrada que contiene la informacion del detalle de entrada
		response.setHeader("Content-Disposition", "attachment; filename=\"DetalleRequisicion-Solicitante-"+requisicion.getSolicitante()+".pdf\""); //esta linea se cambia el nombre del archivo pdf que se genera
		Font boldFont = new Font(Font.TIMES_ROMAN, 11, Font.BOLD);
		Font underline = new Font(Font.TIMES_ROMAN, 11, Font.BOLD|Font.UNDERLINE);
		Font boldFontT = new Font( Font.TIMES_ROMAN,11,Font.BOLD); // tipos de letras personalizadas a utilizar en el pdf
		Font boldFontTit = new Font(Font.TIMES_ROMAN, 22, Font.BOLD);
		Font normalFont = new Font(Font.TIMES_ROMAN, 11, Font.NORMAL);
		
		/* sirve en casa
		 * Image img = Image.getInstance((
		 * "E:\\eclipse\\eclipse-workspace\\Spring-boot-data-jpa-AMHON-SGI\\src\\main\\resources\\static\\img\\logo.png"
		 * ));
		 */		//Image  img = Image.getInstance(("C:\\Spring5\\eclipse-workspace\\Spring-boot-data-jpa-AMHON-SGI\\src\\main\\resources\\static\\img\\logo.png"));//obtenemos la imagen del logo desde el directorio en el que se encuentra
		Image  img = Image.getInstance("src/main/resources/static/logo.png"); //correcto
		
		 img.scalePercent(35f);//código sirve para escalar el tamaño de la imagen en el archivo pdf
		document.add(img); // añadimos la imagen al documento que estamos creando
		
		document.add(new Paragraph("\n\n")); // código para un salto de linea
		
		PdfPCell cell = null; //instanciamos PdfPCell el cual sirve para ingresar elementos a las celdas
		
		Paragraph preface = new Paragraph( "ASOCIACIÓN DE MUNICIPIOS DE HONDURAS",boldFontTit ); //añadimos una linea de texto con el titulo del documento
		preface.setAlignment(Element.ALIGN_CENTER); //alineamos al centro el párrafo de texto que antes de agregarlo al documento
		document.add( preface ); //añadimos al documento el párrafo que se creo anteriormente
		
		Paragraph preface1 = new Paragraph( "AMHON",boldFontTit ); //añadimos una linea de texto con la continuación del titulo del documento
		preface1.setAlignment(Element.ALIGN_CENTER); //alineamos al centro el párrafo de texto que antes de agregarlo al documento
		document.add( preface1 ); //añadimos al documento el párrafo que se creo anteriormente
		
		Paragraph preface2 = new Paragraph( "SOLICITUD DE MATERIALES" ); //añadimos una linea de texto con la continuacion del titulo del documento
		preface2.setAlignment(Element.ALIGN_CENTER); //alineamos al centro el párrafo de texto que antes de agregarlo al documento
		document.add( preface2 ); //añadimos al documento el párrafo que se creo anteriormente
		
		
		document.add(new Paragraph("\n\n"));// introducmios otro salto de linea despues del párrafo de texto
		
		Chunk text= new Chunk("Fecha: " ,boldFontT);
		String pattern = "dd-MM-yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		Chunk text1= new Chunk(" "+ simpleDateFormat.format(requisicion.getFecha_aprob()),normalFont);//añadimos campo fecha de requisicion al documento y la fuente de negrita y subrayado
		Phrase p= new Phrase();//creamos una nueva phrase
		p.add(text);// agregamos los textos a la frase
		p.add(text1);// agregamos los textos a la frase
		Paragraph par = new Paragraph(); //Creamos el parrafo
		par.add(p);//agregamos al parrafo los textos creados
		document.add( par ); //añadimos al documento el párrafo que se creo anteriormente
		
		Chunk text2= new Chunk("Solicitante:          ",boldFontT );//añadimos una linea de texto con el solicitante de la requisicion
		Chunk text3= new Chunk(" "+requisicion.getSolicitante(),normalFont);//añadimos el campo solicitante de la requisicion y la fuente de negrita y subrayado
		Phrase p2= new Phrase();//creamos una nueva phrase
		p2.add(text2); // agregamos los textos a la frase
		p2.add(text3); // agregamos los textos a la frase
		Paragraph par2 = new Paragraph( ); //Creamos el parrafo
		par2.add( p2 ); //agregamos al parrafo las frases de textos creados
		document.add( par2 ); //añadimos al documento el párrafo que se creo anteriormente
		
		Chunk text4= new Chunk( "Cargo:                 ",boldFontT ); //añadimos una linea de texto con el texto cargo
		Chunk text5= new Chunk(" "+ requisicion.getCargo(),normalFont); //agregamos el campo cargo al segundo texto y la fuente de negrita y subrayado
		Phrase p3= new Phrase(); //creamos una nueva phrase
		p3.add(text4); // agregamos los textos a la frase
		p3.add(text5); // agregamos los textos a la frase
		Paragraph par3 = new Paragraph( ); //creamos el parrafo
		par3.add( p3 ); //agregamos al parrafo las frases de textos creados
		document.add( par3 ); //añadimos al documento el párrafo que se creo anteriormente
		
		Chunk text6= new Chunk( "Departamento:   " ,boldFontT);//añadimos una linea de texto con el departamento
		Chunk text7= new Chunk( " "+requisicion.getDepartamento(),normalFont);//añadimos el campo departamento y la fuente de negrita y subrayado
		Phrase p4= new Phrase(); //creamos una nueva phrase
		p4.add(text6); // agregamos los textos a la frase
		p4.add(text7); // agregamos los textos a la frase
		Paragraph par4 = new Paragraph( ); //creamos el parrafo
		par4.add( p4 ); //agregamos al parrafo los textos creados
		document.add( par4 ); //añadimos al documento el párrafo que se creo anteriormente
		document.add(new Paragraph("\n\n")); // código para un salto de linea
		
		PdfPTable tabla2 = new PdfPTable(5); //Creamos una segunda tabla para agregar los articulos.
		//tabla2.setHorizontalAlignment();
		tabla2.setSpacingAfter(70);//agregamos espacio despues de la creacion de la tabla

		tabla2.setWidths(new float[] {8.5f, 2.5f, 2.5f, 3.5f, 3.5f}); //Le asignamos dimensiones a las tablas.
		cell = new PdfPCell(new Phrase("PRODUCTO",boldFont)); // Agregamos la columna producto.
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); //Le damos una alineación a la celda.
		tabla2.addCell(cell);//Agregamos la celda a la  segunda tabla.
		
		cell = new PdfPCell(new Phrase("UNIDAD",boldFont)); // Agregamos la columna unidad.
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); //Le damos una alineación a la celda.
		tabla2.addCell(cell);//Agregamos la celda a la  segunda tabla.
		
		cell = new PdfPCell(new Phrase("PRECIO",boldFont)); //Agregamos la columna Precio
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); //Le damos una alineación a la celda.
		tabla2.addCell(cell);//Agregamos la celda a la  segunda tabla.
		
		
		cell = new PdfPCell(new Phrase("CANTIDAD",boldFont));// Agregamos la columna Cantidad.
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); //Le damos una alineación a la celda.
		tabla2.addCell(cell);//Agregamos la celda a la  segunda tabla.
		
		
		cell = new PdfPCell(new Phrase("TOTAL",boldFont));//Agregamos la columna Total
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);//Le damos una alineación a la celda.
		tabla2.addCell(cell); //Agregamos la celda a la  segunda tabla.
		
		for(DetalleRequisicion detreq: requisicion.getLineasRe()) { //recorremos las lineas del detalle de entrada para obtener cada registro y lo guardamos en detmov
			cell = new PdfPCell(new Phrase(detreq.getArticulo().getNombre())); // agregamos el nombre de cada articulo en secuencia
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); //le damos una alineacion centrada a la celda
			tabla2.addCell(cell); //agregamos el registro a la tabla
			
			cell = new PdfPCell(new Phrase(detreq.getArticulo().getUnidadMedida().getNombre_unid()));// agregamos el nombre de cada articulo en secuencia
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); //le damos una alineacion centrada a la celda
			tabla2.addCell(cell); //agregamos el registro a la tabla
			
			cell = new PdfPCell(new Phrase(String.format("%.2f", detreq.getPrecio())));// agregamos el precio del articulo con un formato de 2 decimales después del punto
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); //le damos una alineacion centrada a la celda
			tabla2.addCell(cell); //agregamos el registro a la tabla
			
			cell = new PdfPCell(new Phrase(detreq.getCantidad().toString())); // agregamos la cantidad de manera secuencial
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); //le damos una alineacion centrada a la celda
			tabla2.addCell(cell); //agregamos la celda a la tabla2
			
			cell = new PdfPCell(new Phrase(String.format("%.2f", detreq.calcularImportePrecProm())));//agregamos el total del importe dandole formato de 2 cifras despues del punto
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); //le damos una alineacion centrada a la celda
			tabla2.addCell(cell); //agregamos la celda a la tabla2
	}

		cell = new PdfPCell(new Phrase("***Última Línea***",normalFont)); // agregamos el texto de Gran total
		cell.setColspan(5); // unimos 4 columnas para que se muestre justo antes del total
		cell.setHorizontalAlignment(PdfCell.ALIGN_CENTER);// le damos un alineado a la derecha
		tabla2.addCell(cell);// agregamos la celda a la tablas
		
		cell = new PdfPCell(new Phrase("GRAN TOTAL: ",boldFont)); // agregamos el texto de Gran total
		cell.setColspan(4); // unimos 4 columnas para que se muestre justo antes del total
		cell.setHorizontalAlignment(PdfCell.ALIGN_RIGHT);// le damos un alineado a la derecha
		tabla2.addCell(cell);// agregamos la celda a la tablas
		tabla2.addCell(String.format("%.2f", requisicion.getTotal()));// agregamos el total de la entrada ala tabla
		
		document.add(tabla2); //agregamos la segunda tabla al documento
		
		Chunk text8= new Chunk("                                            ",underline );//añadimos el campo solicitante de la requisicion y la fuente de negrita y subrayado
		Chunk derecha = new Chunk(new VerticalPositionMark(),360.75f, true); //alinear
		Chunk izquierda = new Chunk(new VerticalPositionMark(),48.25f, true); //alinear
		Chunk text9= new Chunk("                                            ",underline );//añadimos una linea de texto con el solicitante de la requisicion
		Phrase pf= new Phrase();//creamos una nueva phrase
		pf.add(izquierda); // agregamos los espaciados
		pf.add(text8); // agregamos los textos a la frase
		pf.add(derecha); // agregamos los espaciados
		pf.add(text9); // agregamos los textos a la frase
		Paragraph parf = new Paragraph( ); //Creamos el parrafo
		parf.add( pf ); //agregamos al parrafo las frases de textos creados
		document.add( parf ); //añadimos al documento el párrafo que se creo anteriormente
		
		Chunk soli = new Chunk("Firma Jefe Inmediato",boldFont);//añadimos el campo solicitante de la requisicion y la fuente de negrita y subrayado
		Chunk derecha1 = new Chunk(new VerticalPositionMark(),377.70f, true); //alinear
		Chunk izquierda1 = new Chunk(new VerticalPositionMark(),58.55f, true); //alinear
		Chunk firma= new Chunk("Firma de Recibido",boldFont );//añadimos una linea de texto con el solicitante de la requisicion
		Phrase pf1= new Phrase();//creamos una nueva phrase
		pf1.add(izquierda1); // el espaciado del la firma del jefe inmediato
		pf1.add(soli); // agregamos los textos a la frase
		pf1.add(derecha1); // agregamos los textos a la frase
		pf1.add(firma); // agregamos los textos a la frase
		Paragraph parf1 = new Paragraph( ); //Creamos el parrafo
		parf1.add( pf1 ); //agregamos al parrafo las frases de textos creados
		document.add( parf1 ); //añadimos al documento el párrafo que se creo anteriormente
		
		document.add(new Paragraph("\n\n")); // código para un salto de linea
		
		
		//Segunda linea de firmas
		
		Chunk text10= new Chunk("                                            ",underline );//añadimos el campo solicitante de la requisicion y la fuente de negrita y subrayado
		Chunk derecha2 = new Chunk(new VerticalPositionMark(),360.75f, true); //alinear
		Chunk izquierda2 = new Chunk(new VerticalPositionMark(),48.25f, true); //alinear
		Chunk text11= new Chunk("                                            ",underline );//añadimos una linea de texto con el solicitante de la requisicion
		Phrase pf2= new Phrase();//creamos una nueva phrase
		pf2.add(izquierda2); // espaciado del parrafo de firma del lado izquierdo
		pf2.add(text10); // agregamos los textos a la frase
		pf2.add(derecha2); // agregamos los espaciados
		pf2.add(text11); // agregamos los textos a la frase
		Paragraph parf2 = new Paragraph( ); //Creamos el parrafo
		parf2.add( pf2 ); //agregamos al parrafo las frases de textos creados
		document.add( parf2 ); //añadimos al documento el párrafo que se creo anteriormente
		
		Chunk geref = new Chunk("Firma de Gerente",boldFont);//añadimos el campo solicitante de la requisicion y la fuente de negrita y subrayado
		Chunk derecha3 = new Chunk(new VerticalPositionMark(),380.10f, true); //alinear380
		Chunk izquierda3 = new Chunk(new VerticalPositionMark(),67.25f, true); //alinear
		Chunk sumi= new Chunk("Firma Encargado",boldFont );//añadimos una linea de texto con el solicitante de la requisicion
		Phrase pf3= new Phrase();//creamos una nueva phrase
		pf3.add(izquierda3); // agregamos los espaciados
		pf3.add(geref); // agregamos los textos a la frase
		pf3.add(derecha3); // agregamos los espaciados
		pf3.add(sumi); // agregamos los textos a la frase
		Paragraph parf3 = new Paragraph( ); //Creamos el parrafo
		parf3.add( pf3 ); //agregamos al parrafo las frases de textos creados
		document.add( parf3 ); //añadimos al documento el párrafo que se creo anteriormente
		

		Chunk geref1 = new Chunk("Financiero Administrativo",boldFont);//añadimos el campo solicitante de la requisicion y la fuente de negrita y subrayado
		Chunk derecha4 = new Chunk(new VerticalPositionMark(),386.55f, true); //alinear
		Chunk izquierda4 = new Chunk(new VerticalPositionMark(),46.25f, true); //alinear
		Chunk sumi1= new Chunk("de Suministros",boldFont );//añadimos una linea de texto con el solicitante de la requisicion
		Phrase pf4= new Phrase();//creamos una nueva phrase
		pf4.add(izquierda4); // agregamos los espaciados
		pf4.add(geref1); // agregamos los textos a la frase
		pf4.add(derecha4); // agregamos los espaciados
		pf4.add(sumi1); // agregamos los textos a la frase
		Paragraph parf4 = new Paragraph( ); //Creamos el parrafo
		parf4.add( pf4 ); //agregamos al parrafo las frases de textos creados
		document.add( parf4 ); //añadimos al documento el párrafo que se creo anteriormente
		
		//cabe mencionar que spring se encarga de abrir y cerrar el documento automaticamente.
		
	}

	
}
