package usuarios;

public class Organizador extends Usuario {

    private double ingresosTotales;
    private double gastosTotales;

    public Organizador(String login, String password) {
        super(login, password);
        this.ingresosTotales = 0.0;
        this.gastosTotales = 0.0;
    }

    @Override
    public void abonar(double monto) {
        if (monto <= 0)
            throw new IllegalArgumentException("Monto inválido para abonar");
        saldoPlataforma += monto;
        ingresosTotales += monto;
        System.out.println("Organizador " + login + " recibió abono de " + monto + ". Saldo actual: " + saldoPlataforma);
    }

    @Override
    public boolean debitar(double monto) {
        if (monto <= 0)
            throw new IllegalArgumentException("Monto inválido para debitar");
        if (monto > saldoPlataforma) {
            System.out.println("Saldo insuficiente para el organizador " + login);
            return false;
        }
        saldoPlataforma -= monto;
        gastosTotales += monto;
        System.out.println("Organizador " + login + " registró gasto de " + monto + ". Nuevo saldo: " + saldoPlataforma);
        return true;
    }

    public void estadoFinanciero() {
        System.out.println("Estado financiero del organizador " + login);
        System.out.println("Ingresos totales: " + ingresosTotales);
        System.out.println("Gastos totales: " + gastosTotales);
        System.out.println("Saldo actual: " + saldoPlataforma);
    }

    public double getIngresosTotales() { return ingresosTotales; }
    public double getGastosTotales() { return gastosTotales; }
}
