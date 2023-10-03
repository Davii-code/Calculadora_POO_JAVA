public class CalculadoraAdapter implements CalculadoraInterface {
    private final Calculadora calculadora;

    public CalculadoraAdapter(Calculadora calculadora) {
        this.calculadora = calculadora;
    }

    public void adicionarNumero(String numero) {
        calculadora.adicionarNumero(numero);
    }

    public void adicionarOperacao(String operacao) {
        calculadora.adicionarOperacao(operacao);
    }

    public void calcularResultado() {
        calculadora.calcularResultado();
    }

    public void limpar() {
        calculadora.limpar();
    }

    public int getResultado() {
        return calculadora.getResultado();
    }

    public int getResultadoAnterior() {
       return calculadora.getResultadoAnterior();
    }

}
