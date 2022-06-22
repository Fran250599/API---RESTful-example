package com.progra.personas.resources;

//import com.itextpdf.io.font.constants.StandardFonts;
//import com.itextpdf.kernel.colors.Color;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import com.progra.personas.logic.Service;
import com.progra.personas.logic.Persona;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.POST;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/personas")
public class Personas {
    String location="C:/AAA/images/";
    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Persona> search(@DefaultValue("") @QueryParam("nombre") String nombre) { 
        return Service.instance().personaSearch(nombre);
    } 
    
    @GET
    @Path("{cedula}")
    @Produces({MediaType.APPLICATION_JSON})
    public Persona get(@PathParam("cedula") String cedula) {
        try {
            return Service.instance().personaEdit(cedula);
        } catch (Exception ex) {
            throw new NotFoundException(); 
        }
    }
    
    @GET
    @Path("{cedula}/imagen")
    @Produces("image/png")
    public Response getImge(@PathParam("cedula") String cedula) throws IOException {
        File file = new File(location+cedula);
        ResponseBuilder response = Response.ok((Object) file);
        return response.build();
    }    

    @POST
    @Consumes(MediaType.APPLICATION_JSON) 
    @RolesAllowed({"ADM"})  
    public void add(Persona p) {  
        try {
            Service.instance().personaAdd(p);
        } catch (Exception ex) {
            throw new NotAcceptableException(); 
        }
    }
    
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA) 
    @Path("{cedula}/imagen")
    @RolesAllowed({"ADM"})  
    public void addImage(@PathParam("cedula") String cedula, @FormDataParam("imagen") InputStream in) {  
        try{
                OutputStream out = new FileOutputStream(new File(location + cedula));
                in.transferTo(out);
                out.close();
            } catch (Exception ex) {
                throw new NotAcceptableException(); 
            }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADM"})  
    public void update(Persona p) {  
        try {
            Service.instance().personaUpdate(p);
        } catch (Exception ex) {
            throw new NotFoundException(); 
        }
    }
    

    @DELETE
    @Path("{cedula}")
    @RolesAllowed({"ADM"})  
    public void del(@PathParam("cedula") String cedula) {
        try {
            Service.instance().personaDelete(cedula);
        } catch (Exception ex) {
            throw new NotFoundException(); 
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("mujeres")
    public List<Persona> searchMujeres() { 
        List<Persona> todos=Service.instance().personaSearch("");
        List<Persona> mujeres = new ArrayList<>();
        for(Persona p: todos){ if(p.getSexo().equals("F")) mujeres.add(p);};
        return mujeres;
    }  
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})  
    @Path("filtrar")    
    public List<Persona> filtrar(Persona filtro) {  
        List<Persona> todos=Service.instance().personaSearch("");
        List<Persona> filtrados = new ArrayList<>();
        for(Persona p: todos){ 
            if (    p.getCedula().contains(filtro.getCedula())
                  && p.getNombre().contains(filtro.getNombre())
                  && p.getSexo().contains(filtro.getSexo()))  filtrados.add(p);
        };
        return filtrados;
    }

    @GET 
    @Path("{cedula}/pdf")
    @Produces("application/pdf")
    @PermitAll    
    public Response getPdf(@PathParam("cedula") String cedula) throws IOException {
        Persona per=null;
        try { per = Service.instance().personaEdit(cedula);} catch (Exception ex) {}
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfDocument pdf = new PdfDocument(new PdfWriter(out));
        Document doc = new Document(pdf, PageSize.A4.rotate());
        
        Paragraph p;
        
        p = new Paragraph("DATOS DE PERSONA");
        p.setTextAlignment(TextAlignment.CENTER);
        p.setBold();
        p.setBackgroundColor(Color.PINK);
        doc.add(p);

        p = new Paragraph("CEDULA: "+per.getCedula());
        p.setTextAlignment(TextAlignment.LEFT);
        p.setBold();
        doc.add(p);

        p = new Paragraph("NOMBRE: "+per.getNombre());
        p.setTextAlignment(TextAlignment.LEFT);
        p.setBold();
        doc.add(p);
        
        Image img = new Image(ImageDataFactory.create(location+per.getCedula()));
        doc.add(img);        

        p = new Paragraph("FIN");
        p.setTextAlignment(TextAlignment.CENTER);
        p.setBold();
        p.setBackgroundColor(Color.PINK);
        doc.add(p);

        doc.close(); 
        
        return Response.ok(out.toByteArray()).build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("batch")
    @PermitAll    
    public void batch(List<Persona> ps) {  
        System.out.println("hola");
    }    
}
