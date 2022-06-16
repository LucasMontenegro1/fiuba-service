package fiuba.service

enum EstadoOferta{
   aceptada,
    enEspera,
    cancelada
}
class Oferta {
    Alumno alumno
    Ticket ticketOfertado
    BigDecimal precio
    String descripcion
    EstadoOferta estado

    static constraints = {
        ticketOfertado nullable: false
        descripcion nullable: false
        precio nullable: false
    }

    Oferta(Ticket ticketOfertado, String descripcion, BigDecimal precio, Alumno alumno){
        this.ticketOfertado = ticketOfertado
        this.precio = precio
        this.descripcion = descripcion
        this.estado = EstadoOferta.enEspera
        this.alumno = alumno
        ticketOfertado.addOferta(this)
    }

    boolean cancelarOferta(){
        if (estado == EstadoOferta.aceptada){
            return false
        }else{
            ticketOfertado.eliminarOferta(this)
            this.estado = EstadoOferta.cancelada
            return true
        }
    }
}
