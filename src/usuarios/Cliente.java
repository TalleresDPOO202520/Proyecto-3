package usuarios;

public class Cliente extends Usuario {

    public Cliente(String login, String password) {
        super(login, password);
    }

    @Override
    public void abonar(double monto) {
        if (monto <= 0)
            throw new IllegalArgumentException("Monto inválido para abonar");
        saldoPlataforma += monto;
        System.out.println("Cliente " + login + " abonó " + monto + ". Saldo actual: " + saldoPlataforma);
    }

    @Override
    public boolean debitar(double monto) {
        if (monto <= 0)
            throw new IllegalArgumentException("Monto inválido para debitar");
        if (monto > saldoPlataforma) {
            System.out.println("Saldo insuficiente para el cliente " + login);
            return false;
        }
        saldoPlataforma -= monto;
        System.out.println("Cliente " + login + " realizó una compra de " + monto + ". Nuevo saldo: " + saldoPlataforma);
        return true;
    }
}
