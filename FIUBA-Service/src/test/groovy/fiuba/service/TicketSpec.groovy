package fiuba.service

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime
import static groovy.test.GroovyAssert.shouldFail

class TicketSpec extends Specification implements DomainUnitTest<Ticket> {

    def setup() {
    }

    def cleanup() {
    }

    void "se crea un ticket y esta en espera"() {
        given:
        Cliente cliente = new Cliente("nombre", "apellido", "email@email.com");
        Ticket ticket = cliente.emitirTicket("");
        expect:
            ticket.getEstado() == Estado.enEspera
    }

    void "se acepta un ticket y esta aceptado y con fecha de hoy"(){
        given:
        Cliente cliente = new Cliente("nombre","apellido", "email@email.com")
        Ticket ticket = cliente.emitirTicket("nuevo ticket")
        Alumno alumno= new Alumno("nombre","apellido",1,"email@email.com")
        Oferta oferta = alumno.ofertar(ticket,"descripcion",33.70)
        expect:
        cliente.aceptarOferta(oferta)
        ticket.getEstado() == Estado.activo
        ticket.getFechaAceptado() == LocalDateTime.now()
    }

    void "se finaliza ticket por solo una de las partes por lo que sigue activo"(){
        given:
        Cliente cliente = new Cliente("nombre","apellido", "email@email.com")
        Ticket ticket = cliente.emitirTicket("nuevo ticket")
        Alumno alumno= new Alumno("nombre","apellido",1,"email@email.com")
        Oferta oferta = alumno.ofertar(ticket,"descripcion",33.70)
        cliente.aceptarOferta(oferta)
        expect:
        cliente.finalizarTicket(ticket) ==  Estado.activo
    }

    void "se finaliza ticket por ambas partes"(){
        given:
        Cliente cliente = new Cliente("nombre","apellido", "email@email.com")
        Ticket ticket = cliente.emitirTicket("nuevo ticket")
        Alumno alumno= new Alumno("nombre","apellido",1,"email@email.com")
        Oferta oferta = alumno.ofertar(ticket,"descripcion",33.70)
        cliente.aceptarOferta(oferta)
        expect:
        alumno.finalizarTicket(ticket)
        cliente.finalizarTicket(ticket) ==  Estado.cerrado
    }

    void "se quiere puntuar un ticket no finalizado"(){

        given:
        Cliente cliente = new Cliente("nombre","apellido", "email@email.com")
        Ticket ticket = cliente.emitirTicket("nuevo ticket")
        Alumno alumno= new Alumno("nombre","apellido",1,"email@email.com")
        Oferta oferta = alumno.ofertar(ticket,"descripcion",33.70)
        cliente.aceptarOferta(oferta)
        alumno.finalizarTicket(ticket)
        expect:
        shouldFail (Exception) {
            cliente.puntuarAlumno("muy buen servicio", ticket)
        }
    }
}
