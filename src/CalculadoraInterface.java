// CalculadoraInterface.java
public interface CalculadoraInterface {
    void adicionarNumero(String numero);
    void adicionarOperacao(String operacao);
    void calcularResultado();
    void limpar();
    int getResultado();
    int getResultadoAnterior();

}
