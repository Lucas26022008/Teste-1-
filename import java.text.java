import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootApplication
public class SistemaVendasApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemaVendasApplication.class, args);
    }
}

@RestController
@RequestMapping("/vendas")
class VendasController {
    private Map<String, Double> produtos = new HashMap<>();
    private Map<String, Integer> carrinho = new HashMap<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public VendasController() {
        produtos.put("Vara de Pesca", 50.00);
        produtos.put("Iscas Artificiais", 5.00);
        produtos.put("Carretilha", 80.00);
        produtos.put("Anzóis", 2.00);
    }

    @GetMapping("/catalogo")
    public Map<String, Double> exibirCatalogo() {
        return produtos;
    }

    @PostMapping("/adicionar")
    public String adicionarProdutoAoCarrinho(@RequestParam String produto, @RequestParam int quantidade) {
        if (produtos.containsKey(produto)) {
            carrinho.put(produto, carrinho.getOrDefault(produto, 0) + quantidade);
            return quantidade + " " + produto + "(s) adicionado(s) ao carrinho.";
        } else {
            return "Produto não encontrado no catálogo.";
        }
    }

    @GetMapping("/carrinho")
    public Map<String, Integer> exibirCarrinho() {
        return carrinho;
    }

    @GetMapping("/finalizar")
    public String finalizarCompra() {
        double total = 0.0;
        for (Map.Entry<String, Integer> entry : carrinho.entrySet()) {
            String produto = entry.getKey();
            int quantidade = entry.getValue();
            double precoUnitario = produtos.get(produto);
            double subtotal = precoUnitario * quantidade;
            total += subtotal;
        }
        carrinho.clear();
        return "Compra finalizada. Total: R$" + String.format("%.2f", total);
    }
}