package fiuba.service

import java.time.LocalDateTime

enum Estado{
    enEspera,
    activo,
    cancelado,
    cerrado
}

class Ticket {
    LocalDateTime fechaAceptado
    Cliente emisor
    Alumno alumno
    String descripcion
    Estado estado
    boolean alumnoFinalizo
    boolean clienteFinalizo
    ArrayList<Oferta> ofertas

    static constraints = {
        emisor nullable: false
        descripcion blank: false
    }

    Ticket (Cliente cliente, String descripcion){
        this.emisor = cliente
        this.descripcion = descripcion
        estado = Estado.enEspera
        ofertas = new ArrayList<Oferta>()
        alumnoFinalizo  = false
        clienteFinalizo = false
    }

    void setAlumno(Alumno alumno){
        if (estado == Estado.enEspera){
            this.alumno = alumno
            this.estado = Estado.activo
            this.fechaAceptado = LocalDateTime.now()
            alumno.addTicket(this)
        }else{
            throw new IllegalStateException("Ticket en estado no valido")
        }
    }

    void addOferta(Oferta oferta){
        if (estado==Estado.enEspera) {
            ofertas.add(oferta)
        }else{
            throw new IllegalStateException("Ticket en estado no valido")
        }
    }

    void eliminarOferta(Oferta oferta) {
        if (ofertas.contains(oferta)){
            ofertas.remove(oferta)
        }else{
            throw new IllegalArgumentException("no existe la oferta")
        }
    }

     Estado cancelar() {
         if (estado == Estado.enEspera){
             estado = Estado.cancelado
             alumno.deleteTicket(this)
             return estado
         }else if (estado == Estado.activo){
            LocalDateTime aux = fechaAceptado.plusDays(1)
             if (aux >= LocalDateTime.now()){
                 estado = Estado.cancelado
                 alumno.deleteTicket(this)
                 return estado
             }else{
                 throw new IllegalStateException("pasaron mas de 24 hs")
             }
         }else{
             throw new IllegalStateException("no es cancelable")
         }
     }
    Estado finalizar (Alumno alumno){
        if (alumno == this.alumno && estado==Estado.activo){
            alumnoFinalizo = true
            if (clienteFinalizo){
                estado = Estado.cerrado
            }
            return estado
        }else{
            throw new Exception("error en la finalizacion")
        }
    }

    Estado finalizar (Cliente cliente){
        if (cliente == this.emisor && estado==Estado.activo){
            clienteFinalizo = true
            if (alumnoFinalizo){
                estado = Estado.cerrado
            }
            return estado
        }else{
            throw new Exception("error en la finalizacion")
        }
    }

    void puntuarAlumno(String s) {
        if (estado == Estado.cerrado){
            alumno.getOpiniones().put(this, s)
        }else{
            throw new Exception("error")
        }
    }

    void puntuarCliente(String s) {
        if (estado == Estado.cerrado){
            emisor.getOpiniones().put(this, s)
        }else{
            throw new Exception("error")
        }
    }
}
