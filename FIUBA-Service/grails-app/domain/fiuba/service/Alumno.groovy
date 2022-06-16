package fiuba.service

class Alumno {
    String nombre
    String apellido
    Long padron
    ArrayList<Oferta> ofertas
    ArrayList<Ticket> misTickets
    Map<Ticket, String> opiniones
    String email

    static constraints = {
        nombre blank: false, nullable: false
        apellido blank: false, nullable: false
        padron nullable: false
        email email: true, nullable: false, blank: false
    }

    Alumno(String nombre, String apellido, Long padron, String email){
        this.nombre = nombre
        this.apellido = apellido
        this.padron = padron
        this.email = email
        ofertas = new ArrayList<Oferta>()
        misTickets = new ArrayList<Ticket>()
        opiniones  = new HashMap<Ticket, String>()
    }

    Oferta ofertar(Ticket ticket, String descripcion, BigDecimal precio) {
        if (ticket.getEstado()==Estado.enEspera){
            Oferta oferta = new Oferta(ticket, descripcion, precio, this)
            this.ofertas.add(oferta)
            return oferta
        }else{
            throw new IllegalStateException("el ticket no es ofertable")
        }
    }

    EstadoOferta cancelarOferta(Oferta oferta) {
        if (oferta.cancelarOferta()){
            return oferta.getEstado()
        }else{
            throw new IllegalStateException("no se puede cancelar oferta")
        }
    }
    void addTicket(Ticket ticket){
        misTickets.add(ticket)
    }

    void deleteTicket(Ticket ticket){
        if(!misTickets.remove(ticket)){
            throw new IllegalStateException("el ticket no le pertenece")
        }
    }

    Estado finalizarTicket(Ticket ticket){
        return ticket.finalizar(this)
    }

    void puntuarCliente(String s, Ticket ticket) {
        if (misTickets.contains(ticket)){
            ticket.puntuarCliente(s)
        }else{
            throw new IllegalStateException("el ticket no le pertenece")
        }
    }
}
