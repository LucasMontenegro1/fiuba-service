package fiuba.service

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification
import static groovy.test.GroovyAssert.shouldFail

class OfertaSpec extends Specification implements DomainUnitTest<Oferta> {

    def setup() {
    }

    def cleanup() {
    }

    void "se crea una oferta y esta en espera"() {
        given:
        Cliente cliente = new Cliente("nombre","apellido", "email@email.com")
        Ticket ticket = cliente.emitirTicket("nuevo ticket")
        Alumno alumno= new Alumno("nombre","apellido",1,"email@email.com")
        Oferta oferta = alumno.ofertar(ticket,"descripcion",33.70)
        expect:
        oferta.getEstado() == EstadoOferta.enEspera
    }

    void "se cancela oferta de manera exitosa"(){
        given:
        Cliente cliente = new Cliente("nombre","apellido", "email@email.com")
        Ticket ticket = cliente.emitirTicket("nuevo ticket")
        Alumno alumno= new Alumno("nombre","apellido",1,"email@email.com")
        Oferta oferta = alumno.ofertar(ticket,"descripcion",33.70)
        EstadoOferta estado = alumno.cancelarOferta(oferta)
        expect:
        estado == EstadoOferta.cancelada
    }

    void "no se puede cancelar oferta al estar aceptada" (){
        given:
        Cliente cliente = new Cliente("nombre","apellido", "email@email.com")
        Ticket ticket = cliente.emitirTicket("nuevo ticket")
        Alumno alumno= new Alumno("nombre","apellido",1,"email@email.com")
        Oferta oferta = alumno.ofertar(ticket,"descripcion",33.70)
        cliente.aceptarOferta(oferta)
        expect:
        shouldFail(IllegalStateException){
            alumno.cancelarOferta(oferta)
        }
    }
}
