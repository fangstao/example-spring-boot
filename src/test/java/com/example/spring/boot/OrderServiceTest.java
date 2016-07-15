package com.example.spring.boot;

import com.example.spring.boot.domain.Item;
import com.example.spring.boot.domain.Order;
import com.example.spring.boot.domain.Product;
import com.example.spring.boot.domain.User;
import com.example.spring.boot.repository.ItemRepository;
import com.example.spring.boot.repository.OrderRepository;
import com.example.spring.boot.service.InsufficientProductStockException;
import com.example.spring.boot.service.OrderService;
import com.example.spring.boot.service.impl.OrderServiceImpl;

import static org.assertj.core.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
public class OrderServiceTest {
    private OrderServiceImpl orderService;

    private OrderRepository orderRepository;

    private ItemRepository itemRepository;

    private User user;

    private Product product1;

    private Product product2;

    private Product insufficientProduct;

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
        orderService.setOrderRepository(orderRepository);
        orderService.setItemRepository(itemRepository);
    }

    private void initProducts() {
        product1 = new Product();
        product1.setId(1L);
        product1.setName("spaghetti");
        product1.setPrice(2.9);
        product1.setTotal(20);

        product2 = new Product();
        product2.setId(2L);
        product2.setName("noodle");
        product2.setPrice(0.9);
        product2.setTotal(20);

        insufficientProduct = new Product();
        insufficientProduct.setName("pizza");
        insufficientProduct.setPrice(0.9);
        insufficientProduct.setTotal(3);
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
    }

    private void mockItemRepository() {
        itemRepository = mock(ItemRepository.class);

        when(itemRepository.save(any(Item.class))).then(new Answer<Item>() {
            @Override
            public Item answer(InvocationOnMock invocation) throws Throwable {
                Item item = invocation.getArgumentAt(0, Item.class);
                item.setId(generatedOrderId);
                return item;
            }
        });

    }

    private void mockOrderRepository() {
        orderRepository = mock(OrderRepository.class);
        when(orderRepository.save(any(Order.class))).then(new Answer<Order>() {
            @Override
            public Order answer(InvocationOnMock invocation) throws Throwable {
                Order order = invocation.getArgumentAt(0, Order.class);
                order.setId(generatedOrderId);
                return order;
            }
        });

        when(orderRepository.findOne(generatedOrderId)).then(new Answer<Order>() {
            @Override
            public Order answer(InvocationOnMock invocation) throws Throwable {
                Order order = Order.create(createUser(), items);
                order.setId(generatedOrderId);
                return order;
            }
        });
    }

    @Test
    public void createOrderSuccess() throws Exception {

        double totalPrice = items.stream()
                .mapToDouble(p -> {
                    return p.getProduct().getPrice() * p.getCount();
                })
                .sum();
        //save order success
        Order order = orderService.createOrder(user, items);
        assertThat(order.getId()).isNotNull();

        // find order and then validate items and user
        Order orderEntity = orderService.findById(order.getId());
        assertThat(orderEntity).isEqualTo(order);
        assertThat(orderEntity.getUser()).isEqualTo(user);
        assertThat(orderEntity.getItems()).hasSameElementsAs(items);
        assertThat(orderEntity.getTotalPrice()).isEqualTo(totalPrice);
    }


    @Test(expected = InsufficientProductStockException.class)
    public void itemsContainsInsufficientProduct() throws Exception {
        Order order = orderService.createOrder(user, itemsContainsInsufficientProduct);
    }
}
