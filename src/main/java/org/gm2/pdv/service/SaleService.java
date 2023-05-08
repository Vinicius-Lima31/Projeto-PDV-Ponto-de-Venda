package org.gm2.pdv.service;

import lombok.RequiredArgsConstructor;
import org.gm2.pdv.dto.ProductDTO;
import org.gm2.pdv.dto.ProductInfoDTO;
import org.gm2.pdv.dto.SaleDTO;
import org.gm2.pdv.dto.SaleInfoDTO;
import org.gm2.pdv.entity.ItemSale;
import org.gm2.pdv.entity.Product;
import org.gm2.pdv.entity.Sale;
import org.gm2.pdv.entity.User;
import org.gm2.pdv.exceptions.InvalidOperationException;
import org.gm2.pdv.exceptions.NoItemException;
import org.gm2.pdv.repository.ItemSaleRepository;
import org.gm2.pdv.repository.ProductRepository;
import org.gm2.pdv.repository.SaleRepository;
import org.gm2.pdv.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SaleService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final SaleRepository saleRepository;
    private final ItemSaleRepository itemSaleRepository;

    /*
        {
            "user": "Fulando",
            "data": "03/07/2022",
            "products": [
                {
                    "description": "Notebook dell",
                    "quantity": 1
                }
            ]
        }
     */

public List<SaleInfoDTO> findAll() {
    return saleRepository.findAll().stream().map(sale -> getSaleInfo(sale)).collect(Collectors.toList());
}

// Quero retornar um SaleInfoDTO, esse método é da linha 47
private SaleInfoDTO getSaleInfo(Sale sale) {
    SaleInfoDTO saleInfoDTO = new SaleInfoDTO();

    saleInfoDTO.setUser(sale.getUser().getName()); // Passar nome do usuário
    saleInfoDTO.setDate(sale.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    saleInfoDTO.setProducts(getProductInfo(sale.getItems()));

    return saleInfoDTO;
}

// Método responsavel por transformar meu Products acima em uma List de ProductInfoDTO
private List<ProductInfoDTO> getProductInfo(List<ItemSale> items) {
    return items.stream().map(item -> {
        ProductInfoDTO productInfoDTO = new ProductInfoDTO();

        productInfoDTO.setId(item.getId());

        productInfoDTO.setDescription(item.getProduct().getDescription());
        productInfoDTO.setQuantity(item.getQuantity());

        return productInfoDTO;

    }).collect(Collectors.toList());
}


    /*
    {
        "userid": 3,
        "items": [
            {
                "productid": 2,
                "quantity": 2
    },
*/
    // POST
    @Transactional
    public long save(SaleDTO sale) {
        // Tratando erro, caso não exista esse usuário, não quis usar Optional
        User user = userRepository.findById(sale.getUserid())
                .orElseThrow(() -> new NoItemException("Usuário não encontrado!"));

        Sale newSale = new Sale();
        newSale.setUser(user);
        newSale.setDate(LocalDate.now());
        List<ItemSale> items = getItemSale(sale.getItems());

        newSale = saleRepository.save(newSale);

        saveItemSale(items, newSale);

        return newSale.getId();

    }

    // Função da linha 49
    // Aqui estamos fazendo o salvamento, já salvamos o sale,
    // Agora estamos salvando o ItemSale, de sale para ItemSale
    private void saveItemSale (List<ItemSale> items, Sale newSale){
        for (ItemSale item: items) {
            item.setSale(newSale);
            itemSaleRepository.save(item);
        }
    }


    // Vou retornar uma List com <ItemSale> para estar setando no newSale
    // Passando no método acima "public long save(SaleDTO sale)"
    private List<ItemSale> getItemSale(List<ProductDTO> products) {

        // Pacote exceptions, tratações de erros
        if(products.isEmpty()) {
            throw new InvalidOperationException("Não é possivel adicionar vendas sem itens");
        }

        return products.stream().map(item -> {
            Product product = productRepository.getReferenceById(item.getProductid());

            ItemSale itemSale = new ItemSale();
            itemSale.setProduct(product);
            itemSale.setQuantity(item.getQuantity());

            // Pacote exceptions, tratações de erros
            if (product.getQuantity() == 0 ) {
                throw new NoItemException("Produto sem estoque.");
            }
            // Pacote exceptions, tratações de erros
            else if (product.getQuantity() < item.getQuantity()) {
                throw new InvalidOperationException(String.format("A quantidade de itens da venda (%s) " +
                        "é maior do que a quantidade disponivel em estoque (%s)", item.getQuantity(), product.getQuantity()));
            }

            int total = product.getQuantity() - item.getQuantity();
            product.setQuantity(total);
            productRepository.save(product);

            return itemSale;

        }).collect(Collectors.toList());
    }

    // SELECT ID
    // SaleController iremos pegar uma venda pelo id
    public SaleInfoDTO getById(long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new NoItemException("Venda não encontrada!"));
        return getSaleInfo(sale);
    }
}
