package com.example.Artisan.utils;

import com.example.Artisan.DTOs.ArtistDTO;
import com.example.Artisan.DTOs.ArtistStoryDTO;
import com.example.Artisan.entities.Artist;
import com.example.Artisan.entities.ArtistStory;
import com.example.Artisan.entities.products.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class DtoEntityConverterTest {

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DtoEntityConverter converter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void convertToDto_NullEntity_ReturnsNull() {
        Artist entity = null;
        Class<ArtistDTO> dtoClass = ArtistDTO.class;

        ArtistDTO dto = converter.convertToDto(entity, dtoClass);

        Assertions.assertNull(dto);
    }

    @Test
    public void convertToDto_ValidEntity_ReturnDTO() {
        Artist entity = new Artist();
        entity.setCity("halifax");
        entity.setEmail("test@test.com");
        entity.setProducts(new ArrayList<Product>());
        Class<ArtistDTO> dtoClass = ArtistDTO.class;
        ArtistDTO expectedDto = new ArtistDTO();
        expectedDto.setCity("halifax");
        expectedDto.setEmail("test@test.com");

        Mockito.when(modelMapper.map(entity, dtoClass)).thenReturn(expectedDto);

        var dto = converter.convertToDto(entity, dtoClass);

        Assertions.assertEquals(expectedDto, dto);
        Assertions.assertEquals("halifax", dto.getCity());
        Mockito.verify(modelMapper).map(entity, dtoClass);
    }

    @Test
    public void convertToEntity_NullDto_ReturnsNull() {
        ArtistStoryDTO dto = null;
        Class<ArtistStory> entityClass = ArtistStory.class;

        ArtistStory entity = converter.convertToEntity(dto, entityClass);

        Assertions.assertNull(entity);
    }

    @Test
    public void convertToEntity_ValidDto_returnsEntity() {
        ArtistStoryDTO dto = new ArtistStoryDTO();
        dto.setStory("test story");
        dto.setDatePosted(LocalDate.of(2023, 10, 10));
        Class<ArtistStory> entityClass = ArtistStory.class;
        ArtistStory expectedEntity = new ArtistStory();
        expectedEntity.setStory("test story");
        expectedEntity.setDate_posted(new Date(2023, 10, 10));

        Mockito.when(modelMapper.map(dto, entityClass)).thenReturn(expectedEntity);

        var entity = converter.convertToEntity(dto, entityClass);

        Assertions.assertEquals(expectedEntity, entity);
        Mockito.verify(modelMapper).map(dto, entityClass);
    }
}