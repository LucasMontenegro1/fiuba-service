package fiuba.service

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

import java.time.LocalDateTime

import static groovy.test.GroovyAssert.shouldFail

class ClienteSpec extends Specification implements DomainUnitTest<Cliente> {

    def setup() {
    }

    def cleanup() {
    }

    void "crear Cliente"() {
        given:
        String nombre = "Nombre"
        String apellido = "Apellido"
        String email = "email@gmail.com"
        Cliente cliente = new Cliente(nombre,apellido,email)
        expect:
        cliente.getNombre() == nombre
        cliente.getApellido() == apellido
        cliente.getEmail() == email
    }

    void "cliente crea Ticket y le pertenece"(){
        given:
        Cliente cliente = new Cliente("nombre","apellido", "email@email.com")
        Ticket ticket = cliente.emitirTicket("nuevo ticket")
        expect:
        ticket.getEmisor() == cliente
    }

    void "Cliente acepta una oferta"(){
        given:
        Cliente cliente = new Cliente("nombre","apellido", "email@email.com")
        Ticket ticket = cliente.emitirTicket("nuevo ticket")
        Alumno alumno= new Alumno("nombre","apellido",1,"email@email.com")
        Oferta oferta = alumno.ofertar(ticket,"descripcion",33.70)
        expect:
        cliente.aceptarOferta(oferta)
        ticket.getEstado() == Estado.activo
    }

    void "no se puede aceptar una oferta con el ticket ya aceptado"(){
        given:
        Cliente cliente = new Cliente("nombre","apellido", "email@email.com")
        Ticket ticket = cliente.emitirTicket("nuevo ticket")
        Alumno alumno= new Alumno("nombre","apellido",1,"email@email.com")
        Oferta oferta = alumno.ofertar(ticket,"descripcion",33.70)
        Oferta oferta2 = alumno.ofertar(ticket,"descripcion",33.70)
        cliente.aceptarOferta(oferta)
        expect:
        shouldFail(IllegalStateException){
            cliente.aceptarOferta(oferta2)
        }
    }

    void "cliente puede cancelar ticket aceptado antes de 24hs" (){
        given:
        Cliente cliente = new Cliente("nombre","apellido", "email@email.com")
        Ticket ticket = cliente.emitirTicket("nuevo ticket")
        Alumno alumno= new Alumno("nombre","apellido",1,"email@email.com")
        Oferta oferta = alumno.ofertar(ticket,"descripcion",33.70)
        cliente.aceptarOferta(oferta)
        cliente.cancelarTicket(ticket)
        expect:
        ticket.getEstado() == Estado.cancelado
    }

    void  "cliente puede cancelar ticket aceptado despues de 24hs"(){
        given:
        Cliente cliente = new Cliente("nombre","apellido", "email@email.com")
        Ticket ticket = cliente.emitirTicket("nuevo ticket")
        Alumno alumno= new Alumno("nombre","apellido",1,"email@email.com")
        Oferta oferta = alumno.ofertar(ticket,"descripcion",33.70)
        cliente.aceptarOferta(oferta)
        LocalDateTime hora = LocalDateTime.now()
        ticket.setFechaAceptado(hora.minusDays(3))  //simulo que pasaron mas de  24 horas
        expect:
        shouldFail (IllegalStateException){
            cliente.cancelarTicket(ticket)
        }
    }

    void "cliente puntua ticket ya finalizado exitosamente"(){
        given:
        Cliente cliente = new Cliente("nombre","apellido", "email@email.com")
        Ticket ticket = cliente.emitirTicket("nuevo ticket")
        Alumno alumno= new Alumno("nombre","apellido",1,"email@email.com")
        Oferta oferta = alumno.ofertar(ticket,"descripcion",33.70)
        cliente.aceptarOferta(oferta)
        alumno.finalizarTicket(ticket)
        cliente.finalizarTicket(ticket)
        expect:
        String opinion = "muy buen servicio"
        cliente.puntuarAlumno(opinion,ticket)
        alumno.getOpiniones().size()==1
        alumno.getOpiniones().get(ticket) == opinion
    }

    void "cliente puntua un ticket no finalizado"(){
        given:
        Cliente cliente = new Cliente("nombre","apellido", "email@email.com")
        Ticket ticket = cliente.emitirTicket("nuevo ticket")
        Alumno alumno= new Alumno("nombre","apellido",1,"email@email.com")
        Oferta oferta = alumno.ofertar(ticket,"descripcion",33.70)
        cliente.aceptarOferta(oferta)
        alumno.finalizarTicket(ticket)
        expect:
        shouldFail(Exception) {
            cliente.puntuarAlumno("opinion", ticket)
        }
    }

}

