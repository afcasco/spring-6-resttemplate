package guru.springframework.spring6resttemplate.client;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class BeerClientImplTest {

    @Autowired
    BeerClientImpl beerClient;

    @Test
    void listBeersNoBeerName() {
        beerClient.listBeers(null, null, null, null, null);
    }


    @Test
    void testListBeers() {
        beerClient.listBeers("ALE", null, null, null, null);
    }

    @Test
    void testGetBeerById() {
        Page<BeerDTO> beerDTOS = beerClient.listBeers();
        BeerDTO beerDTO = beerDTOS.getContent().get(0);
        BeerDTO byId = beerClient.getBeerById(beerDTO.getId());
        assertNotNull(byId);
    }

    @Test
    void testSaveNewBeer() {
        BeerDTO beerDTO = BeerDTO.builder()
                .price(new BigDecimal("10.99"))
                .beerName("New Test Beer")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("1234456")
                .build();

        BeerDTO savedDto = beerClient.createBeer(beerDTO);
        assertNotNull(savedDto);
    }


    @Test
    void testUpdateBeerById() {

        BeerDTO newDto = BeerDTO.builder()
                .price(new BigDecimal("10.99"))
                .beerName("UPDATED BEER")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("123456")
                .build();

        BeerDTO beerDto = beerClient.createBeer(newDto);

        final String newName = "Updated Beer";

        beerDto.setBeerName(newName);

        BeerDTO updatedBeer = beerClient.updateBeer(beerDto);

        assertEquals(newName, updatedBeer.getBeerName());
    }


    @Test
    void testDeleteBeerById() {
        BeerDTO newDto = BeerDTO.builder()
                .price(new BigDecimal("10.99"))
                .beerName("Mango Bobs 2")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("123455")
                .build();

        BeerDTO beerDTO = beerClient.createBeer(newDto);
        beerClient.deleteBeer(beerDTO.getId());

        assertThrows(HttpClientErrorException.class, ()-> beerClient.getBeerById(beerDTO.getId()));
    }
}