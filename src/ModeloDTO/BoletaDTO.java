package ModeloDTO;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class BoletaDTO {

    private Long idBoleta;
    private Date fechaEmision;
    private ClienteDTO cliente;
    private List<VentaDTO> ventas;

    public BoletaDTO(Long idBoleta, Date fechaEmision,
            ClienteDTO cliente, List<VentaDTO> ventas) {
        this.idBoleta = idBoleta;
        this.fechaEmision = fechaEmision;
        this.cliente = cliente;
        this.ventas = ventas;
    }

    public BoletaDTO(String idBoleta, Timestamp timestamp,
            ClienteDTO cliente, List<VentaDTO> ventas) {
        this.idBoleta = Long.parseLong(idBoleta);               
        this.fechaEmision = new Date(timestamp.getTime());     
        this.cliente = cliente;
        this.ventas = ventas;
    }

    public BoletaDTO(String idBoleta, Date fechaEmision,
            ClienteDTO cliente, List<VentaDTO> ventas) {

        try {
            this.idBoleta = Long.parseLong(idBoleta);
        } catch (NumberFormatException nfe) {
            this.idBoleta = null;
        }
        this.fechaEmision = fechaEmision;
        this.cliente = cliente;
        this.ventas = ventas;
    }

    public Long getIdBoleta() {
        return idBoleta;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public List<VentaDTO> getVentas() {
        return ventas;
    }

    public double getTotal() {
        return ventas.stream()
                .mapToDouble(v -> v.getCantidad() * v.getPrecioUnitario())
                .sum();
    }
}
