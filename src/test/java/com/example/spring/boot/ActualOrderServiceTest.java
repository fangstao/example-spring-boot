package com.example.spring.boot;

import com.example.spring.boot.domain.ActualOrder;
import com.example.spring.boot.domain.Item;
import com.example.spring.boot.domain.Product;
import com.example.spring.boot.domain.User;
import com.example.spring.boot.repository.ItemRepository;
import com.example.spring.boot.repository.OrderRepository;
import com.example.spring.boot.service.InsufficientProductStockException;
import com.example.spring.boot.service.ProductService;
import com.example.spring.boot.service.impl.OrderServiceImpl;

import static org.assertj.core.api.Assertions.*;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 创建订单
 */
@SpringBootTest
public class ActualOrderServiceTest {
    private OrderServiceImpl orderService;

    private OrderRepository orderRepository;

    private ItemRepository itemRepository;

    private User user;

    private Product product1;

    private Product product2;

    private Product insufficientProduct;

    private ProductService productService;

    private List<Item> items;

    private List<Item> itemsContainsInsufficientProduct;

    private long generatedOrderId = 1L;

    @Before
    public void setUp() throws Exception {
        orderService = new OrderServiceImpl();
        initUser();
        initProducts();
        initOrderItems();
        initItemsContainsInsufficientProduct();
        mockOrderRepository();
        mockItemRepository();
        mockProductService();
        orderService.setOrderRepository(orderRepository);
        orderService.setItemRepository(itemRepository);
        orderService.setProductService(productService);
    }

    private void mockProductService() {
        productService = mock(ProductService.class);
        when(productService.findById(product1.getId())).thenReturn(product1);
        when(productService.findById(product2.getId())).thenReturn(product2);
        when(productService.findById(insufficientProduct.getId())).thenReturn(insufficientProduct);
    }

    private void initProducts() {
        product1 = new Product();
        product1.setId(1L);
        product1.setName("spaghetti");
        product1.setPrice(2.9);
        product1.setStock(20);

        product2 = new Product();
        product2.setId(2L);
        product2.setName("noodle");
        product2.setPrice(0.9);
        product2.setStock(20);

        insufficientProduct = new Product();
        insufficientProduct.setName("pizza");
        insufficientProduct.setPrice(0.9);
        insufficientProduct.setStock(3);
    }

    private void initItemsContainsInsufficientProduct() {
        itemsContainsInsufficientProduct = new ArrayList<>();
        Item item = new Item();
        item.setProduct(product1);
        item.setCount(2);
        itemsContainsInsufficientProduct.add(item);

        Item item2 = new Item();
        item2.setProduct(product2);
        item2.setCount(3);
        itemsContainsInsufficientProduct.add(item2);

        Item item3 = new Item();
        item3.setProduct(insufficientProduct);
        item3.setCount(5);
        itemsContainsInsufficientProduct.add(item3);
    }

    private void initUser() {
        user = createUser();
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        return user;
    }

    private void initOrderItems() {
        items = new ArrayList<>();
        Item item = new Item();
        item.setProduct(product1);
        item.setCount(2);
        items.add(item);
        Item item2 = new Item();
        item2.setProduct(product2);
        item2.setCount(3);
        items.add(item2);
        items = ImmutableList.copyOf(this.items);
    }

    private void mockItemRepository() {
        itemRepository = mock(ItemRepository.class);

        when(itemRepository.save(any(Item.class))).then(new Answer<Item>() {
            @Override
            public Item answer(InvocationOnMock invocation) throws Throwable {
                Item item = invocation.getArgumentAt(0, Item.class);
                item.setId(new Random().nextLong());
                return item;
            }
        });

    }

    private void mockOrderRepository() {
        orderRepository = mock(OrderRepository.class);
        when(orderRepository.save(any(ActualOrder.class))).then(new Answer<ActualOrder>() {
            @Override
            public ActualOrder answer(InvocationOnMock invocation) throws Throwable {
                ActualOrder actualOrder = invocation.getArgumentAt(0, ActualOrder.class);
                actualOrder.setId(generatedOrderId);
                return actualOrder;
            }
        });

        when(orderRepository.findOne(generatedOrderId)).then(new Answer<ActualOrder>() {
            @Override
            public ActualOrder answer(InvocationOnMock invocation) throws Throwable {
                ActualOrder actualOrder = ActualOrder.create(createUser(), items);
                actualOrder.setId(generatedOrderId);
                return actualOrder;
            }
        });
    }

    /**
     * 创建订单
     * @throws Exception
     */
    @Test
    public void createOrderSuccess() throws Exception {

        double totalPrice = items.stream()
                .mapToDouble(p -> {
                    return p.getProduct().getPrice() * p.getCount();
                })
                .sum();

        //save actualOrder success
        ActualOrder actualOrder = orderService.createOrder(user, items);
        assertThat(actualOrder.getId()).isNotNull();

        //check items
        assertThat(items).filteredOnNull("id").isEmpty();


        // check product stock
        assertThat(product1.getStock()).isEqualTo(18);
        assertThat(product2.getStock()).isEqualTo(17);
    }

    // order contains insufficient product stock
    @Test(expected = InsufficientProductStockException.class)
    public void itemsContainsInsufficientProduct() throws Exception {
        ActualOrder actualOrder = orderService.createOrder(user, itemsContainsInsufficientProduct);
    }


}
