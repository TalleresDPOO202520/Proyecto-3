package usuarios;

public abstract class Usuario {
    protected String login;
    protected String password;
    protected double saldoPlataforma;

    public Usuario(String login, String password) {
        if (login == null || login.isBlank())
            throw new IllegalArgumentException("El login no puede estar vacío");
        if (password == null || password.isBlank())
            throw new IllegalArgumentException("La contraseña no puede estar vacía");

        this.login = login;
        this.password = password;
        this.saldoPlataforma = 0.0;
    }

    public void abonar(double monto) {
        if (monto <= 0)
            throw new IllegalArgumentException("Monto inválido para abonar");
        saldoPlataforma += monto;
    }

    public boolean debitar(double monto) {
        if (monto <= 0)
            throw new IllegalArgumentException("Monto inválido para debitar");
        if (monto > saldoPlataforma)
            return false;
        saldoPlataforma -= monto;
        return true;
    }

    public boolean autenticar(String login, String password) {
        return this.login.equals(login) && this.password.equals(password);
    }

    public String getLogin() { return login; }
    public double getSaldoPlataforma() { return saldoPlataforma; }

    public void setPassword(String newPassword) {
        if (newPassword == null || newPassword.isBlank())
            throw new IllegalArgumentException("Contraseña no válida");
        this.password = newPassword;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +	
                "login='" + login + '\'' +
                ", saldo=" + saldoPlataforma +
                '}';
    }
}

