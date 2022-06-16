package fiuba.service

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification
import static groovy.test.GroovyAssert.shouldFail

import java.awt.peer.TextAreaPeer

class AlumnoSpec extends Specification implements DomainUnitTest<Alumno> {

    def setup() {
    }

    def cleanup() {
    }

    void "crear Alumno"() {
        given:
        String nombre = "Nombre"
        String apellido = "Apellido"
        String email = "email@gmail.com"
        Long padron = 100000
        Alumno alumno= new Alumno(nombre,apellido,padron,email)
        expect:
        alumno.getNombre() == nombre
        alumno.getApellido() == apellido
        alumno.getEmail() == email
        alumno.getPadron() == padron
    }

    void "alumno oferta sobre un ticket en espera"(){
        given:
        Alumno alumno= new Alumno("nombre","apellido",1,"email@email.com")
        Cliente cliente = new Cliente("nombre","apellido", "email@email.com")
        Ticket ticket = cliente.emitirTicket("nuevo ticket")
        BigDecimal precio = 22.60
        Oferta oferta = alumno.ofertar(ticket,"mensaje",precio)
        expect:
        oferta.getTicketOfertado()== ticket
    }

    void "alumno oferta sobre ticket no ofetrable"(){
        given:
        Alumno alumno= new Alumno("nombre","apellido",1,"email@email.com")
        Cliente cliente = new Cliente("nombre","apellido", "email@email.com")
        Ticket ticket = cliente.emitirTicket("nuevo ticket")
        ticket.setEstado(Estado.cerrado)
        BigDecimal precio = 22.60;
        expect:
        shouldFail(IllegalStateException) {
            alumno.ofertar(ticket, "mensaje", precio)
        }
    }

    void "alumno puntua cliente exitosamente"(){
        Cliente cliente = new Cliente("nombre","apellido", "email@email.com")
        Ticket ticket = cliente.emitirTicket("nuevo ticket")
        Alumno alumno= new Alumno("nombre","apellido",1,"email@email.com")
        Oferta oferta = alumno.ofertar(ticket,"descripcion",33.70)
        cliente.aceptarOferta(oferta)
        alumno.finalizarTicket(ticket)
        cliente.finalizarTicket(ticket)
        expect:
        String opinion = "muy buen servicio"
        alumno.puntuarCliente(opinion,ticket);
        cliente.getOpiniones().size()==1
        cliente.getOpiniones().get(ticket) == opinion

    }
    void "alumno puntua un ticket no finalizado"(){
        given:
        Cliente cliente = new Cliente("nombre","apellido", "email@email.com")
        Ticket ticket = cliente.emitirTicket("nuevo ticket")
        Alumno alumno= new Alumno("nombre","apellido",1,"email@email.com")
        Oferta oferta = alumno.ofertar(ticket,"descripcion",33.70)
        cliente.aceptarOferta(oferta)
        alumno.finalizarTicket(ticket)
        expect:
        shouldFail(Exception) {
            alumno.puntuarCliente("opinion", ticket);
        }
    }
}
