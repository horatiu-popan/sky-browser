package org.example.spring1.item;

import org.example.spring1.item.model.Item;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ItemServiceTest {

  private ItemService itemService;

  @Mock
  private ItemRepository itemRepository;
  @Mock
  private ItemMapper itemMapper;

  @BeforeAll
  void setUp() {
    MockitoAnnotations.openMocks(this);
    itemService = new ItemService(itemRepository, itemMapper);
  }

  @Test
  void findAll() {
    Item item1 = Item.builder().id(-1L).build();
    Item item2 = Item.builder().id(0L).name("item2").build();
    Item item3 = Item.builder().id(1L).name("item3").build();

    List<Item> preparedItems = List.of(item1, item2, item3);

    when(itemRepository.findAll()).thenReturn(preparedItems);

    List<Item> returnedItems = itemService.findAll();
    assertEquals(preparedItems.size(), returnedItems.size());
    assertEquals(preparedItems, returnedItems);
  }
  @Test
  void changeName() {}
  @Test
  void update() {}
  @Test
  void create() {}
  @Test
  void findAllFiltered() {}
  @Test
  void delete() {}
  @Test
  void deleteMultiple() {}
  @Test
  void get() {}
}
