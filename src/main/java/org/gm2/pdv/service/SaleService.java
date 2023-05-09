package org.gm2.pdv.service;

import lombok.RequiredArgsConstructor;
import org.gm2.pdv.dto.ProductSaleDTO;
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
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
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
    findAll (SELECT)
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

    var products = getProductInfo(sale.getItems());
    BigDecimal total = getTotal(products);

    return SaleInfoDTO.builder()
            .user(sale.getUser().getName())
            .date(sale.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            .products(products)
            .total(total) // Setando valor total
            .build();
}

// Método para calcular valor total dos Products
private BigDecimal getTotal(List<ProductInfoDTO> products) {
    // Assim que definimos um valor inicial de um BigDecimal
    BigDecimal total = new BigDecimal(0);

    // Vou percorrer toda List de ProductInfoDTO, em cada venda vou fazer a operação do valor total
    for (int i = 0; i < products.size(); i++) {
        ProductInfoDTO currentProduct = products.get(i);

        total = total.add(currentProduct.getPrice()
                .multiply(new BigDecimal(currentProduct.getQuantity())));
    }

    // Retornando o total de vendas por User
    return total;
}

// Método responsavel por transformar meu Products acima em uma List de ProductInfoDTO
// Acima eu preciso que seja retornada uma Lista de ProductInfoDTO e não um ItemSale
private List<ProductInfoDTO> getProductInfo(List<ItemSale> items) {

    if(CollectionUtils.isEmpty(items)){
        return Collections.emptyList();
    }

    return items.stream().map(
            item ->
                ProductInfoDTO.builder()
                    .id(item.getId())
                    .price(item.getProduct().getPrice())   // Setando o Valor
                    .description(item.getProduct().getDescription())
                    .quantity(item.getQuantity())
                    .build()
    ).collect(Collectors.toList());
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

    // Função da linha 106
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
    private List<ItemSale> getItemSale(List<ProductSaleDTO> products) {

        // Pacote exceptions, tratações de erros
        if(products.isEmpty()) {
            throw new InvalidOperationException("Não é possivel adicionar vendas sem itens");
        }

        return products.stream().map(item -> {
            Product product = productRepository.findById(item.getProductid())
                    .orElseThrow(() -> new NoItemException("Item da venda não encontrado"));

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
