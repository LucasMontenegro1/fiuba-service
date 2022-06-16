package fiuba.service



class Cliente {
    String nombre
    String apellido
    String email
    ArrayList<Ticket> tickets
    Map<Ticket, String> opiniones

    static constraints = {
        nombre blank: false, nullable: false
        apellido blank: false, nullable: false
        email email: true, nullable: false, blank: false
    }

    Cliente (String nombre, String apellido, String email){
        this.nombre = nombre
        this.apellido = apellido
        this.email = email
        this.tickets= new ArrayList<Ticket>()
        opiniones  = new HashMap<Ticket, String>()
    }

    Ticket emitirTicket(String descripcion){
        Ticket nuevo = new Ticket(this, descripcion)
        tickets.add(nuevo)
        return  nuevo
    }

     Ticket aceptarOferta(Oferta oferta) {
        Ticket ticket = oferta.getTicketOfertado()
        if (tickets.contains(ticket) && ticket.getEstado()==Estado.enEspera){
            oferta.setEstado(EstadoOferta.aceptada)
            ticket.setAlumno(oferta.getAlumno())
            return ticket
        }else{
            throw new IllegalStateException("el ticket no permite aceptar ofertas")
        }
    }

    Estado cancelarTicket(Ticket ticket) {
        if (this.tickets.contains(ticket)){
            ticket.cancelar()
        }else{
            throw new IllegalStateException("el ticket no le pertenece")
        }
    }
    Estado finalizarTicket(Ticket ticket){
        return ticket.finalizar(this)
    }

    void puntuarAlumno(String s, Ticket ticket) {
        if (tickets.contains(ticket)){
            ticket.puntuarAlumno(s)
        }else{
            throw new IllegalStateException("el ticket no le pertenece")
        }
    }
}
